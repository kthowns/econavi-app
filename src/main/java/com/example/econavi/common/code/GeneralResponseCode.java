package com.example.econavi.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GeneralResponseCode implements ApiResponseCode {
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장소입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 찜입니다."),
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "포인트 정보를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),
    DUPLICATED_PLACE(HttpStatus.CONFLICT, "주소에 중복된 장소가 존재합니다."),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "중복된 계정입니다."),
    DUPLICATED_BOOKMARK(HttpStatus.CONFLICT, "이미 찜한 장소입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    LOGIN_OK(HttpStatus.OK, "로그인에 성공했습니다.");

    private final HttpStatus status;
    private final String message;
}