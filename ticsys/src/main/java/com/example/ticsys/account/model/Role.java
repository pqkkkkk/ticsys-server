package com.example.ticsys.account.model;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class Role implements GrantedAuthority {
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
