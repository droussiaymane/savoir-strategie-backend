package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Attachement;
import com.savoirstrategie.app.entity.AttachmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachementRequestRepository extends JpaRepository<AttachmentRequest,Long> {

        List<AttachmentRequest> findAllByIntervenantIdOrderByRequestDateDesc(Long id);
        List<AttachmentRequest> findAllByEtablissementIdOrderByRequestDate(Long id);
}
