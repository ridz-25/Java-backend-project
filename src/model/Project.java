package src.model;

import java.sql.Timestamp;

public class Project {

    private int projectId;
    private int clientId;
    private String title;
    private String description;
    private double budget;
    private Timestamp deadline;
    private ProjectStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Project(int projectId, int clientId, String title,
        String description, double budget, Timestamp deadline,
        ProjectStatus status, Timestamp createdAt,
        Timestamp updatedAt) {

        this.projectId = projectId;
        this.clientId = clientId;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.deadline = deadline;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getProjectId() { return projectId; }
    public int getClientId() { return clientId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getBudget() { return budget; }
    public Timestamp getDeadline() { return deadline; }
    public ProjectStatus getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }

    // Setters
    public void setTitle(String title) {
        this.title = title;
        updateTimestamp();
    }

    public void setDescription(String description) {
        this.description = description;
        updateTimestamp();
    }

    public void setBudget(double budget) {
        this.budget = budget;
        updateTimestamp();
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
        updateTimestamp();
    }

    // Project operations
    public void updateStatus(ProjectStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public void cancelProject() {
        this.status = ProjectStatus.CANCELLED;
        updateTimestamp();
    }

    public void completeProject() {
        this.status = ProjectStatus.COMPLETED;
        updateTimestamp();
    }

    public boolean isOpen() {
        return this.status == ProjectStatus.OPEN;
    }

    private void updateTimestamp() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}