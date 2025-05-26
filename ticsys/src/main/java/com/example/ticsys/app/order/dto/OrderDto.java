package com.example.ticsys.app.order.dto;

import java.util.List;

import com.example.ticsys.app.order.model.Order;
import com.example.ticsys.app.order.model.TicketOfOrder;
import com.example.ticsys.app.shared_dto.SharedEventDto;
import com.example.ticsys.app.shared_dto.SharedPromotionDto;
import com.example.ticsys.app.shared_dto.SharedTicketDto;
import com.example.ticsys.app.shared_dto.SharedUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDto {
    Order order;
    SharedEventDto event;
    List<TicketOfOrder> ticketOfOrders;
    List<SharedTicketDto> ticketInfos;
    SharedUserDto userInfos;
    SharedPromotionDto promotionInfo;
}
