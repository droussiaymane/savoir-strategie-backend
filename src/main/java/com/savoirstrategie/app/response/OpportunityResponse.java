package com.savoirstrategie.app.response;

import com.savoirstrategie.app.entity.Etablissement;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
public class OpportunityResponse {


    private Long id;

    private String title;

    private String ville;

    private String pays;


    private String descriptionOffre;



    private String descriptionPoste;

    private Long salaire;
    private Long volumeHoraire;


    private int applicationsNumber;
    private LocalDateTime creationDate;



    private EtablissementResponse etablissementResponse;

}
