package launcher;

import launcher.util.LaunchLogger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class License {

    public static File Cache;

    public static void setCacheLocation(Settings launcherCfg) {
        Cache = new File(launcherCfg.licenseCache);
    }

    public static void cache(String license) {
        if (!isCached()) {
            try {
                FileUtils.writeStringToFile(Cache, license);
            }
            catch (IOException e) {
                LaunchLogger.error("There was a problem caching your license. Please view launcher.log for more information.");
                LaunchLogger.exception(e);
            }
        }
    }

    public static boolean isCached() {
        return Cache.exists();
    }

    public static String getCached() {
        if (Cache.exists()) {
            try {
                return FileUtils.readFileToString(Cache);
            }
            catch (IOException e) {
                LaunchLogger.error("There was a problem reading the cached license. Please view launcher.log for more information.");
                LaunchLogger.exception(e);
            }
        }
        return null;
    }
}
