package com.savoirstrategie.app.service.userservice;



import com.savoirstrategie.app.entity.*;
import com.savoirstrategie.app.exception.EmailAlreadyExistsException;
import com.savoirstrategie.app.repository.*;
import com.savoirstrategie.app.request.UserRegisterRequest;
import com.savoirstrategie.app.response.*;
import com.savoirstrategie.app.response.stats.AdminStatsResponse;
import com.savoirstrategie.app.response.stats.EtablissementStatsResponse;
import com.savoirstrategie.app.response.stats.IntervenantStatsResponse;
import com.savoirstrategie.app.service.intervenant.IntervenantService;
import com.savoirstrategie.app.service.mailsender.MailSender;
import com.savoirstrategie.app.service.opportunity.OpportunityService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    PasswordEncoder passwordEncoder;

    final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    EtablissementService etablissementService;

    @Autowired
    OpportunityService opportunityService;


    @Autowired
    TokenConfirmationService tokenConfirmationService;
    @Autowired
    IntervenantRepository intervenantRepository;

    @Autowired
    AttachementRepository attachementRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    MailSender mailSender;

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    EtablissementRepository etablissementRepository;

    @Override
    public Object getstats(Long userid) {
        User user =userRepository.findById(userid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));
        List<Intervenant> intervenants=intervenantRepository.findAll();
        List<Etablissement> etablissements=etablissementRepository.findAll();
        List<Opportunity> opportunities=opportunityRepository.findAll();
        if(user.getRole().equals("ROLE_ADMIN")){
            AdminStatsResponse adminStatsResponse=new AdminStatsResponse();
            int nbrOpportunitesActifs=opportunities.size();
            int nbrIntervenantsActifs=intervenants.stream().filter(
                    intervenant -> intervenant.getStatusAccount().equals("verified")
            ).collect(Collectors.toList()).size();
            int nbrEtablissementsActifs=etablissements.size();
            adminStatsResponse.setNbrOpportunitesActifs(nbrOpportunitesActifs);
            adminStatsResponse.setNbrIntervenantsActifs(nbrIntervenantsActifs);
            adminStatsResponse.setNbrEtablissementsActifs(nbrEtablissementsActifs);
            return adminStatsResponse;
        }
        else if(user.getRole().equals("ROLE_INTERVENANT")){
            IntervenantStatsResponse intervenantStatsResponse= new IntervenantStatsResponse();
            List<ApplicationResponse> applicationResponses =opportunityService.findallApplicationsByIntervenant(userid);
            intervenantStatsResponse.setStatusAccount(user.getIntervenant().getStatusAccount());
            int nbrCondidatureAcceptes=applicationResponses.stream().filter(applicationResponse -> applicationResponse.isApproved()).collect(Collectors.toList()).size();
            int nbrCondidatureEnCours=applicationResponses.stream().filter(applicationResponse -> !applicationResponse.isApproved()).collect(Collectors.toList()).size();
            intervenantStatsResponse.setNbrCondidatureAcceptes(nbrCondidatureAcceptes);
            intervenantStatsResponse.setNbrEnCours(nbrCondidatureEnCours);
            return intervenantStatsResponse;
        }

        else if(user.getRole().equals("ROLE_ETABLISSEMENT")){
            List<AttachementrequestEntityResponse> demands =opportunityService.findallDemandStatus(userid);
            int nbrDemandeAccepte = demands.stream().filter(demand->demand.isApproved()).collect(Collectors.toList()).size();
            int nbrDemandeEnCours = demands.stream().filter(demand-> !demand.isApproved()).collect(Collectors.toList()).size();


            EtablissementStatsResponse etablissementStatsResponse=new EtablissementStatsResponse();
            etablissementStatsResponse.setStatusAccount(user.getEtablissement().getStatusAccount());
            etablissementStatsResponse.setNbrDemandeAccepte(nbrDemandeAccepte);
            etablissementStatsResponse.setNbrDemandeEnCours(nbrDemandeEnCours);

            return etablissementStatsResponse;

        }

        return null;
    }

    @Override
    public User getUser(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        return user;
    }

    @Override
    public String getEmail(Long id) {
        User user = userRepository.findById(id).get();
        if(user==null){
            throw new RuntimeException("No user");
        }
        return user.getEmail();
    }

    @Override
    @Transactional()
    public void registerUser(UserRegisterRequest userRegisterRequest) {
        if(!emailAlreadyExists(userRegisterRequest)){ // no email exist
            if(userRegisterRequest.getRole().equals("ROLE_INTERVENANT")){
                registerUserIntervenant(userRegisterRequest);
            }
            else{
                registerUserEtablissement(userRegisterRequest);
            }
        }
        // success => send email now for email verification

        try {
            // send email for email verification
            sendEmailVerification(userRegisterRequest.getEmail());
        } catch (Exception e) {
            // handle exception here (e.g. log error)
            log.info("User is registred successfully. But email verification was not sent.");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponseList=new ArrayList<>();

        List<User> users=userRepository.findAll();

        for (User user:users){
            UserResponse userResponse=getUserInfo(user.getId());
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    @Override
    public UserResponse getUserInfo(Long userid) {
        Optional<User> user=userRepository.findById(userid);
        if(user.isPresent()){


            UserResponse userResponse=modelMapper.map(user.get(),UserResponse.class);

            if(user.get().getIntervenant()!=null){
                IntervenantResponse intervenantResponse=modelMapper.map(user.get().getIntervenant(),IntervenantResponse.class);
                List<Feedback> feedbackList=user.get().getIntervenant().getFeedbacks();
                List<FeedbackResponse> feedbackResponses=new ArrayList<>();
                feedbackList.stream().forEach(
                        feedback -> {
                            FeedbackResponse feedbackResponse=modelMapper.map(feedback,FeedbackResponse.class);
                            feedbackResponse.setEtablissementName(feedback.getEtablissement().getName());
                            feedbackResponses.add(feedbackResponse);
                        }
                );



                List<Attachement> attachements=attachementRepository.findAllByIntervenantIdOrderByCreatedDateDesc(intervenantResponse.getId());
                List<AttachementResponse> attachementResponseList = attachements.stream().map(attachement -> modelMapper.map(attachement,AttachementResponse.class)).collect(Collectors.toList());
                intervenantResponse.setAttachementsResponse(attachementResponseList);
                userResponse.setIntervenantResponse(intervenantResponse);
                userResponse.setFeedbackResponseList(feedbackResponses);
            }
            else if(user.get().getEtablissement()!=null){

                EtablissementResponse etablissementResponse=modelMapper.map(user.get().getEtablissement(), EtablissementResponse.class);




                userResponse.setEtablissementResponse(etablissementResponse);
            }


            return userResponse;
        }
        else{
            throw new UsernameNotFoundException("l'utilisateur que vous cherchez est introuvable.");
        }


    }


    @Override
    public void sendEmailVerification(String email)  {
        // see if the email exists
        if(userRepository.existsByEmail(email)){
            Token token=tokenConfirmationService.generateConfirmationToken(email);
            String emailDestination= email;
            String message="To confirm your account, please click here : "+ apiUrl + "/user/confirm-account?token="+token.getConfirmationtoken();
            String objet="Complete Registration!";
            mailSender.sendEmail(email,message,objet);
        }
        else{
            throw new BadCredentialsException("Utilisateur introuvable");
        }

    }

    @Override
    public void blockAccount(Long id) {
        // search user by id
        User user=userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur est introuvable."));
        user.setStatus("blocked");
        userRepository.save(user);

    }

    @Override
    public void activateAccount(Long id) {
        User user=userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur est introuvable."));
        if(!user.isEmailVerified()){
            user.setStatus("unverified");
        }
        else{
            user.setStatus("active");
        }

        userRepository.save(user);

    }

    @Override
    public void approuverAccount(Long id) {

        User user=userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur est introuvable."));
        if(user.getRole().equals("ROLE_INTERVENANT")){
            Intervenant intervenant=user.getIntervenant();
            intervenant.setStatusAccount("verified");
            intervenantRepository.save(intervenant);
        }
        else if(user.getRole().equals("ROLE_ETABLISSEMENT")){
            Etablissement etablissement=user.getEtablissement();
            etablissement.setStatusAccount("verified");
            etablissementRepository.save(etablissement);
        }

    }
    @Override
    public void desapprouverAccount(Long id) {

        User user=userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur est introuvable."));
        if(user.getRole().equals("ROLE_INTERVENANT")){
            Intervenant intervenant=user.getIntervenant();
            intervenant.setStatusAccount("unverified");
            intervenantRepository.save(intervenant);
        }
        else if(user.getRole().equals("ROLE_ETABLISSEMENT")){
            Etablissement etablissement=user.getEtablissement();
            etablissement.setStatusAccount("unverified");
            etablissementRepository.save(etablissement);
        }

    }

    @Override
    public void deleteAccount(Long id) {
        User user=userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur est introuvable."));
        user.setEtablissement(null);
        user.setIntervenant(null);
        // delete user

        Token token =confirmationTokenRepository.findByUserEmail(user.getEmail());
        if(token!=null){
            confirmationTokenRepository.delete(token);
        }


        // delete inter et etab
        if(user.getRole().equals("ROLE_ETABLISSEMENT")){
            Etablissement etablissement=etablissementRepository.findByUserId(user.getId());

            etablissementRepository.delete(etablissement);
        }
        else if(user.getRole().equals("ROLE_INTERVENANT")){
           Intervenant intervenant=intervenantRepository.findByUserId(user.getId());

            intervenantRepository.delete(intervenant);
        }
        // check if the intervennat ou etab is deleted too ..


    }

    @Override
    public List<Notification> findallnotifications(Long userid) {
        User user =userRepository.findById(userid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));
        List<Notification> notifications=user.getNotifications();
        notifications.stream().forEach(
                notification -> {
                    notification.setUser(null);
                }
        );
        notifications.sort(Comparator.comparing(Notification::getNotificationDate).reversed());
        return notifications;
    }


    public void registerUserIntervenant(UserRegisterRequest userRegisterRequest){

        User userSaved=registerSimpleUser(userRegisterRequest);
        Intervenant intervanantCreated = modelMapper.map(userRegisterRequest, Intervenant.class);
        intervanantCreated.setUser(userSaved);
        intervanantCreated.setStatusAccount("unverified");
        intervenantRepository.save(intervanantCreated);
    }

    public void registerUserEtablissement(UserRegisterRequest userRegisterRequest){
        User userSaved=registerSimpleUser(userRegisterRequest);
        Etablissement etablissementCreated = modelMapper.map(userRegisterRequest, Etablissement.class);
        etablissementCreated.setUser(userSaved);
        etablissementCreated.setStatusAccount("unverified");
        etablissementRepository.save(etablissementCreated);
    }

    public User registerSimpleUser(UserRegisterRequest userRegisterRequest){
        User userCreated = modelMapper.map(userRegisterRequest, User.class);
        userCreated.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        /*
        old code
        userCreated.setStatus("unverified");
        userCreated.setEmailVerified(false);
         */
        // new code
        userCreated.setStatus("active");
        userCreated.setEmailVerified(true);
        userCreated.setCreation_date(new Date());
        User userSaved=userRepository.save(userCreated);
        return userSaved;
    }


    public boolean emailAlreadyExists(UserRegisterRequest userRegisterRequest){
        User user=userRepository.findByEmail(userRegisterRequest.getEmail());
        if(user!=null){
            throw new EmailAlreadyExistsException("Email existe déja.");
        }
        return false;
    }



}
