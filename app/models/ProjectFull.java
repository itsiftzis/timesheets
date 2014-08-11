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
    private List<ProjectClient> proejctClients;

    public List<ProjectClient> getProejctClients() {
        return proejctClients;
    }

    public void setProejctClients(List<ProjectClient> proejctClients) {
        this.proejctClients = proejctClients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        projectComponent.setComponent("component");
        List<ProjectComponent> projectComponents = new ArrayList<ProjectComponent>();
        projectComponents.add(projectComponent);

        List<ProjectName> projectNames = new ArrayList<ProjectName>();
        ProjectName projectName = new ProjectName();
        projectName.setProjectComponents(projectComponents);
        projectNames.add(projectName);

        ProjectClient projectClient = new ProjectClient();
        projectClient.setClient("client");
        projectClient.setProjectNames(projectNames);

        List<ProjectClient> projectClients = new ArrayList<ProjectClient>();
        projectClients.add(projectClient);

        ProjectFull pr = new ProjectFull();
        pr.setProejctClients(projectClients);

        ProjectFull.create(pr);
    }

    public static void deleteProjectFull(ProjectFull pr) {
        ProjectFull.coll.remove(pr);
    }

    public static void update(ProjectFull pr) {
        ProjectFull.coll.updateById(pr.id, pr);
    }
}
