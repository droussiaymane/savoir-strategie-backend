package com.savoirstrategie.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @Email(message = "Merci de renseigner un e-mail valide.")
    @NotBlank
    private String email;
    @NotBlank
    private String password;

}