package com.project.projectVoucher.controllers;

import com.project.projectVoucher.model.User;
import com.project.projectVoucher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

//    @GetMapping
//    public List<User> getUsers(){
//        return userService.getUsers();
//    }
//
//    @PostMapping("create")
//    public String createUser(@RequestBody User user){
//        return userService.createUser(user);
//    }

}
