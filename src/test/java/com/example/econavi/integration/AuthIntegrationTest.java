package com.example.econavi.integration;

import com.example.econavi.auth.dto.LoginDto;
import com.example.econavi.auth.dto.SignUpRequestDto;
import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.product.repository.PointRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AuthIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    private final String BASE_URI = "/auth";

    private final String rawPassword = "Password123!";
    private Member member;
    @Autowired
    private PointRepository pointRepository;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
                .username("testuser@test.com")
                .password(passwordEncoder.encode(rawPassword))
                .name("테스트유저")
                .role(Role.USER)
                .build());
    }

    @AfterEach
    void tearDown() {
        pointRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("[AuthService][Integration] signUp_test_success")
    void signUp_test_success() throws Exception {
        SignUpRequestDto request = SignUpRequestDto.builder()
                .username("newuser@test.com")
                .password("NewPassword123!")
                .name("신규유저")
                .role(Role.USER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URI + "/signup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[AuthService][Integration] login_test_success")
    void login_test_success() throws Exception {
        LoginDto.Request request = LoginDto.Request.builder()
                .username(member.getUsername())
                .password(rawPassword)
                .build();

        mockMvc.perform(post(BASE_URI + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").exists())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("[AuthService][Integration] logout_test_success")
    void logout_test_success() throws Exception {
        // JWT 발급
        String token = jwtUtil.generateToken(member.getId());
        String authHeader = "Bearer " + token;

        mockMvc.perform(post(BASE_URI + "/logout")
                        .header("Authorization", authHeader)
                        .param("memberId", member.getId().toString()))
                .andExpect(status().isOk());
    }
}
