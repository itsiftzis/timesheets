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
public class ProjectName {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @ObjectId
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    private String client;

    private static JacksonDBCollection<ProjectName, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("projectName"),
            ProjectName.class, String.class);

    public static void create(ProjectName component) {
        ProjectName.coll.save(component);
    }

    public static void delete(ProjectName component) {
        ProjectName.coll.remove(component);
    }

    public static void update(ProjectName component) {
        ProjectName.coll.updateById(component.id, component);
    }

    public static List<ProjectName> all() {
        return ProjectName.coll.find().sort(new BasicDBObject("client", 1)).sort(new BasicDBObject("name",1)).toArray();
    }

    public static List<ProjectName> findByClient(String client) {
        return ProjectName.coll.find().is("client", client).toArray();
    }

}
