package com.example.ticsys.notification.service;

import java.util.List;

import com.example.ticsys.notification.model.Notification;

public interface NotificationService {
    public boolean CreateNotification(Notification notification);
    public boolean UpdateNotification(Notification notification);
    public List<Notification> GetNotifications(String receiverId, int eventId, boolean seen, String type);
}
