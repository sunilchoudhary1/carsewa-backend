package com.project.carsewa.dto;

import com.project.carsewa.enums.UserRole;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
}
