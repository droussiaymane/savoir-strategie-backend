package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Application;
import com.savoirstrategie.app.entity.Attachement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    List<Application> findAllByIntervenantIdOrderByCreationDateDesc(Long id);

}
