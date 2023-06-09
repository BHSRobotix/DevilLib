package bhs.devilbotz.devillib.utils.alerts;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.*;
import java.util.function.Predicate;

/**
 * Class for creating alerts that can be displayed on the driver station. Alerts are displayed in
 * NetworkTables, and can be sorted by type. Alerts can be set to active or inactive
 * <p>
 * TODO:
 * - Add a way to set the alert to active for a certain amount of time
 *
 * @author Parker Meyers
 * @author Mechanical-Advantage
 * @see AlertType
 */
public class Alert {
    private static final Map<String, SendableAlerts> groups =
            new HashMap<>();

    private final AlertType type;
    private boolean active = false;
    private double activeStartTime = 0.0;
    private double activeDuration = 0.0;
    private String text;

    /**
     * Creates a new Alert in the default group - "Alerts". If this is the first to be instantiated,
     * the appropriate entries will be added to NetworkTables.
     *
     * @param text Text to be displayed when the alert is active.
     * @param type Alert level specifying urgency.
     */
    public Alert(String text, AlertType type) {
        this("Alerts", text, type, -1);
    }

    /**
     * Creates a new Alert in the default group - "Alerts". If this is the first to be instantiated,
     * the appropriate entries will be added to NetworkTables.
     *
     * @param text Text to be displayed when the alert is active.
     * @param type Alert level specifying urgency.
     */
    public Alert(String text, AlertType type, double activeDuration) {
        this("Alerts", text, type, activeDuration);
    }

    /**
     * Creates a new Alert. If this is the first to be instantiated in its group, the appropriate
     * entries will be added to NetworkTables.
     *
     * @param group          Group identifier, also used as NetworkTables title
     * @param text           Text to be displayed when the alert is active.
     * @param type           Alert level specifying urgency.
     * @param activeDuration Duration in seconds that the alert should be active for.
     */
    public Alert(String group, String text, AlertType type, double activeDuration) {
        if (!groups.containsKey(group)) {
            groups.put(group, new SendableAlerts());
            SmartDashboard.putData(group, groups.get(group));
        }

        this.text = text;
        this.type = type;
        this.activeDuration = activeDuration;

        // Start a thread to automatically deactivate the alert after a certain amount of time
        if (activeDuration > 0) {
            new Thread(() -> {
                try {
                    Thread.sleep((long) (activeDuration * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                set(false);
            }).start();
        }
        groups.get(group).alerts.add(this);
    }

    /**
     * Sets whether the alert should currently be displayed. When activated, the alert text will also
     * be sent to the console.
     *
     * @param active Whether the alert should be displayed.
     */
    public void set(boolean active) {
        if (active && !this.active) {
            activeStartTime = Timer.getFPGATimestamp();
            switch (type) {
                case ERROR:
                    DriverStation.reportError(text, false);
                    break;
                case WARNING:
                    DriverStation.reportWarning(text, false);
                    break;
                case INFO:
                    System.out.println(text);
                    break;
            }
        }
        this.active = active;
    }

    /**
     * Updates current alert text.
     *
     * @param text New text to be displayed when the alert is active.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sendable class for displaying alerts in NetworkTables.
     *
     * @author Parker Meyers
     * @author Mechanical-Advantage
     */
    private static class SendableAlerts implements Sendable {
        public final List<Alert> alerts = new ArrayList<>();

        public String[] getStrings(AlertType type) {
            Predicate<Alert> activeFilter = (Alert x) -> x.type == type && x.active;
            Comparator<Alert> timeSorter = (Alert a1,
                                            Alert a2) -> (int) (a2.activeStartTime - a1.activeStartTime);
            return alerts.stream().filter(activeFilter).sorted(timeSorter)
                    .map((Alert a) -> a.text).toArray(String[]::new);
        }

        @Override
        public void initSendable(SendableBuilder builder) {
            builder.setSmartDashboardType("Alerts");
            builder.addStringArrayProperty("errors",
                    () -> getStrings(AlertType.ERROR), null);
            builder.addStringArrayProperty("warnings",
                    () -> getStrings(AlertType.WARNING), null);
            builder.addStringArrayProperty("infos", () -> getStrings(AlertType.INFO),
                    null);
        }
    }
}