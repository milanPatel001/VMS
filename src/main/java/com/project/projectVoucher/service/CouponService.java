package com.project.projectVoucher.service;

import com.project.projectVoucher.dao.CouponDao;
import com.project.projectVoucher.model.Coupon;
import com.project.projectVoucher.model.CouponFieldUpdate;
import com.project.projectVoucher.model.CouponInfoJSON;
import com.project.projectVoucher.model.UseCaseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Transactional
@Service
public class CouponService {

    @Autowired
    CouponDao couponDao;


    public List<Coupon> getCoupons(Integer adminId) {
        return couponDao.findAllByAdminId(adminId);
    }


    public CouponInfoJSON getCouponInfo(String coupon) {
        Coupon c = couponDao.findByName(coupon);
        return new CouponInfoJSON(c.getId(), c.getCouponId(), c.getDiscount(), c.getExpiryDate(), c.getExtra(), c.isUsed(), c.getClientId(), c.getBatch());
    }

    public ResponseEntity<String> useCoupon(String coupon, String clientId, Boolean match) {
        Coupon c = couponDao.findByName(coupon);

        // match==true means that clientId is given to coupon on creation; only that clientId can use that coupon
        // match==false means that coupon can be used by any clientId
        if(match==null) match = false;

        if(c==null){
            return ResponseEntity.badRequest().body("Coupon not found");
        }

        if(match) {
            if(!c.getClientId().equals(clientId)) return ResponseEntity.badRequest().body("This clientId can't use this coupon");
        }

        if (c.isUsed()) {
            if(match) return ResponseEntity.badRequest().body("This coupon is already used one time by this client");
            else return ResponseEntity.badRequest().body("This coupon is already used one time by a client");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if(currentTime.isAfter(c.getExpiryDate())){
            return ResponseEntity.badRequest().body("This coupon is already expired. Expiry Date: "+c.getExpiryDate().toString());
        }

        c.setUsed(true);
        if(!match) c.setClientId(clientId);

        try{
            couponDao.save(c);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }

        return ResponseEntity.ok("Coupon used!");

    }

    public List<Coupon> getBatchCoupons(List<String> batchArr) {
        return couponDao.findAllByBatch(batchArr);
    }

    public String updateCoupon(String coupon, List<CouponFieldUpdate> updateBody) {
        Coupon c = couponDao.findByName(coupon);

        if(c==null) return "Coupon not found";

        Object fieldValue;
        String fieldName;

        for(CouponFieldUpdate cu: updateBody){
            fieldName = cu.getField();
            fieldValue = cu.getValue();

            if ("used".equalsIgnoreCase(fieldName)) {

                if (fieldValue instanceof Boolean) c.setUsed((Boolean) fieldValue);
                else return "'Used' field doesn't have correct value. It should be a boolean!!";

            } else if ("extra".equalsIgnoreCase(fieldName)) {

                if (fieldValue instanceof String) c.setExtra((String)fieldValue);
                else return "Unexpected value type passed in 'extra' field";

            } else if("discount".equalsIgnoreCase(fieldName)){

                if(fieldValue instanceof String) c.setDiscount((String) fieldValue);
                else return "Unexpected value type passed in 'discount' field";

            }else if("batch".equalsIgnoreCase(fieldName)){

                if(fieldValue instanceof String) c.setBatch((String) fieldValue);
                else return "Unexpected value type passed in 'batch' field";

            }else if("expiry_date".equalsIgnoreCase(fieldName)){
                if(fieldValue instanceof String) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime time = LocalDateTime.parse((String) fieldValue, formatter);
                    c.setExpiryDate(time);
                }
                else return "Unexpected value type passed in 'expiry_date' field";

            }
        }

        try{
            couponDao.save(c);
        }catch (Exception e){
            return e.toString();
        }

        return "Coupon updated successfully.";

    }

    public String deleteCoupon(String coupon, String batch) {
        if(batch==null && coupon==null) return "No Query Params provided!!";

        try{
            if(batch!=null) couponDao.deleteByBatch(batch);
            else{
                couponDao.deleteBycouponId(coupon);
            }
        }catch (Exception e){
            return e.toString();
        }


        return "Deleted successfully";
    }

    public List<UseCaseBody> createCoupons(Integer length, String pattern, Integer count, List<UseCaseBody> useCases, Integer adminId) throws Exception {

            if(useCases==null) throw new Exception("Use Cases not provided");


            StringBuilder coupon = new StringBuilder();
            Set<String> set = new HashSet<>();
            Random rand = new Random();

            if(count==null) count = 1;

            if(useCases.size() != count) throw new Exception("Number of useCases should match the count (default is 1)");

            if(pattern==null){
                pattern = "";
                if(length==null) {
                    length = 6;
                }

                for (int i = 0; i < length; i++) pattern += "#";
            }

            List<Coupon> couponList = new ArrayList<>();
            UseCaseBody u;
            Coupon c;

            int randomNum;

            int i;
            for(i=0;i<count;i++){
                    int j = 0;
                    while(j<pattern.length()){
                        randomNum = rand.nextInt(26);
                        if(pattern.charAt(j)=='#'){
                            coupon.append((char) ('A' + randomNum));
                        }else coupon.append(pattern.charAt(j));
                        j++;
                    }

                    if(set.contains(coupon.toString())) i--;
                    else {
                       u = useCases.get(i);
                       u.setPattern(coupon.toString());

                       c = new Coupon();

                       c.setCouponId(coupon.toString());
                       c.setExpiryDate(u.getExpiryDate());
                       c.setBatch(u.getBatchNum());
                       c.setDiscount(u.getDiscount());
                       c.setExtra(u.getExtra());
                       c.setAdminId(adminId);

                       couponList.add(c);


                       set.add(coupon.toString());
                    }
                    coupon = new StringBuilder();
            }

            couponDao.saveAll(couponList);

            return useCases;

    }
}
