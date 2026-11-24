package src.model;

import java.sql.Timestamp;

public class Proposal {

    private int proposalId;
    private int projectId;
    private int freelancerId;
    private String coverLetter;
    private double proposedBudget;
    private int estimatedDays;
    private ProposalStatus status;
    private Timestamp submittedAt;

    public Proposal(int proposalId, int projectId, int freelancerId,
        String coverLetter, double proposedBudget, int estimatedDays,
        ProposalStatus status, Timestamp submittedAt) {

        this.proposalId = proposalId;
        this.projectId = projectId;
        this.freelancerId = freelancerId;
        this.coverLetter = coverLetter;
        this.proposedBudget = proposedBudget;
        this.estimatedDays = estimatedDays;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    // Getters
    public int getProposalId() { return proposalId; }
    public int getProjectId() { return projectId; }
    public int getFreelancerId() { return freelancerId; }
    public String getCoverLetter() { return coverLetter; }
    public double getProposedBudget() { return proposedBudget; }
    public int getEstimatedDays() { return estimatedDays; }
    public ProposalStatus getStatus() { return status; }
    public Timestamp getSubmittedAt() { return submittedAt; }

    // Setters
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public void setProposedBudget(double proposedBudget) {
        this.proposedBudget = proposedBudget;
    }

    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    // Proposal operations
    public void acceptProposal() {
        this.status = ProposalStatus.ACCEPTED;
    }

    public void rejectProposal() {
        this.status = ProposalStatus.REJECTED;
    }

    public void withdrawProposal() {
        this.status = ProposalStatus.WITHDRAWN;
    }

    public boolean isPending() {
        return this.status == ProposalStatus.PENDING;
    }
}