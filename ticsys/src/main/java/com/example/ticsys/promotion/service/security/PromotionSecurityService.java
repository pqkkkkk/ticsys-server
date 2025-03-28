package com.example.ticsys.promotion.service.security;

public interface PromotionSecurityService {
    public boolean CanAccessCreateAndUpdatePromotion(Integer eventId);
    public boolean CanAccessGetPromotions(Integer eventId);
    public boolean CanGetPromotionsWithOrderCount(Integer eventId);
    public Boolean CanAccessGetVoucherOfUser(String userId, Integer promotionId);
}
