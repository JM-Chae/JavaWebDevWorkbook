package com.example.jdbcex.filter;


import com.example.jdbcex.dto.MemberDTO;
import com.example.jdbcex.service.MemberService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(urlPatterns = {"/todo/*"})
@Log4j2
public class LoginCheckFilter implements Filter
  {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
      {
        log.info("Login check filter....");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();


        if(session.getAttribute("loginInfo") != null)
          {
            chain.doFilter(request, response);
            return;
          }

        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        if (cookie == null)
          {
            resp.sendRedirect("/login");
            return;
          }

        log.info("쿠키가 존재");
        String uuid = cookie.getValue();

        try
          {
            MemberDTO memberDTO = MemberService.INSTANCE.getByUUID(uuid);
            log.info(memberDTO);
            if(memberDTO == null)
              {
                throw new EOFException("Cookie value is not valid");
              }

            session.setAttribute("loginInfo", memberDTO);
            chain.doFilter(request, response);
          }catch (Exception e)
            {
              e.printStackTrace();
              resp.sendRedirect("/login");
            }
      }

    private Cookie findCookie(Cookie[] cookies, String name)
      {
        if(cookies == null || cookies.length == 0)
          {
            return null;
          }

        Optional<Cookie> result = Arrays.stream(cookies)
            .filter(ck -> ck.getName().equals(name))
            .findFirst();

        return result.isPresent() ? result.get() : null;
      }
  }
