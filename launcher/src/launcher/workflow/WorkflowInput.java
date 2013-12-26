package launcher.workflow;

import launcher.gui.WindowManager;

public class WorkflowInput {
    private static WindowManager _windowManager;

    public static void register(WindowManager windowManager) {
        _windowManager = windowManager;
    }

    public static void setEnabled(boolean enabled) {
        if (_windowManager != null) {
            _windowManager.setInputEnabled(enabled);
        }
    }
}
