package com.example.ticsys.order.service.security;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.ticsys.event.service.Public.PublicEventService;
import com.example.ticsys.order.dao.order.query.IOrderQueryDao;
import com.example.ticsys.order.dto.OrderInfoForCheckAuthorityDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderSecurityServiceImpl implements OrderSecurityService {
    private final IOrderQueryDao orderQueryDao;
    private final PublicEventService publicEventService;
    @Autowired
    public OrderSecurityServiceImpl(IOrderQueryDao eventQueryDao, PublicEventService publicEventService) {
        this.orderQueryDao = eventQueryDao;
        this.publicEventService = publicEventService;

    }
    @Override
    public boolean CheckOrderAccessAuthority(int orderId) {
        try{
            OrderInfoForCheckAuthorityDto orderInfo = orderQueryDao.GetOrderInfoForCheckAuthority(orderId);

            if (orderInfo == null) {
                log.warn("Order not found: {}", orderId);
                return false;
            }

            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            if (currentUsername.equals(orderInfo.getCreatedBy())) {
                return true;
            }
            if (currentUsername.equals(orderInfo.getEventOwner())) {
                return true;     
            }
            
            return false;
        }
        catch(Exception e){
            log.error("Error while checking order access authority", e);
            return false;
        }
    }
    @Override
    public boolean CanAccessGetOrders(String userIdOfOrder, Integer eventIdOfOrder) {
        try{
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(r -> r.getAuthority()).toList();

        if(currentRoles.contains("ROLE_ADMIN")){
            return true;
        }
        else if(currentRoles.contains("ROLE_ORGANIZER")){
            if(eventIdOfOrder == null){
                return false;
            }

            String eventOwnerOfOrder = publicEventService.GetUsernameOfEventOwner(eventIdOfOrder);
            if(currentUsername.equals(eventOwnerOfOrder)){
                return true;
            }
        }
        else if(currentRoles.contains("ROLE_USER")){
            if (userIdOfOrder != null && currentUsername.equals(userIdOfOrder)) {
                return true;
            }
        }
        
        return false;
        }
        catch(Exception e){
            log.error("Error while checking order access authority", e);
            return false;
        }
    }
    @Override
    public boolean CanAccessGetOrdersBySearch(Integer eventIdOfOrder) {
        try{
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(r -> r.getAuthority()).toList();
            
            if(currentRoles.contains("ROLE_ADMIN")){
                return true;
            }
            else if(currentRoles.contains("ROLE_ORGANIZER")){
                if(eventIdOfOrder == null){
                    return false;
                }

                String eventOwnerOfOrder = publicEventService.GetUsernameOfEventOwner(eventIdOfOrder);
                if(currentUsername.equals(eventOwnerOfOrder)){
                    return true;
                }
            }
            else if(currentRoles.contains("ROLE_USER")){
                return false;
            }

            return false;
        }
        catch(Exception e){
            log.error("Error while checking order access authority", e);
            return false;
        }
    }

}
