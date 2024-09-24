

package com.skander.employee_manager.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skander.employee_manager.email.EmailService;
import com.skander.employee_manager.email.EmailTemplateName;
import com.skander.employee_manager.role.RoleRepository;
import com.skander.employee_manager.security.JwtService;
import com.skander.employee_manager.user.Token;
import com.skander.employee_manager.user.TokenRepository;
import com.skander.employee_manager.user.User;
import com.skander.employee_manager.user.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";
    @Value("${custom.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException{
        var userRoles = roleRepository.findByName("USER")
            .orElseThrow(()-> new IllegalStateException("ROLE USER WAS NOT INITIALIZED"));
        var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountLocked(false)
            .enabled(false)
            .roles(List.of(userRoles))
            .build();
        userRepository.save(user);
        sendValidationEmail(user);


    }
    private void sendValidationEmail(User user) throws MessagingException{
        var newToken = generateAndSaveActivateToken(user);
        System.out.print(RED + newToken +"the token in sendcalidationemail" + RESET + "\n"); 

        emailService.sendEmail(
            user.getEmail(),
            user.getFullName(), 
            EmailTemplateName.ACTIVATE_ACCOUNT, 
            activationUrl,
            newToken,
            "account activation");
    }

    private String generateAndSaveActivateToken(User user){
        //generate token
        String generatedToken = generateActivationToken(6);
        var token = Token.builder()
            .token(generatedToken)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .user(user)
            .build();
        try {
            tokenRepository.save(token);
        } catch (DataIntegrityViolationException e) {
            // Handle the constraint violation
            System.out.println("Constraint violation: " + e.getMessage());
            }

        
        System.out.print(RED + token.getToken() + "the token in generate and save token " + RESET + "\n"); 
        return generatedToken;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i=0;i<length; i++ ){
            int randomIndex= secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        System.out.println(RED + codeBuilder.toString() + "THE token in generateActivationToken" + RESET + "\n");
        return codeBuilder.toString();
    }
    public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
        var auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword())
        );
        var claims = new HashMap<String, Object>();
        var user =((User)auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    // @Transactional
    public void activateAccount(String token) throws MessagingException {
        System.out.println("AAAAAAAAAAAAAAAAA" + token);
        Token savedToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("invalid token "));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email ");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
            .orElseThrow(() -> new UsernameNotFoundException("  User not Found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt((LocalDateTime.now()));
        tokenRepository.save(savedToken);
        
    }
}
