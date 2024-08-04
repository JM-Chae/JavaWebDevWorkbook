package com.example.jdbcex.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j2;

@WebListener
@Log4j2
public class AppListener implements ServletContextListener
  {
    @Override
    public void contextInitialized(ServletContextEvent sce)
      {
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("appName", "JDBCEX");
      }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
      {
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
      }
  }
