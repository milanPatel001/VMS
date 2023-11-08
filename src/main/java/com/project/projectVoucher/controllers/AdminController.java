package com.project.projectVoucher.controllers;

import com.project.projectVoucher.model.Admin;
import com.project.projectVoucher.model.AllowRequest;
import com.project.projectVoucher.model.AllowedUser;
import com.project.projectVoucher.model.User;
import com.project.projectVoucher.service.AdminService;
import com.project.projectVoucher.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    AdminService adminService;


    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("create")
    public String createAdmin(@RequestBody Admin admin){
            return adminService.createAdmin(admin);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("users")
    public List<User> getUsers(){
        CustomUserDetails c = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return adminService.getUsers(c.getId());
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allow")
    public AllowedUser allowUser(@RequestBody AllowRequest allowRequest){
        CustomUserDetails c = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return adminService.allowUser(allowRequest, Integer.parseInt(c.getId()));
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("permissions")
    public List<String> getPermissions(@RequestParam String id){
        return adminService.getPermissions(id);
    }


    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("update_permissions")
    public String updatePermissions(@RequestParam(required = false) List<String> permissions, String userId){
        //overwrite the permissions
        return adminService.updatePermissions(permissions, userId);
    }
}
