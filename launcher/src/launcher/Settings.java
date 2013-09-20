package launcher;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Settings {
    public static Settings load(String configPath){
        try {
            Gson gson = new Gson();
            return gson.fromJson(FileUtils.readFileToString(new File(configPath)), Settings.class);
        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
        return null;
    }

    public String siteRoot;
    public String scriptRoot;
    public String infoPath;
    public String updateTempName;
    public String licenseCache;
    public String licenseApi;
    public String versionApi;
    public String downloadApi;
    public int downloadTimeoutMilliseconds;
    public int responseTimeoutMilliseconds;
    public int launcherDelayMilliseconds;
    public String launchCommand;
    public String windowTitle;
    public int windowWidth;
    public int windowHeight;

    private Settings() {

    }

    public String scriptUrl() {
        return siteRoot + scriptRoot;
    }

    public String licenseCall(String license) {
        return scriptUrl() + licenseApi + license;
    }

    public String versionCall(String version) {
        return scriptUrl() + versionApi + version;
    }

    public String downloadCall(String license) {
        return scriptUrl() + downloadApi + license;
    }
}
