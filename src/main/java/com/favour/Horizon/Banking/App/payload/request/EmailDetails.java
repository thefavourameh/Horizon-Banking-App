package com.favour.Horizon.Banking.App.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {
    private String recipient;

    private String messageBody;

    private String attachment;

    private String subject;

}
