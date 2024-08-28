package com.example.boot.controller;


import com.example.boot.dto.MemberJoinDTO;
import com.example.boot.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController
  {
    private final MemberService memberService;

    @GetMapping("/login")
    public void loginGET(String error, String logout)
      {
        log.info("Login get!");
        log.info(logout);

        if(logout != null)
          {
            log.info("user logout");
          }
      }

    @GetMapping("/join")
    public void joinGET()
      {
        log.info("Join get!");
      }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes)
      {
        log.info("Join post!");
        log.info(memberJoinDTO);

        try
          {
            memberService.join(memberJoinDTO);
          }catch (MemberService.MidExistException e)
          {
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
          }

        redirectAttributes.addFlashAttribute("success", "joined");

        return "redirect:/member/login";
      }
  }
