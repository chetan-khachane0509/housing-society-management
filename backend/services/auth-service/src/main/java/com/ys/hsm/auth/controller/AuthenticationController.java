package com.ys.hsm.auth.controller;


import com.ys.hsm.auth.constants.ApplicationConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApplicationConstants.AUTH_BASE)
public class AuthenticationController {

    @GetMapping(ApplicationConstants.HEALTH)
    public String health(){
        return "running";
    }
}
