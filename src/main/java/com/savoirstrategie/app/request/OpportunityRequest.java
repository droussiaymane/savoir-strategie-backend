package com.savoirstrategie.app.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityRequest {


    @NotBlank
    private String title;

    @NotBlank
    private String ville;

    @NotBlank
    private String pays;


    @NotBlank
    private String descriptionOffre;

    @NotBlank
    private String descriptionPoste;


    @Min(0)
    private Long salaire;

    @Min(0)
    private Long volumeHoraire;



}
