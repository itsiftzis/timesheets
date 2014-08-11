package models;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.MongoCollection;
import net.vz.mongodb.jackson.ObjectId;

import java.util.List;

/**
 * Created by itsiftzis on 8/11/2014.
 */

@MongoCollection(name = "projectClient")
public class ProjectClient {

    @Id
    @ObjectId
    private String id;
    private String client;

    public String getClient() {

        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<ProjectName> getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(List<ProjectName> projectNames) {
        this.projectNames = projectNames;
    }

    private List<ProjectName> projectNames;
}
