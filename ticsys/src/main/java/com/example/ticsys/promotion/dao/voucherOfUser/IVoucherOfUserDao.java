package com.example.ticsys.promotion.dao.voucherOfUser;

import java.util.List;
import java.util.Map;

import com.example.ticsys.promotion.model.VoucherOfUser;

public interface IVoucherOfUserDao {
    public int CreateVoucherOfUser(VoucherOfUser voucherOfUser);
    public int DeleteVoucherOfUser(int voucherId);
    public int UpdateVoucherOfUser(VoucherOfUser voucherOfUser);
    List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status);
    public VoucherOfUser GetVoucherOfUserById(int voucherId);
    public int UpdateVoucherOfUser(int id, Map<String, Object> newValues);
} 
