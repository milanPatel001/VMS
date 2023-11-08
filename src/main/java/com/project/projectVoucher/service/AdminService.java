package com.project.projectVoucher.service;

import com.project.projectVoucher.dao.AdminDao;
import com.project.projectVoucher.dao.UserDao;
import com.project.projectVoucher.model.Admin;
import com.project.projectVoucher.model.AllowRequest;
import com.project.projectVoucher.model.AllowedUser;

import com.project.projectVoucher.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Transactional
@Service
public class AdminService {
    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UtilService utilService;

    public List<Admin> getAdmins() {
        return adminDao.findAll();
    }

    public String createAdmin(Admin admin) {
        adminDao.save(admin);
        return "Saved";
    }

    public List<User> getUsers(String id) {
        return userDao.findAllByAdminId(id);
    }


    public AllowedUser allowUser(AllowRequest allowRequest, Integer adminId) {

        //generate Auth key and send it to dao

        int keyLength = 32;
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);
        String authKey = Base64.getEncoder().encodeToString(bytes);

        // user is responsible for adding secret key to auth key to make complete key and then authenticate
        String hashedKey = utilService.hashApiKey(allowRequest.getSecretKey()+""+authKey);


        // secret key, user id, permissions is provided by request body;
        // adminId, authKey, clientKey is provided by server

        try{

            User user = new User();

            user.setUserId(allowRequest.getId());
            user.setAdminId(adminId);
            user.setAuthKey(hashedKey);
            user.setPermissions(allowRequest.getPermissions());

            userDao.save(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return new AllowedUser(allowRequest.getId(), authKey);
    }



    public List<String> getPermissions(String id) {
        return userDao.getPermissions(id);
    }

    public String updatePermissions(List<String> permissions, String userId) {
        if(permissions==null){
            userDao.deleteByuserId(userId);
            return "User deleted successfully!!";
        }

        userDao.updatePermissions(permissions, userId);
        return "Permissions updated successfully!!";
    }
}
