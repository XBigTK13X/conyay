package launcher;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

import javax.swing.*;
import java.io.File;
import java.util.UUID;

public class Email {
    public static void sendLogs(final String license, final Settings launcherCfg) {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    File logs = new File(launcherCfg.logsDirectory);


                    String licenseId = license;
                    if (license.isEmpty()) {
                        licenseId = "nolicense";
                    }

                    String logID = UUID.randomUUID() + "." + licenseId;

                    File zipTarget = new File("./" + logID + ".zip");
                    Archive.zipDir(logs, zipTarget);

                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setPath(zipTarget.getAbsolutePath());
                    attachment.setDisposition(EmailAttachment.ATTACHMENT);
                    attachment.setDescription(logID + " " + launcherCfg.windowTitle + " Logs");
                    attachment.setName(launcherCfg.windowTitle + " Logs");

                    MultiPartEmail email = new MultiPartEmail();
                    email.setHostName("conyay.launcher.com");
                    email.addTo(launcherCfg.toEmailForLogs, launcherCfg.windowTitle + " Logs");
                    email.setFrom(logID + "@conyay.launcher.com", "Me");
                    email.setSubject(logID + " Logs");
                    email.setMsg("Logs attached");

                    email.attach(attachment);

                    email.send();

                    LaunchLogger.info("Your logs have been e-mailed. Thank you! Log ID is: " + logID);
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
