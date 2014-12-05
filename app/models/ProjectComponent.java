package models;

import com.mongodb.BasicDBObject;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @ObjectId
    private String id;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String client;
    private String name;

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

    public static void delete(ProjectComponent component) {
        ProjectComponent.coll.remove(component);
    }

    public static void update(ProjectComponent component) {
        ProjectComponent.coll.updateById(component.id, component);
    }

    public static List<ProjectComponent> all() {
        BasicDBObject sort = new BasicDBObject("client", 1);
        sort.append("name", 1);
        sort.append("component", 1);
        return ProjectComponent.coll.find().sort(sort).toArray();
    }

    public static List<ProjectComponent> findByName(String name) {
        return ProjectComponent.coll.find().is("name", name).toArray();
    }

    public static List<ProjectComponent> findByNameClient(String name, String client) {
        return ProjectComponent.coll.find().is("name", name).is("client", client).toArray();
    }
}

