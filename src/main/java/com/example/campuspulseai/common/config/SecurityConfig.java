package com.example.campuspulseai.common.config;

import com.example.campuspulseai.southBound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    public static final String AUTH_BASE = "/api/auth/**";
    public static final String AUTH_ROOT = "/api/auth";
    public static final String DOCS_ROOT = "docs/**";
    public static final String DOCS_SINGLE = "/docs";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_HTML = "/swagger-ui.html";
    public static final String API_DOCS = "/v3/api-docs/**";
    public static final String WEBJARS = "/webjars/";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";

    private final IUserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    // Bean to load user details by email using the custom user repository
//    @Bean
//    UserDetailsService userDetailsService() {
//        return email -> userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
//    }

    // Bean to provide password encoding using BCrypt hashing algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean to provide the authentication manager for processing authentication requests
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //this bean is used to handle unauthorized access to the endpoints and return a custom response
    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setHeader("WWW-Authenticate", "");
            response.getWriter().write("{\"error\": \"Unauthorized access\"");
        };
    }

    //this bean is used to configure the security filter chain to apply the security rules for each endpoint and general security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(
                                        AUTH_ROOT,
                                        DOCS_ROOT,
                                        SWAGGER_UI,
                                        API_DOCS,
                                        DOCS_SINGLE,
                                        WEBJARS,
                                        SWAGGER_RESOURCES,
                                        SWAGGER_HTML,
                                        AUTH_BASE
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(csrf -> csrf.disable());
        return http.build();
    }


}
