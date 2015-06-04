package db;

import com.mongodb.*;
import play.Configuration;
import play.Logger;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by giannis on 8/2/14.
 */
public class MongoDB {

    private static Mongo mongo;
    private static MongoClient mongoClient;
    public static DB db;

    private static Configuration configuration = play.Play.application().configuration();

    static {
        try {
            String host = configuration.getString("mongodb.server.host");
            String port = configuration.getString("mongodb.server.port");
            String dbname = configuration.getString("mongodb.database");
            String user = configuration.getString("mongodb.user");
            String password = configuration.getString("mongodb.password");
            MongoCredential credential = MongoCredential.createCredential(user, dbname, password.toCharArray());

            mongoClient = new MongoClient(new ServerAddress(host + ":" + port), Arrays.asList(credential));

            //mongo = new Mongo( new DBAddress(host, port));
            db = mongoClient.getDB( dbname );
            Logger.info("mongodb connection " + host + ":" + port + " db->" + dbname);
        } catch (UnknownHostException e) {
            Logger.error("Error connection to MONGO", e);
        }
    }
}
