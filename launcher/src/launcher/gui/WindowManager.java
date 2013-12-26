package launcher.gui;

import launcher.Logs;
import launcher.Settings;
import launcher.Updater;
import launcher.gui.SwingWindow;
import launcher.util.LaunchLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WindowManager {

    private SwingWindow _window;
    private Settings _cfg;
    private Updater _updater;

    public WindowManager(Settings settings) {
        _cfg = settings;
        _updater = new Updater(_cfg);
        _window = new SwingWindow(_cfg.windowWidth, _cfg.windowHeight) {
            @Override
            void launchBtnAction(ActionEvent evt) {
                updateAndRunGame();
            }

            @Override
            void sendLogsBtnAction(ActionEvent evt) {
                sendLogs();
            }
        };

        if (_updater.licenseIsCached()) {
            _window.getLicense().setVisible(false);
            _window.getLicenseLabel().setVisible(false);
        }
    }

    public Container getMainPanel() {
        return _window.getContentPane();
    }

    public void loadNews() {
        LaunchLogger.setLogArea(_window.getLog());

        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                LaunchLogger.info("Loading latest news...");
                _window.loadUrl(_cfg.newsUrl);

                return null;
            }
        };
        worker.execute();
    }

    private String getLicense() {
        String license = _updater.getCachedLicense();
        if (license == null) {
            license = _window.getLicense().getText().trim();
        }
        return license;
    }

    private void sendLogs() {
        Logs.upload(_cfg, getLicense());
    }

    private void updateAndRunGame() {
        _updater.updateIfNeeded(getLicense());
    }

    public void init(JFrame frame) {
        _window.init(frame);
    }
}
