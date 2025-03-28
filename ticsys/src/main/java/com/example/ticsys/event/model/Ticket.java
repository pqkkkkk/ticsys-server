package com.example.ticsys.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    int id;
    int eventId;
    int price;
    int quantity;
    String service;
    String name;
    int minQtyInOrder;
    int maxQtyInOrder;
    String colorHex;
}
