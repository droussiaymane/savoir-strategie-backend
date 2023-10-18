package com.savoirstrategie.app.service.userservice;


import com.savoirstrategie.app.entity.Token;

public interface TokenConfirmationService {

    public Token generateConfirmationToken(String email);

    public Token createToken(String email);


    public void confirmEmail(String confirmationToken);

}
