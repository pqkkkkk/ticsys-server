package com.example.ticsys.app.account.dao;

import java.util.List;

import com.example.ticsys.app.account.model.OrganizerInfo;
import com.example.ticsys.app.account.model.User;

public interface IUserDao {
    public List<User> GetAllUsers(String role);
    public User GetUserByUsername(String username);
    public boolean createUser(User user);
    public boolean addRolesToUser(String username, List<String> roles);
    public boolean AddOrganizerInfo(OrganizerInfo organizerInfo);
    public boolean UpdateAvatarOfUser(String username, String avatarPath);
    public Boolean IsValidUser(String username);
}
