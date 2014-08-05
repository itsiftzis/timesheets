package controllers;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import play.Logger;
import play.mvc.Http;
import play.mvc.Http.Session;

public class SessionManager {

    private static JSONSerializer s = new JSONSerializer();

    public static void addSession(String key, Object value) {

        if(value != null) {
            Session session = Http.Context.current().session();
            session.put(key, s.deepSerialize(value));
        } else {
            Logger.info("Value for " + key + " is null");
        }
    }

    public static <T> T get(String key) {

        Session session = Http.Context.current().session();
        final String value = session.get(key);

        if (value == null) {
            return null;
        }

        return new JSONDeserializer<T>().deserialize(value);
    }

}