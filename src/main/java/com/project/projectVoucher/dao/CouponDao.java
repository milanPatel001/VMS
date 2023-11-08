package com.project.projectVoucher.dao;

import com.project.projectVoucher.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CouponDao extends JpaRepository<Coupon, Integer> {
    @Query("SELECT c FROM Coupon c WHERE c.couponId = ?1")
    public Coupon findByName(String coupon);

    public void deleteBycouponId(String coupon);

    @Query("SELECT c FROM Coupon c WHERE c.batch IN :batchArr")
    public List<Coupon> findAllByBatch(@Param("batchArr") List<String> batchArr);

    @Modifying
    @Query("DELETE FROM Coupon c WHERE c.batch = :batch")
    public void deleteByBatch(@Param("batch") String batch);

    @Query("SELECT c FROM Coupon c WHERE c.adminId = ?1")
    List<Coupon> findAllByAdminId(Integer adminId);
}
