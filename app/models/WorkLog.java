package models;

import com.mongodb.*;
import db.MongoDB;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import play.Logger;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.net.UnknownHostException;
import java.util.*;

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
    @DateTimeFormat(pattern = "dd.MM.yy hh:mm:ss")
    @Temporal(value= TemporalType.TIMESTAMP)
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
        return dateLog;
    }

    public void setDateLog(Date dateLog) {
        this.dateLog = dateLog;
    }

    public static List<WorkLog> all() {
        return WorkLog.coll.find().sort(new BasicDBObject("dateLog",-1)).toArray();
    }

    public static List<WorkLog> worklogPerUser(String userName, int count) {
        if (count == -1)
            return WorkLog.coll.find().is("userName", userName).sort(new BasicDBObject("dateLog",-1)).toArray();
        else
            return WorkLog.coll.find().is("userName", userName).sort(new BasicDBObject("dateLog",-1)).limit(count).toArray();
    }

    public static List<WorkLog> recentWorklogs(int count) {
        if (count == -1)
            return WorkLog.coll.find().sort(new BasicDBObject("dateLog",-1)).toArray();
        else
            return WorkLog.coll.find().sort(new BasicDBObject("dateLog",-1)).limit(count).toArray();
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

    public static WorkLog fetchWorklog(String id)    {
        return WorkLog.coll.findOneById(id);
    }

    public static void update(WorkLog worklog) {
        WorkLog.coll.updateById(worklog.id, worklog);
    }

    public static List<WorkLog> getReport(Date startDate, Date endDate, String user) {
        if (user!="all")
            return WorkLog.coll.find(DBQuery.greaterThan("dateLog", startDate).lessThan("dateLog", endDate)).
                    is("userName", user).toArray();
        else
            return WorkLog.coll.find(DBQuery.greaterThan("dateLog", startDate).lessThan("dateLog", endDate)).toArray();
    }

    public static List<WorkLog> getWorkLogsPerMonth(String user, Date startDate, Date endDate) {
        return WorkLog.coll.find(DBQuery.greaterThan("dateLog", startDate).lessThan("dateLog", endDate)).
                is("userName", user).sort(new BasicDBObject("dateLog",-1)).toArray();
    }

    public static List<WorkLog> getWorkLogs(String user) {
        return WorkLog.coll.find(DBQuery.is("userName", user)).sort(new BasicDBObject("dateLog",-1)).toArray();
    }

    public static List<WorkLog> fetchMissingHourWlogs(String userName) {
        List<WorkLog> wl = WorkLog.coll.find(DBQuery.is("userName", userName)).
                sort(new BasicDBObject("dateLog",-1)).toArray();
        List<WorkLog> filteredWl = new ArrayList<WorkLog>();
        for (WorkLog wlog:wl) {
            double totalDailyHours = 0;
            for (Project pr:wlog.getProjects()) {
                totalDailyHours += pr.getHours();
            }
            if (totalDailyHours < 8)
                filteredWl.add(wlog);
        }
        return filteredWl;
    }

    public static List<WorkLog> fetchUserFrequentWlogs(String userName, int count) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        DB db = mongoClient.getDB( "mydb" );
        DBCollection coll = db.getCollection("worklog");

        /*db.eval("db.worklog.aggregate(\n" +
                "     [\n" +
                "     { $unwind: \"$projects\"  },\n" +
                "     { $group: { _id: \"$projects.client\", total: { $sum: \"$projects.client\" }, count{$sum: 1}}},\n" +
                "     { $sort : {count: -1}  }\n" +
                "     ]\n" +
                "     )");
*/
        DBObject match = new BasicDBObject("$match", new BasicDBObject("userName", userName));

        DBObject fields = new BasicDBObject("worklog", 4);
        fields.put("projects.component", 2);
        fields.put("projects.client", 1);
        fields.put("projects.name", 3);
        fields.put("_id", 0);
        DBObject project = new BasicDBObject("$project", fields );

        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("projects.client", "$projects.client");
        dbObjIdMap.put("projects.component", "$projects.component");
        dbObjIdMap.put("projects.name", "$projects.name");

        DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject(dbObjIdMap));
        groupFields.put("count", new BasicDBObject( "$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("count", -1));
        DBObject limit = new BasicDBObject("$limit", count);

        List<DBObject> myList = new ArrayList<DBObject>();
        if (!userName.equals("all"))
            myList.add(match);
        myList.add(project);

        List<DBObject> pipeline = Arrays.asList(match, project, group, sort, limit);
        AggregationOutput output = coll.aggregate(pipeline);
        Iterator iter = output.results().iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        return null;
    }

    public static List<WorkLog> frequentWorklogs(Integer count, String userName) {
        return fetchUserFrequentWlogs(userName, count);
    }

    /**
     * db.worklog.aggregate(
     [
     { $unwind: "$projects"  },
     { $group: { _id: "$projects.client", total: { $sum: "$projects.client" }, count{$sum: 1}}},
     { $sort : {count: -1}  }
     ]
     )
     */
}
