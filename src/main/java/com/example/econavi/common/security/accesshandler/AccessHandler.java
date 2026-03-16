package com.example.econavi.common.security.accesshandler;

import com.example.econavi.member.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AccessHandler {
    public final boolean ownershipCheck(Long resourceId) {
        return isResourceOwner(resourceId);
    }

    public final Long getCurrentMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((Member) auth.getPrincipal()).getId();
    }

    abstract boolean isResourceOwner(Long resourceId);
}
