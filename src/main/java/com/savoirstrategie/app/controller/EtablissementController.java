package com.savoirstrategie.app.controller;


import com.savoirstrategie.app.entity.Opportunity;
import com.savoirstrategie.app.helpers.CODE;
import com.savoirstrategie.app.request.OpportunityRequest;
import com.savoirstrategie.app.request.UserRegisterRequest;
import com.savoirstrategie.app.request.update.EtablissementAboutMeRequest;
import com.savoirstrategie.app.request.update.IntervenantAboutMeRequest;
import com.savoirstrategie.app.request.update.UserChangePasswordRequest;
import com.savoirstrategie.app.response.OpportunityResponse;
import com.savoirstrategie.app.response.Response;
import com.savoirstrategie.app.response.UserResponse;
import com.savoirstrategie.app.service.userservice.EtablissementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/etablissement")
public class EtablissementController {


    @Autowired
    EtablissementService etablissementService;

    @PostMapping("/opportunity/create")
    public ResponseEntity createOpportunity(@Valid @RequestBody OpportunityRequest opportunityRequest,@RequestParam("userid")Long userid) throws Exception {
        etablissementService.createOpportunity(userid,opportunityRequest);
        Response<Object> response = Response.builder()
                .message("Opportunity created.")
                .code(CODE.CREATED.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }



    @GetMapping("/opportunity/findall")
    public ResponseEntity findallOpportunities() throws Exception {
        List<Opportunity> opportunities=etablissementService.findAllOpportunities();
        Response<Object> response = Response.builder()
                .data(opportunities)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/opportunity/findallbyetab")
    public ResponseEntity findallOpportunitiesByEtablissementUserId(@RequestParam("userid")Long userid) throws Exception {
        List<Opportunity> opportunities=etablissementService.findAllOpportunitiesByEtabUserID(userid);
        Response<Object> response = Response.builder()
                .data(opportunities)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }



    @GetMapping("/opportunity/get/info")
    public ResponseEntity getOpportunityInfo(@RequestParam("opportunityid")Long opportunityid){
        OpportunityResponse opportunityResponse=etablissementService.findOpportunityById(opportunityid);
        Response<Object> response = Response.builder()
                .data(opportunityResponse)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }

    @PutMapping("/compte/update/avatar")
    public ResponseEntity<?> updatecompteAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userid")Long userid) {

        etablissementService.updateAvatar(userid,file);

        Response<Object> response = Response.builder()
                .message("le fichier a été uploadé avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PutMapping("/compte/update/about")
    public ResponseEntity<?> updateAboutMe(@RequestBody EtablissementAboutMeRequest etablissementAboutMeRequest, @RequestParam("userid")Long userid) {

        etablissementService.updateAboutMe(userid,etablissementAboutMeRequest);

        Response<Object> response = Response.builder()
                .message("les informations ont été updatés avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PutMapping("/compte/update/changepassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UserChangePasswordRequest userChangePasswordRequest, @RequestParam("userid")Long userid) {

        etablissementService.changePassword(userid,userChangePasswordRequest);

        Response<Object> response = Response.builder()
                .message("les informations ont été updatés avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @Operation(summary = "get USer", description = "get users")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get/info")
    @ApiResponse(description = "get all users",responseCode = "200")
    public ResponseEntity getUserInfo(@RequestParam("userid")Long userid){
        UserResponse userResponse= etablissementService.getUserInfo(userid);
        Response<Object> response = Response.builder()
                .data(userResponse)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }


}
