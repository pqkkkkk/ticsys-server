package com.example.ticsys.app.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.example.ticsys.app.account.dao.IPaymentMethodDao;
import com.example.ticsys.app.account.dao.IUserDao;
import com.example.ticsys.app.account.dto.request.LinkBankAccountRequest;
import com.example.ticsys.app.account.model.OrganizerInfo;
import com.example.ticsys.app.account.model.PaymentMethod;
import com.example.ticsys.app.account.model.User;
import com.example.ticsys.app.outbound.eventPublisher.IEventPublisher;
import com.example.ticsys.external_service.storage.CloudinaryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final IUserDao userDao;
    private final CloudinaryService cloudinaryService;
    private final IEventPublisher eventPublisher;
    private final IPaymentMethodDao paymentMethodDao;
    @Autowired
    public UserService(IUserDao userDao, PasswordEncoder passwordEncoder,
                        IPaymentMethodDao paymentMethodDao,
                        IEventPublisher eventPublisher, CloudinaryService cloudinaryService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.eventPublisher = eventPublisher;
        this.paymentMethodDao = paymentMethodDao;
    }
    public List<User> GetAllUsers(String role) {
        return userDao.GetAllUsers(role);
    }
    public User GetUserByUsername(String username) {
        try{
            return userDao.GetUserByUsername(username);
        }
        catch(Exception e){
            log.error("Error in GetUserByUsername of UserService: " + e.getMessage());
            return null;
        }
    }
    @Transactional
    public boolean CreateUser(User user) {
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        if(userDao.createUser(user)){
            return userDao.addRolesToUser(user.getUserName(), user.getRoles());
        } else {
            return false;
        }
    }
    @Transactional
    public boolean RegisterforOrganizer(OrganizerInfo organizerInfo, MultipartFile organizerAvt) {
        String avatarPath = "initial";
        try{
            if(organizerAvt != null){
                avatarPath = cloudinaryService.uploadFile(organizerAvt);
        }
            if(!avatarPath.equals("") && !avatarPath.equals("initial")) {
                throw new Exception("Failed to upload avatar");
            }
            if(!userDao.AddOrganizerInfo(organizerInfo)) {
                throw new Exception("Failed to add organizer info");
            }
            if(!avatarPath.equals("initial"))
            {
                if(!userDao.UpdateAvatarOfUser(organizerInfo.getUserId(), avatarPath)) {
                    throw new Exception("Failed to update avatar");
                }
            }
            if(!userDao.addRolesToUser(organizerInfo.getUserId(), List.of("ORGANIZER"))) {
                throw new Exception("Failed to add role");
            }
            return true;
        } 
        catch(Exception e) {
            if(avatarPath != "") {
                String deleteResult = cloudinaryService.deleteFile(avatarPath);
                log.info("RegisterForOrganizer of UserSerivce, delete avatar result: " + deleteResult);
            }
            log.error("RegisterForOrganizer, error: ", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
    public String LinkToBankAccount(LinkBankAccountRequest request){
        try{
            Boolean isValidUser = userDao.IsValidUser(request.getUsername());

            if(!isValidUser){
                throw new Exception("Invalid user");
            }

            eventPublisher.SendLinkBankAccountRequest(request.getUsername(),
                                    request.getBankAccountId(), request.getBankAccountOwnerName());

            return "processing";

        }
        catch(Exception e){
            log.error("Error in LinkToBankAccount of UserService: " + e.getMessage());
            return "error";
        }
    }
    public List<PaymentMethod> GetPaymentMethodsOfUser(String userId, String bankName) {
        try{
            return paymentMethodDao.GetPaymentMethodsOfUser(userId, bankName);
        }
        catch(Exception e){
            log.error("Error in GetPaymentMethodsOfUser of UserService: " + e.getMessage());
            return null;
        }
    }
}
