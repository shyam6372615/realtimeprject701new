package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData<T> {
    private int code;
    private String message;
    private T data;
}
