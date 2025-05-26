package com.example.ticsys.app.promotion.dao.promotion.command;

import com.example.ticsys.app.promotion.model.Promotion;

public interface IPromotionCommandDao {
    public int CreatePromotion(Promotion promotion);
    public int UpdatePromotion(Promotion promotion);
}
