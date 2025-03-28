package com.example.ticsys.order.controller;

import java.sql.Time;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticsys.order.dto.OrderDto;
import com.example.ticsys.order.dto.request.CreateOrderRequest;
import com.example.ticsys.order.dto.response.CreateOrderResponse;
import com.example.ticsys.order.dto.response.GetOrdersResponse;
import com.example.ticsys.order.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PutMapping("/{id}")
    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<String> ReserveOrder(@PathVariable int id,
                                                @RequestParam(required = false) Integer voucherOfUserId){
        if(voucherOfUserId == null){
            voucherOfUserId = -1;
        }
        String result = orderService.ReserveOrder(id, voucherOfUserId);
        if (result.equals("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    @PostMapping
    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<CreateOrderResponse> CreateOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        CreateOrderResponse result = orderService.CreateOrder(createOrderRequest);
        if(result.getMessage().equals("success"))
        {
            return ResponseEntity.ok(result);
        }
        else
        {
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping
    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN", "ROLE_USER"})
    @PreAuthorize("@orderSecurityServiceImpl.CanAccessGetOrders(#userIdStr,#eventId)")
    public ResponseEntity<GetOrdersResponse> GetOrders(@RequestParam(value = "include" ,required = false) String includeStr,
                                                    @RequestParam(value = "userId", required = false) String userIdStr,
                                                    @RequestParam(required = false, value = "eventId") String eventIdStr,
                                                    @RequestParam(required = false, value = "dateCreatedAt") String dateCreatedAtStr,
                                                    @RequestParam(required = false, value = "timeCreatedAt") String timeCreatedAtStr,
                                                    @RequestParam(required = false, value = "status") String statusStr){

        String userId = userIdStr;
        int eventId = eventIdStr == null ? -1 : Integer.parseInt(eventIdStr);
        LocalDate dateCreatedAt = dateCreatedAtStr == null ? null : LocalDate.parse(dateCreatedAtStr);
        Time timeCreatedAt = timeCreatedAtStr == null ? null : Time.valueOf(timeCreatedAtStr);
        String status = statusStr;

        GetOrdersResponse result = orderService.GetOrders(includeStr, userId, eventId, dateCreatedAt, timeCreatedAt, status);

        if (result.getMessage().equals("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
            
        }
    }
    @GetMapping("/search")
    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @PreAuthorize("hasRole('ROLE_ADMIN') or @orderSecurityServiceImpl.CanAccessGetOrdersBySearch(#eventId)")
    public ResponseEntity<GetOrdersResponse> GetOrdersBySearch(@RequestParam(required = false) String userFullNameKeyword,
                                                        @RequestParam(value="eventId", required = false) Integer eventId,
                                                        @RequestParam(value = "include" ,required = false) String includeStr){
        if(userFullNameKeyword == null){
            userFullNameKeyword = "";
        }
        if(eventId == null){
            eventId = -1;
        }
        GetOrdersResponse result = orderService.GetOrdersBySearch(userFullNameKeyword,eventId, includeStr);

        if (result.getMessage().equals("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping("/{id}")
    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN", "ROLE_USER"})
    @PreAuthorize("hasRole('ROLE_ADMIN') or @orderSecurityServiceImpl.CheckOrderAccessAuthority(#id)")
    public ResponseEntity<OrderDto> GetOrderById(@PathVariable int id, @RequestParam(value = "include" ,required = false) String includeStr){
        OrderDto result = orderService.GetOrderById(id, includeStr);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
