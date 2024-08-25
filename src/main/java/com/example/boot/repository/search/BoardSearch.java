package com.example.boot.repository.search;

import com.example.boot.domain.Board;
import com.example.boot.dto.BoardListAllDTO;
import com.example.boot.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch
  {
    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);
    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);
  }
