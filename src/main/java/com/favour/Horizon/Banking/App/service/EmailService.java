package com.favour.Horizon.Banking.App.service;

import com.favour.Horizon.Banking.App.payload.request.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);
}
