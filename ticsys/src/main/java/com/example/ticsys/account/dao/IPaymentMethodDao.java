package com.example.ticsys.account.dao;

import java.util.List;

import com.example.ticsys.account.model.PaymentMethod;

public interface IPaymentMethodDao {
    public List<PaymentMethod> GetPaymentMethodsOfUser(String userId, String bankName);
}
