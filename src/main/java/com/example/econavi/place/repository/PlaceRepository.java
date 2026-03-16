package com.example.econavi.place.repository;

import com.example.econavi.place.entity.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByIdGreaterThanOrderByIdAsc(long cursor, Pageable pageable);

    int countByNameAndAddress(String name, String address);

    long countByNameAndAddressAndIdNot(String name, String address, Long id);

    List<Place> findByNameContaining(String keyword);

    @Query(value = "SELECT * FROM places p " +
            "WHERE 6371 * acos( " +
            "cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(p.latitude)) " +
            ") < :distance", nativeQuery = true)
    List<Place> findPlaceWithDistance(BigDecimal latitude, BigDecimal longitude, double distance);
}
