package com.example.ticsys.account.dao;

import java.util.List;

import com.example.ticsys.account.model.OrganizerInfo;
import com.example.ticsys.account.model.User;

public interface IUserDao {
    public List<User> GetAllUsers(String role);
    public User GetUserByUsername(String username);
    public boolean createUser(User user);
    public boolean addRolesToUser(String username, List<String> roles);
    public boolean AddOrganizerInfo(OrganizerInfo organizerInfo);
    public boolean UpdateAvatarOfUser(String username, String avatarPath);
}
