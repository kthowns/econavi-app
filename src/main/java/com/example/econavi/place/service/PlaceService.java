package com.example.econavi.place.service;

import com.example.econavi.common.code.AuthResponseCode;
import com.example.econavi.common.code.GeneralResponseCode;
import com.example.econavi.common.exception.ApiException;
import com.example.econavi.common.service.FileStorageService;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.photo.entity.PlacePhoto;
import com.example.econavi.photo.repository.PlacePhotoRepository;
import com.example.econavi.place.dto.AddPlaceRequestDto;
import com.example.econavi.place.dto.PlaceDto;
import com.example.econavi.place.entity.Place;
import com.example.econavi.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;
    private final PlacePhotoRepository placePhotoRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<PlaceDto> getPlaces(Long lastId, int size) {
        long cursor = lastId == null ? 0L : lastId;
        int pageSize = (size <= 0) ? 10 : size;

        List<Place> places = placeRepository.findByIdGreaterThanOrderByIdAsc(
                cursor, PageRequest.of(0, pageSize));

        return places.stream().map(PlaceDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceDto> searchPlaces(String keyword) {
        List<Place> places = placeRepository.findByNameContaining(keyword);

        return places.stream().map(PlaceDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceDto> getAroundPlaces(BigDecimal latitude, BigDecimal longitude, double distance) {
        List<Place> places = placeRepository.findPlaceWithDistance(
                latitude,
                longitude,
                distance
        );

        return places.stream().map(PlaceDto::fromEntity).toList();
    }

    @Transactional
    @PreAuthorize("@memberAccessHandler.ownershipCheck(#memberId)")
    public PlaceDto addPlace(Long memberId, AddPlaceRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        if (member.getRole() != Role.STAFF) {
            throw new ApiException(AuthResponseCode.UNAUTHORIZED);
        }

        int count = placeRepository.countByNameAndAddress(request.getName(), request.getAddress());

        if (count > 0) {
            throw new ApiException(GeneralResponseCode.DUPLICATED_PLACE);
        }

        Place place = Place.builder()
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

        place = placeRepository.save(place);

        return PlaceDto.fromEntity(place);
    }

    @Transactional
    @PreAuthorize("@placeAccessHandler.ownershipCheck(#placeId)")
    public PlaceDto updatePlace(Long placeId, AddPlaceRequestDto request) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.PLACE_NOT_FOUND));

        long count = placeRepository.countByNameAndAddressAndIdNot(request.getName(), request.getAddress(), placeId);

        if (count > 0) {
            throw new ApiException(GeneralResponseCode.DUPLICATED_PLACE);
        }

        place.setName(request.getName());
        place.setDescription(request.getDescription());
        place.setAddress(request.getAddress());
        place.setPlaceType(request.getPlaceType());
        place.setLatitude(request.getLatitude());
        place.setLongitude(request.getLongitude());
        place.setStartDate(request.getStartDate());
        place.setEndDate(request.getEndDate());
        place = placeRepository.save(place);

        return PlaceDto.fromEntity(place);
    }

    @Transactional
    @PreAuthorize("@placeAccessHandler.ownershipCheck(#placeId)")
    public PlaceDto deletePlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.PLACE_NOT_FOUND));

        List<PlacePhoto> placePhoto = placePhotoRepository.findByPlace(place);

        placeRepository.delete(place);

        for (PlacePhoto photo : placePhoto) {
            fileStorageService.delete(photo.getPhotoUrl());
        }

        return PlaceDto.fromEntity(place);
    }
}
