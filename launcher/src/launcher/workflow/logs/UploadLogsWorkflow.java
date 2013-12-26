package launcher.workflow.logs;

import launcher.Settings;
import launcher.util.Archive;
import launcher.util.LaunchLogger;
import launcher.util.REST;
import launcher.workflow.WorkflowAction;
import launcher.workflow.WorkflowStep;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;

public class UploadLogsWorkflow {

    public static void begin(final Settings launcherCfg, final String license) {
        WorkflowStep prepare = new WorkflowStep("Preparing to upload logs.", new WorkflowAction() {
            @Override
            public boolean act() {
                String licenseId = license;
                if (license.isEmpty()) {
                    licenseId = "nolicense";
                }
                LogsWorkflowData.LogID = launcherCfg.windowTitle.replaceAll(" ", "") + "--" + licenseId + "--" + UUID.randomUUID();
                LogsWorkflowData.LogsArchive = new File(LogsWorkflowData.LogID + ".zip");
                LogsWorkflowData.Logs = new File(launcherCfg.logsDirectory);

                if (!LogsWorkflowData.Logs.exists()) {
                    LaunchLogger.error("No logs were found to upload.");
                    return false;
                }
                return true;
            }
        });

        WorkflowStep archive = new WorkflowStep("Archiving the logs directory", new WorkflowAction() {
            @Override
            public boolean act() {
                Archive.zipDir(LogsWorkflowData.Logs, LogsWorkflowData.LogsArchive);
                return true;
            }
        });

        WorkflowStep upload = new WorkflowStep("Uploading logs archive.", new WorkflowAction() {
            @Override
            public boolean act() throws Exception {
                REST.fileUpload(LogsWorkflowData.LogsArchive.getAbsoluteFile(), launcherCfg.logUploadApi);
                LaunchLogger.info("Thank you for uploading your logs!");
                LaunchLogger.info("Log ID: " + LogsWorkflowData.LogID);
                return true;
            }
        });

        WorkflowStep cleanup = new WorkflowStep("Cleaning up temporary files.", new WorkflowAction() {
            @Override
            public boolean act() throws Exception {
                FileUtils.forceDelete(LogsWorkflowData.LogsArchive);
                return true;
            }
        });

        prepare.setOnSuccess(archive);
        archive.setOnSuccess(upload);
        upload.setOnSuccess(cleanup);

        prepare.execute();
    }
}
