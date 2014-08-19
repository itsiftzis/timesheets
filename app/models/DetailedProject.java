package models;

import db.MongoDB;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;

import java.util.List;

/**
 * Created by itsiftzis on 8/19/2014.
 */
public class DetailedProject {

    @Id
    @ObjectId
    private String id;

    public List<ProjectFull> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectFull> projects) {
        this.projects = projects;
    }

    private List<ProjectFull> projects;

    public DetailedProject() {

    }

    private static JacksonDBCollection<DetailedProject, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("detailedproject"),
            DetailedProject.class, String.class);



}


