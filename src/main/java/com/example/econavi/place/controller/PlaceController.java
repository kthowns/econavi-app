package com.example.econavi.place.controller;

import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.place.dto.AddPlaceRequestDto;
import com.example.econavi.place.dto.PlaceDto;
import com.example.econavi.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@Tag(name = "장소 관련 서비스", description = "장소 CRUD")
public class PlaceController {
    private final PlaceService placeService;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "장소 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("")
    public ResponseEntity<List<PlaceDto>> getPlaces(
            @RequestParam(required = false) Long last_id,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(placeService.getPlaces(last_id, size));
    }

    @Operation(
            summary = "장소 검색",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchPlaces(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(placeService.searchPlaces(keyword));
    }

    @Operation(
            summary = "주변 장소 검색",
            description = "거리는 Km 단위",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/around")
    public ResponseEntity<List<PlaceDto>> aroundPlaces(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam double distance
    ) {
        return ResponseEntity.ok(placeService.getAroundPlaces(latitude, longitude, distance));
    }

    @Operation(
            summary = "장소 추가",
            description = "STAFF만 사용 가능",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/add")
    public ResponseEntity<PlaceDto> addPlace(
            @Valid @RequestBody AddPlaceRequestDto request,
            HttpServletRequest httpServletRequest
    ) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getIdFromToken(token);

        return ResponseEntity.ok(placeService.addPlace(memberId, request));
    }

    @Operation(
            summary = "장소 정보 수정",
            description = "STAFF만 사용 가능",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/update/{placeId}")
    public ResponseEntity<PlaceDto> updatePlace(
            @PathVariable Long placeId,
            @Valid @RequestBody AddPlaceRequestDto request
    ) {
        return ResponseEntity.ok(placeService.updatePlace(placeId, request));
    }

    @Operation(
            summary = "장소 삭제",
            description = "STAFF만, 본인이 업로드한 장소만 삭제 가능",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/delete/{placeId}")
    public ResponseEntity<PlaceDto> deletePlace(
            @PathVariable Long placeId
    ) {
        return ResponseEntity.ok(placeService.deletePlace(placeId));
    }
}
