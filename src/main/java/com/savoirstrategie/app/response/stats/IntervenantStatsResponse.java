package com.savoirstrategie.app.response.stats;

import lombok.Data;

@Data
public class IntervenantStatsResponse {
    private int nbrCondidatureAcceptes;
    private int nbrEnCours;

    private String statusAccount;
}
