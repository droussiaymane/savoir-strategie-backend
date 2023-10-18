package com.savoirstrategie.app.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role;

    private String status; //blocked , active , unverified

    private boolean isEmailVerified=false;
    private Date creation_date;
    @OneToOne(mappedBy = "user")
    private Intervenant intervenant;

    @OneToOne(mappedBy = "user")
    private Etablissement etablissement;

    @OneToMany(mappedBy="user")
    private List<Notification> notifications;

}
