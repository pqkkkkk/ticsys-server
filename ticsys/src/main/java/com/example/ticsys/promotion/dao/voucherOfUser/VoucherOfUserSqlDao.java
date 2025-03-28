package com.example.ticsys.promotion.dao.voucherOfUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.promotion.model.VoucherOfUser;
import com.example.ticsys.promotion.rowmapper.VoucherOfUserRowMapper;

@Repository
public class VoucherOfUserSqlDao implements IVoucherOfUserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public VoucherOfUserSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public int CreateVoucherOfUser(VoucherOfUser voucherOfUser) {
        String sql = """
            INSERT INTO voucherOfUser (userId, promotionId, status,voucherValue)
            VALUES (:userId, :promotionId, :status, :voucherValue)
                """;
        
        Map<String, Object> paramMap = Map.of(
            "userId", voucherOfUser.getUserId(),
            "promotionId", voucherOfUser.getPromotionId(),
            "status", voucherOfUser.getStatus(),
            "voucherValue", voucherOfUser.getVoucherValue()
        );

        return jdbcTemplate.update(sql, paramMap);
    }

    @Override
    public int DeleteVoucherOfUser(int voucherId) {
        String sql = """
            DELETE FROM voucherOfUser
            WHERE id = :id
                """;
        
        Map<String, Object> paramMap = Map.of(
            "id", voucherId
        );

        return jdbcTemplate.update(sql, paramMap);
    }

    @Override
    public int UpdateVoucherOfUser(VoucherOfUser voucherOfUser) {
        String sql ="""
            UPDATE voucherOfUser
            SET userId = :userId, promotionId = :promotionId, status = :status, voucherValue = :voucherValue
            WHERE id = :id
                """;
            
        Map<String, Object> paramMap = Map.of(
            "userId", voucherOfUser.getUserId(),
            "promotionId", voucherOfUser.getPromotionId(),
            "status", voucherOfUser.getStatus(),
            "voucherValue", voucherOfUser.getVoucherValue(),
            "id", voucherOfUser.getId()
        );

        return jdbcTemplate.update(sql, paramMap);
    }

    @Override
    public List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status) {
        String sql = """
            SELECT * FROM voucherOfUser
            WHERE 1=1
                """;
        Map<String, Object> paramMap = new HashMap<>();

        if(userId != null){
            sql += " AND userId = :userId";
            paramMap.put("userId", userId);
        }
        if(promotionId != -1){
            sql += " AND promotionId = :promotionId";
            paramMap.put("promotionId", promotionId);
        }
        if(status != null){
            sql += " AND status = :status";
            paramMap.put("status", status);
        }

        return jdbcTemplate.query(sql, paramMap, new VoucherOfUserRowMapper());
    }
    @Override
    public VoucherOfUser GetVoucherOfUserById(int voucherId) {
        String sql = """
            SELECT * FROM voucherOfUser
            WHERE id = :id
                """;
        Map<String, Object> paramMap = Map.of(
            "id", voucherId
        );

        return jdbcTemplate.queryForObject(sql, paramMap, new VoucherOfUserRowMapper());
    }
    @Override
    public int UpdateVoucherOfUser(int id, Map<String, Object> newValues) {
        if (newValues.isEmpty()) {
            throw new IllegalArgumentException("orderValues cannot be empty");
        }
          
        StringBuilder sqlBuilder = new StringBuilder("UPDATE [voucherOfUser] SET ");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        int count = 0;
        for (Map.Entry<String, Object> entry : newValues.entrySet()) {
            if (count > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append(entry.getKey()).append(" = :").append(entry.getKey());
            paramMap.put(entry.getKey(), entry.getValue());
            count++;
        }
        sqlBuilder.append(" WHERE id = :id");
        
        String sql = sqlBuilder.toString();
        return jdbcTemplate.update(sql, paramMap);
    }

}
