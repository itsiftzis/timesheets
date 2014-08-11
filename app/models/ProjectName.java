package models;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.MongoCollection;
import net.vz.mongodb.jackson.ObjectId;

import java.util.List;

/**
 * Created by itsiftzis on 8/11/2014.
 */
@MongoCollection(name = "projectName")
public class ProjectName {

    @Id
    @ObjectId
    private String id;

    public List<ProjectComponent> getProjectComponents() {
        return projectComponents;
    }

    public void setProjectComponents(List<ProjectComponent> projectComponents) {
        this.projectComponents = projectComponents;
    }

    private List<ProjectComponent> projectComponents;

}
