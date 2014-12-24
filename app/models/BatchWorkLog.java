package models;

import com.mongodb.BasicDBObject;
import db.MongoDB;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Created by giannis on 8/2/14.
 */
public class BatchWorkLog {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @ObjectId
    private String id;
    @DateTimeFormat(pattern = "dd.MM.yy hh:mm:ss")
    @Temporal(value= TemporalType.TIMESTAMP)
    private Date dateLog;

    public Date getDateLogFrom() {
        return dateLogFrom;
    }

    public void setDateLogFrom(Date dateLogFrom) {
        this.dateLogFrom = dateLogFrom;
    }

    public Date getDateLogTo() {
        return dateLogTo;
    }

    public void setDateLogTo(Date dateLogTo) {
        this.dateLogTo = dateLogTo;
    }

    private Date dateLogFrom;
    private Date dateLogTo;
    private int totalHours;
    private List<Project> projects;
    private String userName;

    public BatchWorkLog() {}

    public BatchWorkLog(Date dateLog, int totalHours, List<Project> projects, String userName) {
        this.dateLog = dateLog;
        this.totalHours = totalHours;
        this.projects =  projects;
        this.userName = userName;
    }


    private static JacksonDBCollection<BatchWorkLog, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("worklog"),
            BatchWorkLog.class, String.class);

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
        return dateLog;
    }

    public void setDateLog(Date dateLog) {
        this.dateLog = dateLog;
    }

    public static List<BatchWorkLog> all() {
        return BatchWorkLog.coll.find().sort(new BasicDBObject("dateLog",-1)).toArray();
    }

    public static List<BatchWorkLog> worklogPerUser(String userName) {
        return BatchWorkLog.coll.find().is("userName", userName).sort(new BasicDBObject("dateLog",-1)).toArray();
    }

    public static void create(BatchWorkLog task) {
        BatchWorkLog.coll.save(task);
    }

    public static void create(Date dateLog, int totalHours, List<Project> projects, String user){
        create(new BatchWorkLog(dateLog, totalHours, projects, user));
    }

    public static void delete(String id) {
        BatchWorkLog worklog = BatchWorkLog.coll.findOneById(id);
        if (worklog != null)
            BatchWorkLog.coll.remove(worklog);
    }

    public static void removeAll(){
        BatchWorkLog.coll.drop();
    }

    public static void deleteWorklog(BatchWorkLog worklog) {
        BatchWorkLog.coll.remove(worklog);
    }

    public static BatchWorkLog fetchWorklog(String id)    {
        return BatchWorkLog.coll.findOneById(id);
    }

    public static void update(BatchWorkLog worklog) {
        BatchWorkLog.coll.updateById(worklog.id, worklog);
    }

    public static List<BatchWorkLog> getReport(Date startDate, Date endDate, String user) {
        if (user!="all")
            return BatchWorkLog.coll.find(DBQuery.greaterThan("dateLog", startDate).lessThan("dateLog", endDate)).is("userName", user).toArray();
        else
            return BatchWorkLog.coll.find(DBQuery.greaterThan("dateLog", startDate).lessThan("dateLog", endDate)).toArray();
    }
}
