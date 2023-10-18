package com.savoirstrategie.app.request;

import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class UserRegisterRequest {

    @Email(message = "Merci de renseigner un e-mail valide.")
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp ="^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=_!*(){}\\\\[\\\\]:;<>,.?/~`|\\\\-])[A-Za-z0-9@#$%^&+=_!*(){}\\\\[\\\\]:;<>,.?/~`|\\\\-]{8,}$" , message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;

    @NotBlank
    private String role;

    private String firstName;
    private String lastName;
    @NotBlank
    private String ville;
    @NotBlank
    private String pays;


    private String name;

    private String type;





}
