package launcher;

import launcher.gui.WindowManager;
import launcher.util.BrowserComponent;
import launcher.util.DesktopApi;
import launcher.util.LaunchLogger;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyEvent;

public class DesktopMain {
    public static String ConfigPath;

    public static void main(String[] args) {
        if (args.length == 0) {
            ConfigPath = "launcher.cfg";
        }
        else {
            ConfigPath = args[0];
        }

        //Attempt to set UI to Nimbus, use default if it isn't installed
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    //Nimbus doesn't use the standard copy/paste chords on Mac OS X.
                    //This corrects that behavior.
                    if (DesktopApi.getOs() == DesktopApi.EnumOS.macos) {
                        InputMap im = (InputMap) UIManager.get("TextField.focusInputMap");
                        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
                        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
                        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.META_DOWN_MASK), DefaultEditorKit.cutAction);
                        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.META_DOWN_MASK), DefaultEditorKit.selectAllAction);
                    }
                    break;
                }
            }
        }
        catch (Exception ex) {
            LaunchLogger.exception(ex);
        }

        BrowserComponent.initialize();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Settings settings = Settings.load(DesktopMain.ConfigPath);

                JFrame frame = new JFrame(settings.windowTitle);
                WindowManager gui = new WindowManager(settings);
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