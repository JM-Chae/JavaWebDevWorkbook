package com.example.boot.service;

import com.example.boot.domain.Member;
import com.example.boot.domain.MemberRole;
import com.example.boot.dto.MemberJoinDTO;
import com.example.boot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService
  {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException
      {
        String mid = memberJoinDTO.getMid();
        boolean exist = memberRepository.existsById(mid);
        log.info(exist);
        if (exist)
          {
            throw new MidExistException();
          }

        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        log.info("===============");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
      }
  }
