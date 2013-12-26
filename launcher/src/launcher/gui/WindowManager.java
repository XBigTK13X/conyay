package launcher.gui;

import launcher.License;
import launcher.Settings;
import launcher.util.LaunchLogger;
import launcher.workflow.WorkflowInput;
import launcher.workflow.launch.LaunchWorkflow;
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
                runGame();
            }

            @Override
            void sendLogsBtnAction(ActionEvent evt) {
                sendLogs();
            }

            @Override
            void updateBtnAction(ActionEvent evt) {
                updateGame();
            }
        };

        WorkflowInput.register(this);
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

    public void setInputEnabled(boolean enabled) {
        _window.setInputEnabled(enabled);
    }

    private String getLicense() {
        String license = License.getCached();
        if (license == null) {
            license = _window.getLicense().getText().trim();
        }
        return license;
    }

    private void sendLogs() {
        setInputEnabled(false);
        UploadLogsWorkflow.begin(_cfg, getLicense());
    }

    private void updateGame() {
        setInputEnabled(false);
        UpdateWorkflow.begin(_cfg, getLicense());
    }

    private void runGame() {
        setInputEnabled(false);
        LaunchWorkflow.begin(_cfg);
    }
}
