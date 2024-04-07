package com.favour.Horizon.Banking.App.service.impl;

import com.favour.Horizon.Banking.App.domain.entities.UserEntity;
import com.favour.Horizon.Banking.App.payload.request.*;
import com.favour.Horizon.Banking.App.payload.response.AccountInfo;
import com.favour.Horizon.Banking.App.payload.response.BankResponse;
import com.favour.Horizon.Banking.App.repository.UserRepository;
import com.favour.Horizon.Banking.App.service.EmailService;
import com.favour.Horizon.Banking.App.service.TransactionService;
import com.favour.Horizon.Banking.App.service.UserService;
import com.favour.Horizon.Banking.App.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;
    @Override
    public BankResponse creditAccount(CreditAndDebitRequest request) {

        //to credit an account, first check if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
            if (!isAccountExists){
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_EXIST_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                        .accountInfo(null)
                        .build();

            }

            UserEntity userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

            userRepository.save(userToCredit);

            EmailDetails creditAlert = EmailDetails.builder()
                    .subject("CREDIT ALERT")
                    .recipient(userToCredit.getEmail())
                    .messageBody("Your Account has been Credited with $" + request.getAmount() + " from " + userToCredit.getFirstName() + " "
                    + userToCredit.getLastName() + ". Your current Account Balance is " + "$"+ userToCredit.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(creditAlert);

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToCredit.getFirstName() + " " + userToCredit.getMiddleName() + " " + userToCredit.getLastName())
                            .accountBalance(userToCredit.getAccountBalance())
                            .accountNumber(request.getAccountNumber())
                            .build())
                    .build();
    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest request) {
        //to debit an account, first check if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        UserEntity userToDebit = userRepository.findByAccountNumber((request.getAccountNumber()));


        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if (availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of  $"+ request.getAmount() + " has been deducted from your account. Your current Account Balance is  $" + userToDebit.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(debitAlert);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())
                    .build();

            transactionService.saveTransaction(transactionRequest);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getMiddleName() + " " + userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(userToDebit.getAccountNumber())
                            .build())
                    .build();
        }
    }

    @Override

    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NUMBER_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getMiddleName() + " " + foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExists){
            return AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE;
        }


        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUser.getFirstName() + " " + foundUser.getMiddleName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest)  {
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());

        if (!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        UserEntity destinationAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());

        if (transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            //compareTo method is the reason why it is > 0 and not < 0
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceAccountUser);

        String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getMiddleName() + " " + sourceAccountUser.getLastName();

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of  $"+ transferRequest.getAmount() + " has been deducted from your account and transferred to " + destinationAccountUser.getFirstName() +
                        " " + destinationAccountUser.getMiddleName() + " " + destinationAccountUser.getLastName() + ". Your current Account Balance is  $" + sourceAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(debitAlert);

     //   UserEntity destinationAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Your Account has been Credited with $" + transferRequest.getAmount() + " from " + sourceAccountUser.getFirstName() + " " + sourceAccountUser.getMiddleName() + " "
                        + sourceAccountUser.getLastName() + ". Your current Account Balance is " + "$"+ destinationAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        //save transfer transaction done
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("TRANSFER")
                .amount(transferRequest.getAmount())
                .build();
        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
