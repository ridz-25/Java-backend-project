
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Project;
import src.model.ProjectStatus;
import src.model.User;
import src.model.Buyer;
import src.util.ValidationUtil;

public class ProjectService {

    private ArrayList<Project> projects = new ArrayList<>();
    private int nextProjectId = 1;

    private UserService userService;

    public ProjectService(UserService userService) {
        this.userService = userService;
    }

    // Validate client
    private boolean isValidClient(int clientId) {
        User user = userService.getUserById(clientId);
        return user instanceof Buyer && user.isActive();
    }

    // Create project
    public Project createProject(
            int clientId,
            String title,
            String description,
            double budget,
            Timestamp deadline) {

        if (!isValidClient(clientId)) {
            throw new IllegalArgumentException(
                "Invalid or inactive client."
            );
        }

        if (ValidationUtil.isEmpty(title) ||
            ValidationUtil.isEmpty(description)) {
            throw new IllegalArgumentException(
                "Project title and description are required."
            );
        }

        if (budget <= 0 || !Double.isFinite(budget)) {
            throw new IllegalArgumentException(
                "Budget must be greater than zero."
            );
        }

        if (deadline == null ||
            !deadline.after(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException(
                "Project deadline must be in the future."
            );
        }

        Timestamp currentTime =
            new Timestamp(System.currentTimeMillis());

        Project project = new Project(
            nextProjectId++,
            clientId,
            title.trim(),
            description.trim(),
            budget,
            deadline,
            ProjectStatus.OPEN,
            currentTime,
            currentTime
        );

        projects.add(project);
        return project;
    }

    // Get project by ID
    public Project getProjectById(int projectId) {
        for (Project project : projects) {
            if (project.getProjectId() == projectId) {
                return project;
            }
        }
        return null;
    }

    // Get all projects
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects);
    }

    // Get projects by client
    public List<Project> getProjectsByClient(int clientId) {
        ArrayList<Project> result = new ArrayList<>();

        for (Project project : projects) {
            if (project.getClientId() == clientId) {
                result.add(project);
            }
        }

        return result;
    }

    // Get open projects
    public List<Project> getOpenProjects() {
        ArrayList<Project> result = new ArrayList<>();

        for (Project project : projects) {
            if (project.getStatus() == ProjectStatus.OPEN) {
                result.add(project);
            }
        }

        return result;
    }

    // Search projects by title or description
    public List<Project> searchProjects(String keyword) {
        ArrayList<Project> result = new ArrayList<>();

        if (ValidationUtil.isEmpty(keyword)) {
            return result;
        }

        String search = keyword.trim().toLowerCase();

        for (Project project : projects) {
            if (project.getStatus() == ProjectStatus.OPEN &&
                (project.getTitle().toLowerCase().contains(search) ||
                 project.getDescription().toLowerCase().contains(search))) {

                result.add(project);
            }
        }

        return result;
    }

    // Update project details
    public boolean updateProject(
            int projectId,
            int clientId,
            String title,
            String description,
            double budget,
            Timestamp deadline) {

        Project project = getProjectById(projectId);

        if (project == null ||
            project.getClientId() != clientId ||
            !isValidClient(clientId) ||
            project.getStatus() != ProjectStatus.OPEN) {
            return false;
        }

        if (ValidationUtil.isEmpty(title) ||
            ValidationUtil.isEmpty(description) ||
            budget <= 0 || !Double.isFinite(budget) ||
            deadline == null ||
            !deadline.after(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }

        project.setTitle(title.trim());
        project.setDescription(description.trim());
        project.setBudget(budget);
        project.setDeadline(deadline);

        return true;
    }

    // Cancel project
    public boolean cancelProject(int projectId, int clientId) {
        Project project = getProjectById(projectId);

        if (project == null ||
            project.getClientId() != clientId ||
            !isValidClient(clientId) ||
            project.getStatus() == ProjectStatus.COMPLETED ||
            project.getStatus() == ProjectStatus.CANCELLED) {
            return false;
        }

        project.cancelProject();
        return true;
    }

    // Update project status
    public boolean updateProjectStatus(
            int projectId,
            ProjectStatus newStatus) {

        Project project = getProjectById(projectId);

        if (project == null || newStatus == null ||
            project.getStatus() == ProjectStatus.COMPLETED ||
            project.getStatus() == ProjectStatus.CANCELLED ||
            newStatus == ProjectStatus.OPEN ||
            newStatus == ProjectStatus.CANCELLED) {
            return false;
        }

        if (newStatus == ProjectStatus.COMPLETED) {
            project.completeProject();
        } else {
            project.updateStatus(newStatus);
        }

        return true;
    }
}
