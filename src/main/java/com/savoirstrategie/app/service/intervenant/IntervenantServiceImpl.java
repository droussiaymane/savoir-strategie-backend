package com.savoirstrategie.app.service.intervenant;

import com.savoirstrategie.app.entity.Attachement;
import com.savoirstrategie.app.entity.AttachmentRequest;
import com.savoirstrategie.app.entity.Intervenant;
import com.savoirstrategie.app.entity.User;
import com.savoirstrategie.app.exception.ValidationException;
import com.savoirstrategie.app.helpers.FilePath;
import com.savoirstrategie.app.repository.AttachementRepository;
import com.savoirstrategie.app.repository.AttachementRequestRepository;
import com.savoirstrategie.app.repository.IntervenantRepository;
import com.savoirstrategie.app.repository.UserRepository;
import com.savoirstrategie.app.request.update.IntervenantAboutMeRequest;
import com.savoirstrategie.app.request.update.UserChangePasswordRequest;
import com.savoirstrategie.app.response.UserResponse;
import com.savoirstrategie.app.service.filestorage.FileStorageService;
import com.savoirstrategie.app.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class IntervenantServiceImpl implements IntervenantService{

    @Autowired
    IntervenantRepository intervenantRepository;

    @Autowired
    AttachementRequestRepository attachementRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AttachementRepository attachementRepository;


    @Autowired
    FileStorageService fileStorageService;


    @Autowired
    UserService userService;
    @Override
    public List<UserResponse> findAll() {

        List<UserResponse> userResponseList=userService.getAllUsers().stream().filter(userResponse -> userResponse.getRole().equals("ROLE_INTERVENANT")).collect(Collectors.toList());

        return userResponseList;
    }
    @Override
    public void updateAvatar(Long userid,MultipartFile file) {
        // find intevenant by id
        Intervenant intervenant=intervenantRepository.findByUserId(userid);


        // upload image on the dropbox
        // make it constants ses paths ...
        String pathFile= FilePath.INTERVENANT_FOLDER_PREFIX+"/"+intervenant.getUser().getId()+FilePath.INTERVENANT_FOLDER_AVATAR_SUFFIX;
        String uniqueFileName=fileStorageService.uploadFileAndReturnUniqueFileName(pathFile,file);



        // get public path url
        String avatar_url=fileStorageService.getPathPublicUrl(pathFile,uniqueFileName);
        // delete old image

         if(intervenant.getAvatar_unique_file_name()!=null && !intervenant.getAvatar_unique_file_name().equals("")){
            fileStorageService.deleteFile(pathFile,intervenant.getAvatar_unique_file_name());
       }

        // set avatar to this image uploaded

        intervenant.setAvatar_url(avatar_url);
        intervenant.setAvatar_unique_file_name(uniqueFileName);
        intervenantRepository.save(intervenant);

    }

    @Override
    public void updateAttachement(Long userid, MultipartFile file, String type) {

        // find intevenant by id
        Intervenant intervenant=intervenantRepository.findByUserId(userid);


        String pathFile= FilePath.INTERVENANT_FOLDER_PREFIX+"/"+intervenant.getUser().getId()+FilePath.INTERVENANT_FOLDER_ATTACHEMENTS_SUFFIX+"/"+type;

        String uniqueFileName=fileStorageService.uploadFileAndReturnUniqueFileName(pathFile,file);

        // get public path url
        String attachement_url=fileStorageService.getPathPublicUrl(pathFile,uniqueFileName);


        // delete old attachement
        List<Attachement> attachements=attachementRepository.findAllByIntervenantIdOrderByCreatedDateDesc(intervenant.getId());

        //filter only for the type needed
        List<Attachement> attachementstyped= attachements.stream().filter(attachement -> attachement.getType().equals(type)).collect(Collectors.toList());

        if(attachementstyped.size()>0){
            Attachement myattachement=attachementstyped.get(0);
            fileStorageService.deleteFile(pathFile,myattachement.getAttachement_unique_file_name());
            myattachement.setIntervenant(null);
            attachementRepository.delete(myattachement);
        }

        // create a new attachement
        Attachement attachement=new Attachement();
        attachement.setIntervenant(intervenant);
        attachement.setAttachement_url(attachement_url);
        attachement.setAttachement_unique_file_name(uniqueFileName);
        attachement.setType(type);
        attachement.setCreatedDate(LocalDateTime.now());
        attachementRepository.save(attachement);

    }

    @Override
    public void updateAboutMe(Long userid, IntervenantAboutMeRequest intervenantAboutMeRequest) {
        // find user
        Intervenant intervenant=intervenantRepository.findByUserId(userid);

        if(intervenant!=null){
            intervenant.setFirstName(intervenantAboutMeRequest.getFirstName());
            intervenant.setLastName(intervenantAboutMeRequest.getLastName());
            intervenant.setPays(intervenantAboutMeRequest.getPays());
            intervenant.setVille(intervenantAboutMeRequest.getVille());
            intervenant.setProfile(intervenantAboutMeRequest.getProfile());
            intervenant.setDescription(intervenantAboutMeRequest.getDescription());
            intervenantRepository.save(intervenant);
        }
        else{
            throw new UsernameNotFoundException("l'utilisateur est introuvable.");
        }



    }

    @Override
    public void changePassword(Long userid, UserChangePasswordRequest userChangePasswordRequest) {

        Intervenant intervenant=intervenantRepository.findByUserId(userid);

        if(intervenant!=null){
            User user=intervenant.getUser();
            boolean matches=passwordEncoder.matches(userChangePasswordRequest.getOldPassword(),user.getPassword());
            if(matches) {
                user.setPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
                userRepository.save(user);
            }else{
                throw new ValidationException("Old Password incorrect.");
            }
            intervenantRepository.save(intervenant);
        }
        else{
            throw new UsernameNotFoundException("l'utilisateur est introuvable.");
        }
    }

    @Override
    public boolean canAccessAttachements(Long userid,Long intervenantuserid) {
        AtomicBoolean canAccess= new AtomicBoolean(false);
        User user=userRepository.findById(userid).orElseThrow(() -> new UsernameNotFoundException("User not found exception"));
        if(user.getRole().equals("ROLE_ETABLISSEMENT")){
            // check if
            Long etablissementId=user.getEtablissement().getId();
            Long intervenantId=intervenantRepository.findByUserId(intervenantuserid).getId();

            List<AttachmentRequest> attachmentRequests=  attachementRequestRepository.findAllByIntervenantIdOrderByRequestDateDesc(intervenantId);
            List<AttachmentRequest> attachmentRequestsfiltred= attachmentRequests.stream().filter(
                    attachmentRequest -> {
                        return attachmentRequest.getEtablissement().getId()==etablissementId;
                    }

            ).collect(Collectors.toList());

            attachmentRequestsfiltred.stream().forEach(attachmentRequest -> {
                if(attachmentRequest.isApproved()){
                    canAccess.set(true);
                }
            });
            return canAccess.get();
        }
        else if(user.getRole().equals("ROLE_ADMIN")){
            canAccess.set(true);
        }

        return canAccess.get();
    }

}
