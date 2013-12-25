package launcher;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

public class Archive {
    private Archive() {
    }

    public static void zipFile(final File source, final File targetArchive) {
        if (source.isDirectory()) {
            throw new RuntimeException("Only a non-directory file can be passed into zipFile()");
        }
        zip(source, targetArchive, false);
    }

    public static void zipDir(final File source, final File targetArchive) {
        if (!source.isDirectory()) {
            throw new RuntimeException("Only a directory can be passed into zipDir()");
        }
        zip(source, targetArchive, true);
    }

    private static void zip(final File source, final File archive, final boolean sourceIsDir) {
        try {
            ZipFile zipFile = new ZipFile(archive);
            ZipParameters parameters = new ZipParameters();

            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);

            if (sourceIsDir) {
                zipFile.createZipFileFromFolder(source, parameters, false, 0);
            }
            else {
                zipFile.createZipFile(source, parameters);
            }

        }
        catch (Exception e) {
            LaunchLogger.exception(e);
        }
    }

    public static void unzip(final File sourceArchive, final File dest) {
        try {
            ZipFile zipFile = new ZipFile(sourceArchive);
            zipFile.extractAll(dest.getAbsolutePath());
        }
        catch (ZipException e) {
            LaunchLogger.exception(e);
        }
    }
}
