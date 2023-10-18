package com.savoirstrategie.app.response.stats;

import lombok.Data;

@Data
public class EtablissementStatsResponse {
    private int nbrDemandeAccepte;

    private int nbrDemandeEnCours;

    private String statusAccount;


}
