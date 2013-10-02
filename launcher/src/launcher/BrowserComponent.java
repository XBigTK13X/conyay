package launcher;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.net.URI;

public class BrowserComponent {
    private WebEngine _engine;
    private JFXPanel _news;
    private final String _newsUrl;

    public BrowserComponent(final String url) {
        _newsUrl = url;
    }

    public Component getSwingComponent() {
        return _news;
    }

    public void init(final Component container) {
        _news = new JFXPanel();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Group group = new Group();
                Scene scene = new Scene(group);
                _news.setScene(scene);

                WebView webView = new WebView();
                group.getChildren().add(webView);
                webView.setMaxSize(container.getWidth() * .98, container.getHeight() * .98);

                _engine = webView.getEngine();
                _engine.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                        if (newValue.equalsIgnoreCase(_newsUrl)) {
                            load(newValue);
                            LaunchLogger.info("Latest news has been loaded");
                        }
                        else {
                            try {
                                Desktop.getDesktop().browse(new URI(newValue));
                                load(oldValue);
                            }
                            catch (Exception swallow) {

                            }
                        }

                    }
                }

                );
            }
        });
    }

    public void load(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LaunchLogger.info("Loading URL: " + url);
                _engine.load(url);
            }
        });
    }
}
