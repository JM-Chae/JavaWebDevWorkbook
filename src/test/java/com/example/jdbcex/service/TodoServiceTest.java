package com.example.jdbcex.service;

import com.example.jdbcex.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@Log4j2
public class TodoServiceTest
  {
    private TodoService todoService;

    @BeforeEach
    public void ready()
      {
        todoService = TodoService.INSTANCE;
      }

    @Test
    public void testRegister() throws  Exception
      {
        TodoDTO todoDTO = TodoDTO.builder()
            .title("JDBC Test Title")
            .dueDate(LocalDate.now())
            .build();

        log.info("-----------------------------------------");
        log.info(todoDTO);

        todoService.register(todoDTO);
      }
  }
