package com.example.econavi.place.service;

import com.example.econavi.common.code.AuthResponseCode;
import com.example.econavi.common.code.GeneralResponseCode;
import com.example.econavi.common.exception.ApiException;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.place.dto.BookmarkDto;
import com.example.econavi.place.entity.Bookmark;
import com.example.econavi.place.entity.Place;
import com.example.econavi.place.repository.BookmarkRepository;
import com.example.econavi.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @PreAuthorize("@memberAccessHandler.ownershipCheck(#memberId)")
    public List<BookmarkDto> getBookmarks(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));
        List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);

        return bookmarks.stream().map(BookmarkDto::fromEntity).toList();
    }

    @Transactional
    @PreAuthorize("@memberAccessHandler.ownershipCheck(#memberId)")
    public BookmarkDto addBookmark(Long memberId, Long placeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.PLACE_NOT_FOUND));

        int count = bookmarkRepository.countByMemberAndPlace(member, place);

        if (count > 0) {
            throw new ApiException(GeneralResponseCode.DUPLICATED_PLACE);
        }

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .place(place)
                .build();

        bookmark = bookmarkRepository.save(bookmark);

        return BookmarkDto.fromEntity(bookmark);
    }

    @Transactional
    @PreAuthorize("@memberAccessHandler.ownershipCheck(#memberId)")
    public BookmarkDto deleteBookmark(Long memberId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.BOOKMARK_NOT_FOUND));
        bookmarkRepository.delete(bookmark);

        return BookmarkDto.fromEntity(bookmark);
    }
}
