package com.favour.Horizon.Banking.App.payload.response;

import com.favour.Horizon.Banking.App.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class APIResponse<T> {
    private String message;

    private T data;

    private String responseTime;

    public APIResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());
    }

    public APIResponse(String message){
        this.message = message;
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());

    }
}
