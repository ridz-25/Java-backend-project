
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Notification;
import src.model.NotificationType;
import src.model.User;
import src.util.ValidationUtil;

public class NotificationService {

    // In-memory storage
    private ArrayList<Notification> notifications =
        new ArrayList<>();

    // Automatic ID generation
    private int nextNotificationId = 1;

    // Shared service
    private UserService userService;

    // Constructor injection
    public NotificationService(UserService userService) {
        this.userService = userService;
    }

    // Create Notification
    public Notification createNotification(
            int userId,
            String message,
            NotificationType type) {

        User user = userService.getUserById(userId);

        if (user == null || !user.isActive()) {
            throw new IllegalArgumentException(
                "Invalid or inactive user."
            );
        }

        if (ValidationUtil.isEmpty(message) || type == null) {
            throw new IllegalArgumentException(
                "Notification message and type are required."
            );
        }

        Notification notification = new Notification(
            nextNotificationId++,
            userId,
            message.trim(),
            type,
            new Timestamp(System.currentTimeMillis()),
            false
        );

        notifications.add(notification);

        return notification;
    }

    // Get Notification by ID
    public Notification getNotificationById(int notificationId) {

        for (Notification notification : notifications) {

            if (notification.getNotificationId() == notificationId) {
                return notification;
            }
        }

        return null;
    }

    // Get all Notifications
    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    // Get Notifications by User
    public List<Notification> getNotificationsByUser(int userId) {

        ArrayList<Notification> result = new ArrayList<>();

        for (Notification notification : notifications) {

            if (notification.getUserId() == userId) {
                result.add(notification);
            }
        }

        return result;
    }

    // Get Unread Notifications
    public List<Notification> getUnreadNotifications(int userId) {

        ArrayList<Notification> result = new ArrayList<>();

        for (Notification notification : notifications) {

            if (notification.getUserId() == userId &&
                !notification.isRead()) {

                result.add(notification);
            }
        }

        return result;
    }

    // Get Unread Notification Count
    public int getUnreadCount(int userId) {

        return getUnreadNotifications(userId).size();
    }

    // Mark Notification as Read
    public boolean markAsRead(int notificationId, int userId) {

        Notification notification =
            getNotificationById(notificationId);

        if (notification == null ||
            notification.getUserId() != userId) {

            return false;
        }

        notification.markAsRead();

        return true;
    }

    // Mark All Notifications as Read
    public void markAllAsRead(int userId) {

        for (Notification notification : notifications) {

            if (notification.getUserId() == userId) {
                notification.markAsRead();
            }
        }
    }

    // Delete Notification
    public boolean deleteNotification(
            int notificationId,
            int userId) {

        Notification notification =
            getNotificationById(notificationId);

        if (notification == null ||
            notification.getUserId() != userId) {

            return false;
        }

        notifications.remove(notification);

        return true;
    }

    // Delete All Notifications of a User
    public void deleteAllNotifications(int userId) {

        notifications.removeIf(
            notification -> notification.getUserId() == userId
        );
    }
}
