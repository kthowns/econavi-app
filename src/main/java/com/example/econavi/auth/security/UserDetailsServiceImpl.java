package com.example.econavi.auth.security;

import com.example.econavi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
    }
}
