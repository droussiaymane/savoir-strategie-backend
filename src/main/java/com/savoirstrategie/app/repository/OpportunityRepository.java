package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity,Long> {



    List<Opportunity> findAllByEtablissementIdOrderByCreationDateDesc(Long etablissementId);
}
