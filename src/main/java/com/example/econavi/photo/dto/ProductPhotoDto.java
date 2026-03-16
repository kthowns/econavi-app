package com.example.econavi.photo.dto;

import com.example.econavi.photo.entity.ProductPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPhotoDto {
    private Long id;
    private Long productId;
    private String photoUrl;
    private Timestamp createdAt;

    public static ProductPhotoDto fromEntity(ProductPhoto productPhoto) {
        return ProductPhotoDto.builder()
                .id(productPhoto.getId())
                .photoUrl(productPhoto.getPhotoUrl())
                .productId(productPhoto.getProduct().getId())
                .createdAt(productPhoto.getCreatedAt())
                .build();
    }
}
