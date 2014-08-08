package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import db.MongoDB;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.bson.types.BSONTimestamp;
import play.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by giannis on 8/2/14.
 */
public class WorkLog {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @ObjectId
    private String id;
    private Date dateLog;
    private int totalHours;
    private List<Project> projects;
    private String userName;

    public WorkLog() {}

    public WorkLog(Date dateLog, int totalHours, List<Project> projects, String userName) {
        this.dateLog = dateLog;
        this.totalHours = totalHours;
        this.projects =  projects;
        this.userName = userName;
    }


    private static JacksonDBCollection<WorkLog, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("worklog"),
            WorkLog.class, String.class);

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getUserName() {
        return userName;
    }

    public void setUser(String userName) {
        this.userName = userName;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public Date getDateLog() {
        Logger.info("getting date " + dateLog);
        return dateLog;
    }

    public void setDateLog(Date dateLog) {
        this.dateLog = dateLog;
    }

    public static List<WorkLog> all() {
        return WorkLog.coll.find().toArray();
    }

    public static List<WorkLog> worklogPerUser(String userName) {
        return WorkLog.coll.find().is("userName", userName).sort(new BasicDBObject("date",-1)).toArray();
    }

    public static void create(WorkLog task) {
        WorkLog.coll.save(task);
    }

    public static void create(Date dateLog, int totalHours, List<Project> projects, String user){
        create(new WorkLog(dateLog, totalHours, projects, user));
    }

    public static void delete(String id) {
        WorkLog worklog = WorkLog.coll.findOneById(id);
        if (worklog != null)
            WorkLog.coll.remove(worklog);
    }

    public static void removeAll(){
        WorkLog.coll.drop();
    }

    public static void deleteWorklog(WorkLog worklog) {
        WorkLog.coll.remove(worklog);
    }

    public static WorkLog fetchWorklog(String id) {
        return WorkLog.coll.findOneById(id);
    }

    public static void update(WorkLog worklog) {
        WorkLog.coll.updateById(worklog.id, worklog);
    }
}
