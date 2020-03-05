import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ObjectEncoder {
    public String serialize(Object o) {
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return json;
    }

    public Object deserialize(InputStream inputStream, Class<?> classType) {
        Gson gson = new GsonBuilder().create();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return gson.fromJson(inputStreamReader, classType);
    }

}
