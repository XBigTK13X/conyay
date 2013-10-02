package launcher;


import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class BrowserComponent {
    public static void initialize() {

    }

    private JEditorPane _news;

    public BrowserComponent() {
        _news = new JEditorPane();
    }

    public Component getSwingComponent() {
        return _news;
    }

    public void init(final Component container, final JFrame frame) {
        _news.setContentType("text/html");
        _news.setEditable(false);
        _news.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            LaunchLogger.info("Opening link in a web browser.");
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        }
                        catch (Exception exception) {
                            LaunchLogger.exception(exception);
                        }
                    }
                }
            }
        });
    }

    public void load(final String url) {
        try {

            InputStream in = null;
            try {
                in = new URL(url).openStream();
                String news = IOUtils.toString(in);
                _news.setText(news);
                LaunchLogger.info("Finished loading latest news");
            }
            catch (Exception e) {
                _news.setText("The game will still launch, but the latest news could not be loaded.");
            }
            finally {
                if (in != null) {
                    IOUtils.closeQuietly(in);
                }
            }
        }
        catch (Exception swallow) {
            LaunchLogger.info("The game can still be launched, but the latest news could not be loaded");
        }
    }
}
