package com.project.projectVoucher.service;

import com.project.projectVoucher.dao.AdminDao;
import com.project.projectVoucher.dao.UserDao;
import com.project.projectVoucher.model.Admin;
import com.project.projectVoucher.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserDao userDao;

    @Autowired
    private final AdminDao adminDao;

    public CustomUserDetailsService(UserDao userDao, AdminDao adminDao) {
        this.userDao = userDao;
        this.adminDao = adminDao;
    }

    @Override
    public UserDetails loadUserByUsername(String apiKey) throws UsernameNotFoundException {
        return null;
    }


    public CustomUserDetails loadUserById(String id) throws UsernameNotFoundException {
        User user = userDao.findById(id);
        if (user != null) {
            // Create and return UserDetails for User
            return new CustomUserDetails(user.getAuthKey(),  user.getPermissions(), false, user.getUserId());
        }

        // If not found in User entity, try to find in the Admin entity
        Optional<Admin> admin = adminDao.findById(Integer.parseInt(id));
        if (admin.isPresent()) {
            // Create and return UserDetails for Admin
            return new CustomUserDetails(admin.get().getApiKey(),null, true, admin.get().getId().toString());
        }

        throw new UsernameNotFoundException("User or Admin not found with this id");
    }
}
