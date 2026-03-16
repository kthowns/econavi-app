package com.example.econavi.unit.service;

import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.photo.repository.PlacePhotoRepository;
import com.example.econavi.place.dto.AddPlaceRequestDto;
import com.example.econavi.place.dto.PlaceDto;
import com.example.econavi.place.entity.Place;
import com.example.econavi.place.repository.PlaceRepository;
import com.example.econavi.place.service.PlaceService;
import com.example.econavi.place.type.PlaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PlacePhotoRepository placePhotoRepository;

    @InjectMocks
    private PlaceService placeService;

    private Member member;
    private Place place;
    private AddPlaceRequestDto request;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .role(Role.STAFF)
                .username("testuser")
                .build();

        request = AddPlaceRequestDto.builder()
                .name("부산예술회관")
                .address("부산진구 중앙대로")
                .placeType(PlaceType.EVENT)
                .description("예술 전시장")
                .latitude(new BigDecimal("35.1796"))
                .longitude(new BigDecimal("129.0756"))
                .startDate(Timestamp.valueOf(LocalDateTime.of(2025, 8, 1, 10, 0)))
                .endDate(Timestamp.valueOf(LocalDateTime.of(2025, 8, 30, 18, 0)))
                .build();

        place = Place.builder()
                .id(1L)
                .name(request.getName())
                .address(request.getAddress())
                .placeType(request.getPlaceType())
                .owner(member)
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }

    @Test
    @DisplayName("[PlaceService][Unit] addPlace test_success")
    void addPlace_test_success() {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(placeRepository.countByNameAndAddress(request.getName(), request.getAddress())).willReturn(0);
        given(placeRepository.save(any(Place.class))).willReturn(place);

        // when
        PlaceDto result = placeService.addPlace(member.getId(), request);

        // then
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getAddress(), result.getAddress());
        verify(memberRepository).findById(member.getId());
        verify(placeRepository).countByNameAndAddress(request.getName(), request.getAddress());
        verify(placeRepository).save(any(Place.class));
    }

    @Test
    @DisplayName("[PlaceService][Unit] getPlaces test_success")
    void getPlaces_test_success() {
        // given
        List<Place> places = List.of(place);
        given(placeRepository.findByIdGreaterThanOrderByIdAsc(anyLong(), any(PageRequest.class)))
                .willReturn(places);

        // when
        List<PlaceDto> result = placeService.getPlaces(null, 10);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(place.getName(), result.get(0).getName());
        verify(placeRepository).findByIdGreaterThanOrderByIdAsc(0L, PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("[PlaceService][Unit] updatePlace test_success")
    void updatePlace_test_success() {
        // given
        given(placeRepository.findById(place.getId())).willReturn(Optional.of(place));
        given(placeRepository.countByNameAndAddressAndIdNot(request.getName(), request.getAddress(), place.getId())).willReturn(0L);
        given(placeRepository.save(any(Place.class))).willReturn(place);

        // when
        PlaceDto result = placeService.updatePlace(place.getId(), request);

        // then
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getAddress(), result.getAddress());
        verify(placeRepository).findById(place.getId());
        verify(placeRepository).countByNameAndAddressAndIdNot(request.getName(), request.getAddress(), place.getId());
        verify(placeRepository).save(any(Place.class));
    }

    @Test
    @DisplayName("[PlaceService][Unit] deletePlace test_success")
    void deletePlace_test_success() {
        // given
        given(placeRepository.findById(place.getId())).willReturn(Optional.of(place));
        doNothing().when(placeRepository).delete(place);

        // when
        PlaceDto result = placeService.deletePlace(place.getId());

        // then
        assertNotNull(result);
        assertEquals(place.getName(), result.getName());
        verify(placeRepository).findById(place.getId());
        verify(placeRepository).delete(place);
    }
}
