package com.example.econavi.integration;

import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.common.service.FileStorageService;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.photo.dto.MemberPhotoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class PhotoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // JWT 테스트 토큰 (실제 발급 or TestDouble 필요)
    private String token;

    @Test
    @DisplayName("[통합] 사용자 프로필 사진 업로드 성공")
    void addMemberPhoto_success() throws Exception {
        // given
        Member testMember = memberRepository.save(Member.builder()
                .username("photo@test.com")
                .password("pw1234")
                .name("포토맨")
                .role(Role.USER)
                .build());

        MockMultipartFile image = new MockMultipartFile(
                "images",                // must match @RequestPart name
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        token = jwtUtil.generateToken(testMember.getId());

        // when & then
        mockMvc.perform(multipart("/photo/member/" + testMember.getId())
                        .file(image)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)); // 예상 사진 개수
    }

    @Test
    @DisplayName("[통합] 사용자 프로필 사진 메타데이터 조회 성공")
    void getMemberPhotos_success() throws Exception {
        // given
        Member testMember = memberRepository.save(Member.builder()
                .username("meta@test.com")
                .password("pw1234")
                .name("메타맨")
                .role(Role.USER)
                .build());

        // 업로드 먼저 해줘야 조회에 걸릴 데이터가 있음
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test-content".getBytes()
        );

        MockPart memberId = new MockPart(
                "memberId", String.valueOf(testMember.getId()).getBytes()
        );

        token = jwtUtil.generateToken(testMember.getId());

        mockMvc.perform(multipart("/photo/member/" + testMember.getId())
                        .file(image)
                        .part(memberId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // when & then
        mockMvc.perform(get("/photo/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("memberId", String.valueOf(testMember.getId()))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("[FileStorageSerivce][Integration] download resource test_success")
    void downloadPhoto_success() throws Exception {
        // given
        Member testMember = memberRepository.save(Member.builder()
                .username("meta@test.com")
                .password("pw1234")
                .name("메타맨")
                .role(Role.USER)
                .build());

        // 업로드 먼저 해줘야 조회에 걸릴 데이터가 있음
        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test-content".getBytes()
        );

        MockPart memberId = new MockPart(
                "memberId", String.valueOf(testMember.getId()).getBytes()
        );

        token = jwtUtil.generateToken(testMember.getId());

        mockMvc.perform(multipart("/photo/member/" + testMember.getId())
                        .file(image)
                        .part(memberId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // when & then
        MvcResult result = mockMvc.perform(get("/photo/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("memberId", String.valueOf(testMember.getId()))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        List<MemberPhotoDto> list = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<MemberPhotoDto>>() {
                }
        );

        mockMvc.perform(get("/download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("file_name", list.get(0).getPhotoUrl())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}

