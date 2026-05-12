package com.fraud_auth_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/teste")
    public String test(){
        return "TESTE ADM";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/debug")
    public String debug(Authentication auth){
    return auth.getAuthorities().toString();
}
}
