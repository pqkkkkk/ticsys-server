package com.example.ticsys.order.service;


import java.sql.Time;
import java.time.LocalDate;

import com.example.ticsys.order.dto.OrderDto;
import com.example.ticsys.order.dto.request.CreateOrderRequest;
import com.example.ticsys.order.dto.response.CreateOrderResponse;
import com.example.ticsys.order.dto.response.GetOrdersResponse;
import com.example.ticsys.order.model.Order;

public interface OrderService {
    public CreateOrderResponse CreateOrder(CreateOrderRequest createOrderRequest);
    public OrderDto GetOrderById(int id, String includeStr);
    public GetOrdersResponse GetOrders(String includeStr, String userId, int eventId,
                                     LocalDate dateCreatedAt, Time timeCreatedAt, String status);
    public String ReserveOrder(int id, int voucherOfUserId);
    public int UpdateOrder(int id, Order order);
    public GetOrdersResponse GetOrdersBySearch(String userFullnameKeyword,int eventId,String includeStr);
}
