package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.dto.PageRequestDTO;
import com.example.boot.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class BoardServiceTests
  {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister()
      {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
            .title("Sample Title")
            .content("Sample Content")
            .writer("user00")
            .build();

        Long bno = boardService.register(boardDTO);
        log.info(bno);
      }

    @Test
    public void testModify()
      {
        BoardDTO boardDTO = BoardDTO.builder()
            .bno(101L)
            .title("Update!")
            .content("Update!")
            .build();

        boardService.modify(boardDTO);
      }

    @Test
    public void testList()
      {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .type("tcw")
            .keyword("1")
            .page(1)
            .size(10)
            .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
      }
  }