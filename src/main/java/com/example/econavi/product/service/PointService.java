package com.example.econavi.product.service;

import com.example.econavi.common.code.AuthResponseCode;
import com.example.econavi.common.code.GeneralResponseCode;
import com.example.econavi.common.exception.ApiException;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.product.dto.PointDto;
import com.example.econavi.product.dto.PointHistoryDto;
import com.example.econavi.product.entity.Point;
import com.example.econavi.product.entity.PointHistory;
import com.example.econavi.product.repository.PointHistoryRepository;
import com.example.econavi.product.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(readOnly = true)
    @PreAuthorize("@memberAccessHandler.ownershipCheck(#memberId)")
    public PointDto getPoint(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        Point point = pointRepository.findByMember(member)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.POINT_NOT_FOUND));

        return PointDto.fromEntity(point);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@memberAccessHandler.ownershipCheck(#memberId)")
    public List<PointHistoryDto> getPointHistories(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        List<PointHistory> pointHistories = pointHistoryRepository.findAllByMember(member);

        return pointHistories.stream().map(PointHistoryDto::fromEntity).toList();
    }
}
