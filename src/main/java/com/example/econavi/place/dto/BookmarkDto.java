package com.example.econavi.place.dto;

import com.example.econavi.place.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkDto {
    private Long id;
    private Long memberId;
    private Long placeId;

    public static BookmarkDto fromEntity(Bookmark bookmark) {
        return BookmarkDto.builder()
                .id(bookmark.getId())
                .placeId(bookmark.getPlace().getId())
                .memberId(bookmark.getMember().getId())
                .build();
    }
}
