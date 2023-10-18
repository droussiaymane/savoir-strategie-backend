package com.savoirstrategie.app.response;

import com.savoirstrategie.app.entity.Attachement;
import com.savoirstrategie.app.entity.User;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Set;


@Data
public class AttachementResponse {

    private Long id;

    private String attachement_url;

    private String type;

    private String attachement_unique_file_name;

    private LocalDateTime createdDate;
}
