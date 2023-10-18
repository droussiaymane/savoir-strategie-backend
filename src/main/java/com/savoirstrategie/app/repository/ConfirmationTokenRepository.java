package com.savoirstrategie.app.repository;


import com.savoirstrategie.app.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByConfirmationtoken(String confirmationToken);
    Token findByConfirmationtoken(String confirmationToken);
    Token findByUserEmail(String confirmationToken);

     void deleteByUserId(Long userid);
}