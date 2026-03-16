package com.example.econavi.unit.service;

import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.place.dto.BookmarkDto;
import com.example.econavi.place.entity.Bookmark;
import com.example.econavi.place.entity.Place;
import com.example.econavi.place.repository.BookmarkRepository;
import com.example.econavi.place.repository.PlaceRepository;
import com.example.econavi.place.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private Member member;
    private Place place;
    private Bookmark bookmark;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .username("testuser")
                .build();

        place = Place.builder()
                .id(1L)
                .name("test place")
                .build();

        bookmark = Bookmark.builder()
                .id(1L)
                .member(member)
                .place(place)
                .build();
    }

    @DisplayName("[BookmarkService][Unit] getBookmarks_test_success")
    @Test
    void getBookmarks_test_success() {
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(bookmarkRepository.findByMember(member)).willReturn(List.of(bookmark));

        List<BookmarkDto> result = bookmarkService.getBookmarks(member.getId());

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(bookmark.getId());
    }

    @DisplayName("[BookmarkService][Unit] addBookmark_test_success")
    @Test
    void addBookmark_test_success() {
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(placeRepository.findById(place.getId())).willReturn(Optional.of(place));
        given(bookmarkRepository.countByMemberAndPlace(member, place)).willReturn(0);
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(bookmark);

        BookmarkDto result = bookmarkService.addBookmark(member.getId(), place.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookmark.getId());
    }

    @DisplayName("[BookmarkService][Unit] deleteBookmark_test_success")
    @Test
    void deleteBookmark_test_success() {
        given(bookmarkRepository.findById(bookmark.getId())).willReturn(Optional.of(bookmark));

        BookmarkDto result = bookmarkService.deleteBookmark(member.getId(), place.getId());

        verify(bookmarkRepository).delete(bookmark);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookmark.getId());
    }
}

