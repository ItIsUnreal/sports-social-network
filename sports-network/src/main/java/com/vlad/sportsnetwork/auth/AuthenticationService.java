package com.vlad.sportsnetwork.auth;


import com.vlad.sportsnetwork.role.RoleRepository;
import com.vlad.sportsnetwork.user.Token;
import com.vlad.sportsnetwork.user.TokenRepository;
import com.vlad.sportsnetwork.user.User;
import com.vlad.sportsnetwork.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;



    public void register(RegistrationRequest request) {


        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role user not init"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();


        userRepository.save(user);
        senfValidationEmail(user);

    }

    private void senfValidationEmail(User user) {

        var newToken = generateAndSaveActivationToken(user);
        

    }

    private Object generateAndSaveActivationToken(User user) {
        String generatedtoken = generateActivationCode(6);
        var token = Token.builder()
                    .token(generatedtoken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(20))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedtoken;

    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int rand = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(rand));

        }

        return codeBuilder.toString();

    }
}
