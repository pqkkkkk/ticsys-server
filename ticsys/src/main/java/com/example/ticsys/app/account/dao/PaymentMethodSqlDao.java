package com.example.ticsys.app.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.app.account.model.PaymentMethod;
import com.example.ticsys.app.account.rowmapper.PaymentMethodRowMapper;

@Repository
public class PaymentMethodSqlDao implements IPaymentMethodDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PaymentMethodSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<PaymentMethod> GetPaymentMethodsOfUser(String userId, String bankName) {
        String sql = "SELECT * FROM paymentmethod WHERE userId = :userId AND bankName = :bankName";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("bankName", bankName);
        return jdbcTemplate.query(sql, params, new PaymentMethodRowMapper());
    }

}
