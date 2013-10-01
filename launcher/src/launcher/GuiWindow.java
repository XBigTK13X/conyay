package launcher;
/*
Created using the NetBeans GUI designer
*/

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

public abstract class GuiWindow extends javax.swing.JFrame {
    private JButton _launch;
    private JEditorPane _news;
    private JEditorPane _log;
    private JLabel _licenseLbl;
    private JScrollPane _newsContainer;
    private JScrollPane _logContainer;
    private JTextField _license;

    public GuiWindow() {
        initComponents();
    }


    public JTextField getLicense() {
        return _license;
    }

    public JEditorPane getNews() {
        return _news;
    }

    public JEditorPane getLog() {
        return _log;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        _newsContainer = new JScrollPane();
        _news = new JEditorPane();
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

        _news.setContentType("text/html");
        _log.setContentType("text/html");
        _news.setEditable(false);
        _log.setEditable(false);

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
                                                        .addComponent(_license, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_launch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(_newsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(_logContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(_newsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                                        .addComponent(_logContainer))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(_licenseLbl)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_license, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(_launch, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pack();
    }

    abstract void launchBtnAction(java.awt.event.ActionEvent evt);
}
