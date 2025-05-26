package com.example.ticsys.app.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.app.account.model.OrganizerInfo;
import com.example.ticsys.app.account.model.User;
import com.example.ticsys.app.account.rowmapper.UserRowMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserSqlDao implements IUserDao {
    
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    public UserSqlDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public User GetUserByUsername(String username) {
        log.info("getUserByUsername of UserSqlDao");
        try{
            String sql = """
                SELECT * FROM users WHERE username = :username
            """;
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("username", username);
            User user =  namedParameterJdbcTemplate.queryForObject(sql, paramMap, new UserRowMapper());

            if(user == null) {
                return null;
            }

            List<String> roles = GetRolesOfUser(username);
            user.setRoles(roles);

            return user;
        }
        catch(Exception e)
        {
            return null;
        }

    }
    @Override
    public boolean createUser(User user) {
        String sql = """
            INSERT INTO users (username, password, email, fullName, phoneNumber, birthday, gender, avatarPath)
             values (:username, :password, :email, :fullName, :phoneNumber, :birthday, :gender, :avatarPath)
            """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", user.getUserName());
        paramMap.put("password", user.getPassWord());
        paramMap.put("email", user.getEmail());
        paramMap.put("fullName", user.getFullName());
        paramMap.put("phoneNumber", user.getPhoneNumber());
        paramMap.put("birthday", user.getBirthday());
        paramMap.put("gender", user.getGender());
        paramMap.put("avatarPath", user.getAvatarPath());

        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }
    @Override
    public boolean addRolesToUser(String username, List<String> roles) {
        String sql = """
            INSERT INTO RoleOfUser (userId, roleName) values (:username, :role)
        """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        for(String role : roles) {
            paramMap.put("role", role);
            if(namedParameterJdbcTemplate.update(sql, paramMap) != 1) {
                return false;
            }
            paramMap.remove("role");
        }
        return true;
    }
    @Override
    public List<User> GetAllUsers(String role) {
        String sql = "SELECT u.* FROM users u ";      
        List<User> result = namedParameterJdbcTemplate.query(sql, new UserRowMapper());

        for(User user : result){
            List<String> roles = GetRolesOfUser(user.getUserName());
            user.setRoles(roles);
        }
        
        return result;
    }
    private List<String> GetRolesOfUser(String username) {
        String roleSql = """
            SELECT roleName FROM RoleOfUser WHERE userId = :username
        """;
        return namedParameterJdbcTemplate.queryForList(roleSql, Map.of("username", username), String.class);
    }
    @Override
    public boolean AddOrganizerInfo(OrganizerInfo organizerInfo) {
       String sql = """
            INSERT INTO organizer_infor (userId, name, description) values (:userId, :name, :description)
       """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", organizerInfo.getUserId());
        paramMap.put("name", organizerInfo.getName());
        paramMap.put("description", organizerInfo.getDescription());

        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }
    @Override
    public boolean UpdateAvatarOfUser(String username, String avatarPath) {
        String sql = """
            UPDATE users SET avatarPath = :avatarPath WHERE username = :username
        """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("avatarPath", avatarPath);
        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }
    @Override
    public Boolean IsValidUser(String username) {
        String sql = """
            SELECT COUNT(*) FROM users WHERE username = :username
        """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, paramMap, Integer.class);
        return count != null && count > 0;
    }

}
