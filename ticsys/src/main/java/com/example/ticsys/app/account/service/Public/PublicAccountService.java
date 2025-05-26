package com.example.ticsys.app.account.service.Public;

import com.example.ticsys.app.shared_dto.SharedUserDto;

public interface PublicAccountService {
    public SharedUserDto GetUserByUsername(String username);
}
