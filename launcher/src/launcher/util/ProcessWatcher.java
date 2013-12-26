package launcher.util;

import launcher.util.LaunchLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessWatcher extends Thread {
    InputStream is;
    String type;
    boolean hasOutput = false;

    public ProcessWatcher(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                String message = LaunchLogger.Tab + type + "> " + line;
                if (type == "ERROR") {
                    LaunchLogger.error(message);
                }
                else {
                    LaunchLogger.info(message);
                }
                hasOutput = true;
            }
        }
        catch (IOException ioe) {
            LaunchLogger.exception(ioe);
        }
    }

    public boolean outputDetected() {
        return hasOutput;
    }
}
