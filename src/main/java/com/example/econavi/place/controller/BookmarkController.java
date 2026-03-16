package com.example.econavi.place.controller;

import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.place.dto.BookmarkDto;
import com.example.econavi.place.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
@Tag(name = "찜 관련 서비스", description = "찜 CRUD")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "찜한 장소 전체 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/all")
    public ResponseEntity<List<BookmarkDto>> getBookmarks(
            @RequestParam Long member_id
    ) {
        return ResponseEntity.ok(bookmarkService.getBookmarks(member_id));
    }

    @Operation(
            summary = "북마크 추가",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/add/{placeId}")
    public ResponseEntity<BookmarkDto> addBookmark(
            @PathVariable Long placeId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getIdFromToken(token);

        return ResponseEntity.ok(bookmarkService.addBookmark(memberId, placeId));
    }

    @Operation(
            summary = "북마크 해제",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/delete/{bookmarkId}")
    public ResponseEntity<BookmarkDto> deleteBookmark(
            @PathVariable Long bookmarkId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getIdFromToken(token);

        return ResponseEntity.ok(bookmarkService.deleteBookmark(memberId, bookmarkId));
    }
}
