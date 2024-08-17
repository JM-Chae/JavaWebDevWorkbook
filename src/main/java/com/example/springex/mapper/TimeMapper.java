package com.example.springex.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

public interface TimeMapper
  {
    @Select("select now()")
    String getTime();
  }
