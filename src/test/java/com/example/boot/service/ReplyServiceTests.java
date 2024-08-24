package com.example.boot.service;

import com.example.boot.dto.ReplyDTO;
import com.example.boot.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyServiceTests
  {
    @Autowired
    private ReplyService replyService;
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testRegister()
      {
        ReplyDTO replyDTO = ReplyDTO.builder()
            .replyText("test!!!")
            .replyer("TESTER!!!!")
            .bno(22L)
            .build();
        log.info(replyService.register(replyDTO));
        log.info(replyDTO);
      }
  }
