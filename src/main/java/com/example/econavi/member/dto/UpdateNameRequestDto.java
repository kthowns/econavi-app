package com.example.econavi.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateNameRequestDto {
    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,16}$", message = "이름은 2~16자, 한글/영문/숫자만 허용됩니다.")
    private String name;
}
