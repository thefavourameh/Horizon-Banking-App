package com.favour.Horizon.Banking.App.domain.entities;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {
    private String message;

    private String debugMessage;

    private HttpStatus status;

    private String dateTime;
}
