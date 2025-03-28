package com.example.ticsys.order.dao.order.command;

import java.util.Map;

import com.example.ticsys.order.model.Order;

public interface IOrderCommandDao {
    public Integer CreateOrder(Order order);
    public Integer UpdateOrder(int id, Map<String, Object> orderValues);   
}
