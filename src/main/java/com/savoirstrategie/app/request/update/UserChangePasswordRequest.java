package com.savoirstrategie.app.request.update;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserChangePasswordRequest {


    private String oldPassword;




    @NotBlank
    @Pattern(regexp ="^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=_!*(){}\\\\[\\\\]:;<>,.?/~`|\\\\-])[A-Za-z0-9@#$%^&+=_!*(){}\\\\[\\\\]:;<>,.?/~`|\\\\-]{8,}$" , message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String newPassword;


}
