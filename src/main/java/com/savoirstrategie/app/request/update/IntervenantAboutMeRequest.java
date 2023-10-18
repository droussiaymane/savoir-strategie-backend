package com.savoirstrategie.app.request.update;

import lombok.Data;

import javax.persistence.Column;

@Data
public class IntervenantAboutMeRequest {
    private String firstName;
    private String lastName;
    private String ville;
    private String pays;

    private String profile;

    private String description;


}
