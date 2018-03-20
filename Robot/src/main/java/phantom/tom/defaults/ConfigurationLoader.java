package phantom.tom.defaults;

import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

import java.util.HashMap;
import java.util.Map;

import labs.vex.lumen.firefly.ConfigurationManager;
import labs.vex.lumen.firefly.ILoader;

public class ConfigurationLoader implements ILoader {
    @Override
    public ConfigurationManager load(Map<String, String> map) {
        Map<String, Config> include = new HashMap<>();
        for(Map.Entry<String,String> entry: map.entrySet()) {
            String configurationName = entry.getKey();
            String path = entry.getValue();
            try {
                include.put(configurationName, new Config(SimpleGson.getInstance().fromJson(ServiceEngine.readFromFile("/configuration/" + path), Map.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ConfigurationManager(include);
    }
}
