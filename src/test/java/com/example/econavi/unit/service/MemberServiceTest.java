package com.example.econavi.unit.service;

import com.example.econavi.auth.security.JwtBlacklistService;
import com.example.econavi.auth.service.AuthService;
import com.example.econavi.member.dto.MemberDto;
import com.example.econavi.member.dto.UpdateNameRequestDto;
import com.example.econavi.member.dto.UpdatePasswordRequestDto;
import com.example.econavi.member.entity.Member;
import com.example.econavi.photo.repository.MemberPhotoRepository;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestPropertySource(properties = {
        "jwt.secret=qwerqwerqwerqwerqwerqwerqwerqwer",
        "jwt.exp=3600"
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MemberService.class, MemberServiceTest.TestConfig.class})
@ActiveProfiles("dev")
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberPhotoRepository memberPhotoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthService authService;

    private static final Long UID = 3L;

    @Test
    @DisplayName("[MemberService] Get member detail success")
    void getMemberDetailByIdSuccess() {
        resetMock();

        Member member = Member.builder()
                .id(UID)
                .username("test@test.com")
                .name("test")
                .password(passwordEncoder.encode("test"))
                .build();

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        //when
        MemberDto response = memberService.getMemberDetailById(UID);

        //then
        assertEquals(member.getName(), response.getName());
        assertEquals(member.getUsername(), response.getUsername());
        assertEquals(UID, response.getId());
    }

    @Test
    @DisplayName("[MemberService] Update member name success")
    void updateMemberNameSuccess() {
        resetMock();

        Member defaultMember = Member.builder()
                .id(UID)
                .username("test@test.com")
                .name("test")
                .password(passwordEncoder.encode("testTest1"))
                .build();

        UpdateNameRequestDto request = UpdateNameRequestDto.builder()
                .name("test2")
                .build();

        ArgumentCaptor<Member> memberCaptor =
                ArgumentCaptor.forClass(Member.class);

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(defaultMember));
        given(memberRepository.countByNameAndIdNot(anyString(), anyLong()))
                .willReturn(0);
        given(memberRepository.save(any(Member.class)))
                .willAnswer(invocation -> {
                    Member memberArg = invocation.getArgument(0);
                    memberArg.setName(request.getName());
                    return memberArg;
                });

        //when
        MemberDto response = memberService.updateMemberName(UID, request);

        //then
        verify(memberRepository, times(1))
                .save(memberCaptor.capture());

        assertEquals(request.getName(), response.getName());
        assertEquals(UID, response.getId());
    }

    @Test
    @DisplayName("[MemberService] Update member password success")
    void updateMemberPasswordSuccess() {
        resetMock();

        Member defaultMember = Member.builder()
                .id(UID)
                .username("test@test.com")
                .name("test")
                .password(passwordEncoder.encode("testTest1"))
                .build();

        UpdatePasswordRequestDto request = UpdatePasswordRequestDto.builder()
                .password("testTest2")
                .build();

        ArgumentCaptor<Member> memberCaptor =
                ArgumentCaptor.forClass(Member.class);

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(defaultMember));
        given(memberRepository.save(any(Member.class)))
                .willAnswer(invocation -> {
                    Member memberArg = invocation.getArgument(0);
                    memberArg.setPassword(passwordEncoder.encode(
                            request.getPassword()));
                    return memberArg;
                });

        //when
        memberService.updateMemberPassword(UID, request, UUID.randomUUID().toString());

        //then
        verify(memberRepository, times(1))
                .save(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();

        assertThat(passwordEncoder.matches(request.getPassword(), savedMember.getPassword()));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MemberRepository memberRepository() {
            return Mockito.mock(MemberRepository.class);
        }

        @Bean
        public MemberPhotoRepository memberPhotoRepository() { return Mockito.mock(MemberPhotoRepository.class); }

        @Bean
        public JwtBlacklistService jwtBlacklistService() {
            return Mockito.mock(JwtBlacklistService.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }
    }

    private void resetMock() {
        Mockito.reset(memberRepository);
    }
}