package com.favour.Horizon.Banking.App.service;

import com.favour.Horizon.Banking.App.payload.request.CreditAndDebitRequest;
import com.favour.Horizon.Banking.App.payload.request.EnquiryRequest;
import com.favour.Horizon.Banking.App.payload.request.TransferRequest;
import com.favour.Horizon.Banking.App.payload.response.BankResponse;

public interface UserService {
    BankResponse creditAccount(CreditAndDebitRequest request);

    BankResponse debitAccount(CreditAndDebitRequest request);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse transfer(TransferRequest transferRequest);
}
