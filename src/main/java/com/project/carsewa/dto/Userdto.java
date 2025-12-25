package com.project.carsewa.dto;

import com.project.carsewa.enums.UserRole;
import lombok.Data;

@Data
public class Userdto {

    private Long id;
    private String name;
    private String email;

    private UserRole role;
}
