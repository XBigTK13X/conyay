package launcher;
/*
Created using the NetBeans GUI designer
*/

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public abstract class GuiWindow extends javax.swing.JFrame {
    private WebEngine _engine;
    private JFXPanel _news;

    private JButton _launch;
    private JEditorPane _log;
    private JLabel _licenseLbl;
    private JScrollPane _newsContainer;
    private JScrollPane _logContainer;
    private JTextField _license;
    private int _width;
    private int _height;
    private final String _newsUrl;

    public GuiWindow(int width, int height, String newsUrl) {
        _width = width;
        _height = height;
        _newsUrl = newsUrl;
        initComponents();
    }


    public JTextField getLicense() {
        return _license;
    }

    public JEditorPane getLog() {
        return _log;
    }

    private int w(double percent) {
        return (int) (_width * percent);
    }

    private int h(double percent) {
        return (int) (_height * percent);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        _news = new JFXPanel();

        _newsContainer = new JScrollPane();
        _logContainer = new JScrollPane();
        _log = new JEditorPane();
        _license = new JTextField();
        _licenseLbl = new javax.swing.JLabel();
        _launch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        _newsContainer.setViewportView(_news);

        _logContainer.setViewportView(_log);

        _licenseLbl.setText("License");

        _launch.setText("Launch");

        _launch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchBtnAction(evt);
            }
        });

        _log.setContentType("text/html");
        _log.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(_licenseLbl))
                                                        .addComponent(_license, javax.swing.GroupLayout.PREFERRED_SIZE, w(.3), javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_launch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(_newsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, w(.6), Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(_logContainer, javax.swing.GroupLayout.PREFERRED_SIZE, w(.4), javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(_newsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, h(.8), Short.MAX_VALUE)
                                        .addComponent(_logContainer))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(_licenseLbl)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_license, javax.swing.GroupLayout.PREFERRED_SIZE, h(.1), javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(_launch, javax.swing.GroupLayout.PREFERRED_SIZE, h(.1), javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(_news);
            }
        });

        pack();

    }

    private void initFX(final JFXPanel fxPanel) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Group group = new Group();
                Scene scene = new Scene(group);
                fxPanel.setScene(scene);

                WebView webView = new WebView();
                group.getChildren().add(webView);
                webView.setMaxSize(_newsContainer.getWidth() * .98, _newsContainer.getHeight() * .98);

                _engine = webView.getEngine();
                _engine.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                        if (newValue.equalsIgnoreCase(_newsUrl))
                            loadUrl(newValue);
                        else {
                            try {
                                Desktop.getDesktop().browse(new URI(newValue));
                                loadUrl(oldValue);
                            }
                            catch (Exception swallow) {

                            }
                        }

                    }
                }

                );

            }
        }

        );
    }

    public void loadUrl(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                _engine.load(url);
            }
        });
    }

    abstract void launchBtnAction(java.awt.event.ActionEvent evt);
}
