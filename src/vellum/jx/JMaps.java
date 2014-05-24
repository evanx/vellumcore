/*
 */
package vellum.jx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class JMaps {

    final static Logger logger = LoggerFactory.getLogger(JMaps.class);

    public static Object parse(JsonElement element) {
        if (element.isJsonArray()) {
            return list(element.getAsJsonArray());
        } else if (element.isJsonObject()) {
            return map(element.getAsJsonObject());
        } else if (element.isJsonNull()) {
            return null;
        } else if (element.isJsonPrimitive()) {
            return parsePrimitive(element);
        }
        return element.toString();
    }

    public static Object parsePrimitive(JsonElement element) {
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
        
    public static List list(JsonArray array) {
        List list = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            list.add(parse(array.get(i)));
        }
        return list;
    }

    public static JMap map(JsonObject object) {
        JMap map = new JMap();
        for (Entry<String, JsonElement> entry : object.entrySet()) {
            map.put(entry.getKey(), parse(entry.getValue()));
        }
        return map;
    }
    
    public static List<JMap> listMap(JsonArray array) {
        List list = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            list.add(map(array.get(i).getAsJsonObject()));
        }
        return list;
    }

    public static JsonArray parseJsonArray(String json) throws JsonSyntaxException {
        return new JsonParser().parse(json).getAsJsonArray();
    }

    public static List<JMap> listMap(String json) throws JsonSyntaxException {
        return listMap(parseJsonArray(json));
    }

    public static List list(String json) throws JsonSyntaxException {
        return list(parseJsonArray(json));
    }

    public static JMap parse(String json) throws JsonSyntaxException {
        return map(new JsonParser().parse(json).getAsJsonObject());
    }

    public static JMap mapValue(String key, Object value) {
        JMap map = new JMap();
        map.add(key, value);
        return map;
    }

    public static JEntry entryValue(String key, Object value) {
        return new JEntry(key, value);
    }

    public static JEntry entry(String key, Iterable<? extends JMapped> iterable) {
        return entryValue(key, list(iterable));
    }

    public static JMap map(String key, Iterable<? extends JMapped> iterable) {
        JMap map = new JMap();
        map.add(key, list(iterable));
        return map;
    }

    public static Collection<JMap> list(Iterable<? extends JMapped> iterable) {
        List<JMap> list = new LinkedList();
        for (JMapped mapped : iterable) {
            list.add(mapped.getMap());
        }
        logger.info("list mapped {}", list.size());
        return list;
    }

    public static JMap map(JEntry... entries) {
        JMap map = new JMap();
        for (JEntry entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static JMap map(Properties properties) {
        JMap map = new JMap();
        for (Map.Entry entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue());
        }
        return map;
    }

    public static JMap map(String section, Properties properties) {
        JMap map = new JMap();
        for (Object key : properties.keySet()) {
            String name = key.toString();
            if (name.startsWith(section)) {
                if (name.charAt(section.length()) == '.') {
                    name = name.substring(section.length() + 1);
                    map.put(name, properties.get(key));
                    logger.info("property {} {}", name, properties.get(key));
                }
            }
        }
        return map;
    }

    public static List<JMap> list(JMap map, String key) {
        List<JMap> list = new LinkedList();
        for (Object item : map.listMap(key)) {
            list.add((JMap) item);
        }
        logger.info("listMap {}", list.size());
        return list;
    }
}
