package models;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.MongoCollection;
import net.vz.mongodb.jackson.ObjectId;

/**
 * Created by itsiftzis on 8/11/2014.
 */
@MongoCollection(name = "projectComponent")
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

}
