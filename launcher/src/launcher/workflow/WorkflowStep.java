package launcher.workflow;

import launcher.util.LaunchLogger;

import javax.swing.*;

public class WorkflowStep {
    protected WorkflowStep _onSuccess;
    protected WorkflowStep _onFailure;
    protected WorkflowAction _action;

    protected String _startMessage;

    public WorkflowStep(String start, WorkflowAction action) {
        _startMessage = start;
        _action = action;
    }

    public void execute() {
        LaunchLogger.info(_startMessage);
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if (_action.act()) {
                    if (_onSuccess != null) {
                        _onSuccess.execute();
                    }
                }
                else {
                    if (_onFailure != null) {
                        _onFailure.execute();
                    }
                }
                return null;
            }
        };
        worker.execute();
    }

    public void setOnSuccess(WorkflowStep step) {
        _onSuccess = step;
    }

    public void setOnFailure(WorkflowStep step) {
        _onFailure = step;
    }
}
