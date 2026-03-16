package com.example.econavi.product.entity;

import com.example.econavi.member.entity.Member;
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
@Table(name = "points")
public class Point {
    @Id
    private Long memberId;
    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "updated_at", insertable = false)
    private Timestamp updatedAt;
}
