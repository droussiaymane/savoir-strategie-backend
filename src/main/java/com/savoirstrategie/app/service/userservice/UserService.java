package com.savoirstrategie.app.service.userservice;

import com.savoirstrategie.app.entity.Notification;
import com.savoirstrategie.app.entity.User;
import com.savoirstrategie.app.request.UserRegisterRequest;
import com.savoirstrategie.app.response.UserResponse;

import java.util.List;

public interface UserService  {
    public Object getstats(Long userid);
    public User getUser(String email);
    public String getEmail(Long id);
    public void registerUser(UserRegisterRequest userRegisterRequest);

    public List<UserResponse> getAllUsers();

    public UserResponse getUserInfo(Long userid);


    public void sendEmailVerification(String email);

    public void blockAccount(Long id);

    public void activateAccount(Long id);
    public void approuverAccount(Long id);

    public void desapprouverAccount(Long id);
    public void deleteAccount(Long id);

    public List<Notification> findallnotifications(Long userid);

}