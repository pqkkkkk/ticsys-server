package com.example.ticsys.app.order.service.security;

public interface OrderSecurityService {
    public boolean CheckOrderAccessAuthority(int orderId);
    public  boolean CanAccessGetOrders(String userIdOfOrder,Integer eventIdOfOrder);
    public boolean CanAccessGetOrdersBySearch(Integer eventIdOfOrder);
}
