package launcher.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class REST {
    public static void fileUpload(final File upload, final String postUrl) {
        try {
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(postUrl + upload.getName()).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");

            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(upload));

            long totalByte = FileUtils.sizeOf(upload);
            for (int i = 0; i < totalByte; i++) {
                os.write(fis.read());
            }

            os.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            httpUrlConnection.getInputStream()));

            String s;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
            in.close();
            fis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}