package com.example.ticsys.app.order.dao.order.command;

import java.util.Map;

import com.example.ticsys.app.order.model.Order;

public interface IOrderCommandDao {
    public Integer CreateOrder(Order order);
    public Integer UpdateOrder(int id, Map<String, Object> orderValues);   
}
