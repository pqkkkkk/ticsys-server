package com.example.ticsys.app.account.dto.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String userName;
    private String passWord;
}
