package com.savoirstrategie.app.controller;

import com.savoirstrategie.app.entity.Application;
import com.savoirstrategie.app.entity.Opportunity;
import com.savoirstrategie.app.helpers.CODE;
import com.savoirstrategie.app.request.ApplicationRequest;
import com.savoirstrategie.app.request.FeedbackRequest;
import com.savoirstrategie.app.request.OpportunityRequest;
import com.savoirstrategie.app.request.update.EtablissementAboutMeRequest;
import com.savoirstrategie.app.response.ApplicationResponse;
import com.savoirstrategie.app.response.AttachementrequestEntityResponse;
import com.savoirstrategie.app.response.Response;
import com.savoirstrategie.app.service.opportunity.OpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/opportunity")
public class OpportunityController {

    @Autowired
    OpportunityService opportunityService;

    @PostMapping("/application/create")
    public ResponseEntity createApplication(@Valid @RequestBody ApplicationRequest applicationRequest, @RequestParam("opportunityid")Long opportunityid, @RequestParam("userid")Long userid) throws Exception {
        opportunityService.createApplication(applicationRequest,opportunityid,userid);
        Response<Object> response = Response.builder()
                .message("Application sent successfully.")
                .code(CODE.CREATED.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }

    @GetMapping("/application/findallbyopportunity")
    public ResponseEntity findallApplicationsByOpportunity(@RequestParam("opportunityid")Long opportunityid,@RequestParam("etabid")Long etabid) throws Exception {
        List<Application> applications=opportunityService.findAllByOpportunityId(opportunityid,etabid);
        Response<Object> response = Response.builder()
                .data(applications)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PutMapping("/application/approved")
    public ResponseEntity<?> approvedApplication(@RequestParam("applicationid")Long applicationid) {

        opportunityService.approvedApplication(applicationid);

        Response<Object> response = Response.builder()
                .message("la condidature a été approuvée avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PutMapping("/application/demandapproved")
    public ResponseEntity<?> approvedDemandApplication(@RequestParam("demandid")Long demandid) {

        opportunityService.approvedDemandApplication(demandid);

        Response<Object> response = Response.builder()
                .message("la condidature a été approuvée avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/application/findalldemand")
    public ResponseEntity findallApplicationsByOpportunity() {
        List<AttachementrequestEntityResponse> allAttachementrequest=opportunityService.findAllAttachementrequest();
        Response<Object> response = Response.builder()
                .data(allAttachementrequest)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/application/findalldemandstatus")
    public ResponseEntity findallDemandStatus(@RequestParam("userid")Long userid) {
        List<AttachementrequestEntityResponse> allAttachementrequest=opportunityService.findallDemandStatus(userid);
        Response<Object> response = Response.builder()
                .data(allAttachementrequest)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }



    @GetMapping("/application/findallapplications")
    public ResponseEntity findallApplicationsByIntervenant(@RequestParam("userid") Long userid) {
        List<ApplicationResponse> applicationList=opportunityService.findallApplicationsByIntervenant(userid);
        Response<Object> response = Response.builder()
                .data(applicationList)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping("/application/feedback/etablissement/create")
    public ResponseEntity createfeedbackSourceEtab(@Valid @RequestBody FeedbackRequest feedbackRequest, @RequestParam("intervenantid")Long intervenantid, @RequestParam("userid")Long userid) throws Exception {
        opportunityService.createFeedback(feedbackRequest,intervenantid,userid);
        Response<Object> response = Response.builder()
                .message("Feedback sent successfully.")
                .code(CODE.CREATED.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }








}
