package com.example.econavi.photo.dto;

import com.example.econavi.photo.entity.MemberPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPhotoDto {
    private Long id;
    private Long memberId;
    private String photoUrl;
    private Timestamp createdAt;

    public static MemberPhotoDto fromEntity(MemberPhoto memberPhoto) {
        return MemberPhotoDto.builder()
                .id(memberPhoto.getId())
                .photoUrl(memberPhoto.getPhotoUrl())
                .memberId(memberPhoto.getMember().getId())
                .createdAt(memberPhoto.getCreatedAt())
                .build();
    }
}
