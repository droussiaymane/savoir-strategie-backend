package com.savoirstrategie.app.service.filestorage;


import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.specialized.BlockBlobClient;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService{

    @Autowired
    BlobServiceClient blobServiceClient;


    @Override
    public String uploadFileAndReturnUniqueFileName(String pathFile, MultipartFile file) {
        boolean isSuccess = true;

        String filename = null;
        try {
            System.out.println(pathFile);
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(pathFile);
            filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            BlockBlobClient blobClient = containerClient.getBlobClient(filename).getBlockBlobClient();

            // delete file if already exists in that container
            if (blobClient.exists()) {
                blobClient.delete();
            }

            // upload file to azure blob storage
            BlockBlobItem blockBlobClient=blobClient.upload(new BufferedInputStream(file.getInputStream()), file.getSize(), true);





        } catch (Exception e) {
            isSuccess = false;
            log.error("Error while processing file {}", e.getLocalizedMessage());
        }

        return filename;
    }





    @Override
    public void deleteFile(String pathFile,String filename) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(pathFile);
        BlockBlobClient blobClient = containerClient.getBlobClient(filename).getBlockBlobClient();

        // delete file if already exists in that container

            blobClient.delete();

    }

    @Override
    public String getPathPublicUrl(String pathFile, String uniqueFileName) {

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(pathFile);
        BlockBlobClient blobClient = containerClient.getBlobClient(uniqueFileName).getBlockBlobClient();

        return blobClient.getBlobUrl();
    }

    @Override
    public Boolean uploadAndDownloadFile(@NonNull MultipartFile file, String containerName) {
        return null;
    }
/*
    @Autowired
    private DbxClientV2 dropboxClient;

    @Override
    public String uploadFileAndReturnUniqueFileName(String pathFile,MultipartFile file) {
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();


        try (InputStream in = file.getInputStream()) {

            FileMetadata metadata =dropboxClient.files().uploadBuilder(pathFile + uniqueFilename)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);


        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
        return uniqueFilename;
    }





    @Override
    public void deleteFile(String pathFile) {
        try {
            dropboxClient.files().delete(pathFile);

        } catch (DbxException e) {
            System.out.println("Failed to delete file: " + e.getMessage());
        }
    }

    @Override
    public String getPathPublicUrl(String pathFile, String uniqueFileName) {
        PathLinkMetadata link = null;
        try {
            link = dropboxClient.sharing().createSharedLink(pathFile + uniqueFileName);
        }catch (DbxException e){
            System.out.println(e.getMessage());
        }
        return link.getUrl();
    }


    */




}
