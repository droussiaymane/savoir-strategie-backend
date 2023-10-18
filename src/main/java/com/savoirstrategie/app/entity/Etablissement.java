package com.savoirstrategie.app.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "etablissement")
@Data
public class Etablissement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String ville;
    private String pays;

    @Column(columnDefinition = "TEXT")
    private String description="Non défini.";

    private String avatar_url="https://www.pngall.com/wp-content/uploads/5/University-PNG-Photo.png";

    private String avatar_unique_file_name;

    private String statusAccount;

    @OneToMany(mappedBy = "etablissement")
    private List<Feedback> feedbacks;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
