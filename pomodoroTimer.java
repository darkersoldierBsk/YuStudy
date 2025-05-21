//Import Details
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

//Our Main Function
public class PomodoroTimer {

    //FRAME ADJUSTMENTS AND TIMER SET
    private static JFrame frame;
    private static JLabel timerLabel;
    private static Timer timer;
    private static int timeLeft = 1*60;  //default case for lessons
    private static final int WIDTH = 825;
    private static final int HEIGHT = 540;

    private final Color baseLineColor = new Color(200, 200, 200);

    //BUTTONS
    private static JButton startButton;
    private static JButton resetButton;
    private static JButton settingsButton;
    private static JButton timerButton;

    //TIMER CHANGE
    private static JTextField minuteField;
    private static JTextField secondField;
    private static JLabel colonLabel;

    //FRAME TITLE AND BUTTON TITLE ADJUSTMENTS VARIABLE
    private static int start = 0;
    private static int playerVisibility = 0;
    private static Timer playerHideTimer;
    private static int resizedCheck = 0;

    //TIMER FONT SIZE
    private static int timerFontSize;
    //DOUBLE CLICK CHECKER FOR TIMER BUTTON
    static final long[] lastClickTime = {0};
    static final int doubleClickThreshold = 500;

    //THEME ADJUSTMENT VARIABLE
    public static int colorChoice = 0;

    //TIMER SETTINGS AND VARIABLES
    private static boolean isRunning = false; // 🔥 Added to track if timer is running
    private static Timer countdownTimer;      // 🔥 Timer that ticks every second
    private static boolean isTimerEditable = false; // Timer is in edit mode

    //IMAGE ICONS
    private static ImageIcon resetIcon;
    private static ImageIcon settingsIcon;


    //RESIZED BACKGROUND IMAGE
    private static Image originalBackgroundImage;
    private static Image resizedBackgroundImage;
    private static Image baseSettingsImage;
    private static BackgroundPanel panel; //  Made panel a class variable
    private static RoundedPanel playerPanel;
    private static JPanel hoverAreaPanel;

    private static Font timerFont; //  Move font loading out of showPage()

    //SOUND LOADING VARIABLE
    private static Clip endClip;


