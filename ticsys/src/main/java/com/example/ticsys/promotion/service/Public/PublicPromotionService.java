package com.example.ticsys.promotion.service.Public;

import java.util.List;

import com.example.ticsys.promotion.model.VoucherOfUser;
import com.example.ticsys.sharedDto.SharedPromotionDto;
import com.example.ticsys.sharedDto.SharedVoucherOfUserDto;

public interface PublicPromotionService {
    public int GetReductionOfPromotion(int promotionId, int currentPrice);
    public boolean IsValidPromotion(int promotionId);
    public SharedPromotionDto GetPromotionById(int promotionId);

    public int CreateVoucherOfUser(SharedVoucherOfUserDto sharedVoucherOfUser);
    public int UpdateVoucherOfUser(SharedVoucherOfUserDto sharedVoucherOfUser);
    public List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status);
    public int IsValidVoucherOfUser(int voucherId);
    public SharedVoucherOfUserDto GetVoucherOfUserById(int voucherId);
}
