package launcher;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.util.UUID;

public class Logs {
    public static void upload(final Settings launcherCfg, final String license) {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                LaunchLogger.info("Preparing to upload logs.");
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        String licenseId = license;
                        if (license.isEmpty()) {
                            licenseId = "nolicense";
                        }

                        final String logID = launcherCfg.windowTitle.replaceAll(" ", "") + "--" + licenseId + "--" + UUID.randomUUID();

                        final File logsArchive = new File(logID + ".zip");

                        final File logs = new File(launcherCfg.logsDirectory);
                        if (!logs.exists()) {
                            LaunchLogger.error("No logs were found to upload.");
                            return null;
                        }

                        LaunchLogger.info("Archiving logs directory.");
                        SwingWorker worker = new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {

                                Archive.zipDir(logs, logsArchive);

                                LaunchLogger.info("Uploading log archive.");
                                SwingWorker worker = new SwingWorker() {
                                    @Override
                                    protected Object doInBackground() throws Exception {
                                        REST.fileUpload(logsArchive.getAbsoluteFile(), launcherCfg.logUploadApi);

                                        try {
                                            FileUtils.forceDelete(logsArchive);
                                        }
                                        catch (Exception e) {
                                            LaunchLogger.exception(e);
                                        }

                                        LaunchLogger.info("Thank you for uploading your logs!");
                                        LaunchLogger.info("Log ID: " + logID);

                                        return null;
                                    }
                                };
                                worker.execute();

                                return null;
                            }
                        };
                        worker.execute();
                        return null;
                    }
                };
                worker.execute();
                return null;
            }
        };
        worker.execute();
    }
}
