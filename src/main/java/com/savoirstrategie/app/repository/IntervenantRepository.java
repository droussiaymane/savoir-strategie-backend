package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Etablissement;
import com.savoirstrategie.app.entity.Intervenant;
import com.savoirstrategie.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervenantRepository extends JpaRepository<Intervenant, Long> {
    Intervenant findByUserId(Long id);
    void  deleteByUserId(Long id);
}
