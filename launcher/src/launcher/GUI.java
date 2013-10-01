package launcher;

import org.apache.commons.io.IOUtils;

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
        _window = new GuiWindow() {
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
    }

    private void updateAndRunGame() {
        LaunchLogger.info("Preparing to launch the game.");
        String license = _updater.getCachedLicense();
        if (license == null) {
            license = _window.getLicense().getText().trim();
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
