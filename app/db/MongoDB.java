package db;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import java.net.UnknownHostException;

/**
 * Created by giannis on 8/2/14.
 */
public class MongoDB {

    private static Mongo mongo;
    public static DB db;

    static {
        try {
            mongo = new Mongo( new DBAddress("localhost", "27017"));
            db = mongo.getDB( "mydb" );
            //dbCollection = db.getCollection("test");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
