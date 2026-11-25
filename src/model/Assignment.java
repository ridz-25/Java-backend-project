package src.model;

import java.sql.Timestamp;

public class Assignment {

    private int assignmentId;
    private int projectId;
    private int proposalId;
    private int clientId;
    private int freelancerId;
    private double agreedAmount;
    private Timestamp startDate;
    private Timestamp deadline;
    private Timestamp completionDate;
    private AssignmentStatus status;

    public Assignment(int assignmentId, int projectId, int proposalId,
        int clientId, int freelancerId, double agreedAmount,
        Timestamp startDate, Timestamp deadline,
        Timestamp completionDate, AssignmentStatus status) {

        this.assignmentId = assignmentId;
        this.projectId = projectId;
        this.proposalId = proposalId;
        this.clientId = clientId;
        this.freelancerId = freelancerId;
        this.agreedAmount = agreedAmount;
        this.startDate = startDate;
        this.deadline = deadline;
        this.completionDate = completionDate;
        this.status = status;
    }

    // Getters
    public int getAssignmentId() { return assignmentId; }
    public int getProjectId() { return projectId; }
    public int getProposalId() { return proposalId; }
    public int getClientId() { return clientId; }
    public int getFreelancerId() { return freelancerId; }
    public double getAgreedAmount() { return agreedAmount; }
    public Timestamp getStartDate() { return startDate; }
    public Timestamp getDeadline() { return deadline; }
    public Timestamp getCompletionDate() { return completionDate; }
    public AssignmentStatus getStatus() { return status; }

    // Setters
    public void setAgreedAmount(double agreedAmount) {
        this.agreedAmount = agreedAmount;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    // Assignment operations
    public void startAssignment() {
        if (this.status == AssignmentStatus.NOT_STARTED) {
            this.status = AssignmentStatus.IN_PROGRESS;
        }
    }

    public void completeAssignment() {
        if (this.status == AssignmentStatus.IN_PROGRESS) {
            this.status = AssignmentStatus.COMPLETED;
            this.completionDate =
                new Timestamp(System.currentTimeMillis());
        }
    }

    public void cancelAssignment() {
        if (this.status != AssignmentStatus.COMPLETED) {
            this.status = AssignmentStatus.CANCELLED;
        }
    }

    public boolean isCompleted() {
        return this.status == AssignmentStatus.COMPLETED;
    }
}