package com.example.econavi.product.dto;

import com.example.econavi.product.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDto {
    private Long memberId;
    private Long amount;
    private Timestamp updatedAt;

    public static PointDto fromEntity(Point point) {
        return PointDto.builder()
                .memberId(point.getMemberId())
                .amount(point.getAmount())
                .updatedAt(point.getUpdatedAt())
                .build();
    }
}
