package com.example.ticsys.app.order.dao.ticketoforder;

import java.util.List;

import com.example.ticsys.app.order.model.TicketOfOrder;

public interface ITicketOfOrderDao {
    public boolean CreateTicketOfOrder(TicketOfOrder ticketOfOrder);
    public List<TicketOfOrder> GetTicketsOfOrder(int orderId);   
}
