package models;

import db.MongoDB;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.MongoCollection;
import net.vz.mongodb.jackson.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itsiftzis on 8/11/2014.
 */

@MongoCollection(name = "projectFull")
public class ProjectFull {
    @Id
    @ObjectId
    private String id;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    private List<Project> projects;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private ProjectClient projectClient;
    private ProjectName projectName;

    public ProjectClient getProjectClient() {
        return projectClient;
    }

    public void setProjectClient(ProjectClient projectClient) {
        this.projectClient = projectClient;
    }

    public ProjectName getProjectName() {
        return projectName;
    }

    public void setProjectName(ProjectName projectName) {
        this.projectName = projectName;
    }

    public ProjectComponent getProjectComponent() {
        return projectComponent;
    }

    public void setProjectComponent(ProjectComponent projectComponent) {
        this.projectComponent = projectComponent;
    }

    private ProjectComponent projectComponent;

    public ProjectFull() {}

    private static JacksonDBCollection<ProjectFull, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("ProjectFull"),
            ProjectFull.class, String.class);

    public static List<ProjectFull> all() {
        return ProjectFull.coll.find().toArray();
    }

    public static void create(ProjectFull project) {
        ProjectFull.coll.save(project);
    }

    public static void helperCreate() {
        ProjectComponent projectComponent = new ProjectComponent();
        projectComponent.setComponent("project component");

        ProjectName projectName = new ProjectName();
        projectName.setName("project name");

        ProjectClient projectClient = new ProjectClient();
        projectClient.setClient("client");

        ProjectFull pr = new ProjectFull();

        pr.setProjectClient(projectClient);

        ProjectFull.create(pr);
    }

    public static void deleteProjectFull(ProjectFull pr) {
        ProjectFull.coll.remove(pr);
    }

    public static void update(ProjectFull pr) {
        ProjectFull.coll.updateById(pr.id, pr);
    }
}