    //MAIN FUNCTION TO SHOW POMODORO PAGE *****
    public static void showPage(){

        //FRAME SETTINGS
        frame = new JFrame("Let's Focus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);

        //BACKGROUND SELECTION
        originalBackgroundImage = new ImageIcon(
                PomodoroTimer.class.getResource("/assets/deskkBg.jpg")).getImage();
        resizedBackgroundImage = originalBackgroundImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        //CONTENT PANEL WITH NULL LAYOUT, FREE POSITIONING
        panel = new BackgroundPanel();
        panel.setLayout(null);
        frame.setContentPane(panel);
        frame.setVisible(true);


        //PLAYER PANEL FOR SPOTIFY
        playerPanel = new RoundedPanel(20);
        playerPanel.setLayout(null);
        playerPanel.setBackground(Color.WHITE);
        playerPanel.setBounds((2*WIDTH)/64, (24*HEIGHT)/32, (0*WIDTH)/64, 2*HEIGHT);
        //.setOpaque(false); // Keep it non-opaque for rounded corners
        //playerPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // ADD THIS LINE FOR DEBUGGING
        panel.add(playerPanel);
        panel.revalidate();
        panel.repaint();


        //PLAYER PANEL BOUND OPTIONS
        //playerPanel.setBounds((2*WIDTH)/64, (24*HEIGHT)/32, (11*WIDTH)/32, (4*HEIGHT)/32); ++++++++
        // playerPanel.setBounds(WIDTH/64, (25*HEIGHT)/32, (5*WIDTH)/16, (4*HEIGHT)/32);     --------

        //PLAYER PANEL UI

        //1. SONG NAME JLABEL
        JLabel songName = new JLabel("SONG TITLE");
        songName.setFont(new Font("SansSerif", Font.BOLD, 11));
        int songNameHeight = songName.getHeight() + WIDTH/128;
        songName.setBounds((5*WIDTH)/64, songNameHeight, (8*WIDTH)/32, HEIGHT/32);
        songName.setForeground(Color.BLACK);
        playerPanel.add(songName);

        //2. SONG IMAGE BUTTON
        ImageIcon songImageIcon = new ImageIcon("src/assets/winxx.png");
        FrostedGlowButton songImage = new FrostedGlowButton(songImageIcon);
        int songImageButtonHeight = songImage.getHeight() + (3*WIDTH)/516;
        songImage.setBounds(WIDTH/128, songImageButtonHeight, (4*WIDTH)/64, (4*WIDTH)/64);
        songImage.setFocusable(false);
        songImage.setContentAreaFilled(false); // No background fill
        songImage.setBorderPainted(false);     // No border initially
        songImage.setFocusPainted(false);       // No focus rectangle
        songImage.setOpaque(false);
        playerPanel.add(songImage);

        //3. SHUFFLE BUTTON
        ImageIcon shuffleIcon = new ImageIcon("src/assets/sh16.png");
        FrostedGlowButton shuffleButton = new FrostedGlowButton(shuffleIcon);
        int shuffleImageButtonHeight = songNameHeight + 28;
        shuffleButton.setBounds((22*WIDTH)/256, shuffleImageButtonHeight, (10*WIDTH)/256, (3*WIDTH)/128);
        shuffleButton.setFocusable(false);
        playerPanel.add(shuffleButton);

        //4. BACK BUTTON
        ImageIcon backIcon = new ImageIcon("src/assets/back16.png");
        FrostedGlowButton backButton = new FrostedGlowButton(backIcon);
        backButton.setBounds((32*WIDTH)/256, shuffleImageButtonHeight, (10*WIDTH)/256, (3*WIDTH)/128);
        backButton.setFocusable(false);
        playerPanel.add(backButton);

        //5. START BUTTON
        ImageIcon playIcon = new ImageIcon("src/assets/ply16.png");
        FrostedGlowButton playButton = new FrostedGlowButton(playIcon);
        playButton.setBounds((42*WIDTH)/256, shuffleImageButtonHeight, (10*WIDTH)/256, (3*WIDTH)/128) ;
        playButton.setFocusable(false);
        playerPanel.add(playButton);

        //6. NEXT BUTTON
        ImageIcon nextIcon = new ImageIcon("src/assets/next16.png");
        FrostedGlowButton nextButton = new FrostedGlowButton(nextIcon);
        nextButton.setBounds((52*WIDTH)/256, shuffleImageButtonHeight, (10*WIDTH)/256, (3*WIDTH)/128);
        nextButton.setFocusable(false);
        playerPanel.add(nextButton);

        //7. RESET BUTTON
        ImageIcon rstIcon = new ImageIcon("src/assets/rst16.png");
        FrostedGlowButton rstButton = new FrostedGlowButton(rstIcon);
        rstButton.setBounds((62*WIDTH)/256, shuffleImageButtonHeight, (10*WIDTH)/256, (3*WIDTH)/128);
        rstButton.setFocusable(false);
        playerPanel.add(rstButton);

        //8. SPOTIFY LOGO
        ImageIcon spotifyIcon = new ImageIcon("src/assets/spotify16.png");
        FrostedGlowButton spotifyImage = new FrostedGlowButton(spotifyIcon);
        int spotifyImageHeight = songNameHeight - 2;
        spotifyImage.setBounds((73*WIDTH)/256, spotifyImageHeight, (10*WIDTH)/256, (3*WIDTH)/128); //40
        spotifyImage.setFocusable(false);
        playerPanel.add(spotifyImage);

        //9. PLAYER LINE
        JLabel lineLabel = new JLabel();
        int lineHeight = songNameHeight + 22;
        lineLabel.setBorder(new LineBorder(Color.getHSBColor(200,200,200), 2));  // 2 pixels thick
        lineLabel.setBounds((22*WIDTH)/256, lineHeight, (60*WIDTH)/256, (int)(WIDTH/412) );
        lineLabel.setFocusable(false);
        playerPanel.add(lineLabel);

        //


        songImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Butonu yakaladın");
                playerVisibility = 1;
            }
        });
