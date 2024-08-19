package com.example.boot.repository.search;

import com.example.boot.domain.Board;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import sources.annotationProcessor.java.main.com.example.boot.domain.QBoard;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch
  {
    public BoardSearchImpl()
      {
        super(Board.class);
      }

    @Override
    public Page<Board> search1(Pageable pageable)
      {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
        query.where(board.title.contains("1"));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return null;
      }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable)
      {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if((types != null && types.length > 0) && (keyword != null && keyword.length() > 0))
          {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types)
              {
                switch(type)
                  {
                    case "t":
                      booleanBuilder.or(board.title.contains(keyword));
                      break;
                    case "c":
                      booleanBuilder.or(board.content.contains(keyword));
                      break;
                    case "w":
                      booleanBuilder.or(board.writer.contains(keyword));
                      break;
                  }
              }
            query.where(booleanBuilder);
          }
        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);
      }

  }
