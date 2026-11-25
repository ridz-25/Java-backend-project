
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Proposal;
import src.model.ProposalStatus;
import src.model.Project;
import src.model.ProjectStatus;
import src.model.User;
import src.model.Seller;
import src.model.Buyer;
import src.util.ValidationUtil;

public class ProposalService {

    private ArrayList<Proposal> proposals = new ArrayList<>();
    private int nextProposalId = 1;

    private ProjectService projectService;
    private UserService userService;

    public ProposalService(
            ProjectService projectService,
            UserService userService) {

        if (projectService == null || userService == null) {
            throw new IllegalArgumentException(
                "ProjectService and UserService are required."
            );
        }

        this.projectService = projectService;
        this.userService = userService;
    }

    // Validate active freelancer
    private boolean isValidFreelancer(int freelancerId) {

        User user = userService.getUserById(freelancerId);

        return user instanceof Seller && user.isActive();
    }

    // Submit a proposal
    public Proposal submitProposal(
            int projectId,
            int freelancerId,
            String coverLetter,
            double proposedBudget,
            int estimatedDays) {

        Project project = projectService.getProjectById(projectId);

        if (project == null || project.getStatus() != ProjectStatus.OPEN) {
            throw new IllegalArgumentException(
                "Project does not exist or is not open."
            );
        }

        if (!isValidFreelancer(freelancerId)) {
            throw new IllegalArgumentException(
                "Invalid or inactive freelancer."
            );
        }

        if (ValidationUtil.isEmpty(coverLetter)) {
            throw new IllegalArgumentException(
                "Cover letter is required."
            );
        }

        if (proposedBudget <= 0 || !Double.isFinite(proposedBudget)) {
            throw new IllegalArgumentException(
                "Proposed budget must be greater than zero."
            );
        }

        if (estimatedDays <= 0) {
            throw new IllegalArgumentException(
                "Estimated days must be greater than zero."
            );
        }

        // Prevent duplicate pending proposals
        for (Proposal existing : proposals) {

            if (existing.getProjectId() == projectId
                    && existing.getFreelancerId() == freelancerId
                    && existing.isPending()) {

                throw new IllegalArgumentException(
                    "Freelancer already has a pending proposal for this project."
                );
            }
        }

        Proposal proposal = new Proposal(
            nextProposalId++,
            projectId,
            freelancerId,
            coverLetter.trim(),
            proposedBudget,
            estimatedDays,
            ProposalStatus.PENDING,
            new Timestamp(System.currentTimeMillis())
        );

        proposals.add(proposal);

        return proposal;
    }

    // Get proposal by ID
    public Proposal getProposalById(int proposalId) {

        for (Proposal proposal : proposals) {

            if (proposal.getProposalId() == proposalId) {
                return proposal;
            }
        }

        return null;
    }

    // Get all proposals
    public List<Proposal> getAllProposals() {
        return new ArrayList<>(proposals);
    }

    // Get proposals for a project
    public List<Proposal> getProposalsByProject(int projectId) {

        List<Proposal> result = new ArrayList<>();

        for (Proposal proposal : proposals) {

            if (proposal.getProjectId() == projectId) {
                result.add(proposal);
            }
        }

        return result;
    }

    // Get proposals submitted by a freelancer
    public List<Proposal> getProposalsByFreelancer(int freelancerId) {

        List<Proposal> result = new ArrayList<>();

        for (Proposal proposal : proposals) {

            if (proposal.getFreelancerId() == freelancerId) {
                result.add(proposal);
            }
        }

        return result;
    }

    // Get pending proposals for a project
    public List<Proposal> getPendingProposalsByProject(int projectId) {

        List<Proposal> result = new ArrayList<>();

        for (Proposal proposal : proposals) {

            if (proposal.getProjectId() == projectId
                    && proposal.isPending()) {

                result.add(proposal);
            }
        }

        return result;
    }

    // Accept a proposal (only project owner)
    public boolean acceptProposal(int proposalId, int clientId) {

        Proposal proposal = getProposalById(proposalId);

        if (proposal == null || !proposal.isPending()) {
            return false;
        }

        Project project = projectService.getProjectById(
            proposal.getProjectId()
        );

        if (project == null
                || project.getClientId() != clientId
                || project.getStatus() != ProjectStatus.OPEN) {

            return false;
        }

        User client = userService.getUserById(clientId);

        if (!(client instanceof Buyer) || !client.isActive()) {
            return false;
        }

        // Accept selected proposal
        proposal.acceptProposal();

        // Reject other pending proposals for the same project
        for (Proposal other : proposals) {

            if (other.getProjectId() == proposal.getProjectId()
                    && other.getProposalId() != proposalId
                    && other.isPending()) {

                other.rejectProposal();
            }
        }

        // Move project to IN_PROGRESS
        projectService.updateProjectStatus(
            project.getProjectId(),
            ProjectStatus.IN_PROGRESS
        );

        return true;
    }

    // Reject a proposal (only project owner)
    public boolean rejectProposal(int proposalId, int clientId) {

        Proposal proposal = getProposalById(proposalId);

        if (proposal == null || !proposal.isPending()) {
            return false;
        }

        Project project = projectService.getProjectById(
            proposal.getProjectId()
        );

        if (project == null
                || project.getClientId() != clientId
                || project.getStatus() != ProjectStatus.OPEN) {

            return false;
        }

        User client = userService.getUserById(clientId);

        if (!(client instanceof Buyer) || !client.isActive()) {
            return false;
        }

        proposal.rejectProposal();

        return true;
    }

    // Withdraw a proposal (only freelancer)
    public boolean withdrawProposal(
            int proposalId,
            int freelancerId) {

        Proposal proposal = getProposalById(proposalId);

        if (proposal == null
                || proposal.getFreelancerId() != freelancerId
                || !proposal.isPending()) {

            return false;
        }

        if (!isValidFreelancer(freelancerId)) {
            return false;
        }

        proposal.withdrawProposal();

        return true;
    }

    // Count proposals
    public int getTotalProposals() {
        return proposals.size();
    }

    public int getPendingProposalCount() {

        int count = 0;

        for (Proposal proposal : proposals) {

            if (proposal.isPending()) {
                count++;
            }
        }

        return count;
    }
}
