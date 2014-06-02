
package vellum.jx;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evanx
 */
public class JsonTest {
    Logger logger = LoggerFactory.getLogger(JsonTest.class);
    
    @BeforeClass
    public static void setUpClass() {
        BasicConfigurator.configure();
    }
    
    @Test 
    public void loadJson() throws Exception {
        JMap map = new JMap();
        String text = "\"Hello,\" he said.";
        map.put("text", text);
        map = JMaps.parseMap(map.toJson());
        map = JMaps.parseMap(map.toJson());
        Assert.assertEquals(text, map.get("text"));
    }    
        
}
