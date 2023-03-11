package bhs.devilbotz.DevilLib;

import edu.wpi.first.wpilibj.Filesystem;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Path;

/**
 * This class reads the robot configuration from a file and stores it in a static variable.
 * Reads the YML files in the robot deploy/robotconfig directory by default if no path is specified.
 * The file name is the robot's MAC address.
 * If the robot's MAC address is not found, it will use the default robot's config file.
 */
public class RobotConfig {
    Path robotConfigPath;
    String robotUniqueIdDefault;
    String robotUniqueId;


    // Setup
    public RobotConfig(Path robotConfigPath, String robotUniqueIdDefault) {
        this.robotConfigPath = robotConfigPath;
        this.robotUniqueIdDefault = robotUniqueIdDefault;
        System.out.println("RobotConfig: Using robot config path: " + robotConfigPath);
    }

    public RobotConfig(String robotUniqueIdDefault) {
        this.robotConfigPath = Filesystem.getDeployDirectory().toPath().resolve("robotconfig");
        this.robotUniqueIdDefault = robotUniqueIdDefault;
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
}
