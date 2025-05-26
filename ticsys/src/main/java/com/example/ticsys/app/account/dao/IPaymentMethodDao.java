package com.example.ticsys.app.account.dao;

import java.util.List;

import com.example.ticsys.app.account.model.PaymentMethod;

public interface IPaymentMethodDao {
    public List<PaymentMethod> GetPaymentMethodsOfUser(String userId, String bankName);
}
