package launcher.util;


import launcher.util.DesktopApi;
import launcher.util.LaunchLogger;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.swing.FSMouseListener;
import org.xhtmlrenderer.swing.LinkListener;

import javax.net.ssl.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class BrowserComponent {
    private static class HyperlinkHandler extends LinkListener {
        @Override
        public void linkClicked(org.xhtmlrenderer.swing.BasicPanel panel, java.lang.String uri) {
            final String safeUri = uri;
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        LaunchLogger.info("Opening link in a browser: " + safeUri);
                        DesktopApi.browse(new URI(safeUri));
                    }
                    catch (Exception e) {
                        LaunchLogger.exception(e);
                    }
                    return null;
                }
            };
            worker.execute();
        }
    }

    public static void initialize() {

    }

    private XHTMLPanel _news;

    public BrowserComponent() {
        _news = new XHTMLPanel();
    }

    public Component getSwingComponent() {
        return _news;
    }

    public void init(final Component container, final JFrame frame) {
        for (Object ml : _news.getMouseTrackingListeners()) {
            _news.removeMouseTrackingListener((FSMouseListener) ml);
        }
        _news.addMouseTrackingListener(new HyperlinkHandler());
    }

    // Taken from http://nanashi07.blogspot.tw/2014/06/enable-ssl-connection-for-jsoup.html
    public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public void load(final String url) {
        try {
            enableSSLSocket();
            //TODO Figure out a way to do this without writing to a temp file

            //The process here is (read HTML) -> (write HTML to file) -> (read file with flying-saucer) -> (f-s render)
            //There MUST be a simpler way to use flying-saucer, but I've spent ALL day trying out other web
            //components for swing. This is working (better than JEditorPane), so it is staying for now.
            Connection con = Jsoup.connect(url);
            con.ignoreHttpErrors(true);
            Document doc = con.get();
            String contents = doc.toString();
            File tmp = new File("news.html");
            FileUtils.write(tmp, contents);
            _news.setDocument(tmp);
            LaunchLogger.info("Finished loading latest news");

        }
        catch (Exception e) {
            LaunchLogger.error("The latest news could not be loaded.");
            LaunchLogger.error(e.getMessage());
        }
    }
}
