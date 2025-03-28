package com.example.ticsys.sharedDto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SharedUserDto {
    private String userName;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Date birthday;
    private String gender;
}
