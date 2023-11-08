package com.project.projectVoucher.dao;

import com.project.projectVoucher.model.Admin;
import com.project.projectVoucher.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<Admin, Integer> {
    @Query("SELECT a FROM Admin a WHERE a.apiKey = ?1")
    Admin findByApiKey(String apiKey);

    @Query("SELECT a FROM Admin a WHERE a.id = ?1")
    Admin findByApiKey(int id);

}
