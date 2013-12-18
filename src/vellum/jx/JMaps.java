/*
 */
package vellum.jx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author evan.summers
 */
public class JMaps {
    
    public static Object parse(JsonElement element) {
        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            List list = new ArrayList();
            for (int i = 0; i < array.size(); i++) {
                JsonElement arrayElement = array.get(i);
                list.add(parse(arrayElement));
            }
            return list;
        } else if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            return parse(object);
        } else if (element.isJsonNull()) {
            return null;
        } else if (element.isJsonPrimitive()) {
            String string = element.toString();
            if (string.equals("true")) {
                return element.getAsBoolean();
            } else if (string.equals("false")) {
                return element.getAsBoolean();
            } else if (string.startsWith("\"")) {
                return string.substring(1, string.length() - 1);
            } else if (string.contains(".")) {
                return element.getAsDouble();
            } else if (string.matches("[0-9]*")) {
                return element.getAsLong();
            }
            return element.getAsString();
        }
        return element.toString();
    }

    public static JMap parse(JsonObject object) {
        JMap map = new JMap();
        for (Entry<String, JsonElement> entry : object.entrySet()) {
            map.put(entry.getKey(), parse(entry.getValue()));
        }
        return map;
    }
    
    public static JMap parse(String json) {
        return parse(new JsonParser().parse(json).getAsJsonObject());
    }

    public static JMap create(String key, Object value) {
        JMap map = new JMap();
        map.add(key, value);
        return map;
    }

    public static JMap createMap(String key, Iterable<? extends JMapped> iterable) {
        JMap map = new JMap();
        map.add(key, map(iterable));
        return map;
    }
    
    public static Collection<JMap> map(Iterable<? extends JMapped> iterable) {
        List<JMap> list = new LinkedList();
        for (JMapped mapped : iterable) {
            list.add(mapped.getMap());
        }
        return list;
    }
    
    public static JEntry entry(String key, Object value) {
        return new JEntry(key, value);
    }    

}
