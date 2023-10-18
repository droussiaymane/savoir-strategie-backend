package com.savoirstrategie.app.service.opportunity;


import com.savoirstrategie.app.entity.*;
import com.savoirstrategie.app.repository.*;
import com.savoirstrategie.app.request.ApplicationRequest;
import com.savoirstrategie.app.request.FeedbackRequest;
import com.savoirstrategie.app.response.ApplicationResponse;
import com.savoirstrategie.app.response.AttachementrequestEntityResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OpportunityServiceImpl implements OpportunityService{


    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    FeedbackRepository feedbackRepository;


    @Autowired
    AttachementRequestRepository attachementRequestRepository;


    @Autowired
    OpportunityRepository opportunityRepository;


    @Autowired
    IntervenantRepository intervenantRepository;

    @Autowired
    UserRepository userRepository;
    final ModelMapper modelMapper = new ModelMapper();


    @Transactional
    @Override
    public void createApplication(ApplicationRequest applicationRequest, Long opportunityid, Long userid) {
        Opportunity opportunity=opportunityRepository.findById(opportunityid).orElseThrow(() -> new UsernameNotFoundException("Opportunity not found exception"));
        User user=userRepository.findById(userid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));
        Application application=modelMapper.map(applicationRequest,Application.class);
        application.setOpportunity(opportunity);
        application.setIntervenant(user.getIntervenant());
        application.setCreationDate(LocalDateTime.now());
        applicationRepository.save(application);

        // push notification to the user etablissement
        Notification notification=new Notification();
        notification.setNotificationDate(LocalDateTime.now());
        notification.setMessage("Nouvelle application a été crée pour l´opportunité intitulé de : "+opportunity.getTitle() +".");
        notification.setUser(opportunity.getEtablissement().getUser());
        notificationRepository.save(notification);

        // add +1 pour nbr doncdidature
        opportunity.setApplicationsNumber(opportunity.getApplicationsNumber()+1);
        opportunityRepository.save(opportunity);
    }

    @Override
    public List<Application> findAllByOpportunityId(Long opportunityid,Long userid) {

        Optional<Opportunity> opportunity=opportunityRepository.findById(opportunityid);

        User myuser=userRepository.findById(userid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));

        if(opportunity.isPresent()){

                if(opportunity.get().getEtablissement().getId()==myuser.getEtablissement().getId()){
                    List<Application> applications=opportunity.get().getApplications();

                    applications.stream().forEach(
                            application -> {
                                Intervenant intervenant=application.getIntervenant();
                                intervenant.setOpportunities(null);
                                intervenant.setApplications(null);
                                intervenant.setAttachements(null);
                                intervenant.setFeedbacks(null);

                                User user =intervenant.getUser();
                                user.setIntervenant(null);
                                user.setEtablissement(null);
                                user.setNotifications(null);
                                intervenant.setUser(user);
                                application.setIntervenant(intervenant);
                                application.setOpportunity(null);
                            }
                    );
                    return applications;
                }
                else{
                    return Arrays.asList();
                }
            }



        else{
            throw new UsernameNotFoundException("l'opportunité est introuvable");
        }
    }

    @Override
    @Transactional
    public void approvedApplication(Long applicationid) {
        Application application=applicationRepository.findById(applicationid).orElseThrow(() -> new UsernameNotFoundException("Application not found exception"));

        application.setApproved(true);
        applicationRepository.save(application);



        // push notification to the user etablissement
        Notification notification=new Notification();
        notification.setNotificationDate(LocalDateTime.now());
        notification.setMessage("Votre condidature pour l'opportunité intitulé de :"+application.getOpportunity().getTitle() + " a été accéptée.");
        notification.setUser(application.getIntervenant().getUser());
        notificationRepository.save(notification);

        // push notification to the user admin to approve admin demand
        Notification notificationAdmin=new Notification();
        notification.setNotificationDate(LocalDateTime.now());
        notification.setMessage("Une nouvelle demande d'approbation de voir les attachements de l'intervenant : "+application.getIntervenant().getFirstName() + " "+application.getIntervenant().getLastName() + " a été crée par l'établissmeent : "+application.getOpportunity().getEtablissement().getName()+".");
        notification.setUser(application.getOpportunity().getEtablissement().getUser());
        notificationRepository.save(notification);

        // create attachementrequest
        AttachmentRequest attachmentRequest=new AttachmentRequest();
        attachmentRequest.setRequestDate(LocalDateTime.now());
        attachmentRequest.setOpportunity(application.getOpportunity());
        attachmentRequest.setIntervenant(application.getIntervenant());
        attachmentRequest.setEtablissement(application.getOpportunity().getEtablissement());

        attachementRequestRepository.save(attachmentRequest);

    }

    @Override
    @Transactional
    public void approvedDemandApplication(Long attachementrequestid) {
    AttachmentRequest attachmentRequest =attachementRequestRepository.findById(attachementrequestid).orElseThrow(() -> new UsernameNotFoundException("Demand not found exception"));;
    attachmentRequest.setApproved(true);
    attachementRequestRepository.save(attachmentRequest);


    // send  notif to the etab saying that the demand is approved


        Notification notification=new Notification();
        notification.setNotificationDate(LocalDateTime.now());
        notification.setMessage("Votre  demande de voir les attachements pour l'utilisateur : "+attachmentRequest.getIntervenant().getFirstName() + " "+ attachmentRequest.getIntervenant().getLastName() + " a été approuvé. Vous pouvez consulter son profil et voir tous ses attachaments.");
        notification.setUser(attachmentRequest.getEtablissement().getUser());
        notificationRepository.save(notification);

    }

    @Override
    public List<AttachementrequestEntityResponse> findallDemandStatus(Long userid) {
        User user =userRepository.findById(userid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));
        Long etablissementId=user.getEtablissement().getId();

        List<AttachementrequestEntityResponse> attachementrequestEntityResponses=findAllAttachementrequest();

        List<AttachementrequestEntityResponse> attachementrequestEntityResponsesFiltred=attachementrequestEntityResponses.stream().filter(
                attachementrequest->{
                    return attachementrequest.getEtablissementId()==etablissementId && attachementrequest.isApproved();
                }
                ).collect(Collectors.toList());


        List<AttachementrequestEntityResponse> mylist=attachementrequestEntityResponsesFiltred.stream()
                .collect(Collectors.toMap(AttachementrequestEntityResponse::getIntervenantname, Function.identity(), (a, b) -> a))
                .values().stream().collect(Collectors.toList());


        return mylist;
    }

    @Override
    public List<AttachementrequestEntityResponse> findAllAttachementrequest() {
        List<AttachmentRequest> attachmentRequests=attachementRequestRepository.findAll();
        List<AttachementrequestEntityResponse> attachementrequestEntityResponses=new ArrayList<>();
        attachmentRequests.stream().forEach(
                attachmentRequest -> {
                    AttachementrequestEntityResponse attachementrequestEntityResponse=modelMapper.map(attachmentRequest, AttachementrequestEntityResponse.class);
                    attachementrequestEntityResponse.setOpportunityId(attachmentRequest.getOpportunity().getId());
                    attachementrequestEntityResponse.setOpportunityTitle(attachmentRequest.getOpportunity().getTitle());
                    attachementrequestEntityResponse.setEtablissementId(attachmentRequest.getEtablissement().getId());
                    attachementrequestEntityResponse.setEtablissementname(attachmentRequest.getEtablissement().getName());
                    attachementrequestEntityResponse.setIntervenantname(attachmentRequest.getIntervenant().getFirstName() + " " + attachmentRequest.getIntervenant().getLastName());
                    attachementrequestEntityResponse.setIntervenantUserId(attachmentRequest.getIntervenant().getUser().getId());

                    attachementrequestEntityResponses.add(attachementrequestEntityResponse);
                }
        );
        List<AttachementrequestEntityResponse> mylist=attachementrequestEntityResponses.stream()
                .collect(Collectors.toMap(
                        entity -> entity.getIntervenantname() + entity.getEtablissementname(), // combine name and email into a single key
                        Function.identity(),
                        (entity1, entity2) -> entity1 // keep the first entity if there are duplicates
                ))
                .values().stream()
                .collect(Collectors.toList());


        return mylist;
    }

    @Override
    public List<ApplicationResponse> findallApplicationsByIntervenant(Long userid) {
        List<Application> applications=applicationRepository.findAllByIntervenantIdOrderByCreationDateDesc(userid);

        List<ApplicationResponse> applicationResponses=new ArrayList<>();
        applications.stream().forEach(
                application -> {
                    ApplicationResponse applicationResponse=modelMapper.map(application,ApplicationResponse.class);
                    applicationResponse.setEtablissementId(application.getOpportunity().getEtablissement().getId());
                    applicationResponse.setEtablissementName(application.getOpportunity().getEtablissement().getName());
                    applicationResponse.setOpportunityId(application.getOpportunity().getId());
                    applicationResponse.setOpportunityTitle(application.getOpportunity().getTitle());
                    applicationResponses.add(applicationResponse);
                }
        );

        return applicationResponses;
    }

    @Override
    @Transactional
    public void createFeedback(FeedbackRequest feedbackRequest, Long intervenantid, Long userid) {
        User user =userRepository.findById(userid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));
        Etablissement etablissement=user.getEtablissement();

        Intervenant intervenant =intervenantRepository.findById(intervenantid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));

        Feedback feedback=modelMapper.map(feedbackRequest,Feedback.class);
        feedback.setFeedbackDate(LocalDateTime.now());
        feedback.setEtablissement(etablissement);
        feedback.setIntervenant(intervenant);


        // send notificatio

        feedbackRepository.save(feedback);


    }
}
