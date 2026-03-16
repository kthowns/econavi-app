package com.example.econavi.place.dto;

import com.example.econavi.place.entity.Place;
import com.example.econavi.place.type.PlaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceDto {
    private Long id;
    private Long ownerId;
    private String name;
    private Timestamp startDate;
    private Timestamp endDate;
    private String description;
    private PlaceType placeType;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static PlaceDto fromEntity(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .ownerId(place.getOwner().getId())
                .name(place.getName())
                .startDate(place.getStartDate())
                .endDate(place.getEndDate())
                .description(place.getDescription())
                .placeType(place.getPlaceType())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .createdAt(place.getCreatedAt())
                .updatedAt(place.getUpdatedAt())
                .build();
    }
}
