package com.example.ticsys.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticsys.notification.model.Notification;
import com.example.ticsys.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @PostMapping
    public ResponseEntity<String> CreateNotification(@RequestBody Notification notification) {
        if (notificationService.CreateNotification(notification)) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.badRequest().body("failed");
            
        }
    }
    @GetMapping
    public ResponseEntity<List<Notification>> GetNotifications(@RequestParam (required = false) String receiverId,
                                                @RequestParam(required = false) int eventId,
                                                @RequestParam(required = false) boolean seen,
                                                @RequestParam(required = false) String type) {
        List<Notification> result = notificationService.GetNotifications(receiverId, eventId, seen, type);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping
    public ResponseEntity<String> UpdateNotification(@RequestBody Notification notification) {
        if (notificationService.UpdateNotification(notification)) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.badRequest().body("failed");
        }
    }
}
