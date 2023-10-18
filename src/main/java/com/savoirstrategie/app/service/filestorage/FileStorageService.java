package com.savoirstrategie.app.service.filestorage;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String  uploadFileAndReturnUniqueFileName(String pathFile,MultipartFile file);

    void deleteFile(String pathFile,String filename);

    String getPathPublicUrl(String pathFile,String uniqueFileName);


    Boolean uploadAndDownloadFile(@NonNull MultipartFile file, String containerName);
}
