package com.example.ticsys.account.service.Public;

import com.example.ticsys.sharedDto.SharedUserDto;

public interface PublicAccountService {
    public SharedUserDto GetUserByUsername(String username);
}
