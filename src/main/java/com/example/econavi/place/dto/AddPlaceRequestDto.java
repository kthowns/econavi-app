package com.example.econavi.place.dto;

import com.example.econavi.place.type.PlaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPlaceRequestDto {
    @NotBlank(message = "장소의 이름은 공백이 아니어야 합니다.")
    private String name;
    private Timestamp startDate;
    private Timestamp endDate;
    private String description;
    @NotNull(message = "장소의 종류를 선택해야 합니다. (EVENT 또는 STORE)")
    private PlaceType placeType;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
