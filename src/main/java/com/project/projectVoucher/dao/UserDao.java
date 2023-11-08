package com.project.projectVoucher.dao;

import com.project.projectVoucher.model.Coupon;
import com.project.projectVoucher.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    @Query("SELECT u.permissions FROM User u WHERE u.userId = ?1")
    List<String> getPermissions(String id);

    void deleteByuserId(String userId);

    @Modifying
    @Query("UPDATE User u SET u.permissions = ?1 WHERE u.userId = ?2")
    void updatePermissions(List<String> permissions, String userId);

    @Query("SELECT u FROM User u WHERE u.authKey = ?1")
    User findByApiKey(String apiKey);

    @Query("SELECT u FROM User u WHERE u.userId = ?1")
    User findById(String id);

    @Query("SELECT u FROM User u WHERE u.adminId = ?1")
    List<User> findAllByAdminId(String id);

    User findByuserId(String userId);
}
