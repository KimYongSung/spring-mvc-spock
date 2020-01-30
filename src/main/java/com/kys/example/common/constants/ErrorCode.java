package com.kys.example.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author kody.kim
 * @since 20/01/2020
 */
@RequiredArgsConstructor
@Getter
@ToString
public enum ErrorCode {

    CD_0000("0000","success")
    ,CD_0001("0001","파라미터 유효성 검사 실패")
    ,CD_S999("S999","시스템 에러")
    ;

    private final String code;

    private final String message;
}
