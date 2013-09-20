package launcher;

public class DesktopMain {
    public static String ConfigPath;

    public static void main(String[] args) {
        if (args.length == 0) {
            ConfigPath = "assets/data/launcher.cfg";
        }
        else {
            ConfigPath = args[0];
        }
        LaunchWindow launchWindow = new LaunchWindow();
        launchWindow.show();
    }
}

