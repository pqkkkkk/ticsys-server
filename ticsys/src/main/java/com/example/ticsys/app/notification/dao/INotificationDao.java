package com.example.ticsys.app.notification.dao;

import java.util.List;

import com.example.ticsys.app.notification.model.Notification;

public interface INotificationDao {
    public boolean CreateNotification(Notification notification);
    public boolean UpdateNotification(Notification notification);
    public List<Notification> GetNotifications(String receiverId, int eventId, boolean seen, String type);   
}
