package com.favour.Horizon.Banking.App.payload.request;

import com.favour.Horizon.Banking.App.domain.enums.Roles;
import com.favour.Horizon.Banking.App.validations.ValidEmail;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @Size(min = 2, max = 35, message = "First name must  be at least 2 characters")
    @NotBlank(message = "First name must  not be empty")
    private String firstName;

    @Size(min = 2, max = 35, message = "Last name must  be at least 2 characters")
    @NotBlank(message = "Last name must  not be empty")
    private String lastName;

    private String middleName;

    @Size(min = 11, max = 15, message = "Phone number is too short or too long")
    @NotBlank(message = "Phone number must  not be empty")
    @Digits(fraction = 0, integer = 11, message = "Phone number is incorrect")
    private String phoneNumber;

    private String alternativePhoneNumber;


    @ValidEmail
    @Email(message = "Invalid email")
    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;

    private String stateOfOrigin;

    private String address;

    private String gender;

}
