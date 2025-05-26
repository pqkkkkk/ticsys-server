package com.example.ticsys.app.promotion.service;

import java.util.List;

import com.example.ticsys.app.promotion.dto.PromotionsResponse;
import com.example.ticsys.app.promotion.model.Promotion;
import com.example.ticsys.app.promotion.model.VoucherOfUser;

public interface PromotionService {
    int CreatePromotion(Promotion promotion);
    int UpdatePromotion(Promotion promotion);
    Promotion GetPromotionById(int id);
    PromotionsResponse GetPromotions(int eventId, String status, String type);
    PromotionsResponse GetReductionInfoOfPromotionsOfEvent(int eventId, int currentPrice);
    PromotionsResponse GetPromotionsWithOrderCount(int eventId, String status, String type);
    List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status);
}
