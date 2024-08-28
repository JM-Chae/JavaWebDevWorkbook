package com.example.boot.controller;

import com.example.boot.dto.*;
import com.example.boot.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.util.List;


@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController
  {
    private final BoardService boardService;
    @Value("${com.example.upload.path}")
    private String uploadPath;

    @Operation(summary = "list")
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model)
      {
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithALL(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
      }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "register")
    @GetMapping("/register")
    public void registerGET()
      {

      }

    @Operation(summary = "register")
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes)
      {
        log.info("board POST register");

        if(bindingResult.hasErrors())
          {
            log.info("has errors...");
            Long bno = boardService.register(boardDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
          }


        log.info(boardDTO);
        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
      }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "search")
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model)
      {
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
      }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @Operation(summary = "modify")
    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO, @Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes)
      {
        log.info("board POST modify" + boardDTO);
        if(bindingResult.hasErrors())
          {
            log.info("has errors...");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?"+link;
          }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
      }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @Operation(summary = "remove")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes)
      {
        Long bno = boardDTO.getBno();
        log.info("remove post..." + bno);
        boardService.remove(bno);

        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0)
          {
            removeFiles(fileNames);
          }

        redirectAttributes.addFlashAttribute("result", "removed");
        return "redirect:/board/list";
      }

    public void removeFiles(List<String> files)
      {
        for(String fileName:files)
          {
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();

            try
              {
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                if (contentType.startsWith("image"))
                  {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                  }
              }catch (Exception e)
              {
                log.error(e.getMessage());
              }
          }
      }
  }
