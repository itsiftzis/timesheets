package models;

import com.mongodb.BasicDBObject;
import db.MongoDB;
import net.vz.mongodb.jackson.*;

import java.util.List;

/**
 * Created by giannis on 8/2/14.
 */

public class User {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @ObjectId
    private String id;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public User() {

    }

    public User(String userName, String  password, String email, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private static JacksonDBCollection<User, String> coll = JacksonDBCollection.wrap(MongoDB.db.getCollection("user"),
            User.class, String.class);

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static List<User> all() {
        return User.coll.find().toArray();
    }

    public static void create(User task) {
        User.coll.save(task);
    }

    public static void update(User user) {
        User.coll.updateById(user.id, user);
    }

    public static void create(String userName, String password, String email, String firstName, String lastName){
        create(new User(userName, password, email, firstName,lastName));
    }

    public static void delete(String id) {
        User user = User.coll.findOneById(id);
        if (user != null)
            User.coll.remove(user);
    }

    public static void removeAll(){
        User.coll.drop();
    }

    public static User userByEmail(String userName) {
        DBCursor<User> cursor = coll.find().is("email", userName);
        User user;
        if (cursor.hasNext()) {
            user = cursor.next();
        } else
            user = new User();
        return user;
    }

    public void deleteUser(User user) {
        User.coll.remove(user);
    }
}
