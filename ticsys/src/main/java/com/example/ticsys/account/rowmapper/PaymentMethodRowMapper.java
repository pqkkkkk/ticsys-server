package com.example.ticsys.account.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.example.ticsys.account.model.PaymentMethod;

public class PaymentMethodRowMapper implements RowMapper<PaymentMethod> {

    @Override
    @Nullable
    public PaymentMethod mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(rs.getInt("id"));
        paymentMethod.setUserId(rs.getString("userId"));
        paymentMethod.setBankAccountNumber(rs.getString("bankAccountNumber"));
        paymentMethod.setBankName(rs.getString("bankName"));
        return paymentMethod;
    }

}
