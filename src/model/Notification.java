package src.model;

import java.sql.Timestamp;

public class Notification {

    private int notificationId;
    private int userId;
    private String message;
    private NotificationType type;
    private Timestamp createdAt;
    private boolean isRead;

    // Constructor
    public Notification(int notificationId, int userId,
        String message, NotificationType type,
        Timestamp createdAt, boolean isRead) {

        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    // Getters
    public int getNotificationId() {
        return notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    // Methods
    public void markAsRead() {
        this.isRead = true;
    }

    public void markAsUnread() {
        this.isRead = false;
    }
}