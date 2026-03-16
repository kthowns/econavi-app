package com.example.econavi.photo.repository;

import com.example.econavi.place.entity.Place;
import com.example.econavi.photo.entity.PlacePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacePhotoRepository extends JpaRepository<PlacePhoto, Long> {
    List<PlacePhoto> findByPlace(Place place);

    List<PlacePhoto> findByPlace_id(Long placeId);
}
