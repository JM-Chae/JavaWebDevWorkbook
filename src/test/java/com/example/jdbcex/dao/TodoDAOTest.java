package com.example.jdbcex.dao;

import com.example.jdbcex.domain.TodoVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class TodoDAOTest
  {

    private TodoDAO todoDAO;

    @BeforeEach
    public void ready()
      {
        todoDAO = new TodoDAO();
      }

    @Test
    public void testTime() throws Exception
      {
        System.out.println(todoDAO.getTime2());
      }

    @Test
    public void testInsert() throws Exception
      {
        TodoVO todoVO = TodoVO.builder()
            .title("Sample Title...")
            .dueDate(LocalDate.of(2021,12,31))
            .build();

        todoDAO.insert(todoVO);
      }

    @Test
    public void testList() throws Exception
      {
        List<TodoVO> list = todoDAO.selectAll();
        list.forEach(System.out::println);
      }

    @Test
    public void testSelectOne() throws  Exception
      {
        Long tno = 1L;
        TodoVO vo = todoDAO.selevtOne(tno);
        System.out.println(vo);
      }

    @Test
    public void testDeleteOne() throws Exception
      {
        Long tno = 4L;
        todoDAO.deleteOne(tno);
        testList();
      }

    @Test
    public void testUpdateOne() throws Exception
      {
        TodoVO todoVO = TodoVO.builder()
            .tno(5L)
            .title("Update")
            .dueDate(LocalDate.of(2024,8,3))
            .finished(true)
            .build();

        todoDAO.updateOne(todoVO);

        testList();
      }

  }