package launcher.workflow.update;

import launcher.License;
import launcher.Settings;
import launcher.util.Archive;
import launcher.util.LaunchLogger;
import launcher.workflow.WorkflowAction;
import launcher.workflow.WorkflowStep;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UpdateWorkflow {
    public static void begin(final Settings launcherCfg, final String license) {
        WorkflowStep prepare = new WorkflowStep("Preparing to launch the game", new WorkflowAction() {
            @Override
            public boolean act() {
                return true;
            }
        });
        WorkflowStep checkLicense = new WorkflowStep("Checking to see if a license has been entered.", new WorkflowAction() {
            @Override
            public boolean act() throws Exception {
                if (license == null || license.isEmpty()) {
                    LaunchLogger.error(LaunchLogger.Tab + "No valid license found.");
                    return false;
                }

                URL licenseCheckUrl = new URL(launcherCfg.licenseCall(license));
                String response = IOUtils.toString(licenseCheckUrl.openStream());
                if (response.contains("true")) {
                    LaunchLogger.info(LaunchLogger.Tab + "License is valid.");
                    License.cache(license);
                    return true;
                }
                return false;
            }
        });

        WorkflowStep checkVersion = new WorkflowStep("Checking for updates.", new WorkflowAction() {
            @Override
            public boolean act() throws Exception {
                File versionPath = new File("assets/data/version.dat");
                String myVersion = "0.0.0";
                if (versionPath.exists()) {
                    myVersion = FileUtils.readFileToString(versionPath);
                    LaunchLogger.info("Detected version: " + myVersion);
                }
                URL versionCheckUrl = new URL(launcherCfg.versionCall(myVersion));

                String result = IOUtils.toString(versionCheckUrl.openStream());
                if (result.contains("true")) {
                    LaunchLogger.info(LaunchLogger.Tab + "Local copy of the game is out of date.");
                    return true;
                }
                LaunchLogger.info(LaunchLogger.Tab + "Local copy of the game is up to date. No update required.");
                return false;
            }
        });

        WorkflowStep downloadUpdate = new WorkflowStep("Preparing the update location", new WorkflowAction() {
            @Override
            public boolean act() throws Exception {
                if (UpdateWorkflowData.UpdateArchive.exists()) {
                    FileUtils.forceDelete(UpdateWorkflowData.UpdateArchive);
                }

                int responseTimeoutMs = launcherCfg.responseTimeoutMilliseconds;
                int downloadTimeoutMs = launcherCfg.downloadTimeoutMilliseconds;
                LaunchLogger.info("Attempting to download an update using license: [" + license + "]");

                String downloadUrl = launcherCfg.downloadCall(license, "update");
                LaunchLogger.info("Downloading latest stable edition");
                FileUtils.copyURLToFile(new URL(downloadUrl), UpdateWorkflowData.UpdateArchive, responseTimeoutMs, downloadTimeoutMs);

                LaunchLogger.info(LaunchLogger.Tab + "Update downloaded successfully.");
                return true;
            }
        });

        WorkflowStep applyUpdate = new WorkflowStep("Unpacking update archive", new WorkflowAction() {
            @Override
            public boolean act() throws IOException {
                Archive.unzip(UpdateWorkflowData.UpdateArchive, UpdateWorkflowData.UpdateWorkingDirectory);
                LaunchLogger.info("Replacing old content");
                File game = new File(UpdateWorkflowData.UpdateWorkingDirectory + "/game.jar");
                File gameTarget = new File("./");
                LaunchLogger.info("Attempting to copy: " + game + " to " + gameTarget);
                FileUtils.copyFileToDirectory(game, gameTarget);

                File assets = new File(UpdateWorkflowData.UpdateWorkingDirectory + "/assets");
                File assetsTarget = new File("./assets");
                LaunchLogger.info("Attempting to copy: " + assets + " to " + assetsTarget);
                FileUtils.copyDirectory(assets, assetsTarget);

                LaunchLogger.info(LaunchLogger.Tab + "Update applied successfully.");
                return true;
            }
        });

        WorkflowStep clean = new WorkflowStep("Cleaning up temporary files", new WorkflowAction() {
            @Override
            public boolean act() throws IOException {
                if (UpdateWorkflowData.UpdateArchive.exists()) {
                    FileUtils.forceDelete(UpdateWorkflowData.UpdateArchive);
                }
                if (UpdateWorkflowData.UpdateWorkingDirectory.exists()) {
                    FileUtils.deleteDirectory(UpdateWorkflowData.UpdateWorkingDirectory);
                }
                return true;
            }
        });

        prepare.setOnSuccess(checkLicense);
        checkLicense.setOnSuccess(checkVersion);
        checkVersion.setOnSuccess(downloadUpdate);
        downloadUpdate.setOnSuccess(applyUpdate);
        applyUpdate.setOnSuccess(clean);

        prepare.setOnFailure(clean);
        checkLicense.setOnFailure(clean);
        checkVersion.setOnFailure(clean);
        downloadUpdate.setOnFailure(clean);
        applyUpdate.setOnFailure(clean);

        prepare.execute();
    }
}