package launcher;

import java.io.File;

public class DesktopMain {
    public static String ConfigPath;

    public static void main(String[] args) {
        if (args.length == 0) {
            ConfigPath = "launcher.cfg";
        }
        else {
            ConfigPath = args[0];
        }
        LaunchWindow launchWindow = new LaunchWindow();
        launchWindow.show();
    }
}

