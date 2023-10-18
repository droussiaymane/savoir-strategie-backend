package com.savoirstrategie.app.service.intervenant;

import com.savoirstrategie.app.entity.Intervenant;
import com.savoirstrategie.app.request.update.IntervenantAboutMeRequest;
import com.savoirstrategie.app.request.update.UserChangePasswordRequest;
import com.savoirstrategie.app.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IntervenantService {

    List<UserResponse> findAll();
    void updateAvatar(Long userid,MultipartFile file);
    void updateAttachement(Long userid,MultipartFile file,String type);

    void updateAboutMe(Long userid, IntervenantAboutMeRequest intervenantAboutMeRequest);

    void changePassword(Long userid, UserChangePasswordRequest userChangePasswordRequest);
    boolean canAccessAttachements(Long userid,Long intervenantuserid);
}
