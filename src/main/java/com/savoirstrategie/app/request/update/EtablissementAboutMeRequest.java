package com.savoirstrategie.app.request.update;

import lombok.Data;

import javax.persistence.Column;

@Data
public class EtablissementAboutMeRequest {

    private String name;


    private String ville;
    private String pays;


    private String description;
}
