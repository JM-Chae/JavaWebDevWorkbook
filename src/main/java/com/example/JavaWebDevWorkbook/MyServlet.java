package com.example.JavaWebDevWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = " myServlet", urlPatterns = "/my")
public class MyServlet extends HttpServlet
  {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();

        writer.println("<html><body><h1>My Servlet</h1></body></html>");

        writer.close();
      }
  }
