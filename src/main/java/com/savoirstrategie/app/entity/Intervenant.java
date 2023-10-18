package com.savoirstrategie.app.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "intervenant")
@Data
public class Intervenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String ville;
    private String pays;

    private String profile="Intervenant";

    @Column(columnDefinition = "TEXT")
    private String description="Non défini.";

    private String avatar_url="https://img.freepik.com/free-icon/user_318-159711.jpg";

    private String avatar_unique_file_name;
    private String statusAccount; //verified and unverified

    @OneToMany(mappedBy = "intervenant")
    private List<Feedback> feedbacks;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;



    @OneToMany(mappedBy="intervenant")
    private List<Attachement> attachements;



    @ManyToMany(mappedBy = "intervenants")
    private List<Opportunity> opportunities;

    @OneToMany(mappedBy = "intervenant")
    private List<Application> applications;


}
