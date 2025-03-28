package com.example.ticsys.promotion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticsys.promotion.dao.promotion.command.IPromotionCommandDao;
import com.example.ticsys.promotion.dao.promotion.query.IPromotionQueryDao;
import com.example.ticsys.promotion.dao.voucherOfUser.IVoucherOfUserDao;
import com.example.ticsys.promotion.dto.PromotionDto;
import com.example.ticsys.promotion.dto.PromotionsResponse;
import com.example.ticsys.promotion.model.Promotion;
import com.example.ticsys.promotion.model.VoucherOfUser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {
    private final IPromotionCommandDao promotionCommandDao;
    private final IPromotionQueryDao promotionQueryDao;
    private final IVoucherOfUserDao voucherOfUserDao;

    @Autowired
    public PromotionServiceImpl(IVoucherOfUserDao voucherOfUserDao, IPromotionCommandDao promotionCommandDao, IPromotionQueryDao promotionQueryDao) {
        this.promotionCommandDao = promotionCommandDao;
        this.promotionQueryDao = promotionQueryDao;
        this.voucherOfUserDao = voucherOfUserDao;
    }
    @Override
    public int CreatePromotion(Promotion promotion) {
        try{
            promotion.setStatus("active");
            return promotionCommandDao.CreatePromotion(promotion);
        }
        catch(Exception e){
            log.error("Error in CreatePromotion of PromotionService", e);
            return -1;
        }
    }

    @Override
    public int UpdatePromotion(Promotion promotion) {
        try{
            return promotionCommandDao.UpdatePromotion(promotion);
        }
        catch(Exception e){
            log.error("Error in UpdatePromotion of PromotionService", e);
            return 0;
        }
    }

    @Override
    public PromotionsResponse GetPromotions(int eventId, String status, String type) {
        try{
            List<PromotionDto>promotions = promotionQueryDao.GetPromotions(eventId, status, type);

            PromotionsResponse response = PromotionsResponse.builder()
                    .message("success")
                    .promotions(promotions)
                    .build();

            return response;
        }
        catch(Exception e){
            log.error("Error in GetPromotions of PromotionService", e);
            return PromotionsResponse.builder()
                    .promotions(null)
                    .message("error")
                    .build();
        }
    }
    @Override
    public PromotionsResponse GetReductionInfoOfPromotionsOfEvent(int eventId, int currentPrice) {
        try{
            List<PromotionDto> promotions =  promotionQueryDao.GetPromotions(eventId, "active", null);

            for(PromotionDto promotion : promotions){
                String promotionType = promotion.getType();

                switch (promotionType) {
                    case "Voucher Gift":
                        if(currentPrice < promotion.getMinPriceToReach()){
                            promotion.setReduction(0);
                        }
                        else{
                            promotion.setReduction(promotion.getVoucherValue());
                        }
                        break;
                    case "Flash Sale":
                        int reduction = promotion.getPromoPercent() * currentPrice / 100;
                        promotion.setReduction(reduction);
                        break;
                    default:
                        break;
                }
            }
            
            PromotionsResponse response =  PromotionsResponse.builder()
                    .message("success")
                    .promotions(promotions)
                    .build();
            return response;

        }
        catch(Exception e){
            log.error("Error in GetInfoAboutPromotionsOfEvent of PromotionService", e);
            return PromotionsResponse.builder()
                    .promotions(null)
                    .message("error")
                    .build();
        }
    }
    @Override
    public PromotionDto GetPromotionById(int id) {
        try{
            return promotionQueryDao.GetPromotionById(id);
        }
        catch(Exception e){
            log.error("Error in GetPromotionById of PromotionService", e);
            return null;
        }
    }
    @Override
    public List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status) {
        try{
            return voucherOfUserDao.GetVoucherOfUsers(userId, promotionId, status);
        }
        catch(Exception e){
            log.error("Error in GetVoucherOfUsers of PromotionService", e);
            return null;
        }
    }
    @Override
    public PromotionsResponse GetPromotionsWithOrderCount(int eventId, String status, String type) {
        try{
            List<PromotionDto>promotions = promotionQueryDao.GetPromotionsWithOrderCount(eventId, status, type);

            PromotionsResponse response = PromotionsResponse.builder()
                    .message("success")
                    .promotions(promotions)
                    .build();

            return response;
        }
        catch(Exception e){
            log.error("Error in GetPromotionsWithOrderCount of PromotionService", e);
            return PromotionsResponse.builder()
                    .promotions(null)
                    .message("error")
                    .build();
        }
    }

}
