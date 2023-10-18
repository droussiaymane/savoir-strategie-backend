package com.savoirstrategie.app.helpers;

import org.springframework.beans.factory.annotation.Value;

public class FilePath {



    public static final String CONTAINER_NAME="blob-container";

    public static final String INTERVENANT_FOLDER_PREFIX=CONTAINER_NAME+"/intervenants";

    public static final String ETABLISSEMENT_FOLDER_PREFIX=CONTAINER_NAME+"/etablissements";
    public static final String INTERVENANT_FOLDER_AVATAR_SUFFIX="/avatar";
    public static final String ETABLISSEMENT_FOLDER_AVATAR_SUFFIX="/avatar";
    public static final String INTERVENANT_FOLDER_ATTACHEMENTS_SUFFIX="/attachements";

}
