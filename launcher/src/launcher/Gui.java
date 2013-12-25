package launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Gui {

    private GuiWindow _window;
    private Settings _cfg;
    private Updater _updater;

    public Gui(Settings settings) {
        _cfg = settings;
        _updater = new Updater(_cfg);
        _window = new GuiWindow(_cfg.windowWidth, _cfg.windowHeight, _cfg.newsUrl) {
            @Override
            void launchBtnAction(ActionEvent evt) {
                updateAndRunGame();
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
        Email.sendLogs(getLicense(), _cfg);
    }

    private void updateAndRunGame() {
        LaunchLogger.info("Preparing to launch the game.");
        _updater.updateIfNeeded(getLicense());
    }

    public void init(JFrame frame) {
        _window.init(frame);
    }
}
