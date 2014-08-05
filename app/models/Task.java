package models;

import java.net.UnknownHostException;
import java.util.*;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import db.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.persistence.*;

public class Task{
    
  @Id
  @ObjectId
  public String id;

  public String label;

  private static DBCollection dbCollection = MongoDB.db.getCollection("task");

  private static JacksonDBCollection<Task, String> coll = JacksonDBCollection.wrap(dbCollection, Task.class,
          String.class);

  public Task(){

  }

  public Task(String label) {
    this.label = label;
  }

  public static List<Task> all() {
    return Task.coll.find().toArray();
  }

  public static void create(Task task) {
    Task.coll.save(task);
  }

  public static void create(String label){
      create(new Task(label));
  }

  public static void delete(String id) {
    Task task = Task.coll.findOneById(id);
    if (task != null)
        Task.coll.remove(task);
  }

  public static void removeAll(){
    Task.coll.drop();
  }

}
