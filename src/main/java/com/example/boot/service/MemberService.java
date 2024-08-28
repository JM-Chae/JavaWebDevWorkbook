package com.example.boot.service;

import com.example.boot.dto.MemberJoinDTO;

public interface MemberService
  {
    static class MidExistException extends Exception
      {
      }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
  }
