package com.favour.Horizon.Banking.App.service.impl;

import com.favour.Horizon.Banking.App.domain.entities.Transaction;
import com.favour.Horizon.Banking.App.domain.entities.UserEntity;
import com.favour.Horizon.Banking.App.payload.request.EmailDetails;
import com.favour.Horizon.Banking.App.repository.TransactionRepository;
import com.favour.Horizon.Banking.App.repository.UserRepository;
import com.favour.Horizon.Banking.App.service.EmailService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankStatementImpl {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final String FILE = System.getProperty("user.home") + "/Desktop/BankStatement.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll()
                .stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();

        UserEntity user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
        String customerAddress = user.getAddress();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting size of document");

        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Horizon Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.CYAN);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("1, London Street, Grimsby"));
        bankAddress.setPadding(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(1);
        PdfPCell dateFrom = new PdfPCell(new Phrase("Start Date: " + startDate));
        dateFrom.setBorder(0);

        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);

        PdfPCell dateTo = new PdfPCell(new Phrase("End Date: " + endDate));
        dateTo.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        PdfPCell address = new PdfPCell(new Phrase("Address: " + customerAddress));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.CYAN);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.CYAN);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.CYAN);
        transactionAmount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.CYAN);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        transactionList.forEach(transaction -> {
            transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfo.addCell(dateFrom);
        statementInfo.addCell(statement);
        statementInfo.addCell(dateTo);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your Statement of Account attached")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;


    }
}
