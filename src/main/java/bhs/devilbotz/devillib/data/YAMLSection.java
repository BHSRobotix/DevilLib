package bhs.devilbotz.devillib.data;

import java.util.Map;

public class YAMLSection {
    private final String name;
    private final Map<String, Object> data;

    public YAMLSection(String name, Map<String, Object> data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        return (String) data.get(key);
    }

    public int getInt(String key) {
        return (int) data.get(key);
    }

    public double getDouble(String key) {
        return (double) data.get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) data.get(key);
    }

    public Map<String, Object> getMap(String key) {
        return (Map<String, Object>) data.get(key);
    }

    public Object getObject(String key) {
        return data.get(key);
    }
}
