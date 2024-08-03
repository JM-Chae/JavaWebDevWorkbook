package com.example.jdbcex.service;

import com.example.jdbcex.dao.TodoDAO;
import com.example.jdbcex.domain.TodoVO;
import com.example.jdbcex.dto.TodoDTO;
import com.example.jdbcex.util.MapperUtil;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public enum TodoService
  {
    INSTANCE;

    private final TodoDAO dao;
    private final ModelMapper modelMapper;

    TodoService()
      {
        dao = new TodoDAO();
        modelMapper = MapperUtil.INSTANCE.get();
      }

    public void register(TodoDTO todoDTO) throws Exception
      {

        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);

//    System.out.println("todoVO: " + todoVO);
        log.info(todoVO);
        dao.insert(todoVO);
      }

    public List<TodoDTO> listAll() throws Exception
      {
        List<TodoVO> voList = dao.selectAll();
        log.info("voList................");
        log.info(voList);

        List<TodoDTO> dtoList = voList.stream()
            .map(vo -> modelMapper.map(vo, TodoDTO.class))
            .collect(Collectors.toList());

        return dtoList;
      }

    public TodoDTO get(Long tno) throws Exception
      {
        log.info(tno);
        TodoVO todoVO = dao.selectOne(tno);
        TodoDTO todoDTO = modelMapper.map(todoVO, TodoDTO.class);
        return todoDTO;
      }

    public void remove(Long tno) throws Exception
      {
        log.info(tno);
        dao.deleteOne(tno);
      }

    public void modify(TodoDTO todoDTO) throws Exception
      {
        log.info(todoDTO);
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
        dao.updateOne(todoVO);
      }
  }

