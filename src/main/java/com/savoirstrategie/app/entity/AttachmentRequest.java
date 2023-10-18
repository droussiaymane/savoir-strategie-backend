package com.savoirstrategie.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class AttachmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime requestDate;

    private boolean approved=false;

    @ManyToOne
    @JoinColumn(name = "intervenant_id")
    private Intervenant intervenant;

    @ManyToOne
    @JoinColumn(name = "etablissement_id")
    private Etablissement etablissement;


    @ManyToOne
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

}
