package models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by itsiftzis on 8/6/2014.
 */
public class CustomSerialized2 extends JsonSerializer<WorkLog> {
    @Override
    public void serialize(WorkLog value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:SS");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }
}
