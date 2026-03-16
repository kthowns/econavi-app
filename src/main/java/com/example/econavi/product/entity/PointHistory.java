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
@Table(name = "point_history")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "reason")
    private String reason;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}
