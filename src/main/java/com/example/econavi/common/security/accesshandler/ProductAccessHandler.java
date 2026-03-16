package com.example.econavi.common.security.accesshandler;

import com.example.econavi.common.code.AuthResponseCode;
import com.example.econavi.common.exception.ApiException;
import com.example.econavi.member.entity.Member;
import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.member.type.Role;
import com.example.econavi.product.entity.Product;
import com.example.econavi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductAccessHandler extends AccessHandler {
    private final MemberRepository memberRepository;

    @Override
    boolean isResourceOwner(Long resourceId) {
        Member member = memberRepository.findById(getCurrentMemberId())
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        return member.getRole().equals(Role.STAFF);
    }
}
