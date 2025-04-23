import javax.swing.*;
import java.awt.*;
import java.time.Duration;

public class Pomodoro extends JFrame {
    private Timer timer;
    private final JLabel timeLabel = new JLabel("25:00", SwingConstants.CENTER);
    private final JLabel sessionLabel = new JLabel("WORK SESSION", SwingConstants.CENTER);
    private final JButton startButton = new JButton("START");
    private final JButton pauseButton = new JButton("PAUSE");
    private final JButton resetButton = new JButton("RESET");
    private final JButton settingsButton = new JButton("SETTINGS");
    private int currentRound = 1;
    private int totalRounds = 1; 
    private final JLabel roundsLabel = new JLabel("Round 1", SwingConstants.CENTER);

    private final Color BACKGROUND = new Color(34, 40, 49);
    private final Color PANEL_BG = new Color(48, 71, 94);
    private final Color ACCENT = new Color(240, 84, 84);
    private final Color TEXT = new Color(232, 232, 232);
    private final Color BUTTON_BG = new Color(48, 71, 94);

    private int workMinutes = 25;
    private int breakMinutes = 5;

    public Pomodoro() {
        timer = new Timer(workMinutes, breakMinutes);
        setupUI();
        setupTimerThread();
        center();
    }

    private void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    private void setupUI() {
        setTitle("Pomodoro Timer");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND);
    
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(BACKGROUND);
    
        // Timer display panel
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBackground(BACKGROUND);
        timerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
    
        // Create panel for session and round labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(BACKGROUND);
        
        // Styling for labels
        sessionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        sessionLabel.setForeground(TEXT);
        sessionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        sessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        roundsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        roundsLabel.setForeground(ACCENT);
        roundsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        roundsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        // Add labels to label panel
        labelPanel.add(sessionLabel);
        labelPanel.add(roundsLabel);
    
        // Styling for time label
        timeLabel.setFont(new Font("Arial", Font.BOLD, 64));
        timeLabel.setForeground(TEXT);
    
        // Add components to timer panel
        timerPanel.add(labelPanel, BorderLayout.NORTH);
        timerPanel.add(timeLabel, BorderLayout.CENTER);
    
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.setBackground(BACKGROUND);
    
        styleButton(startButton);
        styleButton(pauseButton);
        styleButton(resetButton);
        styleButton(settingsButton);
    
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(settingsButton);
    
        // Add components to main panel
        mainPanel.add(timerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        // Add main panel to frame
        add(mainPanel);
    
        // Button actions
        startButton.addActionListener(_ -> {
            timer.start();
            updateButtons();
        });
    
        pauseButton.addActionListener(_ -> {
            timer.pause();
            updateButtons();
        });
    
        resetButton.addActionListener(_ -> {
            timer.reset();
            resetRounds();
            updateDisplay();
            updateButtons();
        });
    
        settingsButton.addActionListener(_ -> showSettingsDialog());
    
        updateButtons();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(BUTTON_BG);
        button.setForeground(PANEL_BG);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(BUTTON_BG, 4, true), // Rounded border,
                BorderFactory.createEmptyBorder(10, 25, 10, 25)));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(ACCENT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(PANEL_BG);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setForeground(ACCENT);
            }
        });
    }

    private void showSettingsDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(PANEL_BG);

        JLabel workLabel = new JLabel("Work Duration (minutes):");
        workLabel.setFont(new Font("Arial", Font.BOLD, 14));
        workLabel.setForeground(TEXT);
        panel.add(workLabel);

        JTextField workField = new JTextField(String.valueOf(workMinutes));
        workField.setFont(new Font("Arial", Font.BOLD, 15));
        workField.setBackground(BACKGROUND);
        workField.setForeground(TEXT);
        workField.setBorder(BorderFactory.createLineBorder(BACKGROUND, 2));
        panel.add(workField);

        JLabel breakLabel = new JLabel("Break Duration (minutes):");
        breakLabel.setFont(new Font("Arial", Font.BOLD, 14));
        breakLabel.setForeground(TEXT);
        panel.add(breakLabel);

        JTextField breakField = new JTextField(String.valueOf(breakMinutes));
        breakField.setFont(new Font("Arial", Font.BOLD, 15));
        breakField.setBackground(BACKGROUND);
        breakField.setForeground(TEXT);
        breakField.setBorder(BorderFactory.createLineBorder(BACKGROUND, 2));
        panel.add(breakField);

        JLabel roundsLabel = new JLabel("Total Rounds:");
        roundsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roundsLabel.setForeground(TEXT);
        panel.add(roundsLabel);

        JTextField roundsField = new JTextField(String.valueOf(totalRounds));
        roundsField.setFont(new Font("Arial", Font.BOLD, 15));
        roundsField.setBackground(BACKGROUND);
        roundsField.setForeground(TEXT);
        roundsField.setBorder(BorderFactory.createLineBorder(BACKGROUND, 2));
        panel.add(roundsField);

        
        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Timer Settings",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int newWork = Integer.parseInt(workField.getText());
                int newBreak = Integer.parseInt(breakField.getText());
                int newRounds = Integer.parseInt(roundsField.getText());
                if (newRounds > 0) {
                    totalRounds = newRounds;
                    resetRounds();
                }
                if (newWork > 0 && newBreak > 0) {
                    workMinutes = newWork;
                    breakMinutes = newBreak;
                    timer = new Timer(workMinutes, breakMinutes);
                    updateDisplay();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Please enter positive numbers",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupTimerThread() {
        new Thread(() -> {
            while (true) {
                if (timer.isRunning()) {
                    if (timer.isSessionOver()) {
                        if (timer.isWorkSession()) {
                            currentRound++;
                            if (currentRound > totalRounds) {
                                SwingUtilities.invokeLater(() -> {
                                    int response = JOptionPane.showConfirmDialog(this,
                                            "Congratulations! You've completed all " + totalRounds
                                                    + " rounds!\nClose Pomodoro?",
                                            "Pomodoro Complete",
                                            JOptionPane.OK_CANCEL_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE);

                                    if (response == JOptionPane.OK_OPTION) {
                                        dispose();
                                        System.exit(0);
                                    } else {
                                        resetRounds();
                                        timer.reset();
                                        updateDisplay();
                                        updateButtons();
                                    }
                                });
                            }
                        }

                        timer.switchSession();
                        SwingUtilities.invokeLater(() -> {
                            sessionLabel.setText(timer.isWorkSession() ? "WORK SESSION" : "BREAK TIME!");
                            roundsLabel.setText("ROUND " + currentRound); // Improved text
                            Toolkit.getDefaultToolkit().beep();
                        });
                    }
                    SwingUtilities.invokeLater(this::updateDisplay);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }).start();
    }

    private void updateDisplay() {
        Duration remaining = timer.getRemainingTime();
        timeLabel.setText(String.format("%02d:%02d",
                remaining.toMinutes(),
                remaining.toSecondsPart()));
    }

    private void updateButtons() {
        startButton.setEnabled(!timer.isRunning());
        pauseButton.setEnabled(timer.isRunning());
    }

    private void resetRounds() {
        currentRound = 1;
        roundsLabel.setText("Round " + currentRound );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pomodoro timer = new Pomodoro();
            timer.setVisible(true);
        });
    }
}