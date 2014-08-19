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


    private static JacksonDBCollection<ProjectClient, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("projectClient"),
            ProjectClient.class, String.class);

    public static void create(ProjectClient component) {
        ProjectClient.coll.save(component);
    }

    public static void deleteWorklog(ProjectClient component) {
        ProjectClient.coll.remove(component);
    }

    public static void update(ProjectClient component) {
        ProjectClient.coll.updateById(component.id, component);
    }

    public static List<ProjectClient> all() {
        return ProjectClient.coll.find().toArray();
    }
}
