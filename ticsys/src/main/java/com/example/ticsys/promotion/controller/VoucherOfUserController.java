package com.example.ticsys.promotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticsys.promotion.model.VoucherOfUser;
import com.example.ticsys.promotion.service.PromotionService;

@RestController
@RequestMapping("/api/promotion/voucherOfUser")
public class VoucherOfUserController {
    private final PromotionService promotionService;

    @Autowired
    public VoucherOfUserController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
    @GetMapping
    @PreAuthorize("@promotionSecurityServiceImpl.CanAccessGetVoucherOfUser(#userId, #promotionId)")
    public ResponseEntity<List<VoucherOfUser>> GetVoucherOfUsers(@RequestParam(required = false) String userId,
                                                                 @RequestParam(required = false) Integer promotionIdInteger,
                                                                  @RequestParam(required = false) String status) {
        int promotionId = -1;

        if(promotionIdInteger != null){
            promotionId = promotionIdInteger.intValue();
        }
        List<VoucherOfUser> result = promotionService.GetVoucherOfUsers(userId, promotionId, status);

        if(result != null){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().build();
    }
}
