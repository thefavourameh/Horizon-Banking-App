package com.favour.Horizon.Banking.App.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnquiryRequest {
    private String accountNumber;
}
