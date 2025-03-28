package com.example.ticsys.account.service.Public;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticsys.account.dao.IUserDao;
import com.example.ticsys.account.model.User;
import com.example.ticsys.sharedDto.SharedUserDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PublicAccountServiceImpl implements PublicAccountService {
    private final IUserDao userDao;

    @Autowired
    public PublicAccountServiceImpl(IUserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public SharedUserDto GetUserByUsername(String username) {
        try{
            User user = userDao.GetUserByUsername(username);

            if(user == null){
                return null;
            }

            SharedUserDto sharedUserDto = SharedUserDto.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getBirthday())
                .build();
            
            return sharedUserDto;
        }
        catch(Exception e){
            log.error("Error in PublicAccountServiceImpl.GetUserByUsername: " + e.getMessage());
            return null;
        }
        
    }
    

}
