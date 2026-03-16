package com.example.econavi.photo.entity;

import com.example.econavi.place.entity.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "place_photo")
public class PlacePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}
