package com.savoirstrategie.app.controller;


import com.savoirstrategie.app.helpers.CODE;
import com.savoirstrategie.app.request.update.IntervenantAboutMeRequest;
import com.savoirstrategie.app.request.update.UserChangePasswordRequest;
import com.savoirstrategie.app.response.Response;
import com.savoirstrategie.app.response.UserResponse;
import com.savoirstrategie.app.service.filestorage.FileStorageService;
import com.savoirstrategie.app.service.intervenant.IntervenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/intervenant")
public class IntervenantController {


    @Autowired
    IntervenantService intervenantService;

    @Autowired
    FileStorageService fileStorageService;

    @PutMapping("/compte/update/avatar")
    public ResponseEntity<?> updatecompteAvatar(@RequestParam("file") MultipartFile file,@RequestParam("userid")Long userid) {

        intervenantService.updateAvatar(userid,file);

        Response<Object> response = Response.builder()
                .message("le fichier a été uploadé avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity getAllIntervenants(){
        List<UserResponse> userResponseList=intervenantService.findAll();
        Response<Object> response = Response.builder()
                .data(userResponseList)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }

    @GetMapping("/canaccess")
    public ResponseEntity canAccessAttachements(@RequestParam("userid") Long userid,@RequestParam("intervenantuserid") Long intervenantuserid){
        Boolean result=intervenantService.canAccessAttachements(userid,intervenantuserid);
        Response<Object> response = Response.builder()
                .data(result)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }



    @PutMapping("/compte/update/about")
    public ResponseEntity<?> updateAboutMe(@RequestBody IntervenantAboutMeRequest intervenantAboutMeRequest, @RequestParam("userid")Long userid) {

        intervenantService.updateAboutMe(userid,intervenantAboutMeRequest);

        Response<Object> response = Response.builder()
                .message("les informations ont été updatés avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PutMapping("/compte/update/changepassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UserChangePasswordRequest userChangePasswordRequest, @RequestParam("userid")Long userid) {

        intervenantService.changePassword(userid,userChangePasswordRequest);

        Response<Object> response = Response.builder()
                .message("les informations ont été updatés avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PutMapping("/compte/update/attachements/{type}")
    public ResponseEntity<?> updatecompteAttachements(@PathVariable String type, @RequestParam("file") MultipartFile file, @RequestParam("userid")Long userid) throws IOException {

        intervenantService.updateAttachement(userid,file,type);

        Response<Object> response = Response.builder()
                .message("le fichier a été uploadé avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }




}
