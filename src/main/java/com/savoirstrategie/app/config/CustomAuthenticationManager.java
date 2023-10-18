package com.savoirstrategie.app.config;


import com.savoirstrategie.app.SpringApplicationContext;
import com.savoirstrategie.app.entity.User;
import com.savoirstrategie.app.service.userservice.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.savoirstrategie.app.exception.UnverifiedException;
import java.util.Arrays;

public class CustomAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        PasswordEncoder encoder = (PasswordEncoder) SpringApplicationContext.getBean("passwordEncoder");
        User user = userService.getUser(email);

        if (user != null) {
            if (!user.getStatus().equals("blocked"))  // user is not blocked
            {
                if(user.getStatus().equals("unverified")){

                    throw new UnverifiedException("Votre compte n'est pas vérifié ,consulter votre addresse mail pour le vérifier");
                }
                else if (!encoder.matches(password, user.getPassword())) {
                    throw new BadCredentialsException("Mot de passe incorrect");
                }
                else {
                    return new UsernamePasswordAuthenticationToken(email, password, Arrays.asList(new SimpleGrantedAuthority(user.getRole())));

                }

            }
            else {

                throw new LockedException("Votre compte a été bloqué par l'administrateur. Veuillez essayer de contacter le support.");

            }
        }

        else{
            throw new BadCredentialsException("Utilisateur introuvable");
        }

    }

}
