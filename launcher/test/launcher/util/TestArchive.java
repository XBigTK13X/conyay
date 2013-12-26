package launcher.util;

import launcher.util.Archive;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class TestArchive {
    private static File targetFile = new File("./" + UUID.randomUUID());
    private static File targetDir = new File("./" + UUID.randomUUID());
    private static File targetSubdirParent = new File("./" + UUID.randomUUID());
    private static File targetSubdirChild = new File(targetSubdirParent.getAbsolutePath() + "/" + UUID.randomUUID());

    @BeforeClass
    public static void createSourceFiles() {
        try {
            FileUtils.writeStringToFile(targetFile, "Testing the archiver");

            FileUtils.forceMkdir(targetDir);
            FileUtils.forceMkdir(targetSubdirParent);
            FileUtils.forceMkdir(targetSubdirChild);

            FileUtils.copyFile(targetFile, new File(targetDir.getAbsolutePath() + "/" + UUID.randomUUID()));
            FileUtils.copyFile(targetFile, new File(targetSubdirParent.getAbsolutePath() + "/" + UUID.randomUUID()));
            FileUtils.copyFile(targetFile, new File(targetSubdirChild.getAbsolutePath() + "/" + UUID.randomUUID()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void deleteSourceFiles() {
        remove(targetFile);
        remove(targetDir);
        remove(targetSubdirParent);
    }

    private static void remove(File file) {
        try {
            FileUtils.forceDelete(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File randomZip() {
        return new File(UUID.randomUUID() + ".zip");
    }

    @Test
    public void testZipFile() {
        File archive = randomZip();
        Archive.zipFile(targetFile, archive);
        assertTrue(archive.exists());
        remove(archive);
    }

    @Test
    public void testZipDir() {
        File archive = randomZip();
        Archive.zipDir(targetDir, archive);
        assertTrue(archive.exists());
        remove(archive);
    }

    @Test
    public void testZipSubdir() {
        File archive = randomZip();
        Archive.zipDir(targetSubdirParent, archive);
        assertTrue(archive.exists());
        remove(archive);
    }

    @Test
    public void testUnzipSubdir() {
        File archive = randomZip();
        Archive.zipDir(targetSubdirParent, archive);
        assertTrue(archive.exists());

        File dest = new File("./" + UUID.randomUUID());
        Archive.unzip(archive, dest);
        assertTrue(dest.exists());

        remove(dest);
        remove(archive);
    }
}
