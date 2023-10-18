package com.savoirstrategie.app.service.opportunity;

import com.savoirstrategie.app.entity.Application;
import com.savoirstrategie.app.request.ApplicationRequest;
import com.savoirstrategie.app.request.FeedbackRequest;
import com.savoirstrategie.app.response.ApplicationResponse;
import com.savoirstrategie.app.response.AttachementrequestEntityResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OpportunityService {


    void createApplication(ApplicationRequest applicationRequest,Long opportunityid,Long userid);

    List<Application> findAllByOpportunityId(Long opportunityid,Long etabid);

    void approvedApplication(Long applicationid);
    void   approvedDemandApplication(Long attachementrequestid);

    List<AttachementrequestEntityResponse> findallDemandStatus(Long userid);

    List<AttachementrequestEntityResponse> findAllAttachementrequest();

    List<ApplicationResponse> findallApplicationsByIntervenant(Long userid);


    void  createFeedback(FeedbackRequest feedbackRequest, Long intervenantid, Long userid);

}
