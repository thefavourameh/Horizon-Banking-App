package com.favour.Horizon.Banking.App.service.impl;

import com.favour.Horizon.Banking.App.domain.entities.UserEntity;
import com.favour.Horizon.Banking.App.infrastrusture.config.JwtAuthenticationFilter;
import com.favour.Horizon.Banking.App.infrastrusture.config.JwtTokenProvider;
import com.favour.Horizon.Banking.App.payload.response.BankResponse;
import com.favour.Horizon.Banking.App.repository.UserRepository;
import com.favour.Horizon.Banking.App.service.GeneralUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralUserServiceImpl implements GeneralUserService {
    private final FileUploadServiceImpl fileUploadService;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final HttpServletRequest httpServletRequest;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<BankResponse<String>> uploadProfilePics(MultipartFile profilePics) {

        String token = jwtAuthenticationFilter.getTokenFromRequest(httpServletRequest);
        //this next line is used to attach the bearer token to the email
        String email = jwtTokenProvider.getUserName(token);

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);

        String fileUrl = null;

        try {
            if (userEntityOptional.isPresent()){
                fileUrl = fileUploadService.uploadFile(profilePics);

                UserEntity userEntity = userEntityOptional.get();
                userEntity.setProfilePicture(fileUrl);

                userRepository.save(userEntity);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(
                new BankResponse<>(
                        "Upload successfully",
                        fileUrl
                )
        );
    }
}
