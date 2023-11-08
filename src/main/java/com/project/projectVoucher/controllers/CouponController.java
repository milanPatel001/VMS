package com.project.projectVoucher.controllers;

import com.project.projectVoucher.model.*;
import com.project.projectVoucher.service.CouponService;
import com.project.projectVoucher.service.CustomUserDetails;
import com.project.projectVoucher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('admin') or hasAuthority('fetch')")
    @GetMapping
    public List<Coupon> getCoupons(){
        CustomUserDetails c = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(c.isAdmin()) return couponService.getCoupons(Integer.parseInt(c.getId()));

        User u = userService.getUser(c.getId());
        return couponService.getCoupons(u.getAdminId());

    }

    @PreAuthorize("hasAuthority('admin') or hasAuthority('fetch')")
    @GetMapping("info")
    public CouponInfoJSON getCouponInfo(@RequestParam String coupon){
        return couponService.getCouponInfo(coupon);
    }

    @PreAuthorize("hasAuthority('admin') or hasAuthority('fetch')")
    @GetMapping("batch")
    public List<Coupon> getCouponsInBatch(@RequestParam(name = "value") List<String> batchArr){
        return couponService.getBatchCoupons(batchArr);
    }

    @PreAuthorize("hasAuthority('admin') or hasAuthority('create')")
    @PostMapping("create")
    public ResponseEntity<List<UseCaseBody>> createCoupons(@RequestParam(required = false) Integer length, @RequestParam(required = false) String pattern, @RequestParam(required = false) Integer count, @RequestBody List<UseCaseBody> useCases){
        CustomUserDetails c = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UseCaseBody> l;

        //Admin access
        if(c.isAdmin()){
            try{
                l = couponService.createCoupons(length, pattern, count, useCases, Integer.parseInt(c.getId()));

                return ResponseEntity.ok(l);
            }catch (Exception e){
                l = new ArrayList<UseCaseBody>();
                UseCaseBody u = new UseCaseBody(e.toString());
                l.add(u);
                return ResponseEntity.badRequest().body(l);
            }

        }

        // User access
        User u = userService.getUser(c.getId());

        if(u==null) return ResponseEntity.badRequest().body(new ArrayList<UseCaseBody>());
        try{
            l = couponService.createCoupons(length, pattern, count, useCases, u.getAdminId());
        }catch (Exception e){
            l = new ArrayList<UseCaseBody>();
            UseCaseBody u2 = new UseCaseBody(e.toString());
            l.add(u2);
            return ResponseEntity.badRequest().body(l);
        }

        return ResponseEntity.ok(l);
    }

//    @PreAuthorize("hasAuthority('admin') or hasAuthority('create')")
//    @PostMapping("create")
//    public String createCoupon(@RequestBody Coupon coupon){
//        return couponService.createCoupon(coupon);
//    }


    @PreAuthorize("hasAuthority('admin') or hasAuthority('update')")
    @PutMapping("use")
    public ResponseEntity<String> useCoupon(@RequestParam String coupon, @RequestParam String clientId, @RequestParam(required = false) boolean match){
        return couponService.useCoupon(coupon, clientId, match);
    }


    @PreAuthorize("hasAuthority('admin') or hasAuthority('update')")
    @PutMapping("update")
    public String updateCoupon(@RequestParam String coupon ,@RequestBody List<CouponFieldUpdate> updateBody){
        return couponService.updateCoupon(coupon, updateBody);
    }

    @PreAuthorize("hasAuthority('admin') or hasAuthority('delete')")
    @DeleteMapping("delete")
    public String deleteCoupon(@RequestParam(required = false) String coupon, @RequestParam(required = false) String batch){
        return couponService.deleteCoupon(coupon, batch);
    }

}

