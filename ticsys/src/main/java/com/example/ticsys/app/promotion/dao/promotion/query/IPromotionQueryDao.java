package com.example.ticsys.app.promotion.dao.promotion.query;

import java.util.List;

import com.example.ticsys.app.promotion.dto.PromotionDto;

public interface IPromotionQueryDao {
    public List<PromotionDto> GetPromotions(int eventId, String status, String type);
    public List<PromotionDto> GetPromotionsWithOrderCount(int eventId, String status, String type);
    public PromotionDto GetPromotionById(int promotionId);
}
