package com.example.econavi.auth.dto;


import com.example.econavi.member.type.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDto {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,100}$", message = "계정은 이메일 형식이어야 합니다.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,16}$", message = "이름은 2~16자, 한글/영문/숫자만 허용됩니다.")
    private String name;

    @NotNull
    private Role role;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,72}$",
            message = "비밀번호는 8자 이상이어야 하며 알파벳, 숫자, 특수문자를 각각 최소 하나 포함해야 합니다."
    )    //최소 8자, 숫자와 알파벳과 특수문자
    private String password;
}
