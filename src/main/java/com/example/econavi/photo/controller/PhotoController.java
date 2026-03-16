package com.example.econavi.photo.controller;

import com.example.econavi.photo.dto.MemberPhotoDto;
import com.example.econavi.photo.dto.PlacePhotoDto;
import com.example.econavi.photo.dto.ProductPhotoDto;
import com.example.econavi.photo.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
@Tag(name = "이미지 관련 서비스", description = "이미지 업로드 및 삭제")
public class PhotoController {
    private final PhotoService photoService;

    @Operation(
            summary = "사용자 프로필 사진 메타 데이터 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/member")
    public ResponseEntity<List<MemberPhotoDto>> getMemberPhotos(
            @RequestParam Long memberId
    ) {
        return ResponseEntity.ok(photoService.getMemberPhotos(memberId));
    }

    @Operation(
            summary = "장소 사진 메타 데이터 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/place")
    public ResponseEntity<List<PlacePhotoDto>> getPlacePhotos(
            @RequestParam Long placeId
    ) {
        return ResponseEntity.ok(photoService.getPlacePhotos(placeId));
    }

    @Operation(
            summary = "상품 사진 메타 데이터 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/product")
    public ResponseEntity<List<ProductPhotoDto>> getProductPhotos(
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(photoService.getProductPhotos(productId));
    }

    @Operation(
            summary = "사용자 프로필 사진 업로드",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(value = "/member/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MemberPhotoDto>> addMemberPhoto(
            @PathVariable Long memberId,
            @RequestParam("images") List<MultipartFile> images
    ) {
        return ResponseEntity.ok(photoService.addMemberPhoto(memberId, images));
    }

    @Operation(
            summary = "장소 사진 업로드",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(value = "/place/{placeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlacePhotoDto>> addPlacePhoto(
            @PathVariable Long placeId,
            @RequestPart("images") List<MultipartFile> images
    ) {
        return ResponseEntity.ok(photoService.addPlacePhoto(placeId, images));
    }

    @Operation(
            summary = "상품 사진 업로드",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(value = "/product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductPhotoDto>> addProductPhoto(
            @PathVariable Long productId,
            @RequestPart("images") List<MultipartFile> images
    ) {
        return ResponseEntity.ok(photoService.addProductPhoto(productId, images));
    }
}
