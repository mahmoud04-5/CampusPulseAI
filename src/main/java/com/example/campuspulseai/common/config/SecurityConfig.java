package com.example.campuspulseai.common.config;

import com.example.campuspulseai.southBound.repository.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
public class SecurityConfig {

    private final IUserRepository userRepository;

    public SecurityConfig(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Bean to load user details by email using the custom user repository
    @Bean
    UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

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

    //this bean is used to manage user details in the database and to define the queries for the custom tables in the db
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager
                .setUsersByUsernameQuery("select id, password, active from system_users where user_id=? ");

        jdbcUserDetailsManager
                .setAuthoritiesByUsernameQuery("select id, role from roles where user_id=?");

        return jdbcUserDetailsManager;
    }

    //this bean is used to configure the security filter chain to apply the security rules for each endpoint and general security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/auth/**", "docs/**", "/swagger-ui/**", "/v3/api-docs/**", "/docs"
                                , "webjars/**", "/swagger-recources/**", "/swagger-ui.html", "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        http.exceptionHandling(exceptionHanling ->
                exceptionHanling
                        .authenticationEntryPoint(authenticationEntryPoint()));

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }


}
