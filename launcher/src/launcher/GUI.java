package launcher;

import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;

public class GUI {
    public static void main(String[] args) {

    }

    private JEditorPane _logArea;
    private JEditorPane _newsArea;
    private JTextField _licenseIpt;
    private JButton _launchBtn;
    private JPanel _mainPanel;

    private Settings _cfg;
    private Updater _updater;

    public GUI(Settings settings) {
        _cfg = settings;
        _updater = new Updater(_cfg);

        if (_updater.licenseIsCached()) {
            _licenseIpt.setVisible(false);
        }

        _launchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateAndRunGame();
            }
        });

        _newsArea.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            LaunchLogger.info("Opening link in a web browser.");
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        }
                        catch (Exception exception) {
                            LaunchLogger.exception(exception);
                        }
                    }
                }
            }
        });

        loadNews();
    }

    public JPanel getMainPanel() {
        return _mainPanel;
    }

    private void loadNews() {
        LaunchLogger.setLogArea(_logArea);
        LaunchLogger.info("Loading latest news...");
        InputStream in = null;
        try {
            in = new URL(_cfg.newsUrl()).openStream();
            String news = IOUtils.toString(in);
            _newsArea.setText(news);
            LaunchLogger.info("Finished loading latest news");
        }
        catch (Exception e) {
            _newsArea.setText("The game will still launch, but the latest news could not be loaded.");
        }
        finally {
            if (in != null) {
                IOUtils.closeQuietly(in);
            }
        }
    }

    private void updateAndRunGame() {
        LaunchLogger.info("Preparing to launch the game.");
        String license = _updater.getCachedLicense();
        if (license == null) {
            license = _licenseIpt.getText().trim();
        }
        _updater.runIfNeeded(license);
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
