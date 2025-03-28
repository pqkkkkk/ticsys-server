package com.example.ticsys.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ticsys.notification.dao.INotificationDao;
import com.example.ticsys.notification.model.Notification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final INotificationDao notificationDao;

    public NotificationServiceImpl(INotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public boolean CreateNotification(Notification notification) {
        try{
            return notificationDao.CreateNotification(notification);
        } catch (Exception e) {
            log.error("Error in CreateNotification of NotificationService", e);
            return false;
        }
    }

    @Override
    public boolean UpdateNotification(Notification notification) {
        try{
            return notificationDao.UpdateNotification(notification);
        } catch (Exception e) {
            log.error("Error in UpdateNotification of NotificationService", e);
            return false;
        }
    }

    @Override
    public List<Notification> GetNotifications(String receiverId, int eventId, boolean seen, String type) {
        try{
            return notificationDao.GetNotifications(receiverId, eventId, seen, type);
        } catch (Exception e) {
            log.error("Error in GetNotifications of NotificationService", e);
            return null;
        }
    }
}
