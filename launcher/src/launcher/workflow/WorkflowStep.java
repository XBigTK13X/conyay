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
                try {
                    proceed(_action.act() ? _onSuccess : _onFailure);
                }
                catch (Exception e) {
                    if (e.getMessage().contains("Server returned HTTP response code")) {
                        LaunchLogger.error("There was a problem contacting the server.");
                    }
                    else {
                        LaunchLogger.error("The action was canceled, an exception occurred.");
                    }
                    LaunchLogger.exception(e);
                    proceed(_onFailure);
                }
                return null;
            }
        };
        worker.execute();
    }

    protected void proceed(WorkflowStep step) {
        if (step != null) {
            step.execute();
        }
        else {
            //This should only be called after the final step in a workflow
            WorkflowInput.setEnabled(true);
        }
    }

    public void setOnSuccess(WorkflowStep step) {
        _onSuccess = step;
    }

    public void setOnFailure(WorkflowStep step) {
        _onFailure = step;
    }
}
