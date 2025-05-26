package com.example.ticsys.app.order.service.Public;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticsys.app.order.dao.order.query.IOrderQueryDao;
import com.example.ticsys.app.order.dto.OrderWithTicketCountDto;
import com.example.ticsys.app.order.model.Order;
import com.example.ticsys.app.shared_dto.SharedOrderDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PublicOrderServiceImpl implements PublicOrderService {

    private final IOrderQueryDao orderQueryDao;

    @Autowired
    public PublicOrderServiceImpl(IOrderQueryDao orderQueryDao) {
        this.orderQueryDao = orderQueryDao;
    }
    @Override
    public List<SharedOrderDto> GetOrders(String userId, LocalDate dateCreated, Time timeCreated, String status,
            Integer eventId) {
        try{
            List<Order> orders = orderQueryDao.GetOrders(userId, dateCreated, timeCreated, status, eventId);

            List<SharedOrderDto> result = orders.stream().map(order -> {
                SharedOrderDto sharedOrderDto = new SharedOrderDto();
                sharedOrderDto.setId(order.getId());
                sharedOrderDto.setCreatedBy(order.getCreatedBy());
                sharedOrderDto.setDateCreatedAt(order.getDateCreatedAt());
                sharedOrderDto.setTimeCreatedAt(order.getTimeCreatedAt());
                sharedOrderDto.setEventId(order.getEventId());
                sharedOrderDto.setPrice(order.getPrice());
                sharedOrderDto.setPromotionId(order.getPromotionId());
                sharedOrderDto.setStatus(order.getStatus());
                return sharedOrderDto;
            }).toList();

            return result;
        }
        catch(Exception e){
            log.error("Error in GetOrders", e);
            return null;
        }
    }
    @Override
    public List<SharedOrderDto> GetOrders(String userId, LocalDate dateCreated, Time timeCreated, String status,
            Integer eventId, Integer ticketTypeId) {
        try{
            List<OrderWithTicketCountDto> orders = orderQueryDao.GetOrders(userId, dateCreated, timeCreated, status, eventId, ticketTypeId);

            List<SharedOrderDto> result = orders.stream().map(order -> {
                SharedOrderDto sharedOrderDto = new SharedOrderDto();
                sharedOrderDto.setId(order.getId());
                sharedOrderDto.setCreatedBy(order.getCreatedBy());
                sharedOrderDto.setDateCreatedAt(order.getDateCreatedAt());
                sharedOrderDto.setTimeCreatedAt(order.getTimeCreatedAt());
                sharedOrderDto.setEventId(order.getEventId());
                sharedOrderDto.setPrice(order.getPrice());
                sharedOrderDto.setPromotionId(order.getPromotionId());
                sharedOrderDto.setStatus(order.getStatus());
                sharedOrderDto.setTicketCount(order.getTicketCount());
                return sharedOrderDto;
            }).toList();

            return result;
        }
        catch(Exception e){
            log.error("Error in GetOrders", e);
            return null;
        }
    }

}
