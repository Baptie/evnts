package com.example.gestiongoogle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class Security {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(auth->{
                        auth.requestMatchers("/").permitAll();
                        auth.anyRequest().authenticated();
                        })
                        .oauth2Login(oauth2 -> oauth2
                                .successHandler(new CustomLoginSuccessHandler())) // redirection vers /secured
                .logout(logout -> logout
                        .logoutSuccessUrl("/")  // URL de redirection après déconnexion
                        .permitAll()
                )
                .formLogin(withDefaults())
                        .formLogin(withDefaults())
                        .build();

    }
}