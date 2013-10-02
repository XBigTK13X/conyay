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

        //Attempt to set UI to Nimbux, use default if it isn't installed
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception ex) {
            java.util.logging.Logger.getLogger(GuiWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        BrowserComponent.initialize();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Settings settings = Settings.load(DesktopMain.ConfigPath);

                JFrame frame = new JFrame(settings.windowTitle);
                Gui gui = new Gui(settings);
                frame.setContentPane(gui.getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setSize(settings.windowWidth, settings.windowHeight);
                frame.getContentPane().setSize(settings.windowWidth, settings.windowHeight);

                //Center window in the middle of the screen
                frame.setLocationRelativeTo(null);

                frame.setVisible(true);

                gui.init(frame);

                gui.loadNews();
            }
        });
    }
}