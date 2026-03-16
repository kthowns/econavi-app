package com.example.econavi.place.entity;

import com.example.econavi.member.entity.Member;
import com.example.econavi.place.type.PlaceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;
    @Column(name = "name")
    private String name;
    @Column(name = "start_date")
    private Timestamp startDate;
    @Column(name = "end_date")
    private Timestamp endDate;
    @Column(name = "description")
    private String description;
    @Column(name = "place_type")
    @Enumerated(EnumType.STRING)
    private PlaceType placeType;
    @Column(name = "address")
    private String address;
    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;
    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at", insertable = false)
    private Timestamp updatedAt;
}
