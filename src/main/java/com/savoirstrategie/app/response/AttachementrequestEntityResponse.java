package com.savoirstrategie.app.response;

import com.savoirstrategie.app.entity.Etablissement;
import com.savoirstrategie.app.entity.Intervenant;
import com.savoirstrategie.app.entity.Opportunity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class AttachementrequestEntityResponse {


    private Long id;

    private LocalDateTime requestDate;

    private boolean approved;




    private Long intervenantUserId;
    private String intervenantname;

    private Long etablissementId;

    private String etablissementname;



    private Long opportunityId;

    private String opportunityTitle;
}
