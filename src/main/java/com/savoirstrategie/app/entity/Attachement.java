package com.savoirstrategie.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attachement")
@Data
public class Attachement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String attachement_url;

    private String type;

    private String attachement_unique_file_name;

    private LocalDateTime createdDate;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intervenant_id", referencedColumnName = "id")
    private Intervenant intervenant;

}
