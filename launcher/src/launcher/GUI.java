package launcher;

import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URL;

public class GUI {

    private GuiWindow _window;
    private Settings _cfg;
    private Updater _updater;

    public GUI(Settings settings) {
        _cfg = settings;
        _updater = new Updater(_cfg);
        _window = new GuiWindow(_cfg.windowWidth, _cfg.windowHeight) {
            @Override
            void launchBtnAction(ActionEvent evt) {
                updateAndRunGame();
            }
        };

        if (_updater.licenseIsCached()) {
            _window.getLicense().setVisible(false);
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
                InputStream in = null;
                try {
                    in = new URL(_cfg.newsUrl()).openStream();
                    String news = IOUtils.toString(in);
                    _window.getNews().setText(news);
                    LaunchLogger.info("Finished loading latest news");
                }
                catch (Exception e) {
                    _window.getNews().setText("The game will still launch, but the latest news could not be loaded.");
                }
                finally {
                    if (in != null) {
                        IOUtils.closeQuietly(in);
                    }
                }
                return null;
            }
        };
        worker.execute();
    }

    private void updateAndRunGame() {
        LaunchLogger.info("Preparing to launch the game.");
        String license = _updater.getCachedLicense();
        if (license == null) {
            license = _window.getLicense().getText().trim();
        }
        _updater.updateIfNeeded(license);
    }
}
