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
                String licenseId = license;
                if (license.isEmpty()) {
                    licenseId = "nolicense";
                }

                String logID = licenseId + "--" + UUID.randomUUID();

                File logsArchive = new File("./" + logID + ".zip");

                try {
                    File logs = new File(launcherCfg.logsDirectory);
                    if (!logs.exists()) {
                        LaunchLogger.error("No logs were found to upload");
                        return null;
                    }

                    Archive.zipDir(logs, logsArchive);

                    REST.fileUpload(logsArchive.getAbsoluteFile(), launcherCfg.logUploadApi);

                    LaunchLogger.info("Thank you for uploading your logs!");
                    LaunchLogger.info("Log ID: " + logID);
                }
                catch (Exception e) {
                    LaunchLogger.exception(e);
                }
                try {
                    FileUtils.forceDelete(logsArchive);
                }
                catch (Exception e) {
                    LaunchLogger.exception(e);
                }
                return null;
            }
        };
        worker.run();
    }
}
