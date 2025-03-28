package com.example.ticsys.order.dao.ticketoforder;

import java.util.List;

import com.example.ticsys.order.model.TicketOfOrder;

public interface ITicketOfOrderDao {
    public boolean CreateTicketOfOrder(TicketOfOrder ticketOfOrder);
    public List<TicketOfOrder> GetTicketsOfOrder(int orderId);   
}
