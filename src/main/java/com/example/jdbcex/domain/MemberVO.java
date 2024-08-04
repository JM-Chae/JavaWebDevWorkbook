package com.example.jdbcex.domain;


import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO
  {
    private String mid;
    private String mpw;
    private String mname;
    private String uuid;
  }
