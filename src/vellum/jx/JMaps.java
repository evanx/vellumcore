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

    public static JMap mapValue(String key, Object value) {
        JMap map = new JMap();
        map.add(key, value);
        return map;
    }

    public static JMap map(String key, Iterable<? extends JMapped> iterable) {
        JMap map = new JMap();
        map.add(key, list(iterable));
        return map;
    }
        
    public static JEntry entryValue(String key, Object value) {
        return new JEntry(key, value);
    }    

    public static JEntry entry(String key, Iterable<? extends JMapped> iterable) {
        return entryValue(key, list(iterable));
    }
    
    public static Collection<JMap> list(Iterable<? extends JMapped> iterable) {
        List<JMap> list = new LinkedList();
        for (JMapped mapped : iterable) {
            list.add(mapped.getMap());
        }
        logger.info("list mapped {}", list.size());
        return list;
    }

    public static JMap map(Properties properties) {
        JMap map = new JMap();
        for (Map.Entry entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue());
        }
        return map;
    }

    public static JMap map(JEntry... entries) {
        JMap map = new JMap();
        for (JEntry entry : entries) {
            map.put(entry.getKey(), entry.getValue());
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
}
