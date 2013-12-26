package launcher.workflow;

import launcher.gui.WindowManager;

public class WorkflowWindowManager {
    private static WindowManager _windowManager;

    public static void register(WindowManager windowManager) {
        _windowManager = windowManager;
    }

    public static void setInputEnabled(boolean enabled) {
        if (_windowManager != null) {
            _windowManager.setInputEnabled(enabled);
        }
    }

    public static void setProgress(int percent) {
        if (_windowManager != null) {
            _windowManager.setProgress(percent);
        }
    }

    public static void setProgressVisible(boolean visible) {
        if (_windowManager != null) {
            _windowManager.setProgressVisible(visible);
        }
    }
}
