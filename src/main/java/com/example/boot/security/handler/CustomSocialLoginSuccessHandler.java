package com.example.boot.security.handler;

import com.example.boot.security.dto.MemberSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler
  {
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
      {
        log.info("--------------------------------");
        log.info("Custom Social Login Success Handler");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO  = (MemberSecurityDTO) authentication.getPrincipal();

        String encodedPw = memberSecurityDTO.getMpw();

        if (memberSecurityDTO.isSocial() && (memberSecurityDTO.getMpw().equals("1111") ||
            passwordEncoder.matches("1111", memberSecurityDTO.getMpw())))
          {
            log.info("Should change password");
            log.info("Redirect to member modify");
            response.sendRedirect("/member/modify");

            return;
          }else
          {
            response.sendRedirect("/board/list");
          }
      }
  }
