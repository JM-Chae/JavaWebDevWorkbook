package com.example.springex.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.bytebuddy.description.annotation.AnnotationDescription;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO
  {
    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 1)
    @Max(value = 1000)
    @Positive
    private int size = 10;

    public int getSkip()
      {
        return (page - 1) * 10;
      }

    private String link;

    public String getLink()
      {
        if(link == null)
          {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);
            if(finished)
              {
                builder.append("&finished=true");
              }
            if(types != null && types.length > 0)
              {
                for(int i = 0; i < types.length; i++)
                  {
                    builder.append("&type=" + types[i]);
                  }
              }
            if(keyword != null)
              {
                try
                  {
                    builder.append("&keyword="+ URLEncoder.encode(keyword,"UTF-8"));
                  }catch(UnsupportedEncodingException e)
                  {
                    e.printStackTrace();
                  }
              }
            if(from != null)
              {
                builder.append("&from=" + from.toString());
              }
            if(to != null)
              {
                builder.append("&to=" + to.toString());
              }

            link = builder.toString();
          }
        return link;
      }

    private String[] types;
    private String keyword;
    private boolean finished;
    private LocalDate from;
    private LocalDate to;

    public boolean checkType(String type)
      {
        if(types == null || types.length == 0)
          {
            return false;
          }
        return Arrays.stream(types).anyMatch(type::equals);
      }
  }
