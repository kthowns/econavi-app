package com.example.econavi.common.security.accesshandler;

import com.example.econavi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAccessHandler extends AccessHandler {
    private final MemberRepository memberRepository;

    @Override
    boolean isResourceOwner(Long resourceId) {
        return memberRepository.findById(resourceId)
                .filter((m) -> getCurrentMemberId().equals(resourceId))
                .isPresent();
    }
}
