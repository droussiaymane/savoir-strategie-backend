package com.savoirstrategie.app.response;

import lombok.Data;

@Data
public class EtablissementResponse {


    private Long id;

    private String name;

    private String type;
    private String ville;
    private String pays;

    private String avatar_url;

    private String avatar_unique_file_name;
    private String statusAccount;
    private String description;


}
