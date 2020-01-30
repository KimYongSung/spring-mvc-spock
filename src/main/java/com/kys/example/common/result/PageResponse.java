package com.kys.example.common.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kys.example.common.constants.ErrorCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author kody.kim
 * @since 20/01/2020
 */
@Getter
@ToString(callSuper = true)
public class PageResponse<T> extends Response{

    private int totalSize;

    private int size;

    private int page;

    @JsonProperty(value = "data")
    private List<T> data;

    protected PageResponse(ErrorCode errorCode, int totalSize, int size, int page, List<T> data) {
        super(errorCode);
        this.totalSize = totalSize;
        this.size = size;
        this.page = page;
        this.data = data;
    }

    public static <T> PageResponse<T> success(int totalSize, int size, int page, List<T> data) {
        return new PageResponse<>(ErrorCode.CD_0000, totalSize, size, page, data);
    }
}
