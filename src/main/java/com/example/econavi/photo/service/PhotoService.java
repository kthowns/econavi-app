package com.example.econavi.photo.service;

import com.example.econavi.common.code.AuthResponseCode;
import com.example.econavi.common.code.GeneralResponseCode;
import com.example.econavi.common.exception.ApiException;
import com.example.econavi.common.service.FileStorageService;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.photo.dto.MemberPhotoDto;
import com.example.econavi.photo.dto.PlacePhotoDto;
import com.example.econavi.photo.dto.ProductPhotoDto;
import com.example.econavi.photo.entity.MemberPhoto;
import com.example.econavi.photo.entity.PlacePhoto;
import com.example.econavi.photo.entity.ProductPhoto;
import com.example.econavi.photo.repository.MemberPhotoRepository;
import com.example.econavi.photo.repository.PlacePhotoRepository;
import com.example.econavi.photo.repository.ProductPhotoRepository;
import com.example.econavi.place.entity.Place;
import com.example.econavi.place.repository.PlaceRepository;
import com.example.econavi.product.entity.Product;
import com.example.econavi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final MemberPhotoRepository memberPhotoRepository;
    private final PlacePhotoRepository placePhotoRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<MemberPhotoDto> getMemberPhotos(Long memberId) {
        List<MemberPhoto> photos = memberPhotoRepository.findByMember_id(memberId);

        return photos.stream().map(MemberPhotoDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<PlacePhotoDto> getPlacePhotos(Long placeId) {
        List<PlacePhoto> photos = placePhotoRepository.findByPlace_id(placeId);

        return photos.stream().map(PlacePhotoDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductPhotoDto> getProductPhotos(Long productId) {
        List<ProductPhoto> photos = productPhotoRepository.findByProduct_Id(productId);

        return photos.stream().map(ProductPhotoDto::fromEntity).toList();
    }

    @Transactional
    @PreAuthorize("@memberAccessHandler.ownershipCheck(#memberId)")
    public List<MemberPhotoDto> addMemberPhoto(Long memberId, List<MultipartFile> images) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        List<MemberPhoto> photos = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                String filename = fileStorageService.store(image);
                MemberPhoto memberPhoto = MemberPhoto.builder()
                        .member(member)
                        .photoUrl(filename)
                        .build();
                photos.add(memberPhoto);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image " + image.getOriginalFilename(), e);
            }
        }
        memberPhotoRepository.saveAll(photos);

        return photos.stream().map(MemberPhotoDto::fromEntity).toList();
    }

    @Transactional
    @PreAuthorize("@placeAccessHandler.ownershipCheck(#placeId)")
    public List<PlacePhotoDto> addPlacePhoto(Long placeId, List<MultipartFile> images) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.PLACE_NOT_FOUND));

        List<PlacePhoto> photos = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                String filename = fileStorageService.store(image);
                PlacePhoto placePhoto = PlacePhoto.builder()
                        .place(place)
                        .photoUrl(filename)
                        .build();
                photos.add(placePhoto);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image " + image.getOriginalFilename(), e);
            }
        }
        placePhotoRepository.saveAll(photos);

        return photos.stream().map(PlacePhotoDto::fromEntity).toList();
    }

    @Transactional
    @PreAuthorize("@productAccessHandler.ownershipCheck(#productId)")
    public List<ProductPhotoDto> addProductPhoto(Long productId, List<MultipartFile> images) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.PRODUCT_NOT_FOUND));

        List<ProductPhoto> photos = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                String filename = fileStorageService.store(image);
                ProductPhoto productPhoto = ProductPhoto.builder()
                        .product(product)
                        .photoUrl(filename)
                        .build();
                photos.add(productPhoto);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image " + image.getOriginalFilename(), e);
            }
        }
        productPhotoRepository.saveAll(photos);

        return photos.stream().map(ProductPhotoDto::fromEntity).toList();
    }
}
