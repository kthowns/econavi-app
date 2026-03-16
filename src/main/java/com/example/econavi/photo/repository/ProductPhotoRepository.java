package com.example.econavi.photo.repository;

import com.example.econavi.photo.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
    List<ProductPhoto> findByProduct_Id(Long productId);
}
