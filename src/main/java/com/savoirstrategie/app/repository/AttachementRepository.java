package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Attachement;
import com.savoirstrategie.app.entity.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachementRepository extends JpaRepository<Attachement,Long> {

    List<Attachement> findAllByIntervenantIdOrderByCreatedDateDesc(Long id);

}