/*
        //3.PLAYER LINE (PANEL)
        MusicIndicator musicIndicator = new MusicIndicator();
        musicIndicator.setBounds((5 * WIDTH) / 64,(3 * HEIGHT) / 64, (8 * WIDTH) / 32, HEIGHT / 64);
        musicIndicator.setFocusable(false);
        playerPanel.add(musicIndicator);
*/

        //HOVER AREA PANEL, JUST FOR DETECTING MOVEMENTS
        hoverAreaPanel = new JPanel();
        hoverAreaPanel.setLayout(null);
        hoverAreaPanel.setBounds((2*WIDTH)/64, (24*HEIGHT)/32, (11*WIDTH)/32, (4*HEIGHT)/32);
        hoverAreaPanel.setOpaque(false);

        panel.add(hoverAreaPanel);
        // Class-level variable to prevent multiple timers


        // Class-level variable to prevent multiple timers

        hoverAreaPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Stop the timer if it is already running
                if (playerHideTimer != null && playerHideTimer.isRunning()) {
                    playerHideTimer.stop();
                }

                // Expand playerPanel when mouse enters hover area
                playerPanel.setBounds((2*WIDTH)/64, (24*HEIGHT)/32, (11*WIDTH)/32, (4*HEIGHT)/32);
                playerPanel.revalidate();
                playerPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Create a new timer to hide the player panel after delay
                playerHideTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        playerPanel.setLocation(0, 2 * HEIGHT);  // collapse back to hidden size
                        playerPanel.revalidate();
                        playerPanel.repaint();
                    }
                });
                playerHideTimer.setRepeats(false);  // Run only once
                playerHideTimer.start();
            }
        });


        /*
        hoverAreaPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Expand playerPanel when mouse enters hover area
                playerPanel.setBounds((2*WIDTH)/64, (24*HEIGHT)/32, (11*WIDTH)/32, (4*HEIGHT)/32);
                playerPanel.revalidate();
                playerPanel.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                playerVisibility = 0;
                if(playerVisibility == 0) {
                    Timer timer = new Timer(300, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            // Move the playerPanel outside (hide it)
                            playerPanel.setLocation(0, 2 * HEIGHT);
                            playerPanel.revalidate();
                            playerPanel.repaint();
                        }
                    });
                }else{
                    Timer timer = new Timer(900, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            playerVisibility = 0;
                            // Move the playerPanel outside (hide it)
                            playerPanel.setLocation(0, 2 * HEIGHT);
                            playerPanel.revalidate();
                            playerPanel.repaint();
                        }
                    });
                }
                timer.setRepeats(false);  // Run only once
                timer.start();
            }*/

    /*
            @Override
            public void mouseExited(MouseEvent e) {
                // Hide playerPanel when mouse leaves hover area
                Timer timer = new Timer(300000, e);
                timer.setRepeats(false);
                timer.start();
                playerPanel.setLocation(0, 2*HEIGHT);  // collapse back to hidden size
                playerPanel.revalidate();
                playerPanel.repaint();
            }
        });
*/
        /*
        hoverAreaPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Move playerPanel up (visible)
                playerPanel.setBounds((2*WIDTH)/64, (24*HEIGHT)/32, (11*WIDTH)/32, (4*HEIGHT)/32);
                playerPanel.revalidate();
                playerPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Move playerPanel back down (hidden)
                playerPanel.setLocation(0, 2*HEIGHT);
            }
        });

*/

        // SETTING UP THE TIMER FONT
        try {
            InputStream fontStream = PomodoroTimer.class.getClassLoader().getResourceAsStream("fonts/fontTimer.ttf");
            if (fontStream == null) throw new IOException("Font stream not found");
            timerFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 80f);
        } catch (FontFormatException | IOException e) {
            timerFont = new Font("TimesRoman", Font.PLAIN, 80);
        }


        //START BUTTON, FRAME AND BUTTON TITLE CHANGES
        startButton = new FrostedGlowButton("Start");
        startButton.setBounds((9*WIDTH)/32,(9*HEIGHT)/18,(9*WIDTH)/32,(2*HEIGHT)/18);
        startButton.setForeground(Color.PINK);
        //startButton.setFont(new Font("TimesRoman", Font.PLAIN, (int) (4*HEIGHT)/90));
        startButton.setFont(new Font("TimesRoman", Font.ITALIC, (int) (6*HEIGHT)/90));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isTimerEditable){
                    if (!isRunning) { // If not running, start the timer
                        startCountdown();
                        start = (start == 0 || start == 2) ? 1 : start;
                        start = (start == 5) ? 4 : start;
                    } else { //  If running, pause the timer
                        pauseCountdown();
                        if(start != 4){
                            start = 2;
                        }else{
                            start = 5;
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Timer can't be started, you need to finish editing first");
                }


                // Update the frame title based on the state
                changeTitle(start);

                frame.revalidate();
                frame.repaint();


                //CHANGING BETWEEN VALUES TO ADJUST TITLES
                /*
                if(start == 0 || start == 2){
                    start = 1;
                }else if(start == 1) {
                    start = 2;
                }
                changeTitle(start);
                */



            }

            // FRAME TITLE CHANGES *
            private void changeTitle(int start) {

                switch (start) {
                    case 1:
                        frame.setTitle("Focus Mode is On..");
                        startButton.setText("Pause");
                        System.out.println("case 1");
                        break;
                    case 2:
                        frame.setTitle("Get Back to Work!");
                        startButton.setText("Resume");
                        System.out.println("case 2");
                        break;
                    case 3:
                        frame.setTitle("Pomodoro Ended");
                        System.out.println("case 3");
                        break;
                    case 4:
                        frame.setTitle("Break Time ^_^");
                        startButton.setText("Pause");
                        System.out.println("case 4");
                        break;
                    case 5:
                        frame.setTitle("Extra Break Time (SUSPICIOUS) -_-");
                        startButton.setText("Resume");
                        System.out.println("case 5");
                        break;

                }
                frame.revalidate();
                frame.repaint();
            }
        });
        panel.add(startButton);


        //SETTINGS BUTTON
        baseSettingsImage = new ImageIcon(PomodoroTimer.class.getResource("assets/settings40pink2.png")).getImage();
        settingsIcon = new ImageIcon(baseSettingsImage);
        settingsButton = new FrostedGlowButton(settingsIcon);
        settingsButton.setBounds((29*WIDTH)/32,HEIGHT/72,(2*WIDTH)/32,(2*HEIGHT)/18); //... y = (height/18) / 4 ...
        settingsButton.addActionListener(e -> openSettings());
        //settingsButton.setBounds((19*WIDTH)/32,(9*HEIGHT)/18,(3*WIDTH)/32,(2*HEIGHT)/18);
        //panel.add(settingsButton);
        /*

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //opens a new class named PomodoroSettings
                //PomodoroSettings.setVisible(true);
            }
        });

        */
        panel.add(settingsButton);


        //TIMER BUTTON
        timerButton = new FrostedGlowButton("01:00");
        timerFontSize = (32 * HEIGHT)/ 160;
        timerButton.setFont(new Font("Impact", Font.BOLD, timerFontSize));//2 3 4 5 6
        timerButton.setForeground(Color.PINK);
        timerButton.setBounds(WIDTH/4, (5*HEIGHT)/18, (2*WIDTH)/4, (6*HEIGHT)/32); //beginning of timerButton's text: 2width/8, finish is 6width/8
        /*
        timerTextField = new JTextField(5);
        timerTextField.setText(timerButton.getText());
        timerTextField.setFont(new Font("Impact", Font.BOLD, timerFontSize));
        timerTextField.setForeground(Color.PINK);
        timerTextField.setBounds((11*WIDTH)/32, (5*HEIGHT)/18, (10*WIDTH)/32, (6*HEIGHT)/32);
        timerTextField.setVisible(false);
        panel.add(timerTextField);
        */

        //CHANGING TIME MANUALLY
        String text = timerButton.getText();        // Example: "25:00"
        String minutes = text.substring(0, 2);      // 25
        String seconds = text.substring(3,5);       // 00

        minuteField = new JTextField(2);
        secondField = new JTextField(2);

        minuteField.setText(minutes);
        secondField.setText(seconds);
        colonLabel = new JLabel(":");

        minuteField.setFont(new Font("Impact", Font.BOLD, timerFontSize));
        minuteField.setForeground(Color.PINK);
        secondField.setFont(new Font("Impact", Font.BOLD, timerFontSize));
        secondField.setForeground(Color.PINK);
        colonLabel.setFont(new Font("Impact", Font.BOLD, timerFontSize));
        colonLabel.setForeground(Color.PINK);

        minuteField.setBounds((10*WIDTH)/32, (5*HEIGHT)/18, (5*WIDTH)/32, (6*HEIGHT)/32); //2 1 2
        colonLabel.setBounds((15*WIDTH)/32, (5*HEIGHT)/18, (2*WIDTH)/32, (6*HEIGHT)/32);
        secondField.setBounds((16*WIDTH)/32, (5*HEIGHT)/18, (5*WIDTH)/32, (6*HEIGHT)/32);

        minuteField.setVisible(false);
        secondField.setVisible(false);
        colonLabel.setVisible(false);

        //minuteField's TRANSPARENCY
        minuteField.setOpaque(false);                   // Makes background non-painted
        minuteField.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        minuteField.setBackground(new Color(0, 0, 0, 0)); // Transparent background

        //secondField's TRANSPARENCY
        secondField.setOpaque(false);                   // Makes background non-painted
        secondField.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        secondField.setBackground(new Color(0, 0, 0, 0)); // Transparent background

        panel.add(minuteField);
        panel.add(secondField);
        panel.add(colonLabel);

        timerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                long currentTime = System.currentTimeMillis();

                if (currentTime - lastClickTime[0] <= doubleClickThreshold) {
                    // Double-click detected
                    pauseCountdown();  // ATTENTION MAY CAUSE INJURIES
                    isTimerEditable = true;

                    if(start != 4){
                        start = 2;
                    }else{
                        start = 5;
                    }
                    System.out.println("case" + String.valueOf(start));

                    startButton.setText("Resume");

                    String text = timerButton.getText();        // Example: "25:00"
                    String minutes = text.substring(0, 2);      // 25
                    String seconds = text.substring(3,5);       // 00

                    minuteField.setText(minutes);
                    secondField.setText(seconds);


                    timerButton.setVisible(false);

                    minuteField.setVisible(true);
                    secondField.setVisible(true);
                    colonLabel.setVisible(true);

                    minuteField.requestFocus();
                    secondField.requestFocus();

                    panel.revalidate();  // Important to update layout
                    panel.repaint();
                    /*
                    timerTextField.setText(timerButton.getText());
                    timerButton.setVisible(false);
                    timerTextField.setVisible(true);
                    timerTextField.requestFocus();
                    */

                }

                lastClickTime[0] = currentTime;
            }
        });

        /*
        timerTextField.addActionListener(e -> {
            timerButton.setText(timerTextField.getText());
            timerTextField.setVisible(false);
            timerButton.setVisible(true);
        });
        */

        minuteField.addActionListener(e -> {
            int updated_minutes = Integer.parseInt(minuteField.getText());
            int updated_seconds = Integer.parseInt(secondField.getText());
            updateTimerDisplayManual(updated_minutes, updated_seconds);
            minuteField.setVisible(false);

            if(secondField.isVisible()){
                secondField.setVisible(false);

            }
            if(colonLabel.isVisible()){
                colonLabel.setVisible(false);
            }

            isTimerEditable = false;
            timerButton.setVisible(true);

        });

        secondField.addActionListener(e -> {
            int updated_minutes = Integer.parseInt(minuteField.getText());
            int updated_seconds = Integer.parseInt(secondField.getText());
            updateTimerDisplayManual(updated_minutes, updated_seconds);

            secondField.setVisible(false);

            if(minuteField.isVisible()){
                minuteField.setVisible(false);
            }
            if(colonLabel.isVisible()){
                colonLabel.setVisible(false);
            }

            isTimerEditable = false;
            timerButton.setVisible(true);
        });
        /*
        timerButton = new GlowButton("25:00");
        timerButton.setFont(timerFont.deriveFont(Font.BOLD,80));
        timerButton.setTextColor
        timerButton.setBounds(WIDTH/4, (5*HEIGHT)/18, (2*WIDTH)/4, (6*HEIGHT)/32);
        */

        //TIMER BUTTON TRANSPARENCY
        timerButton.setContentAreaFilled(false); // No background fill
        timerButton.setBorderPainted(false);     // No border initially
        timerButton.setFocusPainted(false);       // No focus rectangle
        timerButton.setOpaque(false);             // Transparent
        panel.add(timerButton);


        //RESET BUTTON
        //resetIcon = new ImageIcon(PomodoroTimer.class.getResource("assets/reset48.png"));
        //resetButton = new FrostedGlowButton(resetIcon);
        //Image baseResetImage = new ImageIcon(PomodoroTimer.class.getResource("assets/reset64.png")).getImage();
        //resetButton = new FrostedGlowButton(baseResetImage);
        Image baseResetImage = new ImageIcon(PomodoroTimer.class.getResource("assets/reset48.png")).getImage();
        resetIcon = new ImageIcon(baseResetImage);
        resetButton = new FrostedGlowButton(resetIcon);
        resetButton.setBounds((19*WIDTH)/32,(9*HEIGHT)/18,(4*WIDTH)/32,(2*HEIGHT)/18);
        //resetButton.setFont(new Font("TimesRoman", Font.PLAIN, (9*HEIGHT)/160)); // 3*3 / 32*5

        //RESET BUTTON TRANSPARENCY
        resetButton.setContentAreaFilled(false); // No background fill
        resetButton.setBorderPainted(false);     // No border initially
        resetButton.setFocusPainted(false);       // No focus rectangle
        resetButton.setOpaque(false);             // Transparent

        //resetButton.addActionListener(e -> resetTimerDisplay());  //Shorter version of the code below

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isTimerEditable){
                    resetTimerDisplay();
                }else{
                    JOptionPane.showMessageDialog(null, "Timer can't be restarted, you need to finish editing first");
                }
            }
        });

        panel.add(resetButton);


        //Font finalTimerFont = timerFont;
        Font finalTimerFont = new Font("Impact", Font.BOLD, 80); // Placeholder size
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = frame.getWidth();    // 🔥 recalculate width
                int newHeight = frame.getHeight();  // 🔥 recalculate height
                resizedBackgroundImage = originalBackgroundImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                playerPanel.setBounds((2*newWidth)/64, (24*newHeight)/32, (0*newWidth)/64, 2*newHeight);
                hoverAreaPanel.setBounds((2*newWidth)/64, (24*newHeight)/32, (11*newWidth)/32, (4*newHeight)/32);
                resizedCheck = resizedCheck + 1;
                
                // 🔥 update button positions dynamically
                int newSongNameHeight = songName.getHeight() + newWidth/128;
                songName.setBounds((5*newWidth)/64, newSongNameHeight, (8*newWidth)/32, newHeight/32);

                int newSongImageButtonHeight = songImage.getHeight() + (3*newWidth)/516;
                songImage.setBounds(newWidth/128, newSongImageButtonHeight, (4*newWidth)/64, (4*newWidth)/64);

                int newShuffleImageButtonHeight = songNameHeight + 28;
                shuffleButton.setBounds((22*newWidth)/256, newShuffleImageButtonHeight, (10*newWidth)/256, (3*newWidth)/128);

                playButton.setBounds((42*newWidth)/256, shuffleImageButtonHeight, (10*newWidth)/256, (3*newWidth)/128) ;

                nextButton.setBounds((52*newWidth)/256, shuffleImageButtonHeight, (10*newWidth)/256, (3*newWidth)/128);

                rstButton.setBounds((62*newWidth)/256, shuffleImageButtonHeight, (10*newWidth)/256, (3*newWidth)/128);

                int spotifyImageHeight = newSongNameHeight - 2;
                spotifyImage.setBounds((73*newWidth)/256, spotifyImageHeight, (10*newWidth)/256, (3*newWidth)/128);

                int lineHeight = newSongNameHeight + 22;
                lineLabel.setBorder(new LineBorder(Color.getHSBColor(200,200,200), 2));  // 2 pixels thick  ///WARNING!!!
                lineLabel.setBounds((22*newWidth)/256, lineHeight, (60*newWidth)/256, (int)(newWidth/412) );

                startButton.setBounds((9 * newWidth) / 32, (9 * newHeight) / 18, (9 * newWidth) / 32, (2 * newHeight) / 18);
                settingsButton.setBounds((29*newWidth)/32,newHeight/72,(2*newWidth)/32,(2*newHeight)/18);
                timerButton.setBounds(newWidth / 4, (5 * newHeight) / 18, (2 * newWidth) / 4, (6 * newHeight) / 32);
                minuteField.setBounds((10*newWidth)/32, (5*newHeight)/18, (5*newWidth)/32, (6*newHeight)/32); //2 1 2
                colonLabel.setBounds((15*newWidth)/32, (5*newHeight)/18, (2*newWidth)/32, (6*newHeight)/32);
                secondField.setBounds((16*newWidth)/32, (5*newHeight)/18, (5*newWidth)/32, (6*newHeight)/32);
                resetButton.setBounds((19 * newWidth) / 32, (9 * newHeight) / 18, (4 * newWidth) / 32, (2 * newHeight) / 18);


                //timerButton.setFont(finalTimerFont.deriveFont(Font.BOLD, (float) (27 * newHeight) /160)); // button sizeının 3 katının 5e bölümü 9*3/32*5
                // 🔥 Dynamically adjust timer font size
                //LOOOKKKKK AGAINNNNNNNNNNNNNNNNNNNN
                /*
                int dynamicFontSize = (32 * newHeight) / 160;
                timerButton.setFont(finalTimerFont.deriveFont(Font.BOLD, dynamicFontSize));
                 */

                timerFontSize = (32 * newHeight) / 160;
                timerButton.setFont(finalTimerFont.deriveFont(Font.BOLD, timerFontSize));
                minuteField.setFont(finalTimerFont.deriveFont(Font.BOLD, timerFontSize));
                secondField.setFont(finalTimerFont.deriveFont(Font.BOLD, timerFontSize));
                colonLabel.setFont(finalTimerFont.deriveFont(Font.BOLD, timerFontSize));

                startButton.setFont(new Font("TimesRoman", Font.ITALIC, (int) (4*newHeight)/90));
                //resetButton.setFont(new Font("TimesRoman", Font.PLAIN, ));
                /*
                if (newHeight > 740) {
                    //size maximized
                    resetIcon = new ImageIcon(PomodoroTimer.class.getResource("assets/reset64.png"));
                } else if (newHeight < 540) {
                    //size minimized
                    resetIcon = new ImageIcon(PomodoroTimer.class.getResource("assets/reset24.png"));
                } else {
                    //base case
                    resetIcon = new ImageIcon(PomodoroTimer.class.getResource("assets/reset48.png"));
                }
                resetButton.setIcon(resetIcon);
                */

                int iconSize = (int)(newHeight/11.25); // Or whatever scaling logic works best
                Image scaledImage = baseResetImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);

                // Wrap it into an ImageIcon (which is an Icon!)
                Icon scaledIcon = new ImageIcon(scaledImage);

                // Set it to your FrostedGlowButton
                resetButton.setIcon(scaledIcon);

                int settingIconSize = (int)(newHeight/13.5); //13.5 -> 14
                Image settingScaledImage = baseSettingsImage.getScaledInstance(settingIconSize, settingIconSize, Image.SCALE_SMOOTH);
                Icon settingScaledIcon = new ImageIcon(settingScaledImage);
                settingsButton.setIcon(settingScaledIcon);

                frame.repaint();
            }
        });
    } //;

    // BACKGROUND PANEL CLASS
    // Updated BackgroundPanel to use resized image cache
    static class BackgroundPanel extends JPanel {
        public BackgroundPanel() {
            setDoubleBuffered(true); // Added double buffering
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (resizedBackgroundImage != null) {
                g.drawImage(resizedBackgroundImage, 0, 0, this);
            }
        }
    }

    //TIMER FUNCTIONS
    private static void startCountdown() { // Start countdown
        if (countdownTimer == null) {
            countdownTimer = new Timer(200, new ActionListener() { // every 1000 ms
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (timeLeft > 0) {
                        timeLeft--;
                        updateTimerDisplay();
                    } else {
                        countdownTimer.stop();
                        System.out.println("Timer stopped because time ran out.");
                        isRunning = false;
                        if(start != 4){
                            start = 3;
                            frame.setTitle("Pomodoro Ended");
                        }else{
                            frame.setTitle("Break Time Ended");
                        }
                        startButton.setText("Start");

                        //POMODORO ENDING SOUND
                        //playEndSound();

                        showEndPrompt();
                    }
                }
            });
        }
        countdownTimer.start();
        isRunning = true;
    }

    private static void pauseCountdown() { // 🔥 Pause countdown
        if (countdownTimer != null) {
            countdownTimer.stop();
            System.out.println("Timer paused.");
        }
        isRunning = false;
    }

    private static void updateTimerDisplay() { // 🔥 Update timer text
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerButton.setText(timeFormatted);
    }

    private static void updateTimerDisplayManual(int minutes, int seconds) {// 🔥 Update timer text
        timeLeft = minutes * 60 + seconds;

        minutes = timeLeft / 60;
        seconds = timeLeft % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerButton.setText(timeFormatted);
    }


    //RESET BUTTON FUNCTIONS
    private static void resetTimerDisplay() {
        pauseCountdown();
        System.out.println("Timer reset and stopped.");

        if(start == 4){
            timeLeft = 1 * 60;  // break
            start = 4;
        }else{
            timeLeft = 1 * 60;
            start = 0;
        }
        updateTimerDisplay();
        startButton.setText("Start");
    }

    private static void setNumericLimitFilter(JTextField field, int maxDigits) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length() <= maxDigits) && string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() - length + text.length() <= maxDigits) && text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private static void showEndPrompt() {
        // Create a JOptionPane with options for the user
        String mesage;
        if(start != 4){
            mesage = "Pomodoro session ended! What would you like to do?";
        }else{
            mesage = "Break time is over! What would you like to do?";
        }

        Object[] options = {"Take a Break", "Start New Pomodoro"};
        int choice = JOptionPane.showOptionDialog(frame,
                mesage,
                "Pomodoro Timer",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            // User chose "Take a Break"
            startBreak();
        } else if (choice == 1) {
            // User chose "Start New Pomodoro"
            start = 1;
            resetTimerDisplay();
        }
    }

    private static void startBreak() {
        // Set up the break timer (for example, 5 minutes)
        timeLeft = 1 * 60; // 5 minutes break
        updateTimerDisplay();
        startButton.setText("Start Break");
        start = 4;
        startButton.addActionListener(e -> startButton.setText("Pause"));
    }

    private static void openSettings() {
        PomodoroSettings.showSettings();  // Open the settings window when the icon is clicked
    }

    public static void initSound() {
        try {
            File soundFile = new File("C:/Users/90505/IdeaProjects/YuStudy1/src/assets/sounds/tadaw.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            endClip = AudioSystem.getClip();
            endClip.open(audioStream); // Open once only!
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }


    private static void playEndSound() {
        if (endClip != null) {
            if (endClip.isRunning()) {
                endClip.stop();
            }
            endClip.setFramePosition(0); // ⏮ Rewind to beginning
            endClip.start();             // ▶️ Play immediately
        }
    }


    /*
    private static void playEndSound() {
        try {
            // Replace the path with the actual path to your sound file
            File soundFile = new File("C:/Users/90505/IdeaProjects/YuStudy1/src/assets/sounds/tadaw.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
    */

    /*
    public static void themes(int colorChoice){
        switch (colorChoice) {
            case 0:
                // White Theme - JapanBg
                originalBackgroundImage = new ImageIcon(
                        PomodoroTimer.class.getResource("/assets/japanBg.jpg")).getImage();
                resizedBackgroundImage = originalBackgroundImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

                baseSettingsImage = new ImageIcon(PomodoroTimer.class.getResource("/assets/settings40red2.png")).getImage();
                settingsIcon = new ImageIcon(baseSettingsImage);
                settingsButton.setIcon(settingsIcon);

                startButton.setForeground(Color.WHITE);
                timerButton.setForeground(Color.WHITE);
                break;
            case 1:
                // Pink Theme - Midori


        }
        frame.revalidate();
        frame.repaint();
    };
    */

    /*
    public static void startButtonColorChanger(int startButtonColorChoice){
        switch (startButtonColorChoice) {
            case 0:
                startButton.setForeground(Color.WHITE);
                break;
            case 1:
                startButton.setForeground(Color.PINK);
                break;
            case 2:
                startButton.setForeground(Color.RED);
                break;
        }
        frame.revalidate();
        frame.repaint();
    };
    */
}
