package com.savoirstrategie.app.controller;


import com.savoirstrategie.app.entity.Notification;
import com.savoirstrategie.app.helpers.CODE;
import com.savoirstrategie.app.request.UserEmailRequest;
import com.savoirstrategie.app.request.UserLoginRequest;
import com.savoirstrategie.app.request.UserRegisterRequest;
import com.savoirstrategie.app.response.ApplicationResponse;
import com.savoirstrategie.app.response.Response;
import com.savoirstrategie.app.response.UserResponse;
import com.savoirstrategie.app.service.userservice.TokenConfirmationService;
import com.savoirstrategie.app.service.userservice.TokenConfirmationServiceImpl;
import com.savoirstrategie.app.service.userservice.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    TokenConfirmationService tokenConfirmationService;

    @PostMapping("/register")
    @ApiResponse(description = "register new user",responseCode = "201")
    public ResponseEntity register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
    userService.registerUser(userRegisterRequest);
        Response<Object> response = Response.builder()
                .message("Merci d'avoir créé un compte avec nous ! Veuillez vérifier votre boîte de réception et cliquer sur le lien de vérification que nous vous avons envoyé.")
                .code(CODE.CREATED.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }


    @Operation(summary = "get all users", description = "get all users")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/all")
    @ApiResponse(description = "get all users",responseCode = "200")
    public ResponseEntity getAllUsers(){
        List<UserResponse> userResponseList= userService.getAllUsers();
        Response<Object> response = Response.builder()
                .data(userResponseList)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginRequest userLoginRequest) {
        // No authentication logic needed
        return ResponseEntity.ok("User authenticated successfully");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid UserEmailRequest userEmailRequest){

        userService.sendEmailVerification(userEmailRequest.getEmail());
        Response<Object> response = Response.builder()
                .message("Veuillez vérifier votre boîte de réception et cliquer sur le lien de vérification que nous vous avons envoyé. ")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {

        tokenConfirmationService.confirmEmail(confirmationToken);
        // make the otoken expired if he exceeds 1 day without touch
        Response<Object> response = Response.builder()
                .message("Votre email a été vérifié avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value="/block", method= {RequestMethod.GET})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> blockAccount(@RequestParam("userid")Long userid) {

        userService.blockAccount(userid);
        // make the otoken expired if he exceeds 1 day without touch
        Response<Object> response = Response.builder()
                .message("Le compte a été bloqué avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value="/activate", method= {RequestMethod.GET})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> activateAccount(@RequestParam("userid")Long userid) {

        userService.activateAccount(userid);
        // make the otoken expired if he exceeds 1 day without touch
        Response<Object> response = Response.builder()
                .message("Le compte a été activé avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value="/approuver", method= {RequestMethod.GET})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> approuverAccount(@RequestParam("userid")Long userid) {

        userService.approuverAccount(userid);
        // make the otoken expired if he exceeds 1 day without touch
        Response<Object> response = Response.builder()
                .message("Le compte a été approuvé avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @RequestMapping(value="/desapprouver", method= {RequestMethod.GET})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> desapprouverAccount(@RequestParam("userid")Long userid) {

        userService.desapprouverAccount(userid);
        // make the otoken expired if he exceeds 1 day without touch
        Response<Object> response = Response.builder()
                .message("Le compte a été approuvé avec succès.")
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value="/delete", method= {RequestMethod.DELETE})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> deleteAccount(@RequestParam("userid")Long userid) {

        userService.deleteAccount(userid);
        // make the otoken expired if he exceeds 1 day without touch
        Response<Object> response = Response.builder()
                .message("Le compte a été supprimé avec succès.")
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
        UserResponse userResponse= userService.getUserInfo(userid);
        Response<Object> response = Response.builder()
                .data(userResponse)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }

    @GetMapping("/findallnotifications")
    public ResponseEntity findallnotifications(@RequestParam("userid") Long userid) {
        List<Notification> notifications=userService.findallnotifications(userid);
        Response<Object> response = Response.builder()
                .data(notifications)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value="/getstats", method= {RequestMethod.GET})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getStatistique(@RequestParam("userid")Long userid) {

        Object responseObject=userService.getstats(userid);
        // make the otoken expired if he exceeds 1 day without touch
        Response<Object> response = Response.builder()
                .data(responseObject)
                .code(CODE.OK.getId())
                .success(true)
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


}
