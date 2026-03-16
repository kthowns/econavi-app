package com.example.econavi.photo.repository;

import com.example.econavi.member.entity.Member;
import com.example.econavi.photo.entity.MemberPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberPhotoRepository extends JpaRepository<MemberPhoto, Long> {
    List<MemberPhoto> findByMember(Member member);

    List<MemberPhoto> findByMember_id(Long memberId);
}
