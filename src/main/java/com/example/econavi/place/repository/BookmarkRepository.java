package com.example.econavi.place.repository;

import com.example.econavi.member.entity.Member;
import com.example.econavi.place.entity.Bookmark;
import com.example.econavi.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMember(Member member);

    int countByMemberAndPlace(Member member, Place place);
}
