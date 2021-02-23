package com.example.aviasale.controller;

import com.example.aviasale.domain.dto.authDto.LoginFormDto;
import com.example.aviasale.domain.dto.authDto.RefreshTokenDto;
import com.example.aviasale.domain.dto.authDto.RegFormDto;
import com.example.aviasale.domain.entity.User;
import com.example.aviasale.security.JwtTokenProvider;
import com.example.aviasale.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginFormDto loginFormDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginFormDto.getEmail(), loginFormDto.getPassword()));
            User user = userService.findByEmail(loginFormDto.getEmail());
            return getTokens(user, loginFormDto.getEmail());
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return new ResponseEntity<>("invalid user/email or user not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        boolean isValid = jwtTokenProvider.validateRefreshToken(refreshToken);

        if (isValid) {
            User user = userService.findByEmail(jwtTokenProvider.getEmailFromToken(refreshToken));
            if (user.getRefreshToken().equals(refreshToken)) {
                return getTokens(user, user.getEmail());
            }
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getTokens(User user, String email) {
        String accessToken = jwtTokenProvider.generateAccessToken(email, user.getRoles());
        String refreshToken = jwtTokenProvider.generateRefreshToken(email, user.getRoles());
        user.setRefreshToken(refreshToken);
        userService.update(user);
        Map<Object, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/api/auth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PostMapping("/api/auth/reg")
    @ResponseStatus(HttpStatus.OK)
    public void reg(@RequestBody RegFormDto regFormDto) {
        userService.createUser(regFormDto);
    }
}
