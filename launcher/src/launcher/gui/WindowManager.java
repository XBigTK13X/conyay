package launcher.gui;

import launcher.Settings;
import launcher.Updater;
import launcher.logs.UploadLogsWorkflow;
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

    public void init(JFrame frame) {
        _window.init(frame);
    }

    public Container getMainPanel() {
        return _window.getContentPane();
    }

    public void loadNews() {
        LaunchLogger.setLogArea(_window.getLog());

        LaunchLogger.info("Loading latest news...");
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
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
        UploadLogsWorkflow.newUpload(_cfg, getLicense());
    }

    private void updateAndRunGame() {
        _updater.updateIfNeeded(getLicense());
    }
}
