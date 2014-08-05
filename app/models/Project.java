package models;

import db.MongoDB;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.MongoCollection;
import net.vz.mongodb.jackson.ObjectId;

import java.util.List;

/**
 * Created by giannis on 8/2/14.
 */
@MongoCollection(name = "project")
public class Project {
    @Id
    @ObjectId
    private String id;
    private String name;
    private String region;
    private int hours;

    public Project() {

    }

    public Project(String name, String region, int hours) {
        this.name = name;
        this.region = region;
        this.hours = hours;
    }

    private static JacksonDBCollection<Project, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("project"),
            Project.class, String.class);

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Project> all() {
        return Project.coll.find().toArray();
    }

    public static void create(Project task) {
        Project.coll.save(task);
    }

    public static void create(String name, String region, int hours){
        create(new Project(name, region, hours));
    }

    public static void delete(String id) {
        Project project = Project.coll.findOneById(id);
        if (project != null)
            Project.coll.remove(project);
    }

    public static void removeAll(){
        Project.coll.drop();
    }


    public static Project projectByName(String projectName) {
        List<Project> projects = all();
        for (Project proj:projects) {
            if (proj.getName().equals(projectName))
                return proj;
        }
        return new Project();
    }
}
