package com.example.econavi.auth.controller;

import com.example.econavi.auth.dto.LoginDto;
import com.example.econavi.auth.dto.SignUpRequestDto;
import com.example.econavi.auth.security.JwtUtil;
import com.example.econavi.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "인증 관련 서비스", description = "로그인, 회원가입 등")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginDto.Response> login(@Valid @RequestBody LoginDto.Request request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "회원가입",
            description = "회원가입"
    )
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(
            @Valid @RequestBody SignUpRequestDto request
    ) {
        return ResponseEntity.ok("회원가입 성공 " + authService.signUp(request));
    }

    @Operation(
            summary = "로그아웃",
            description = "토큰 정보 만으로 로그아웃",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getIdFromToken(token);
        authService.logout(memberId, token);

        return ResponseEntity.ok("로그아웃이 성공했습니다.");
    }
}