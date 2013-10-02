package launcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Updater {
    private final Settings _cfg;

    //Working update paths
    private File update = new File("update.zip");
    private File updateDir = new File("update");

    //License path
    private File licenseCache;

    public Updater(Settings settings) {
        _cfg = settings;
        licenseCache = new File(_cfg.licenseCache);
    }

    public void updateIfNeeded(final String license) {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if (checkLicense(license)) {
                    SwingWorker worker = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            if (checkVersion()) {
                                SwingWorker worker = new SwingWorker() {
                                    @Override
                                    protected Object doInBackground() throws Exception {
                                        if (downloadUpdate(license)) {
                                            SwingWorker worker = new SwingWorker() {
                                                @Override
                                                protected Object doInBackground() throws Exception {
                                                    applyUpdate();
                                                    cleanAndRun();
                                                    return null;
                                                }
                                            };
                                            worker.execute();
                                        }
                                        else {
                                            cleanAndRun();
                                        }
                                        return null;
                                    }
                                };
                                worker.execute();
                            }
                            else {
                                cleanAndRun();
                            }
                            return null;
                        }
                    };
                    worker.execute();
                }
                else {
                    cleanAndRun();
                }
                return null;
            }

            ;
        };
        worker.execute();
    }


    private void cacheLicense(String license) {
        if (!licenseIsCached()) {
            try {
                FileUtils.writeStringToFile(licenseCache, license);
            }
            catch (IOException e) {
                LaunchLogger.error("There was a problem caching your license. Please view launcher.log for more information.");
                LaunchLogger.exception(e);
            }
        }
    }

    public String getCachedLicense() {
        if (licenseCache.exists()) {
            try {
                return FileUtils.readFileToString(licenseCache);
            }
            catch (IOException e) {
                LaunchLogger.error("There was a problem reading the cached license. Please view launcher.log for more information.");
                LaunchLogger.exception(e);
            }
        }
        return null;
    }

    public boolean licenseIsCached() {
        return licenseCache.exists();
    }

    private boolean checkLicense(String license) {
        try {
            LaunchLogger.info("Checking to see if a license has been entered.");
            if (license != null && !license.isEmpty()) {
                URL licenseCheckUrl = new URL(_cfg.licenseCall(license));
                String response = IOUtils.toString(licenseCheckUrl.openStream());
                if (response.contains("true")) {
                    LaunchLogger.info(LaunchLogger.Tab + "License is valid.");
                    cacheLicense(license);
                    return true;
                }
            }
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
        LaunchLogger.error(LaunchLogger.Tab + "No valid license found.");
        return false;
    }

    private boolean checkVersion() {
        try {
            LaunchLogger.info("Checking for updates.");

            File versionPath = new File("assets/data/version.dat");
            String myVersion = "0.0.0";
            if (versionPath.exists()) {
                myVersion = FileUtils.readFileToString(versionPath);
                LaunchLogger.info("Detected version: " + myVersion);
            }
            URL versionCheckUrl = new URL(_cfg.versionCall(myVersion));

            String result = IOUtils.toString(versionCheckUrl.openStream());
            if (result.contains("true")) {
                LaunchLogger.info(LaunchLogger.Tab + "Local copy of the game is out of date.");
                return true;
            }
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
        LaunchLogger.info(LaunchLogger.Tab + "Local copy of the game is up to date. No update required.");
        return false;
    }

    private boolean downloadUpdate(String license) {
        try {
            LaunchLogger.info("Preparing the update location");

            if (update.exists()) {
                FileUtils.forceDelete(update);
            }

            int responseTimeoutMs = _cfg.responseTimeoutMilliseconds;
            int downloadTimeoutMs = _cfg.downloadTimeoutMilliseconds;
            LaunchLogger.info("Attempting to download an update using license: [" + license + "]");
            String spsLicenseUrl = _cfg.downloadCall(license);
            LaunchLogger.info("Downloading latest stable edition");
            FileUtils.copyURLToFile(new URL(spsLicenseUrl), update, responseTimeoutMs, downloadTimeoutMs);
        }
        catch (Exception e) {
            LaunchLogger.error(LaunchLogger.Tab + "There was a problem downloading the update.");
            LaunchLogger.exception(e);
        }
        LaunchLogger.info(LaunchLogger.Tab + "Update downloaded successfully.");
        return true;
    }

    private boolean applyUpdate() {
        try {
            Archive.unzip(update);
            LaunchLogger.info("Replacing old content");
            File updateAssets = new File(updateDir + "/assets");
            File baseAssets = new File("./");
            FileUtils.copyDirectoryToDirectory(updateAssets, baseAssets);

            File updateCore = new File(updateDir + "/game.jar");
            File baseCore = new File("game.jar");
            FileUtils.copyFile(updateCore, baseCore);
        }
        catch (Exception e) {
            LaunchLogger.error(LaunchLogger.Tab + "There was a problem applying the update.");
            LaunchLogger.exception(e);
        }
        LaunchLogger.info(LaunchLogger.Tab + "Update applied successfully.");
        return true;
    }

    private void cleanAndRun() {
        try {
            LaunchLogger.info("Cleaning up temporary files");
            if (update.exists()) {
                FileUtils.forceDelete(update);
            }
            if (updateDir.exists()) {
                FileUtils.deleteDirectory(updateDir);
            }
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
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
