package com.savoirstrategie.app.service.userservice;

import com.savoirstrategie.app.entity.Etablissement;
import com.savoirstrategie.app.entity.Opportunity;
import com.savoirstrategie.app.request.OpportunityRequest;
import com.savoirstrategie.app.request.update.EtablissementAboutMeRequest;
import com.savoirstrategie.app.request.update.IntervenantAboutMeRequest;
import com.savoirstrategie.app.request.update.UserChangePasswordRequest;
import com.savoirstrategie.app.response.OpportunityResponse;
import com.savoirstrategie.app.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EtablissementService {

    List<Etablissement> findAll();
    List<Opportunity>  findAllOpportunitiesByEtabUserID(Long userid);

    void createOpportunity(Long userid, OpportunityRequest opportunityRequest);

    List<Opportunity> findAllOpportunities();

    void updateAboutMe(Long userid, EtablissementAboutMeRequest etablissementAboutMeRequest);

    void updateAvatar(Long userid, MultipartFile file);
    OpportunityResponse findOpportunityById(Long opportunityid);
    void changePassword(Long userid, UserChangePasswordRequest userChangePasswordRequest);

    public UserResponse getUserInfo(Long userid);
 }
