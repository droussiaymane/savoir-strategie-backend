package com.savoirstrategie.app.service.userservice;

import com.savoirstrategie.app.entity.*;
import com.savoirstrategie.app.exception.ValidationException;
import com.savoirstrategie.app.helpers.FilePath;
import com.savoirstrategie.app.repository.EtablissementRepository;
import com.savoirstrategie.app.repository.OpportunityRepository;
import com.savoirstrategie.app.repository.UserRepository;
import com.savoirstrategie.app.request.OpportunityRequest;
import com.savoirstrategie.app.request.update.EtablissementAboutMeRequest;
import com.savoirstrategie.app.request.update.IntervenantAboutMeRequest;
import com.savoirstrategie.app.request.update.UserChangePasswordRequest;
import com.savoirstrategie.app.response.*;
import com.savoirstrategie.app.service.filestorage.FileStorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtablissementServiceImpl implements EtablissementService{
    @Autowired
    EtablissementRepository etablissementRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    UserRepository userRepository;


    final ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<Etablissement> findAll() {
        return etablissementRepository.findAll();
    }

    @Override
    public List<Opportunity> findAllOpportunitiesByEtabUserID(Long userid) {
        Optional<User> user=userRepository.findById(userid);
        if(user.isPresent()){
            if(user.get().getRole().equals("ROLE_ETABLISSEMENT")){
                Long etabId=user.get().getEtablissement().getId();
                List<Opportunity> opportunities=opportunityRepository.findAllByEtablissementIdOrderByCreationDateDesc(etabId);
                opportunities.forEach(
                        opportunity -> {
                            opportunity.setEtablissement(null);
                            opportunity.setIntervenants(null);
                            opportunity.setApplications(null);
                            
                        }
                );
                return opportunities;
            }
            else{
                throw new UsernameNotFoundException("l'etablissement est introuvable");
            }

        }
        else{
            throw new UsernameNotFoundException("l'etablissement est introuvable");
        }


    }

    @Override
    public void createOpportunity(Long userid, OpportunityRequest opportunityRequest) {
        Etablissement etablissement=etablissementRepository.findByUserId(userid);
        if(etablissement==null){
            throw new UsernameNotFoundException("l'etablissement est introuvable");
        }
        else{
            Opportunity opportunity=modelMapper.map(opportunityRequest, Opportunity.class);

            opportunity.setEtablissement(etablissement);
            opportunity.setCreationDate(LocalDateTime.now());
            opportunityRepository.save(opportunity);
        }

    }

    @Override
    public List<Opportunity> findAllOpportunities() {
        List<Opportunity> opportunities=opportunityRepository.findAll();
        opportunities.sort(Comparator.comparing(Opportunity::getCreationDate).reversed());
        opportunities.forEach(
                opportunity -> {
                    opportunity.setEtablissement(null);
                    opportunity.setApplications(null);
                    opportunity.setIntervenants(null);
                }
        );
        return opportunities;
    }

    @Override
    public void updateAboutMe(Long userid, EtablissementAboutMeRequest etablissementAboutMeRequest) {
        // find user
        Etablissement etablissement=etablissementRepository.findByUserId(userid);

        if(etablissement!=null){

            etablissement.setName(etablissementAboutMeRequest.getName());
            etablissement.setPays(etablissementAboutMeRequest.getPays());
            etablissement.setVille(etablissementAboutMeRequest.getVille());
            etablissement.setDescription(etablissementAboutMeRequest.getDescription());
            etablissementRepository.save(etablissement);
        }
        else{
            throw new UsernameNotFoundException("l'utilisateur est introuvable.");
        }



    }


    @Override
    public void updateAvatar(Long userid,MultipartFile file) {
        // find etab by id
        Etablissement etablissement=etablissementRepository.findByUserId(userid);


        // upload image on the dropbox
        // make it constants ses paths ...
        String pathFile= FilePath.ETABLISSEMENT_FOLDER_PREFIX+"/"+etablissement.getUser().getId()+FilePath.INTERVENANT_FOLDER_AVATAR_SUFFIX;
        String uniqueFileName=fileStorageService.uploadFileAndReturnUniqueFileName(pathFile,file);



        // get public path url
        String avatar_url=fileStorageService.getPathPublicUrl(pathFile,uniqueFileName);
        // delete old image

        if(etablissement.getAvatar_unique_file_name()!=null && !etablissement.getAvatar_unique_file_name().equals("")){
            fileStorageService.deleteFile(pathFile,etablissement.getAvatar_unique_file_name());
        }

        // set avatar to this image uploaded

        etablissement.setAvatar_url(avatar_url);
        etablissement.setAvatar_unique_file_name(uniqueFileName);
        etablissementRepository.save(etablissement);

    }


    @Override
    public OpportunityResponse findOpportunityById(Long opportunityid) {
        Opportunity opportunity=opportunityRepository.findById(opportunityid).orElseThrow(() -> new UsernameNotFoundException("Opportunity not found exception"));
        OpportunityResponse opportunityResponse=modelMapper.map(opportunity, OpportunityResponse.class);
        opportunityResponse.setEtablissementResponse(modelMapper.map(opportunity.getEtablissement(), EtablissementResponse.class));
        return opportunityResponse;
    }

    @Override
    public void changePassword(Long userid, UserChangePasswordRequest userChangePasswordRequest) {

        Etablissement etablissement=etablissementRepository.findByUserId(userid);

        if(etablissement!=null){
            User user=etablissement.getUser();
            boolean matches=passwordEncoder.matches(userChangePasswordRequest.getOldPassword(),user.getPassword());
            if(matches) {
                user.setPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
                userRepository.save(user);
            }else{
                throw new ValidationException("Old Password incorrect.");
            }
            etablissementRepository.save(etablissement);
        }
        else{
            throw new UsernameNotFoundException("l'utilisateur est introuvable.");
        }
    }

    @Override
    public UserResponse getUserInfo(Long userid) {
        Optional<Etablissement> etablissement=etablissementRepository.findById(userid);
        if(etablissement.isPresent()){




                EtablissementResponse etablissementResponse=modelMapper.map(etablissement.get(), EtablissementResponse.class);






                UserResponse userResponse=new UserResponse();
                userResponse.setEtablissementResponse(etablissementResponse);


            return userResponse;
        }
        else{
            throw new UsernameNotFoundException("l'utilisateur que vous cherchez est introuvable.");
        }


    }




}
