package com.example.ticsys.app.order.dto.response;

import java.util.List;

import com.example.ticsys.app.order.dto.OrderDto;

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
public class GetOrdersResponse {
    List<OrderDto> orderDtos;
    String message;
}
