package com.example.econavi.integration;

import com.example.econavi.auth.dto.LoginDto;
import com.example.econavi.auth.dto.SignUpRequestDto;
import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.product.entity.PointHistory;
import com.example.econavi.product.repository.PointHistoryRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class PointIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private PointHistoryRepository pointHistoryRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private SignUpRequestDto signUpRequest;
    private LoginDto.Request loginRequest;
    private Long memberId;
    private String token;

    @AfterEach
    void tearDown() {
        pointHistoryRepository.deleteAll();
        pointRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @BeforeEach
    void setUp() throws Exception {
        // 테스트용 사용자 저장
        signUpRequest = SignUpRequestDto.builder()
                .username("testuser@test.com")
                .password("encoded123!")
                .name("테스트")
                .role(Role.USER)
                .build();

        String json = objectMapper.writeValueAsString(signUpRequest);

        loginRequest = LoginDto.Request.builder()
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .build();

        mockMvc.perform(multipart("/auth/signup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .path("token").asText();
        memberId = jwtUtil.getIdFromToken(token);

        pointHistoryRepository.save(PointHistory.builder()
                .member(memberRepository.findById(memberId).get())
                .reason("테스트 적립")
                .amount(0L)
                .build());
    }

    @Test
    @DisplayName("[PointController][Integration] getPoint_test_success")
    void getPoint_test_success() throws Exception {
        mockMvc.perform(get("/point")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.amount").value(0));
    }

    @Test
    @DisplayName("[PointController][Integration] getPointHistories_test_success")
    void getPointHistories_test_success() throws Exception {
        mockMvc.perform(get("/point/histories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberId").value(memberId))
                .andExpect(jsonPath("$[0].amount").value(0L))
                .andExpect(jsonPath("$[0].reason").value("테스트 적립"))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty());
    }
}
