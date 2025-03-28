package com.example.ticsys.account.dto.response;

import com.example.ticsys.account.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRepsonse {
    private boolean authenticated;
    private String token;
    private String message;
    private User user;
}
