package com.savoirstrategie.app.response;

import com.savoirstrategie.app.entity.Etablissement;
import com.savoirstrategie.app.entity.Intervenant;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserResponse {

    private Long id;
    private String email;
    private String role;
    private String status;
    private Date creation_date;

    private IntervenantResponse intervenantResponse;
    private EtablissementResponse etablissementResponse;

    private List<FeedbackResponse> feedbackResponseList;


}
