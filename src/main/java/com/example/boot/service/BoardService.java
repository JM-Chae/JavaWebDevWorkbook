package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.dto.BoardListReplyCountDTO;
import com.example.boot.dto.PageRequestDTO;
import com.example.boot.dto.PageResponseDTO;

public interface BoardService
  {
    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
  }
