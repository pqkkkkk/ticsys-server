package com.example.ticsys.order.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.NonNull;

import com.example.ticsys.order.dto.OrderDto;
import com.example.ticsys.order.model.Order;
import com.example.ticsys.order.model.TicketOfOrder;
import com.example.ticsys.sharedDto.SharedEventDto;
import com.example.ticsys.sharedDto.SharedTicketDto;
import com.example.ticsys.sharedDto.SharedUserDto;

public class OrderDtoExtractor implements ResultSetExtractor<OrderDto> {

    @Override
    public OrderDto extractData(@NonNull java.sql.ResultSet rs) throws java.sql.SQLException {
        OrderDto dto = null;
        // Sử dụng Map để đảm bảo không thêm trùng lặp Order
        Map<Integer, TicketOfOrder> ticketOfOrderMap = new HashMap<>();
        Map<Integer, SharedTicketDto> ticketInfoMap = new HashMap<>();
        while (rs.next()) {
            if (dto == null) {
                dto = new OrderDto();

                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setPrice(rs.getInt("price"));
                order.setCreatedBy(rs.getString("createdBy"));
                order.setEventId(rs.getInt("eventId"));
                order.setDateCreatedAt(rs.getDate("dateCreatedAt").toLocalDate());
                order.setTimeCreatedAt(rs.getTime("timeCreatedAt"));
                order.setStatus(rs.getString("status"));
                order.setPromotionId(rs.getInt("promotionId"));
                dto.setOrder(order);

                SharedUserDto userInfos = new SharedUserDto();
                userInfos.setUserName(rs.getString("userName"));
                userInfos.setFullName(rs.getString("fullName"));
                userInfos.setEmail(rs.getString("email"));
                userInfos.setPhoneNumber(rs.getString("phoneNumber"));
                dto.setUserInfos(userInfos);

                SharedEventDto event = new SharedEventDto();
                event.setId(rs.getInt("eventId"));
                event.setOrganizerId(rs.getString("eventOwner"));
                event.setName(rs.getString("eventName"));
                event.setDescription(rs.getString("eventDescription"));
                event.setLocation(rs.getString("eventLocation"));
                event.setDate(rs.getDate("eventStartDate").toLocalDate());
                event.setBannerPath(rs.getString("eventBannerPath"));
                event.setStatus(rs.getString("eventStatus"));
                event.setCategory(rs.getString("eventCategory"));
                event.setTime(rs.getTime("eventTime"));

                dto.setEvent(event);
                dto.setTicketOfOrders(new ArrayList<>());
                dto.setTicketInfos(new ArrayList<>());
            }
            
            Integer ticketId = rs.getInt("ticketId");
            
            if (ticketId > 0 && !ticketOfOrderMap.containsKey(ticketId)) {
                TicketOfOrder ticketOfOrder = new TicketOfOrder();
                ticketOfOrder.setId(rs.getInt("ticketOfOrderId"));
                ticketOfOrder.setOrderId(rs.getInt("id"));
                ticketOfOrder.setTicketId(rs.getInt("ticketId"));
                ticketOfOrder.setQuantity(rs.getInt("ticketOfOrderQuantity"));
                dto.getTicketOfOrders().add(ticketOfOrder);
                ticketOfOrderMap.put(ticketOfOrder.getTicketId(), ticketOfOrder);

                SharedTicketDto ticketInfo = new SharedTicketDto();
                ticketInfo.setId(rs.getInt("ticketId"));
                ticketInfo.setEventId(rs.getInt("eventId"));
                ticketInfo.setName(rs.getString("ticketName"));
                ticketInfo.setPrice(rs.getInt("ticketPrice"));
                ticketInfo.setService(rs.getString("ticketService"));
                dto.getTicketInfos().add(ticketInfo);
                ticketInfoMap.put(ticketInfo.getId(), ticketInfo);
            }
        }
        return dto;
    }

}
