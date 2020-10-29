package com.dev.cinema.model.dto;

import com.dev.cinema.annotation.EmailValidation;
import com.dev.cinema.annotation.FieldsValueMatch;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords are not equal"
)
public class UserRequestDto {
    @EmailValidation
    private String email;
    @NotNull
    @Size(min = 4)
    private String password;
    private String repeatPassword;
}
