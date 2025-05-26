package com.example.ticsys.app.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ticsys.app.account.dto.request.LinkBankAccountRequest;
import com.example.ticsys.app.account.model.OrganizerInfo;
import com.example.ticsys.app.account.model.PaymentMethod;
import com.example.ticsys.app.account.model.User;
import com.example.ticsys.app.account.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/account/user")
@Slf4j
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/{username}")
    @PreAuthorize("@accountSecurityServiceImpl.IsAccountOwner(#username)")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        log.info("getUserByUsername of UserController, username: ", username);
        User user = userService.GetUserByUsername(username);
        if(user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }
    @PutMapping("/{username}/banking")
    @PreAuthorize("@accountSecurityServiceImpl.IsAccountOwner(#username)")
    public ResponseEntity<String> LinkToBankAccount(@RequestBody LinkBankAccountRequest request, @PathVariable String username) {
        request.setUsername(username);
        request.setBankAccountId(request.getBankAccountId().replaceAll(" ", ""));
        request.setBankAccountOwnerName(request.getBankAccountOwnerName().replaceAll(" ", ""));
        String result = userService.LinkToBankAccount(request);

        if(result.equals("processing")){
            return ResponseEntity.ok("processing");
        } else{
            return ResponseEntity.badRequest().body("error");
        }
    }
    @GetMapping("/{username}/banking")
    @PreAuthorize("@accountSecurityServiceImpl.IsAccountOwner(#username)")
    public ResponseEntity<List<PaymentMethod>> GetPaymentMethodsOfUser(@PathVariable String username,
     @RequestParam(required = false) String bankName) {
        List<PaymentMethod> paymentMethods = userService.GetPaymentMethodsOfUser(username, bankName);
        if(paymentMethods == null || paymentMethods.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(paymentMethods);
        }
    }
    @GetMapping
    public ResponseEntity<List<User>> GetAllUsers(@RequestParam(required = false) String role)
    {
        List<User> users = userService.GetAllUsers(role);
        return ResponseEntity.ok(users);
    }
    @PostMapping
    public ResponseEntity<String> signup(@RequestBody User user) {
        if(userService.CreateUser(user)){
            return ResponseEntity.ok("successfully");
        } else {
            return ResponseEntity.badRequest().body("failed");
        }
    }
    @PostMapping("/organizer")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'USER')")
    public ResponseEntity<String> registerForOrganizer(@RequestParam("organizerInfo") String organizerInfoStr, @RequestParam(required = false) MultipartFile organizerAvt) {
        OrganizerInfo organizerInfo = new OrganizerInfo();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            organizerInfo = objectMapper.readValue(organizerInfoStr, OrganizerInfo.class);
        } catch (Exception e) {
            log.error("registerForOrganizer of UserController, error: ", e);
            return ResponseEntity.badRequest().body("failed");
        }

        if(userService.RegisterforOrganizer(organizerInfo, organizerAvt)){
            return ResponseEntity.ok("successfully");
        } else {
            return ResponseEntity.badRequest().body("failed");
        }
    }
}
