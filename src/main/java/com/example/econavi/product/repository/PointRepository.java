package com.example.econavi.product.repository;

import com.example.econavi.member.entity.Member;
import com.example.econavi.product.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByMember(Member member);
}
