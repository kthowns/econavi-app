package com.example.econavi.photo.dto;

import com.example.econavi.photo.entity.PlacePhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlacePhotoDto {
    private Long id;
    private Long placeId;
    private String photoUrl;
    private Timestamp createdAt;

    public static PlacePhotoDto fromEntity(PlacePhoto placePhoto) {
        return PlacePhotoDto.builder()
                .id(placePhoto.getId())
                .photoUrl(placePhoto.getPhotoUrl())
                .placeId(placePhoto.getPlace().getId())
                .createdAt(placePhoto.getCreatedAt())
                .build();
    }
}
