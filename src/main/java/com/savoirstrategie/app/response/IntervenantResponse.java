package com.savoirstrategie.app.response;

import com.savoirstrategie.app.entity.Feedback;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;
import java.util.Set;

@Data
public class IntervenantResponse {

    private Long id;

    private String firstName;
    private String lastName;
    private String ville;
    private String pays;
    private String profile;


    private String description;

    private String avatar_url;

    private String avatar_unique_file_name;
    private String statusAccount;



    private List<AttachementResponse> attachementsResponse;
}
