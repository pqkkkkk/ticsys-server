package com.example.ticsys.order.service;


import java.sql.Time;
import java.time.LocalDate;

import com.example.ticsys.order.dto.OrderDto;
import com.example.ticsys.order.dto.request.CreateOrderRequest;
import com.example.ticsys.order.dto.response.CreateOrderResponse;
import com.example.ticsys.order.dto.response.GetOrdersResponse;
import com.example.ticsys.order.model.Order;

public interface OrderService {
    public String PayOrder(Integer orderId, Integer voucherOfUserId, String bankAccountId);
    public String ConfirmOrder(Integer orderId, Integer voucherOfUserId, Integer appliedVoucherPrice);
    public String CancelOrder(Integer orderId);
    public CreateOrderResponse CreateOrder(CreateOrderRequest createOrderRequest);
    public String ReserveOrder(Integer id, Integer voucherOfUserId);
    public int UpdateOrder(int id, Order order);

    public OrderDto GetOrderById(int id, String includeStr);
    public GetOrdersResponse GetOrders(String includeStr, String userId, int eventId,
                                     LocalDate dateCreatedAt, Time timeCreatedAt, String status);
    public GetOrdersResponse GetOrdersBySearch(String userFullnameKeyword,int eventId,String includeStr);
}
