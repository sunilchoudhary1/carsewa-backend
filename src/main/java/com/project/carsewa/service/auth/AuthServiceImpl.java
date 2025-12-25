package com.project.carsewa.service.auth;

import com.project.carsewa.dto.SignupRequestDto;
import com.project.carsewa.dto.Userdto;
import com.project.carsewa.entity.User;
import com.project.carsewa.enums.UserRole;
import com.project.carsewa.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){

        User adminAccount = userRepository.findByRole(UserRole.ADMIN);

        if(adminAccount == null){
            User newAdmin = new User();
            newAdmin.setName("admin");
            newAdmin.setEmail("admin");
            newAdmin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            newAdmin.setRole(UserRole.ADMIN);
            userRepository.save(newAdmin);
            System.out.println("Admin account created Successfully");
        }
    }

    @Override
    public Userdto createCustomer(SignupRequestDto signupRequestDto){
        User user = new User();
        user.setName(signupRequestDto.getName());
        user.setEmail(signupRequestDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDto.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);

        Userdto userdto = new Userdto();
        userdto.setId(createdUser.getId());
        userdto.setName(createdUser.getName());
        userdto.setEmail(createdUser.getEmail());
        userdto.setRole(createdUser.getRole());
        return userdto;
    }
    //http://localhost:8080/api/auth/signup
    @Override
    public boolean hasUserByEmail(String email){

        return userRepository.findFirstByEmail(email).isPresent();

    }
}
