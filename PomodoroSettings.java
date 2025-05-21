import javax.swing.*;
import java.awt.*;

public class PomodoroSettings {

    private static JFrame settingsFrame;
    private static JComboBox<String> backgroundComboBox;
    private static JComboBox<String> soundComboBox;
    private static JSpinner pomodoroTimeSpinner;
    private static JSpinner breakTimeSpinner;

    public static void showSettings() {
        settingsFrame = new JFrame("Pomodoro Settings");
        settingsFrame.setSize(400, 400);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        // Background selection
        panel.add(new JLabel( "        Change the Theme:"));

        String[] backgrounds = {"Theme 1", "Theme 2", "Theme 3"};
        backgroundComboBox = new JComboBox<>(backgrounds);
        panel.add(backgroundComboBox);

        // Sound selection
        panel.add(new JLabel("            Select Sound:"));
        String[] sounds = {"Sound 1", "Sound 2", "Sound 3"};
        soundComboBox = new JComboBox<>(sounds);
        panel.add(soundComboBox);

        // Pomodoro time selection
        panel.add(new JLabel("     Pomodoro Time (minutes):"));
        SpinnerModel pomodoroTimeModel = new SpinnerNumberModel(25, 1, 60, 1); // Default: 25 minutes
        pomodoroTimeSpinner = new JSpinner(pomodoroTimeModel);
        pomodoroTimeSpinner.setFont(new Font("Monospaced", Font.BOLD, 20));
        panel.add(pomodoroTimeSpinner);

        // Break time selection
        panel.add(new JLabel("        Break Time (minutes):"));
        SpinnerModel breakTimeModel = new SpinnerNumberModel(5, 1, 30, 1); // Default: 5 minutes
        breakTimeSpinner = new JSpinner(breakTimeModel);
        breakTimeSpinner.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        panel.add(breakTimeSpinner);

        // Apply button
        JButton applyButton = new JButton("Apply");
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(applyButton);

        settingsFrame.add(panel);
        settingsFrame.setVisible(true);
    }
}
