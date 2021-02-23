package com.example.aviasale.service;

import com.example.aviasale.domain.dto.authDto.RegFormDto;
import com.example.aviasale.domain.entity.User;
import com.example.aviasale.domain.enums.Roles;
import com.example.aviasale.repository.RoleRepository;
import com.example.aviasale.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void createUser(RegFormDto regFormDto) {
        User user = new User();
        user.setEmail(regFormDto.getEmail());
        user.setPassword(passwordEncoder.encode(regFormDto.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByRoleName(Roles.ROLE_USER.toString())));
        userRepository.save(user);
    }
}
