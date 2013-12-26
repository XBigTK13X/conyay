package launcher.util;

import javax.swing.*;

public class LaunchLogger {

    public static String Tab = "&nbsp;&nbsp;&nbsp;&nbsp;";
    public static String Token = "---";

    private static JEditorPane __logArea;

    public static void setLogArea(JEditorPane logArea) {
        __logArea = logArea;
    }

    public static void error(final String message) {
        info(message, "<span style=\"color:red;\">" + message + "</span>");
    }

    public static void info(final String message) {
        info(message, message);
    }

    private static void info(final String message, final String styledMessasge) {
        System.out.println(message.replace(Tab, "    "));

        if (__logArea != null) {
            String log = __logArea.getText();
            if (log.isEmpty()) {
                log = Token;
            }
            if (!log.contains(Token)) {
                log = log.replace("<body>", "<body>" + Token);
            }
            log = log.replace(Token, styledMessasge + "<br/>" + Token);
            __logArea.setText(log);
        }
    }

    public static void exception(Exception e) {
        System.out.println(e.getMessage());
        for (StackTraceElement ste : e.getStackTrace()) {
            System.out.println("  " + ste.toString());
        }
    }
}
