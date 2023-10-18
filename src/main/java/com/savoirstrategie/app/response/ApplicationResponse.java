package com.savoirstrategie.app.response;

import com.savoirstrategie.app.entity.Intervenant;
import com.savoirstrategie.app.entity.Opportunity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class ApplicationResponse {

    private Long id;

    private String name;

    private String email;

    private boolean approved;

    private LocalDateTime creationDate;

    private String message;


    private Long opportunityId;
    private String opportunityTitle;


    private String EtablissementName;
    private Long EtablissementId;


}
