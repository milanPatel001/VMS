package com.project.projectVoucher.service;

import com.project.projectVoucher.dao.UserDao;
import com.project.projectVoucher.model.Coupon;
import com.project.projectVoucher.model.CouponInfoJSON;
import com.project.projectVoucher.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserDao userDao;

    public UserService() { }
    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(String id){
        return userDao.findByuserId(id);
    }

//    public List<User> getUsers() {
//        return userDao.findAll();
//    }
//
//    public String createUser(User user) {
//        userDao.save(user);
//        return "Saved User";
//    }



}
