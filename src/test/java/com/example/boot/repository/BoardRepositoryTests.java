package com.example.boot.repository;

import com.example.boot.domain.Board;
import com.example.boot.domain.BoardImage;
import com.example.boot.dto.*;
import com.example.boot.service.BoardService;
import com.example.boot.service.BoardServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
class BoardRepositoryTests
  {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private BoardService boardService;

    @Test
    public void testInsert()
      {
        IntStream.rangeClosed(1, 100).forEach(i ->
          {
            Board board = Board.builder()
                .title("title: " + i)
                .content("content: " + i)
                .writer("user: " + i)
                .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
          });
      }

    @Test
    public void testSelect()
      {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board);
      }

    @Test
    public void testUpdate()
      {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("update.... title: 100", "update.... content: 100");
        boardRepository.save(board);
      }

    @Test
    public void testDelete()
      {
        Long bno = 1L;
        boardRepository.deleteById(bno);
      }

    @Test
    public void testPaging()
      {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Board> todoList = result.getContent();
        todoList.forEach(board -> log.info(board));
      }

    @Test
    public void testSearch1()
      {
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
      }

    @Test
    public void testSearchAll()
      {
        String[] types = {"t", "c", "w"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + ": " + result.hasNext());
        result.forEach(board -> log.info(board));
      }

    @Test
    public void testSearchReplyCount()
      {
        String[] types = {"t", "c", "w"};
        String keyword = "22";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + ": " + result.hasNext());
        result.getContent().forEach(board -> log.info(board));
      }

    @Test
    public void testInsertWithImages()
      {
        Board board = Board.builder()
            .title("Image Test")
            .content("첨부파일 테스트")
            .writer("tester")
            .build();

        for (int i = 0; i < 3; i++)
          {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
          }
        boardRepository.save(board);
      }

    @Test
    public void testReadWithImages()
      {
        Optional<Board> result = boardRepository.findByWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("--------");
        for (BoardImage boardImage : board.getImageSet())
          {
            log.info(boardImage);
          }
      }

    @Transactional
    @Commit
    @Test
    public void testModifyImages()
      {
        Optional<Board> result = boardRepository.findByWithImages(1L);
        Board board = result.orElseThrow();

        board.clearImages();

        for (int i = 0; i < 2; i++)
          {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
          }
        boardRepository.save(board);
      }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll()
      {
        Long bno = 3L;
        replyRepository.deleteByBoard_Bno(bno);
        boardRepository.deleteById(bno);
      }

    @Test
    public void testInsertAll()
      {
        for (int i = 1; i <= 100; i++)
          {
            Board board = Board.builder()
                .title("Title" + i)
                .content("Content" + i)
                .writer("Writer" + i)
                .build();
            for (int j = 0; j < 3; j++)
              {
                if (i % 5 == 0)
                  {
                    continue;
                  }
                board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
              }
            boardRepository.save(board);

          }
      }

    @Transactional
    @Test
    public void testSearchImageReplyCount()
      {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
//        boardRepository.searchWithAll(null, null, pageable);
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);
        log.info("---------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));
      }

    @Test
    public void testRegisterWithImages()
      {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
            .title("Title")
            .content("Content")
            .writer("Writer")
            .build();

        boardDTO.setFileNames(
            Arrays.asList(
                UUID.randomUUID() + "_aaa.jpg",
                UUID.randomUUID() + "_bbb.jpg",
                UUID.randomUUID() + "_ccc.jpg"
            )
        );
        Long bno = boardService.register(boardDTO);
        log.info("bno: " + bno);
      }

    @Test
    public void testReadAll()
      {
        Long bno = 101L;
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);
        for (String fileName : boardDTO.getFileNames())
          {
            log.info(fileName);
          }
      }

    @Test
    public void testModify()
      {
        BoardDTO  boardDTO = BoardDTO.builder()
            .bno(101L)
            .title("Title UD")
            .content("Content UD")
            .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));
        boardService.modify(boardDTO);
      }

    @Test
    public void testRemoveALL()
      {
        Long bno = 1L;
        boardService.remove(bno);
      }

    @Test
    public void testListWithAll()
      {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(1)
            .size(10)
            .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithALL(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO ->
          {
            log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());

            if (boardListAllDTO.getBoardImages() != null)
              {
                for(BoardImageDTO boardImage : boardListAllDTO.getBoardImages())
                  {
                    log.info(boardImage);
                  }
              }
            log.info("-------------------");
          });
      }
  }