package models;

import db.MongoDB;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.MongoCollection;
import net.vz.mongodb.jackson.ObjectId;

import java.util.List;

/**
 * Created by itsiftzis on 8/11/2014.
 */
public class ProjectComponent {

    @Id
    @ObjectId
    private String id;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    private String component;

    private static JacksonDBCollection<ProjectComponent, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("projectComponent"),
            ProjectComponent.class, String.class);

    public static void create(ProjectComponent component) {
        ProjectComponent.coll.save(component);
    }

    public static void deleteWorklog(ProjectComponent component) {
        ProjectComponent.coll.remove(component);
    }

    public static void update(ProjectComponent component) {
        ProjectComponent.coll.updateById(component.id, component);
    }

    public static List<ProjectComponent> all() {
        return ProjectComponent.coll.find().toArray();
    }
}

