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
    private List<ProjectName> projectNames;
    private ProjectFull projectFull;
}
