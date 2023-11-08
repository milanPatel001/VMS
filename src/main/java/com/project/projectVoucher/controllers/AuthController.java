package com.project.projectVoucher.controllers;

import com.project.projectVoucher.dao.AdminDao;
import com.project.projectVoucher.dao.UserDao;
import com.project.projectVoucher.model.TokenBody;
import com.project.projectVoucher.security.JWTService;
import com.project.projectVoucher.service.CustomUserDetails;
import com.project.projectVoucher.service.CustomUserDetailsService;
import com.project.projectVoucher.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UtilService utilService;

    public AuthController(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody TokenBody tokenBody){
        try{
            CustomUserDetails c =  customUserDetailsService.loadUserById(tokenBody.getId());

            //remove first condition
            if(!c.isAdmin() && !utilService.checkApiKey(tokenBody.getApiKey(),c.getApiKey())){
                return ResponseEntity.badRequest().body("This API Key is invalid!!");
            }

            String jwtToken = jwtService.generateToken(c);
            return ResponseEntity.ok(jwtToken);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }

    }
}
