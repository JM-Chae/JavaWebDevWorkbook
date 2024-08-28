package com.example.boot.config;


import com.example.boot.security.CustomUserDetailsService;
import com.example.boot.security.handler.Custom403Handler;
import com.example.boot.security.handler.CustomSocialLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig
  {
    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder()
      {
        return new BCryptPasswordEncoder();
      }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler()
      {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
      }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception
      {
        log.info("---------------config---------------");
        http.formLogin(form -> form.loginPage("/member/login"));

        http.csrf(config -> config.disable());
        http.rememberMe(config ->
          {
            config.key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60 * 60 * 24 * 30);
          }
        );

        http.exceptionHandling(exceptionHandling -> exceptionHandling
            .accessDeniedHandler(accessDeniedHandler()));

        http.oauth2Login(login ->login.loginPage("/member/login").successHandler(authenticationSuccessHandler()));

        return http.build();
      }

    @Bean
    public AccessDeniedHandler accessDeniedHandler()
      {
        return new Custom403Handler();
      }

    @Bean
    public PersistentTokenRepository persistentTokenRepository()
      {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
      }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
      {
        log.info("---------------web config---------------");

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
      }
  }
