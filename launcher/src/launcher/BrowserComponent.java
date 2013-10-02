package launcher;


import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.swing.FSMouseListener;
import org.xhtmlrenderer.swing.LinkListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;

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

    public void load(final String url) {
        try {
            //TODO Figure out a way to do this without writing to a temp file

            //The process here is (read HTML) -> (write HTML to file) -> (read file with flying-saucer) -> (f-s render)
            //There MUST be a simpler way to use flying-saucer, but I've spent ALL day trying out other web
            //components for swing. This is working (better than JEditorPane), so it is staying for now.
            Document doc = Jsoup.connect(url).get();
            String contents = doc.toString();
            File tmp = new File("news.html");
            FileUtils.write(tmp, contents);
            _news.setDocument(tmp);
            LaunchLogger.info("Finished loading latest news");

        }
        catch (Exception e) {
            LaunchLogger.error("The latest news could not be loaded.");
            LaunchLogger.exception(e);
        }
    }
}
