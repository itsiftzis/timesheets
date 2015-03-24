package models;

import com.mongodb.*;
import db.MongoDB;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import play.Configuration;
import play.Logger;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by giannis on 8/2/14.
 */
public class WorkLog {
    private static Configuration configuration = play.Play.application().configuration();
    private static Mongo mongo;
    private static DB db;
    private static MongoClient mongoClient;
    static {
        try {
            String host = configuration.getString("mongodb.server.host");
            String port = configuration.getString("mongodb.server.port");
            String dbname = configuration.getString("mongodb.database");
            if (mongoClient == null)
                mongoClient = new MongoClient(host, Integer.parseInt(port));
            if (db == null)
                db = mongoClient.getDB( dbname );
            Logger.info("mongodb connection with java driver " + host + ":" + port + " db->" + dbname);
        } catch (UnknownHostException e) {
            Logger.error("Error connection to MONGO", e);
        }
    }

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
        List<WorkLog> existingWorkLogs = WorkLog.coll.find(DBQuery.lessThanEquals("dateLog", task.getDateLog())).is("userName", task.getUserName()).toArray();
        WorkLog sameday = null;
        for (WorkLog wlog:existingWorkLogs) {
            if(wlog.getDateLog().equals(task.getDateLog())){
                for(Project proj :task.getProjects()){
                    wlog.getProjects().add(proj);
                }
                sameday = wlog;
            }
        }


        if(sameday != null){
            Logger.info("Updating existing worklog with id ====>>>>  " + sameday.id);
            WorkLog.coll.updateById(sameday.id,sameday);
        }else {
            WorkLog.coll.save(task);
        }
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

        DBCollection coll = db.getCollection("worklog");

        DBObject match = new BasicDBObject("$match", new BasicDBObject("userName", userName));

        DBObject fields = new BasicDBObject("compkey", 1);
        fields.put("projects.client", 1);
        fields.put("projects.component", 1);
        fields.put("projects.name", 1);
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
        myList.add(group);
        myList.add(sort);
        myList.add(limit);

        List<DBObject> pipeline = myList;
        AggregationOutput output = coll.aggregate(pipeline);
        Iterator iter = output.results().iterator();
        List<WorkLog> listOfWorkLogs = new ArrayList<WorkLog>();
        WorkLog workLog = new WorkLog();
        List<Project> projectsList = new ArrayList<Project>();
        int projectsCounter = 0;
        while (iter.hasNext()) {
            BasicDBObject dbObject = (BasicDBObject)iter.next();
            System.out.println(dbObject);
            for (Map.Entry<String,Object> entry:dbObject.entrySet()) {
                if (entry.getKey().equals("_id")) {
                    DBObject val = (DBObject)entry.getValue();
                    BasicDBList clientsList = (BasicDBList)val.get("projects.client");
                    BasicDBList namesList = (BasicDBList)val.get("projects.name");
                    BasicDBList componentsList = (BasicDBList)val.get("projects.component");

                    if (clientsList != null && namesList != null && componentsList != null) {
                        if (clientsList.size() == namesList.size() && clientsList.size() == componentsList.size()) {
                            for (int i=0; i<clientsList.size(); i++) {
                                Project project1 = new Project();
                                project1.setClient(clientsList.get(i).toString());
                                project1.setName(namesList.get(i).toString());
                                project1.setComponent(componentsList.get(i).toString());
                                if (projectsCounter < count && !projectsList.contains(project1)) {
                                    projectsList.add(project1);
                                    projectsCounter++;
                                }
                            }
                        }
                    }
                }
            }
        }
        workLog.setProjects(projectsList);
        listOfWorkLogs.add(workLog);
        return listOfWorkLogs;
    }

    private static boolean containsOverride(List<Project> projectsList, Project project1) {
        for (Project pr:projectsList) {
            if (project1.getClient().equals(pr.getClient()) && project1.getName().equals(pr.getName()) && project1.getComponent().equals(pr.getComponent()))
                return true;
        }
        return false;
    }

    public static List<WorkLog> frequentWorklogs(Integer count, String userName) {
        List<WorkLog> wl = new ArrayList<WorkLog>();
        try {
            wl = fetchUserFrequentWlogs(userName, count);
        } catch (UnknownHostException e) {
            Logger.error("Error fetcing frequent worklogs ", e);
            wl.add(new WorkLog());
            return wl;
        }
        if (wl == null) {
            wl = new ArrayList<WorkLog>();
            wl.add(new WorkLog());
            return wl;
        } else
            return wl;
    }

}
