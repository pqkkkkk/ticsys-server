package com.example.ticsys.order.dto;

import java.util.List;

import com.example.ticsys.order.model.Order;
import com.example.ticsys.order.model.TicketOfOrder;
import com.example.ticsys.sharedDto.SharedEventDto;
import com.example.ticsys.sharedDto.SharedPromotionDto;
import com.example.ticsys.sharedDto.SharedTicketDto;
import com.example.ticsys.sharedDto.SharedUserDto;

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
