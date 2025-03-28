package com.example.ticsys.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    int id;
    String receiverId;
    int eventId;
    boolean seen;
    String template;
    String type;
}
