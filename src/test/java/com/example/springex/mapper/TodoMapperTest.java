package com.example.springex.mapper;

import com.example.springex.domain.TodoVO;
import com.example.springex.dto.PageRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root.context.xml")
public class TodoMapperTest
  {
    @Autowired(required = false)
    private TodoMapper todoMapper;

    @Test
    public void testGetTime()
      {
        log.info(todoMapper.getTime());
      }

    @Test
    public void  testInsert()
      {
        TodoVO todoVO = TodoVO.builder()
            .title("test")
            .dueDate(LocalDate.of(2024,8,6))
            .writer("user00")
            .build();

        todoMapper.insert(todoVO);
      }

    @Test
    public void testSelectAll()
      {
        List<TodoVO> voList = todoMapper.selectAll();
        voList.forEach(vo -> log.info(vo));
      }

    @Test
    public void testSelectOne()
      {
        TodoVO vo = todoMapper.selectOne(3L);
        log.info(vo);
      }

    @Test
    public void testSelectList()
      {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(1)
            .size(10)
            .build();

        List<TodoVO> voList = todoMapper.selectList(pageRequestDTO);
        voList.forEach(vo -> log.info(vo));
      }

    @Test
    public void testSelectSearch()
      {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(1)
            .size(10)
            .types(new String[]{"t","w"})
            .keyword("test")
//            .finished(true)
            .from(LocalDate.of(2024,8,6))
            .to(LocalDate.of(2024,8,7))
            .build();

        List<TodoVO> voList = todoMapper.selectList(pageRequestDTO);
        voList.forEach(vo -> log.info(vo));

        log.info(todoMapper.getCount(pageRequestDTO));
      }
  }
