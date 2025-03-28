package com.example.ticsys.order.service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.example.ticsys.account.service.Public.PublicAccountService;
import com.example.ticsys.event.service.Public.PublicEventService;
import com.example.ticsys.order.dao.order.command.IOrderCommandDao;
import com.example.ticsys.order.dao.order.query.IOrderQueryDao;
import com.example.ticsys.order.dao.ticketoforder.ITicketOfOrderDao;
import com.example.ticsys.order.dto.OrderDto;
import com.example.ticsys.order.dto.request.CreateOrderRequest;
import com.example.ticsys.order.dto.response.CreateOrderResponse;
import com.example.ticsys.order.dto.response.GetOrdersResponse;
import com.example.ticsys.order.model.Order;
import com.example.ticsys.order.model.TicketOfOrder;
import com.example.ticsys.promotion.service.Public.PublicPromotionService;
import com.example.ticsys.sharedDto.SharedEventDto;
import com.example.ticsys.sharedDto.SharedPromotionDto;
import com.example.ticsys.sharedDto.SharedTicketDto;
import com.example.ticsys.sharedDto.SharedUserDto;
import com.example.ticsys.sharedDto.SharedVoucherOfUserDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService  {
    private final IOrderCommandDao orderCommandDao;
    private final IOrderQueryDao orderQueryDao;
    private final ITicketOfOrderDao ticketOfOrderDao;
    private final PublicEventService publicEventService;
    private final PublicAccountService publicAccountService;
    private final PublicPromotionService publicPromotionService;
    @Autowired
    public OrderServiceImpl(ITicketOfOrderDao ticketOfOrderDao,
                            IOrderCommandDao orderCommandDao, IOrderQueryDao orderQueryDao,
                             PublicEventService publicEventService, PublicAccountService publicAccountService,
                             PublicPromotionService publicPromotionService) {
        this.ticketOfOrderDao = ticketOfOrderDao;
        this.publicEventService = publicEventService;
        this.publicAccountService = publicAccountService;
        this.publicPromotionService = publicPromotionService;
        this.orderCommandDao = orderCommandDao;
        this.orderQueryDao = orderQueryDao;
    }
    private boolean isOrderValid(Order order){
        return order.getEventId() != 0 && order.getPrice() != 0;
    }
    private boolean isValidUser(String username){
        return publicAccountService.GetUserByUsername(username) != null;
    }
    private boolean isValidEvent(int eventId){
        return publicEventService.GetEventById(eventId) != null;
    }
    private boolean isValidPromotion(int promotionId){
        return publicPromotionService.IsValidPromotion(promotionId);   
    }
    private int CalculatePriceOfOrder(int promotionId,List<TicketOfOrder> ticketOfOrders){
        int price = 0;
        
        for(TicketOfOrder ticketOfOrder : ticketOfOrders){
            SharedTicketDto ticketInfo = publicEventService.GetTicketById(ticketOfOrder.getTicketId());
            price += ticketInfo.getPrice() * ticketOfOrder.getQuantity();
        }

        if(promotionId == -1){
            return price;
        }

        SharedPromotionDto promotion = publicPromotionService.GetPromotionById(promotionId);

        if(promotion == null){
            return -1;
        }
        if(promotion.getType().equals("Voucher Gift")){
            return price;
        }

        int reduction = publicPromotionService.GetReductionOfPromotion(promotionId, price);
        if(reduction == -1){
            return -1;
        }
        price -= reduction;
        
        return price;
    }
    @Override
    @Transactional
    public CreateOrderResponse CreateOrder(CreateOrderRequest createOrderRequest) {
       try{
            Order order = createOrderRequest.getOrder();
            
            if(!isOrderValid(order)){
                throw new Exception("Order is not valid");
            }
            if(!isValidUser(order.getCreatedBy())){
                throw new Exception("User is not valid");
            }
            if(!isValidEvent(order.getEventId())){
                throw new Exception("Event is not valid");
            }
            if(order.getPromotionId() != -1 && !isValidPromotion(order.getPromotionId())){
                throw new Exception("Promotion is not valid");
            }
            
            int price = CalculatePriceOfOrder(order.getPromotionId(), createOrderRequest.getTicketOfOrders());
            if(price == -1){
                throw new Exception("Price calculation failed");
            }

            order.setPrice(price);
            order.setStatus("PENDING");
            order.setDateCreatedAt(LocalDate.now());
            order.setTimeCreatedAt(Time.valueOf(java.time.LocalTime.now()));

            int orderId = orderCommandDao.CreateOrder(order);
            if(orderId == -1){
                throw new Exception("Order creation failed");
            }

            for(TicketOfOrder ticketOfOrder : createOrderRequest.getTicketOfOrders()){
                ticketOfOrder.setOrderId(orderId);
                if(!ticketOfOrderDao.CreateTicketOfOrder(ticketOfOrder)){
                    throw new Exception("Ticket creation failed");
                }
            }

            return CreateOrderResponse.builder().message("success").orderId(orderId).build();
       }
       catch (Exception e)
       {
            log.error("Error in CreateOrder of OrderService", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CreateOrderResponse.builder().message(e.getMessage()).build();
       }
    }
    private OrderDto populateOrderDto(Order order, String includeStr) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrder(order);
    
        if (includeStr != null) {
            if (includeStr.contains("user")) {
                SharedUserDto user = publicAccountService.GetUserByUsername(order.getCreatedBy());
                orderDto.setUserInfos(user);
            }
            if (includeStr.contains("event")) {
                SharedEventDto event = publicEventService.GetEventById(order.getEventId());
                orderDto.setEvent(event);
            }
            if (includeStr.contains("ticketOfOrders") && includeStr.contains("ticket")) {
                List<TicketOfOrder> ticketOfOrders = ticketOfOrderDao.GetTicketsOfOrder(order.getId());
                List<SharedTicketDto> ticketInfos = new ArrayList<>();
                for (TicketOfOrder ticketOfOrder : ticketOfOrders) {
                    SharedTicketDto ticketInfo = publicEventService.GetTicketById(ticketOfOrder.getTicketId());
                    ticketInfos.add(ticketInfo);
                }
                orderDto.setTicketOfOrders(ticketOfOrders);
                orderDto.setTicketInfos(ticketInfos);
            } else if (includeStr.contains("ticketOfOrders")) {
                List<TicketOfOrder> ticketOfOrders = ticketOfOrderDao.GetTicketsOfOrder(order.getId());
                orderDto.setTicketOfOrders(ticketOfOrders);
                orderDto.setTicketInfos(null);
            }
            if (includeStr.contains("promotion")) {
                SharedPromotionDto promotion = publicPromotionService.GetPromotionById(order.getPromotionId());
                if (promotion != null) {
                    int reduction = order.getPrice() / (100 - promotion.getPromoPercent()) * 100 - order.getPrice();
                    promotion.setReduction(reduction);
                }
                orderDto.setPromotionInfo(promotion);
            }
        }
        return orderDto;
    }
    @Override
    public OrderDto GetOrderById(int id, String includeStr) {
        try{
            OrderDto orderDto = new OrderDto();
            Order order = orderQueryDao.GetOrderById(id);
            orderDto.setOrder(order);

            return populateOrderDto(order, includeStr);
           
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public GetOrdersResponse GetOrders(String includeStr, String userId, int eventId, LocalDate dateCreatedAt, Time timeCreatedAt, String status) {
        try{

            List<OrderDto> orderDtos = new ArrayList<>();
            List<Order> orders = orderQueryDao.GetOrders(userId, dateCreatedAt, timeCreatedAt, status, eventId);

            for(Order order : orders){
                OrderDto orderDto = populateOrderDto(order, includeStr);
                orderDtos.add(orderDto);
            }
            return GetOrdersResponse.builder().message("success").orderDtos(orderDtos).build();
        }
        catch (Exception e) {
            return GetOrdersResponse.builder().orderDtos(null).message(e.getMessage()).build();
        }
    }
    @Override
    public GetOrdersResponse GetOrdersBySearch(String userFullNameKeyword, int eventId, String includeStr) {
        try{

            List<OrderDto> orderDtos = new ArrayList<>();
            List<Order> orders = orderQueryDao.SearchOrders(userFullNameKeyword, eventId);

            for(Order order : orders){
                OrderDto orderDto = populateOrderDto(order, includeStr);
                orderDtos.add(orderDto);
            }
            return GetOrdersResponse.builder().message("success").orderDtos(orderDtos).build();
        }
        catch (Exception e)
        {
            return GetOrdersResponse.builder().orderDtos(null).message(e.getMessage()).build();
        }
    }
    @Override
    @Transactional
    public String ReserveOrder(int id, int voucherOfUserId) {
        try{
            Order order = orderQueryDao.GetOrderById(id);

            if(order == null){
                throw new Exception("Order is not valid");
            }

            int correspondingPromotinoId = order.getPromotionId();
            if(correspondingPromotinoId > 0)
            {
                if(!isValidPromotion(correspondingPromotinoId)){
                    throw new Exception("Promotion is not valid");
                }

                SharedPromotionDto promotion = publicPromotionService.GetPromotionById(correspondingPromotinoId);
                if(promotion == null){
                    throw new Exception("Promotion is not valid");
                }
                
                if(promotion.getType().equals("Voucher Gift")){
                    SharedVoucherOfUserDto sharedVoucherOfUser = SharedVoucherOfUserDto.builder()
                            .promotionId(correspondingPromotinoId)
                            .status("UNUSED")
                            .userId(order.getCreatedBy())
                            .voucherValue(promotion.getVoucherValue())
                            .build();
                    
                    int createResult =  publicPromotionService.CreateVoucherOfUser(sharedVoucherOfUser);
                    if(createResult == -1){
                        throw new Exception("Voucher creation failed");
                    }
                }
            }

            int validVoucherOfUser = publicPromotionService.IsValidVoucherOfUser(voucherOfUserId);
            if(validVoucherOfUser == 0){
                throw new Exception("Voucher is not valid");
            }
            int voucherValue = validVoucherOfUser == -1 ? 0 : publicPromotionService.GetVoucherOfUserById(voucherOfUserId).getVoucherValue();
            int AppliedVoucherPrice = order.getPrice() - voucherValue
                                         < 0 ? 0 : order.getPrice() - voucherValue;

            Order updatedOrder = Order.builder()
                            .price(AppliedVoucherPrice)
                            .status("PAID").build();
            if(UpdateOrder(id, updatedOrder) <= 0){
                throw new Exception("Order update failed");
            }

            SharedVoucherOfUserDto updateSharedVoucherOfUserDto = SharedVoucherOfUserDto.builder()
                                                                .id(voucherOfUserId)
                                                                .promotionId(-1)
                                                                .voucherValue(-1)
                                                                .status("USED")
                                                                .build();
            if(publicPromotionService.UpdateVoucherOfUser(updateSharedVoucherOfUserDto) == 0){
                throw new Exception("Voucher update failed");
            }

            List<TicketOfOrder> ticketOfOrders = ticketOfOrderDao.GetTicketsOfOrder(id);
            for(TicketOfOrder ticketOfOrder : ticketOfOrders){
                SharedTicketDto ticketInfo = publicEventService.GetTicketById(ticketOfOrder.getTicketId());
                int quantity = ticketInfo.getQuantity();
                int afterQuantity = quantity - ticketOfOrder.getQuantity();

                if(afterQuantity < 0){
                    throw new Exception("Ticket quantity is not enough");
                }

                int updateTicketResult = publicEventService.UpdateTicketByRequiredFieldsList(Map.of("quantity", afterQuantity), ticketOfOrder.getTicketId());
                if(updateTicketResult == 0){
                    throw new Exception("Ticket quantity update failed");
                }
            }
        
            return "success";
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.getMessage();
        }
    }
    @Override
    public int UpdateOrder(int id, Order order) {
        try{
            Map<String, Object> orderValues = new HashMap<>();

            if(order.getEventId() != 0){
                orderValues.put("eventId", order.getEventId());
            }
            if(order.getPrice() != 0){
                orderValues.put("price", order.getPrice());
            }
            if(order.getPromotionId() != 0){
                orderValues.put("promotionId", order.getPromotionId());
            }
            if(order.getStatus() != null){
                orderValues.put("status", order.getStatus());
            }
            if(order.getDateCreatedAt() != null){
                orderValues.put("dateCreatedAt", order.getDateCreatedAt());
            }
            if(order.getTimeCreatedAt() != null){
                orderValues.put("timeCreatedAt", order.getTimeCreatedAt());
            }
            if(order.getCreatedBy() != null){
                orderValues.put("createdBy", order.getCreatedBy());
            }
            
            return orderCommandDao.UpdateOrder(id, orderValues);
        }
        catch (Exception e)
        {
            log.error("Error in UpdateOrder of OrderService", e);
            return -1;
        }
    }
    

}
