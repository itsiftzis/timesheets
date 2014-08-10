package models;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by itsiftzis on 8/6/2014.
 */
public class CustomSerialized extends JsonSerializer<WorkLog> {
    @Override
    public void serialize(WorkLog value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:SS");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }
}
