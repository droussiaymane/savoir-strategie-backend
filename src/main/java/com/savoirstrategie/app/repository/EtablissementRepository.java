package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Etablissement;
import com.savoirstrategie.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtablissementRepository extends JpaRepository<Etablissement, Long> {

    Etablissement findByUserId(Long id);

    void  deleteByUserId(Long id);
}
