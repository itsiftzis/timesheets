package db;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import play.Configuration;
import play.Logger;

import java.net.UnknownHostException;

/**
 * Created by giannis on 8/2/14.
 */
public class MongoDB {

    private static Mongo mongo;
    public static DB db;

    private static Configuration configuration = play.Play.application().configuration();

    static {
        try {
            String host = configuration.getString("mongodb.server.host");
            String port = configuration.getString("mongodb.server.port");
            String dbname = configuration.getString("mongodb.database");
            mongo = new Mongo( new DBAddress(host, port));
            db = mongo.getDB( dbname );
            Logger.info("mongodb connection " + host + ":" + port + " db->" + dbname);
        } catch (UnknownHostException e) {
            Logger.error("Error connection to MONGO", e);
        }
    }
}
