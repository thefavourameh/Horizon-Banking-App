package com.favour.Horizon.Banking.App.service;

import com.favour.Horizon.Banking.App.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {
    ResponseEntity<BankResponse<String>> uploadProfilePics(MultipartFile multipartFile);
}
