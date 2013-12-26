package launcher.gui;

import launcher.Settings;
import launcher.util.LaunchLogger;
import launcher.workflow.License;
import launcher.workflow.logs.UploadLogsWorkflow;
import launcher.workflow.update.UpdateWorkflow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WindowManager {

    private SwingWindow _window;
    private Settings _cfg;

    public WindowManager(Settings settings) {
        _cfg = settings;
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

        License.setCacheLocation(_cfg);
        if (License.isCached()) {
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
        String license = License.getCached();
        if (license == null) {
            license = _window.getLicense().getText().trim();
        }
        return license;
    }

    private void sendLogs() {
        UploadLogsWorkflow.begin(_cfg, getLicense());
    }

    private void updateAndRunGame() {
        UpdateWorkflow.begin(_cfg, getLicense());
    }
}
