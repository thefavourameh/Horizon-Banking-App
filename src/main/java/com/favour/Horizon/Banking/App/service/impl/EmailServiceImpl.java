package com.favour.Horizon.Banking.App.service.impl;

import com.favour.Horizon.Banking.App.infrastrusture.exception.EmailNotSentException;
import com.favour.Horizon.Banking.App.payload.request.EmailDetails;
import com.favour.Horizon.Banking.App.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderMail;


    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(senderMail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);

            System.out.println("Mail sent successfully");
        } catch (MailException e) {
            throw new EmailNotSentException("Email not sent");
        }

    }

    @Override
    public void sendEmailWithAttachment(EmailDetails emailDetails) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderMail);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            javaMailSender.send(mimeMessage);

            log.info(file.getFilename() + " has been sent to User with email " + emailDetails.getRecipient());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
