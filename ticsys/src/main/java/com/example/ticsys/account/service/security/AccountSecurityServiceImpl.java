package com.example.ticsys.account.service.security;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountSecurityServiceImpl implements AccountSercurityService {

    @Override
    public boolean IsAccountOwner(String username) {
        try{
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                                            .map(r -> r.getAuthority()).toList();
            if(currentRoles.contains("ROLE_ADMIN")){
                return true;
            }
            else{
                return currentUsername.equals(username);
            }
        } catch (Exception e) {
            log.error("IsAccountOwner of AccountSecurityServiceImpl, username: ", username);
            return false;
        }
    }

}
