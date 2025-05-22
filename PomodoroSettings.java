import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PomodoroSettings {

    private static JFrame settingsFrame;
    private static JComboBox<String> backgroundComboBox;
    private static JComboBox<String> soundComboBox;
    private static JSpinner pomodoroTimeSpinner;
    private static JSpinner breakTimeSpinner;
    private static Color bgColor = Color.WHITE;
    private static Color textColor = Color.BLACK;

    public static void showSettings() {
        settingsFrame = new JFrame("Pomodoro Settings");
        settingsFrame.setSize(400, 400);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        // Labels with references
        JLabel themeLabel = new JLabel("        Change the Theme:");
        JLabel soundLabel = new JLabel("            Select Sound:");
        JLabel pomoLabel = new JLabel("     Pomodoro Time (minutes):");
        JLabel breakLabel = new JLabel("        Break Time (minutes):");

        // Background selection
        String[] backgrounds = {"Default Theme", "Japanese Theme", "Midori Theme", "Anime Theme (Girl)", "Anime Theme (Boy)"};
        backgroundComboBox = new JComboBox<>(backgrounds);
        backgroundComboBox.setSelectedIndex(0);


        // Sound selection
        String[] sounds = {"Sound 1", "Sound 2", "Sound 3"};
        soundComboBox = new JComboBox<>(sounds);

        // Pomodoro time selection
        SpinnerModel pomodoroTimeModel = new SpinnerNumberModel(25, 1, 60, 1); // Default: 25 minutes
        pomodoroTimeSpinner = new JSpinner(pomodoroTimeModel);
        pomodoroTimeSpinner.setFont(new Font("Monospaced", Font.BOLD, 20));

        // Break time selection
        SpinnerModel breakTimeModel = new SpinnerNumberModel(5, 1, 30, 1); // Default: 5 minutes
        breakTimeSpinner = new JSpinner(breakTimeModel);
        breakTimeSpinner.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));

        // Apply button
        JButton applyButton = new JButton("Apply");
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.setBackground(bgColor);
        settingsFrame.getContentPane().setBackground(bgColor);

        // Set label colors
        themeLabel.setForeground(textColor);
        soundLabel.setForeground(textColor);
        pomoLabel.setForeground(textColor);
        breakLabel.setForeground(textColor);

        // Set combo box and spinner colors
        backgroundComboBox.setBackground(bgColor);
        backgroundComboBox.setForeground(textColor);
        soundComboBox.setBackground(bgColor);
        soundComboBox.setForeground(textColor);

        pomodoroTimeSpinner.getEditor().getComponent(0).setBackground(bgColor);
        pomodoroTimeSpinner.getEditor().getComponent(0).setForeground(textColor);

        breakTimeSpinner.getEditor().getComponent(0).setBackground(bgColor);
        breakTimeSpinner.getEditor().getComponent(0).setForeground(textColor);

        // Apply button style
        applyButton.setBackground(textColor);
        applyButton.setForeground(bgColor);
        
        // Add components to panel
        panel.add(themeLabel);
        panel.add(backgroundComboBox);
        panel.add(soundLabel);
        panel.add(soundComboBox);
        panel.add(pomoLabel);
        panel.add(pomodoroTimeSpinner);
        panel.add(breakLabel);
        panel.add(breakTimeSpinner);
        panel.add(applyButton);

        // Apply button action to change theme
        applyButton.addActionListener(e -> {
            String selectedTheme = (String) backgroundComboBox.getSelectedItem();

            Color bgColor = Color.WHITE;
            Color textColor = Color.BLACK;

            switch (selectedTheme) {
                case "Default Theme":
                    bgColor = Color.WHITE;
                    textColor = Color.BLACK;
                    break;
                case "Japanese Theme":
                    bgColor = Color.DARK_GRAY;
                    textColor = Color.WHITE;
                    break;
                case "Midori Theme":
                    bgColor = new Color(173, 216, 230); // light blue
                    textColor = Color.BLACK;
                    break;
                case "Anime Theme (Girl)":
                    bgColor = Color.GREEN;
                    textColor = Color.BLACK;
                    break;
                case "Anime Theme (Boy)":
                    bgColor = Color.RED;
                    textColor = Color.BLACK;
                    break;
            }

            panel.setBackground(bgColor);
            settingsFrame.getContentPane().setBackground(bgColor);

            // Set label colors
            themeLabel.setForeground(textColor);
            soundLabel.setForeground(textColor);
            pomoLabel.setForeground(textColor);
            breakLabel.setForeground(textColor);

            // Set combo box and spinner colors
            backgroundComboBox.setBackground(bgColor);
            backgroundComboBox.setForeground(textColor);
            soundComboBox.setBackground(bgColor);
            soundComboBox.setForeground(textColor);

            pomodoroTimeSpinner.getEditor().getComponent(0).setBackground(bgColor);
            pomodoroTimeSpinner.getEditor().getComponent(0).setForeground(textColor);

            breakTimeSpinner.getEditor().getComponent(0).setBackground(bgColor);
            breakTimeSpinner.getEditor().getComponent(0).setForeground(textColor);

            // Apply button style
            applyButton.setBackground(textColor);
            applyButton.setForeground(bgColor);
            themeChanger();
        });

        settingsFrame.add(panel);
        settingsFrame.setVisible(true);
    }
    public static void themeChanger(){
        if(backgroundComboBox == null){
            backgroundComboBox.setSelectedIndex(0);
        }
        PomodoroTimer.setTheme(backgroundComboBox.getSelectedIndex());

    };

}


/*import javax.swing.*;
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
        panel.setBackground(Color.LIGHT_GRAY);
        settingsFrame.getContentPane().setBackground(Color.LIGHT_GRAY);

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
        applyButton.addActionListener(e -> {
            String selectedTheme = (String) backgroundComboBox.getSelectedItem();

            switch (selectedTheme) {
                case "Theme 1":
                    panel.setBackground(Color.WHITE);
                    break;
                case "Theme 2":
                    panel.setBackground(Color.DARK_GRAY);
                    break;
                case "Theme 3":
                    panel.setBackground(new Color(173, 216, 230)); // light blue
                    break;
            }
        });

        panel.add(applyButton);

        settingsFrame.add(panel);
        settingsFrame.setVisible(true);
    }
}*/
