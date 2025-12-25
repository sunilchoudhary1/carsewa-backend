package com.project.carsewa.service.auth;

import com.project.carsewa.dto.SignupRequestDto;
import com.project.carsewa.dto.Userdto;

public interface AuthService {

    Userdto createCustomer(SignupRequestDto signupRequestDto);
    boolean hasUserByEmail(String email);
}
