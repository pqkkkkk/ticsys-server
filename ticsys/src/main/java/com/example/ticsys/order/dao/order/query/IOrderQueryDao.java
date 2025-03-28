package com.example.ticsys.order.dao.order.query;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import com.example.ticsys.order.dto.OrderDto;
import com.example.ticsys.order.dto.OrderInfoForCheckAuthorityDto;
import com.example.ticsys.order.dto.OrderWithTicketCountDto;
import com.example.ticsys.order.model.Order;

public interface IOrderQueryDao {
    public OrderInfoForCheckAuthorityDto GetOrderInfoForCheckAuthority(int orderId);
    public OrderDto GetOrderWithAllRelatedInfoById(int orderId);
    public Order GetOrderById(int id);
    public List<Order> GetOrders(String userId, LocalDate dateCreated,
                                 Time timeCreated, String status, Integer eventId);
    List<OrderWithTicketCountDto> GetOrders(String userId, LocalDate dateCreated,
                                            Time timeCreated, String status, Integer eventId, Integer ticketTypeId);
    public List<Order> SearchOrders(String userFullNameKeyword,int eventId);
}
