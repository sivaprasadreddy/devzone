package com.sivalabs.devzone.web.api.resources;

import com.sivalabs.devzone.config.ApplicationProperties;
import com.sivalabs.devzone.config.security.SecurityUserDetailsService;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.config.security.TokenHelper;
import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.models.AuthUserDTO;
import com.sivalabs.devzone.domain.models.AuthenticationRequest;
import com.sivalabs.devzone.domain.models.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;
    private final SecurityUserDetailsService userDetailsService;
    private final TokenHelper tokenHelper;
    private final ApplicationProperties applicationProperties;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            String accessToken = tokenHelper.generateToken(user.getUsername());
            return ResponseEntity.ok(getAuthenticationResponse(user, accessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AuthenticationResponse> refreshAuthenticationToken(HttpServletRequest request) {
        String authToken = tokenHelper.getToken(request);
        if (authToken != null) {
            String email = tokenHelper.getUsernameFromToken(authToken);
            SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(email);
            Boolean validToken = tokenHelper.validateToken(authToken, user);
            if (validToken) {
                String refreshedToken = tokenHelper.refreshToken(authToken);
                return ResponseEntity.ok(getAuthenticationResponse(user, refreshedToken));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private AuthenticationResponse getAuthenticationResponse(SecurityUser user, String token) {
        return AuthenticationResponse.builder()
            .user(AuthUserDTO.builder()
                .name(user.getUser().getName())
                .email(user.getUser().getEmail())
                .roles(user.getUser().getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build())
            .accessToken(token)
            .expiresAt(LocalDateTime.now().plusSeconds(applicationProperties.getJwt().getExpiresIn()))
            .build();
    }
}
