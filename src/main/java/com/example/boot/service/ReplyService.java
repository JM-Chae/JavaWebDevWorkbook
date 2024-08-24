package com.example.boot.service;


import com.example.boot.domain.Reply;
import com.example.boot.dto.PageRequestDTO;
import com.example.boot.dto.PageResponseDTO;
import com.example.boot.dto.ReplyDTO;
import org.springframework.data.domain.Pageable;

public interface ReplyService
  {
    Long register(ReplyDTO replyDTO);
    ReplyDTO read(Long rno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
  }