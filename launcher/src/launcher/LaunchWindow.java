package launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LaunchWindow {

    //UI Elements
    static private JLabel messageArea;
    private JFrame guiFrame;
    private JPanel mainPanel;
    private JPanel secPanel;
    private JLabel licenseLbl;
    private JTextField licenseIpt;
    private JButton launchBtn;

    private Settings _cfg;

    Updater updater;

    public static boolean licenseExists() {
        return new File("license.dat").exists();
    }

    public LaunchWindow() {
        _cfg = Settings.load(DesktopMain.ConfigPath);
        updater = new Updater(_cfg);

    }

    private int dW(double percent) {
        return (int) (guiFrame.getWidth() * percent);
    }

    private int dH(double percent) {
        return (int) (guiFrame.getHeight() * percent);
    }

    private Dimension dim(double pW, double pH) {
        return new Dimension(dW(pW), dH(pH));
    }

    public static JLabel getMessageArea() {
        return messageArea;
    }

    public void show() {
        updater.licenseIsCached();

        guiFrame = new JFrame();
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle(_cfg.windowTitle);
        guiFrame.setSize(_cfg.windowWidth, _cfg.windowHeight);

        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);

        mainPanel = new JPanel();
        secPanel = new JPanel();
        licenseLbl = new JLabel("License");
        licenseIpt = new JTextField();
        messageArea = new JLabel();
        launchBtn = new JButton("Launch");

        licenseIpt.setPreferredSize(dim(.8, .1));
        messageArea.setPreferredSize(dim(.8, .5));
        launchBtn.setPreferredSize(dim(.8, .1));

        messageArea.setVerticalAlignment(JLabel.TOP);

        if (!updater.licenseIsCached()) {
            mainPanel.add(licenseLbl);
            mainPanel.add(licenseIpt);
        }

        secPanel.add(messageArea);

        guiFrame.add(mainPanel, BorderLayout.CENTER);
        guiFrame.add(secPanel, BorderLayout.NORTH);

        launchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                messageArea.setText("");
                updateAndRunGame();
            }
        });


        guiFrame.add(launchBtn, BorderLayout.SOUTH);
        guiFrame.setVisible(true);
    }

    private void updateAndRunGame() {
        LaunchLogger.info("Preparing to launch the game.");
        String license = updater.getCachedLicense();
        if (license == null) {
            license = licenseIpt.getText();
        }
        updater.runIfNeeded(license);
        if (runGame()) {
            System.exit(0);
        }
    }

    private boolean runGame() {
        try {
            Process p = Runtime.getRuntime().exec(_cfg.launchCommand);
            ProcessWatcher errorWatcher = new
                    ProcessWatcher(p.getErrorStream(), "ERROR");
            ProcessWatcher outputWatcher = new
                    ProcessWatcher(p.getInputStream(), "OUTPUT");
            errorWatcher.start();
            outputWatcher.start();
            long startTime = System.currentTimeMillis();
            long endTime = startTime;
            while (endTime - startTime < _cfg.launcherDelayMilliseconds) {
                endTime = System.currentTimeMillis();
            }
            if (errorWatcher.outputDetected()) {
                return false;
            }
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
            return false;
        }
        return true;
    }
}
