package com.example.econavi.integration;

import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.place.dto.AddPlaceRequestDto;
import com.example.econavi.place.entity.Place;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlaceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String validToken;
    private String invalidToken;
    private Member testMember;
    private AddPlaceRequestDto addPlaceRequest;

    @BeforeEach
    void setup() {
        placeRepository.deleteAll();
        memberRepository.deleteAll();

        testMember = memberRepository.save(Member.builder()
                .username("test@example.com")
                .name("Test User")
                .password("password")
                .role(Role.STAFF)
                .build());

        validToken = jwtUtil.generateToken(testMember.getId());
        invalidToken = UUID.randomUUID().toString();

        addPlaceRequest = AddPlaceRequestDto.builder()
                .name("부산예술회관")
                .address("부산진구 중앙대로")
                .placeType(PlaceType.EVENT)
                .description("예술 전시장")
                .latitude(new BigDecimal("35.1796"))
                .longitude(new BigDecimal("129.0756"))
                .startDate(Timestamp.valueOf(LocalDateTime.of(2025, 8, 1, 10, 0)))
                .endDate(Timestamp.valueOf(LocalDateTime.of(2025, 8, 30, 18, 0)))
                .build();

        placeRepository.save(Place.builder()
                .name("강남역")
                .placeType(PlaceType.EVENT)
                .owner(testMember)
                .description("test")
                .latitude(new BigDecimal("37.4979"))
                .longitude(new BigDecimal("127.0276"))
                .build());

        placeRepository.save(Place.builder()
                .name("서초역")
                .placeType(PlaceType.EVENT)
                .owner(testMember)
                .description("test")
                .latitude(new BigDecimal("37.4919"))
                .longitude(new BigDecimal("127.0076"))
                .build());
    }

    @AfterEach
    void tearDown() {
        placeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("[PlaceController][Integration] addPlace test_success")
    void addPlace_test_success() throws Exception {
        String json = objectMapper.writeValueAsString(addPlaceRequest);

        mockMvc.perform(post("/place/add")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(addPlaceRequest.getName()))
                .andExpect(jsonPath("$.address").value(addPlaceRequest.getAddress()));
    }

    @Test
    @DisplayName("[PlaceController][Integration] addPlace test_unauthorized")
    void addPlace_test_unauthorized() throws Exception {
        String json = new ObjectMapper().writeValueAsString(addPlaceRequest);

        Member unauthorizedMember = memberRepository.save(Member.builder()
                .username("test2@example.com")
                .name("Test User2")
                .password("password")
                .role(Role.USER)
                .build());
        String token = jwtUtil.generateToken(unauthorizedMember.getId());

        // 일반 유저인 경우
        mockMvc.perform(post("/place/add")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());

        // Authorization 헤더 없음
        mockMvc.perform(post("/place/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());

        // 유효하지 않은 토큰
        mockMvc.perform(post("/place/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[PlaceController][Integration] searchPlaces test_success")
    void searchPlace_test_success() throws Exception {
        mockMvc.perform(get("/place/search")
                        .param("keyword", "강남")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(containsString("강남")));
    }

    @Test
    @DisplayName("[PlaceController][Integration] aroundPlace test_success")
    void aroundPlace_test_success() throws Exception {
        BigDecimal latitude = new BigDecimal("37.4979");
        BigDecimal longitude = new BigDecimal("127.0276");

        mockMvc.perform(get("/place/around")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("distance", "2.0")
                        .param("latitude", latitude.toString())
                        .param("longitude", longitude.toString())
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)));
    }

    @Test
    @DisplayName("[PlaceController][Integration] getPlaces test_success")
    void getPlaces_test_success() throws Exception {
        Place place = Place.builder()
                .name("테스트장소")
                .address("서울 강남구")
                .placeType(PlaceType.EVENT)
                .owner(testMember)
                .description("테스트 설명")
                .latitude(new BigDecimal("37.1234567"))
                .longitude(new BigDecimal("127.1234567"))
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .build();
        placeRepository.save(place);

        mockMvc.perform(get("/place")
                        .param("place_id", "0")
                        .header("Authorization", "Bearer " + validToken)
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("강남역"));
    }

    @Test
    @DisplayName("[PlaceController][Integration] updatePlace test_success")
    void updatePlace_test_success() throws Exception {
        Place place = Place.builder()
                .name("업데이트전")
                .address("서울 강남구")
                .placeType(PlaceType.EVENT)
                .owner(testMember)
                .description("기존 장소")
                .latitude(new BigDecimal("37.1234567"))
                .longitude(new BigDecimal("127.1234567"))
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .build();
        place = placeRepository.save(place);

        AddPlaceRequestDto updateRequest = AddPlaceRequestDto.builder()
                .name("업데이트후")
                .address("서울 강남구 역삼동")
                .placeType(PlaceType.STORE)
                .description("수정된 장소")
                .latitude(new BigDecimal("37.7654321"))
                .longitude(new BigDecimal("127.7654321"))
                .startDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(5)))
                .build();

        String json = new ObjectMapper().writeValueAsString(updateRequest);

        mockMvc.perform(patch("/place/update/{placeId}", place.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validToken)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("업데이트후"))
                .andExpect(jsonPath("$.description").value("수정된 장소"));
    }

    @Test
    @DisplayName("[PlaceController][Integration] updatePlace test_unauthorized")
    void updatePlace_test_unauthorized() throws Exception {
        Place place = Place.builder()
                .name("업데이트전")
                .address("서울 강남구")
                .placeType(PlaceType.EVENT)
                .owner(testMember)
                .description("기존 장소")
                .latitude(new BigDecimal("37.1234567"))
                .longitude(new BigDecimal("127.1234567"))
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .build();
        place = placeRepository.save(place);

        AddPlaceRequestDto updateRequest = AddPlaceRequestDto.builder()
                .name("업데이트후")
                .address("서울 강남구 역삼동")
                .placeType(PlaceType.STORE)
                .description("수정된 장소")
                .latitude(new BigDecimal("37.7654321"))
                .longitude(new BigDecimal("127.7654321"))
                .startDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(5)))
                .build();

        String json = new ObjectMapper().writeValueAsString(updateRequest);

        // Authorization 헤더 없음
        mockMvc.perform(patch("/place/update/{placeId}", place.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());

        // 유효하지 않은 토큰
        mockMvc.perform(patch("/place/update/{placeId}", place.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[PlaceController][Integration] deletePlace test_success")
    void deletePlace_test_success() throws Exception {
        Place place = Place.builder()
                .name("삭제할 장소")
                .address("서울 강남구")
                .placeType(PlaceType.EVENT)
                .owner(testMember)
                .description("삭제 테스트")
                .latitude(new BigDecimal("37.1234567"))
                .longitude(new BigDecimal("127.1234567"))
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .build();
        place = placeRepository.save(place);

        mockMvc.perform(delete("/place/delete/{placeId}", place.getId())
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("삭제할 장소"));
    }

    @Test
    @DisplayName("[PlaceController][Integration] deletePlace test_unauthorized")
    void deletePlace_test_unauthorized() throws Exception {
        Place place = Place.builder()
                .name("삭제할 장소")
                .address("서울 강남구")
                .placeType(PlaceType.EVENT)
                .owner(testMember)
                .description("삭제 테스트")
                .latitude(new BigDecimal("37.1234567"))
                .longitude(new BigDecimal("127.1234567"))
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .endDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .build();
        place = placeRepository.save(place);

        // Authorization 헤더 없음
        mockMvc.perform(delete("/place/delete/{placeId}", place.getId()))
                .andExpect(status().isForbidden());

        // 유효하지 않은 토큰
        mockMvc.perform(delete("/place/delete/{placeId}", place.getId())
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }
}