package com.favour.Horizon.Banking.App.service;

import com.favour.Horizon.Banking.App.payload.request.TransactionRequest;

public interface TransactionService {
    void saveTransaction(TransactionRequest transactionRequest);
}
