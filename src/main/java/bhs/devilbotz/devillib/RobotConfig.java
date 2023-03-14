package bhs.devilbotz.devillib;

import bhs.devilbotz.devillib.data.YAMLSection;
import edu.wpi.first.wpilibj.Filesystem;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class reads the robot configuration from a file and stores it in a static variable.
 * Reads the YML files in the robot deploy/robotconfig directory by default if no path is specified.
 * The file name is the robot's MAC address.
 * If the robot's MAC address is not found, it will use the default robot's config file.
 */
public class RobotConfig {
    private final Yaml yaml = new Yaml();
    private final Path robotConfigPath;
    private final String robotUniqueIdDefault;
    private String robotUniqueId;

    private final ArrayList<YAMLSection> sections = new ArrayList<>();
    private Map<String, Object> data;

    // Setup
    public RobotConfig(Path robotConfigPath, String robotUniqueIdDefault) {
        this.robotConfigPath = robotConfigPath;
        this.robotUniqueIdDefault = robotUniqueIdDefault;

        try {
            loadYaml(getMacAddress());
        } catch (Exception e) {
            loadYaml(robotUniqueIdDefault);
        }
    }

    public RobotConfig(String robotUniqueIdDefault) {
        this.robotConfigPath = Filesystem.getDeployDirectory().toPath().resolve("robotconfig");
        this.robotUniqueIdDefault = robotUniqueIdDefault;

        try {
            loadYaml(getMacAddress());
        } catch (Exception e) {
            loadYaml(robotUniqueIdDefault);
        }
    }

    public String getMacAddress() {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (byte m : mac) {
                sb.append(String.format("%02X", m).replace("-", ""));
            }
            if (sb.length() == 0 || sb.toString().equals("000000000000") || sb.toString().equals("")) {
                return robotUniqueIdDefault;
            } else {
                return sb.toString();
            }
        } catch (SocketException | UnknownHostException e) {
            return robotUniqueIdDefault;
        }
    }

    private void loadYaml(String robotUniqueId) {
        try {
            data = (Map<String, Object>) yaml.load(
                    new FileInputStream(robotConfigPath.resolve(robotUniqueId + ".yml").toFile()));
            for (String key : data.keySet()) {
                sections.add(new YAMLSection(key, (Map<String, Object>) data.get(key)));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Robot config file not found: " + robotConfigPath.resolve(robotUniqueId + ".yml").toFile());
        }
    }

    public String getRobotUniqueId() {
        return robotUniqueId;
    }

    public String getRobotUniqueIdDefault() {
        return robotUniqueIdDefault;
    }

    public Path getRobotConfigPath() {
        return robotConfigPath;
    }

    // Add a section
    public void addSection(YAMLSection section) {
        sections.add(section);
    }

    // Get a section
    public YAMLSection getSection(String name) {
        for (YAMLSection section : sections) {
            if (section.getName().equals(name)) {
                return section;
            }
        }
        return null;
    }

    // Get all sections
    public List<YAMLSection> getSections() {
        return sections;
    }

    // Remove a section
    public void removeSection(String name) {
        for (YAMLSection section : sections) {
            if (section.getName().equals(name)) {
                sections.remove(section);
                return;
            }
        }
    }
}
