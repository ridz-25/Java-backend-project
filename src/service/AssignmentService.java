
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Assignment;
import src.model.AssignmentStatus;
import src.model.Proposal;
import src.model.ProposalStatus;
import src.model.Project;
import src.model.User;
import src.model.Buyer;
import src.model.Seller;

public class AssignmentService {

    private ArrayList<Assignment> assignments = new ArrayList<>();
    private int nextAssignmentId = 1;

    private ProposalService proposalService;
    private ProjectService projectService;
    private UserService userService;

    public AssignmentService(
            ProposalService proposalService,
            ProjectService projectService,
            UserService userService) {

        if (proposalService == null ||
                projectService == null ||
                userService == null) {

            throw new IllegalArgumentException(
                "All services are required."
            );
        }

        this.proposalService = proposalService;
        this.projectService = projectService;
        this.userService = userService;
    }

    // Create assignment from an accepted proposal
    public Assignment createFromAcceptedProposal(int proposalId) {

        Proposal proposal =
            proposalService.getProposalById(proposalId);

        if (proposal == null ||
                proposal.getStatus() != ProposalStatus.ACCEPTED) {

            throw new IllegalArgumentException(
                "Only accepted proposals can create assignments."
            );
        }

        // Prevent duplicate assignments
        for (Assignment existing : assignments) {

            if (existing.getProposalId() == proposalId) {
                throw new IllegalArgumentException(
                    "Assignment already exists for this proposal."
                );
            }
        }

        Project project =
            projectService.getProjectById(proposal.getProjectId());

        if (project == null) {
            throw new IllegalArgumentException(
                "Project not found."
            );
        }

        User client = userService.getUserById(project.getClientId());

        User freelancer =
            userService.getUserById(proposal.getFreelancerId());

        if (!(client instanceof Buyer) || !client.isActive()) {
            throw new IllegalArgumentException(
                "Invalid or inactive client."
            );
        }

        if (!(freelancer instanceof Seller) || !freelancer.isActive()) {
            throw new IllegalArgumentException(
                "Invalid or inactive freelancer."
            );
        }

        Timestamp currentTime =
            new Timestamp(System.currentTimeMillis());

        Assignment assignment = new Assignment(
            nextAssignmentId++,
            project.getProjectId(),
            proposal.getProposalId(),
            project.getClientId(),
            proposal.getFreelancerId(),
            proposal.getProposedBudget(),
            currentTime,
            project.getDeadline(),
            null,
            AssignmentStatus.NOT_STARTED
        );

        assignments.add(assignment);

        return assignment;
    }

    // Get assignment by ID
    public Assignment getAssignmentById(int assignmentId) {

        for (Assignment assignment : assignments) {

            if (assignment.getAssignmentId() == assignmentId) {
                return assignment;
            }
        }

        return null;
    }

    // Get all assignments
    public List<Assignment> getAllAssignments() {
        return new ArrayList<>(assignments);
    }

    // Get assignments by project
    public List<Assignment> getAssignmentsByProject(int projectId) {

        List<Assignment> result = new ArrayList<>();

        for (Assignment assignment : assignments) {

            if (assignment.getProjectId() == projectId) {
                result.add(assignment);
            }
        }

        return result;
    }

    // Get assignments by client
    public List<Assignment> getAssignmentsByClient(int clientId) {

        List<Assignment> result = new ArrayList<>();

        for (Assignment assignment : assignments) {

            if (assignment.getClientId() == clientId) {
                result.add(assignment);
            }
        }

        return result;
    }

    // Get assignments by freelancer
    public List<Assignment> getAssignmentsByFreelancer(
            int freelancerId) {

        List<Assignment> result = new ArrayList<>();

        for (Assignment assignment : assignments) {

            if (assignment.getFreelancerId() == freelancerId) {
                result.add(assignment);
            }
        }

        return result;
    }

    // Update assignment status
    public boolean updateStatus(
            int assignmentId,
            AssignmentStatus newStatus) {

        Assignment assignment =
            getAssignmentById(assignmentId);

        if (assignment == null || newStatus == null) {
            return false;
        }

        AssignmentStatus currentStatus = assignment.getStatus();

        if (currentStatus == AssignmentStatus.COMPLETED ||
                currentStatus == AssignmentStatus.CANCELLED) {
            return false;
        }

        if (newStatus == AssignmentStatus.IN_PROGRESS) {

            if (currentStatus != AssignmentStatus.NOT_STARTED) {
                return false;
            }

            assignment.startAssignment();
            return true;
        }

        if (newStatus == AssignmentStatus.COMPLETED) {

            if (currentStatus != AssignmentStatus.IN_PROGRESS) {
                return false;
            }

            assignment.completeAssignment();
            return true;
        }

        if (newStatus == AssignmentStatus.CANCELLED) {

            assignment.cancelAssignment();
            return true;
        }

        return false;
    }

    // Get total assignments
    public int getTotalAssignments() {
        return assignments.size();
    }

    // Get assignments by status
    public int getAssignmentCountByStatus(
            AssignmentStatus status) {

        if (status == null) {
            return 0;
        }

        int count = 0;

        for (Assignment assignment : assignments) {

            if (assignment.getStatus() == status) {
                count++;
            }
        }

        return count;
    }

    // Get completed assignments
    public int getCompletedAssignments() {
        return getAssignmentCountByStatus(
            AssignmentStatus.COMPLETED
        );
    }

    // Get active assignments
    public int getActiveAssignments() {

        return getAssignmentCountByStatus(
            AssignmentStatus.IN_PROGRESS
        );
    }
}
