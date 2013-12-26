package launcher.workflow.launch;

import launcher.Settings;
import launcher.util.LaunchLogger;
import launcher.util.ProcessWatcher;
import launcher.workflow.WorkflowAction;
import launcher.workflow.WorkflowStep;

public class LaunchWorkflow {
    public static void begin(final Settings launcherCfg) {
        WorkflowStep prepare = new WorkflowStep("Preparing to launch the game", new WorkflowAction() {
            @Override
            public boolean act() {
                return true;
            }
        });

        WorkflowStep launchGame = new WorkflowStep("Preparing to launch the game", new WorkflowAction() {
            @Override
            public boolean act() {
                try {
                    Process p = Runtime.getRuntime().exec(launcherCfg.launchCommand);
                    ProcessWatcher errorWatcher = new ProcessWatcher(p.getErrorStream(), "ERROR");
                    ProcessWatcher outputWatcher = new ProcessWatcher(p.getInputStream(), "OUTPUT");
                    errorWatcher.start();
                    outputWatcher.start();
                    long startTime = System.currentTimeMillis();
                    long endTime = startTime;
                    while (endTime - startTime < launcherCfg.launcherDelayMilliseconds) {
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
        });

        prepare.setOnSuccess(launchGame);

        prepare.execute();
    }
}