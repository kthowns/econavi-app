package com.example.econavi.common.security.accesshandler;

import com.example.econavi.member.repository.MemberRepository;
import com.example.econavi.place.entity.Place;
import com.example.econavi.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceAccessHandler extends AccessHandler {
    private final PlaceRepository placeRepository;

    @Override
    boolean isResourceOwner(Long resourceId) {
        return placeRepository.findById(resourceId)
                .map(Place::getOwner)
                .filter((m) -> getCurrentMemberId().equals(m.getId()))
                .isPresent();
    }
}
