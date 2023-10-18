package com.savoirstrategie.app.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "opportunity")
@Data
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String ville;

    private String pays;

    @Column(columnDefinition = "TEXT")
    private String descriptionOffre;


    @Column(columnDefinition = "TEXT")
    private String descriptionPoste;

    private Long salaire;
    private Long volumeHoraire;


    private int applicationsNumber=0;

    private LocalDateTime creationDate;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "etablissement_id", referencedColumnName = "id")
    private Etablissement etablissement;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "opportunity_intervenant",
            joinColumns = @JoinColumn(name = "opportunity_id"),
            inverseJoinColumns = @JoinColumn(name = "intervenant_id"))
    private List<Intervenant> intervenants;


    @OneToMany(mappedBy = "opportunity")
    private List<Application> applications;



}
