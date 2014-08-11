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
    private List<ProjectComponent> projectComponents;
    private ProjectClient projectClient;

}
