package com.example.ticsys.promotion.service.Public;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticsys.promotion.dao.promotion.query.IPromotionQueryDao;
import com.example.ticsys.promotion.dao.voucherOfUser.IVoucherOfUserDao;
import com.example.ticsys.promotion.dto.PromotionDto;
import com.example.ticsys.promotion.model.VoucherOfUser;
import com.example.ticsys.sharedDto.SharedPromotionDto;
import com.example.ticsys.sharedDto.SharedVoucherOfUserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PublicPromotionServiceImpl implements PublicPromotionService {
    private final IPromotionQueryDao promotionQueryDao;
    private final IVoucherOfUserDao voucherOfUserDao;
    @Autowired
    public PublicPromotionServiceImpl(IPromotionQueryDao promotionQueryDao, IVoucherOfUserDao voucherOfUserDao) {
        this.voucherOfUserDao = voucherOfUserDao;
        this.promotionQueryDao = promotionQueryDao;
    }
    @Override
    public int GetReductionOfPromotion(int promotionId, int currentPrice) {
        try{
            PromotionDto promotion = promotionQueryDao.GetPromotionById(promotionId);

            if(promotion.getType().equals("Voucher Gift")){
                return promotion.getVoucherValue();
            }
            else if(promotion.getType().equals("Flash Sale")){
                return promotion.getPromoPercent() * currentPrice / 100;
            }
            else{
                return 0;
            }
        }
        catch(Exception e){
            log.error("Error in GetPromotionById of PublicPromotionService", e);
            return -1;
        }
    }
    @Override
    public boolean IsValidPromotion(int promotionId) {
        try{
            SharedPromotionDto promotion = GetPromotionById(promotionId);

            if(promotion == null){
                return false;
            }
            if(promotion.getStatus().equals("inactive")){
                return false;
            }
            if(promotion.getStartDate().isAfter(java.time.LocalDate.now()) || promotion.getEndDate().isBefore(java.time.LocalDate.now())){
                return false;
            }

            return true;
        }
        catch(Exception e){
            log.error("Error in IsValidPromotion of PublicPromotionService", e);
            return false;
        }
    }
    @Override
    public SharedPromotionDto GetPromotionById(int promotionId) {
        try{
            if(promotionId <= 0){
                return null;
            }

            PromotionDto promotion = promotionQueryDao.GetPromotionById(promotionId);

            if(promotion == null){
                return null;
            }
            
            return SharedPromotionDto.builder()
                    .id(promotion.getId())
                    .eventId(promotion.getEventId())
                    .minPriceToReach(promotion.getMinPriceToReach())
                    .promoPercent(promotion.getPromoPercent())
                    .voucherValue(promotion.getVoucherValue())
                    .status(promotion.getStatus())
                    .type(promotion.getType())
                    .startDate(promotion.getStartDate())
                    .endDate(promotion.getEndDate())
                    .build();
        }

        catch(Exception e){
            log.error("Error in GetPromotionById of PublicPromotionService", e);
            return null;
        }
    }
    @Override
    public int CreateVoucherOfUser(SharedVoucherOfUserDto sharedVoucherOfUser) {
        try{
            VoucherOfUser voucherOfUser = VoucherOfUser.builder()
                    .voucherValue(sharedVoucherOfUser.getVoucherValue())
                    .userId(sharedVoucherOfUser.getUserId())
                    .promotionId(sharedVoucherOfUser.getPromotionId())
                    .status(sharedVoucherOfUser.getStatus())
                    .build();
            return voucherOfUserDao.CreateVoucherOfUser(voucherOfUser);
        }
        catch(Exception e){
            log.error("Error in CreateVoucherOfUser of PublicPromotionService", e);
            return -1;
        }
    }
    @Override
    public int UpdateVoucherOfUser(SharedVoucherOfUserDto sharedvoucherOfUser) {
        try{
            if(sharedvoucherOfUser.getId() <= 0){
                return -1;
            }

            Map<String, Object> newValues = new HashMap<>();

            if(sharedvoucherOfUser.getUserId() != null){
                newValues.put("userId", sharedvoucherOfUser.getUserId());
            }
            if(sharedvoucherOfUser.getPromotionId() != -1){
                newValues.put("promotionId", sharedvoucherOfUser.getPromotionId());
            }
            if(sharedvoucherOfUser.getStatus() != null){
                newValues.put("status", sharedvoucherOfUser.getStatus());
            }
            if(sharedvoucherOfUser.getVoucherValue() != -1){
                newValues.put("voucherValue", sharedvoucherOfUser.getVoucherValue());
            }

            return voucherOfUserDao.UpdateVoucherOfUser(sharedvoucherOfUser.getId(), newValues);
        }
        catch(Exception e){
            log.error("Error in UpdateVoucherOfUser of PublicPromotionService", e);
            return 0;
        }
    }
    @Override
    public List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status) {
        try{
            return voucherOfUserDao.GetVoucherOfUsers(userId, promotionId, status);
        }
        catch(Exception e){
            log.error("Error in GetVoucherOfUsers of PublicPromotionService", e);
            return null;
        }
    }
    @Override
    public int IsValidVoucherOfUser(int voucherId) {
        try{

            if(voucherId <= 0){
                return -1;
            }
            VoucherOfUser voucherOfUser = voucherOfUserDao.GetVoucherOfUserById(voucherId);

            if(voucherOfUser == null){
                return -1;
            }

            if(voucherOfUser.getStatus().equals("USED")){
                return 0;
            }

            return 1;
        }
        catch(Exception e){
            log.error("Error in IsValidVoucherOfUser of PublicPromotionService", e);
            return -1;
        }
    }
    @Override
    public SharedVoucherOfUserDto GetVoucherOfUserById(int voucherId) {
        try{
            VoucherOfUser voucherOfUser = voucherOfUserDao.GetVoucherOfUserById(voucherId);

            if(voucherOfUser == null){
                return null;
            }

            return SharedVoucherOfUserDto.builder()
                    .id(voucherOfUser.getId())
                    .userId(voucherOfUser.getUserId())
                    .promotionId(voucherOfUser.getPromotionId())
                    .status(voucherOfUser.getStatus())
                    .voucherValue(voucherOfUser.getVoucherValue())
                    .build();
        }
        catch(Exception e){
            log.error("Error in GetVoucherOfUserById of PublicPromotionService", e);
            return null;
        }
    }

}
