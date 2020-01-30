package com.kys.example.common.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kys.example.common.constants.ErrorCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author kody.kim
 * @since 20/01/2020
 */
@ToString(callSuper = true)
@Getter
public class DataResponse<T> extends Response {

    @JsonProperty(value = "data")
    private T data;

    protected DataResponse(ErrorCode errorCode, T data) {
        super(errorCode);
        this.data = data;
    }

    public static <T> DataResponse<T> ok(T data){
        return new DataResponse<>(ErrorCode.CD_0000, data);
    }
}
