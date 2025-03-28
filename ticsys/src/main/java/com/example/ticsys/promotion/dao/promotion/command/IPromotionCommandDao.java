package com.example.ticsys.promotion.dao.promotion.command;

import com.example.ticsys.promotion.model.Promotion;

public interface IPromotionCommandDao {
    public int CreatePromotion(Promotion promotion);
    public int UpdatePromotion(Promotion promotion);
}
