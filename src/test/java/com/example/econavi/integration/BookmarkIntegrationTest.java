package com.example.econavi.integration;

import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.place.entity.Bookmark;
import com.example.econavi.place.entity.Place;
import com.example.econavi.place.repository.BookmarkRepository;
import com.example.econavi.place.repository.PlaceRepository;
import com.example.econavi.place.type.PlaceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class BookmarkIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private Member member;
    private Place place;
    private String jwt;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 및 장소 생성
        member = memberRepository.save(Member.builder()
                .username("testuser")
                .password("encodedpassword")
                .name("testuser")
                .role(Role.USER)
                .build());

        place = placeRepository.save(Place.builder()
                .name("테스트장소")
                .address("서울 강남구")
                .placeType(PlaceType.EVENT)
                .owner(member)
                .description("테스트 설명")
                .latitude(new BigDecimal("37.1234567"))
                .longitude(new BigDecimal("127.1234567"))
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .build());

        jwt = "Bearer " + jwtUtil.generateToken(member.getId());
    }

    @AfterEach
    void tearDown() {
        bookmarkRepository.deleteAll();
        placeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("[BookmarkController][Integration] addBookmark_test_success")
    void addBookmark_test_success() throws Exception {
        mockMvc.perform(post("/bookmark/add/{placeId}", place.getId())
                        .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.placeId").value(place.getId()))
                .andExpect(jsonPath("$.memberId").value(member.getId()));
    }

    @Test
    @DisplayName("[BookmarkController][Integration] getBookmarks_test_success")
    void getBookmarks_test_success() throws Exception {
        // 즐겨찾기 등록
        bookmarkRepository.save(Bookmark.builder()
                .member(member)
                .place(place)
                .build());

        mockMvc.perform(get("/bookmark/all")
                        .param("member_id", member.getId().toString())
                        .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].memberId").value(member.getId()))
                .andExpect(jsonPath("$[0].placeId").value(place.getId()));
    }

    @Test
    @DisplayName("[BookmarkController][Integration] deleteBookmark_test_success")
    void deleteBookmark_test_success() throws Exception {
        Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
                .member(member)
                .place(place)
                .build());

        mockMvc.perform(delete("/bookmark/delete/{bookmarkId}", bookmark.getId())
                        .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookmark.getId()));
    }
}
