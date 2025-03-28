package com.example.ticsys.promotion.dao.promotion.query;

import java.util.List;

import com.example.ticsys.promotion.dto.PromotionDto;

public interface IPromotionQueryDao {
    public List<PromotionDto> GetPromotions(int eventId, String status, String type);
    public List<PromotionDto> GetPromotionsWithOrderCount(int eventId, String status, String type);
    public PromotionDto GetPromotionById(int promotionId);
}
