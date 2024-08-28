package com.example.boot.security.handler;

import com.example.boot.domain.Member;
import com.example.boot.domain.MemberRole;
import com.example.boot.repository.MemberRepository;
import com.example.boot.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService
  {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
      {
        log.info("USER REQUEST");
        log.info(userRequest);

        log.info("-------------------------");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();

        log.info("name: "+registrationId);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;

        switch(registrationId)
          {
            case "kakao":
              email = getKakaoEmail(attributes);
              break;
          }
        log.info("-------------------------");
        log.info(email);
        log.info("-------------------------");

        return generateDTO(email, attributes);
      }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params)
      {
        Optional<Member> result = memberRepository.findByEmail(email);

        if (result.isEmpty())
          {
            Member member = Member.builder()
                .mid(email)
                .mpw(passwordEncoder.encode("1111"))
                .email(email)
                .social(true)
                .build();

            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO
                (email, "1111", email, false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProp(params);

            return memberSecurityDTO;
          }else
          {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO
                (member.getMid(),member.getMpw(),member.getEmail(), member.isDel(), member.isSocial(),member.getRoleSet()
                    .stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toList()));

            return memberSecurityDTO;
          }
      }

    private String getKakaoEmail(Map<String, Object> attributes)
      {
        log.info("------------KAKAO------------");

        Object value = attributes.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap)value;

        String email = (String)accountMap.get("email");

        log.info(email);

        return email;
      }
  }
