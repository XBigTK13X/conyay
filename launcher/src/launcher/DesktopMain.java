package launcher;

import javax.swing.*;

public class DesktopMain {
    public static String ConfigPath;

    public static void main(String[] args) {
        if (args.length == 0) {
            ConfigPath = "launcher.cfg";
        }
        else {
            ConfigPath = args[0];
        }

        Settings settings = Settings.load(DesktopMain.ConfigPath);
        JFrame frame = new JFrame(settings.windowTitle);
        frame.setContentPane(new GUI(settings).getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(settings.windowWidth, settings.windowHeight);
        frame.getContentPane().setSize(settings.windowWidth, settings.windowHeight);

        //Center window in the middle of the screen
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}