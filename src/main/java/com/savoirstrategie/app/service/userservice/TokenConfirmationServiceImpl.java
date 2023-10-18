package com.savoirstrategie.app.service.userservice;

import com.savoirstrategie.app.entity.Token;
import com.savoirstrategie.app.entity.User;
import com.savoirstrategie.app.exception.ExpiredTokenException;
import com.savoirstrategie.app.exception.TokenNotFoundException;
import com.savoirstrategie.app.exception.UserAlreadyVerifiedException;
import com.savoirstrategie.app.repository.ConfirmationTokenRepository;
import com.savoirstrategie.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class TokenConfirmationServiceImpl implements TokenConfirmationService{

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;


    @Autowired
    UserRepository userRepository;


    @Override
    public Token generateConfirmationToken(String email) {
        // token already exists
        Token confirmationToken=confirmationTokenRepository.findByUserEmail(email);
        if(confirmationToken!=null){
            if(confirmationToken.isExpired()){
                return createToken(email);
            }
            else{
                return confirmationToken;
            }
        }
        return createToken(email);


    }

    @Override
    public Token createToken(String email) {
        Token confirmationToken=new Token();
        confirmationToken.setConfirmationtoken(generateToken());

        confirmationToken.setCreatedDate(LocalDateTime.now());
        confirmationToken.setExpired(false);
        User user=userRepository.findByEmail(email);
        confirmationToken.setUser(user);
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    @Override
    public void confirmEmail(String confirmationToken) {
        Token token=confirmationTokenRepository.findByConfirmationtoken(confirmationToken);

        if(token!=null) {
            // check if the user already verified
            User user=token.getUser();
            if(user.getStatus().equals("unverified")){
                if (token.isExpired()) { // alreaady expired
                    // throw token expired message
                    throw new ExpiredTokenException("Token is expired. Generate new one please.");
                } else {
                    // Get the creation date of the token
                    LocalDateTime createdDate = token.getCreatedDate();

                    // Get the current date and time
                    LocalDateTime now = LocalDateTime.now();

                    // Calculate the difference between the two dates in days
                    long daysBetween = ChronoUnit.DAYS.between(createdDate, now);

                    if (daysBetween >= 1) {
                        // Token has expired
                        token.setExpired(true);
                        confirmationTokenRepository.save(token);
                        throw new ExpiredTokenException("Token is expired. Generate new one please.");
                        // throw token expired message
                    } else {
                        // Token is valid
                        // Activate user's account

                        user.setStatus("active");
                        user.setEmailVerified(true);
                        userRepository.save(user);

                    }


                }
            }
            else{
                throw new UserAlreadyVerifiedException("User is already verified");
            }


        }
        else{
            throw new TokenNotFoundException("No user found exception.");
        }

    }


    public String generateToken(){
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        // Convert the UUID to a string
        String token = uuid.toString();
        return token;
    }


}
