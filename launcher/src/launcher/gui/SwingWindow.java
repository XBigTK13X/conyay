package launcher.gui;
/*
Originally created using the NetBeans GUI designer
*/

import launcher.util.BrowserComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class SwingWindow extends JFrame {
    private BrowserComponent _news;
    private JButton _launch;
    private JButton _logs;
    private JButton _update;
    private JEditorPane _log;
    private JLabel _licenseLbl;
    private JScrollPane _newsContainer;
    private JScrollPane _logContainer;
    private JTextField _license;
    private int _width;
    private int _height;

    public SwingWindow(int width, int height) {
        _width = width;
        _height = height;

        _news = new BrowserComponent();
        _newsContainer = new JScrollPane();
        _logContainer = new JScrollPane();
        _log = new JEditorPane();
        _license = new JTextField();
        _licenseLbl = new JLabel();
        _launch = new JButton();
        _logs = new JButton();
        _update = new JButton();
    }

    public void setInputEnabled(boolean available) {
        _update.setVisible(available);
        _launch.setVisible(available);
        _logs.setVisible(available);
    }

    public JTextField getLicense() {
        return _license;
    }

    public JLabel getLicenseLabel() {
        return _licenseLbl;
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

    public void init(JFrame frame) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        _newsContainer.setViewportView(_news.getSwingComponent());

        _logContainer.setViewportView(_log);

        _licenseLbl.setText("License");

        _launch.setText("Start Game");

        _launch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                launchBtnAction(evt);
            }
        });

        _logs.setText("Send Logs");

        _logs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendLogsBtnAction(evt);
            }
        });

        _update.setText("Update Game");

        _update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateBtnAction(evt);
            }
        });

        _log.setContentType("text/html");
        _log.setEditable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(_licenseLbl))
                                                        .addComponent(_license, GroupLayout.PREFERRED_SIZE, w(.3), GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)

                                                .addComponent(_logs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(_update, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(_launch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(_newsContainer, GroupLayout.DEFAULT_SIZE, w(.6), Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(_logContainer, GroupLayout.PREFERRED_SIZE, w(.4), GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(_newsContainer, GroupLayout.DEFAULT_SIZE, h(.8), Short.MAX_VALUE)
                                        .addComponent(_logContainer))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(_licenseLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_license, GroupLayout.PREFERRED_SIZE, h(.1), GroupLayout.PREFERRED_SIZE))
                                        .addComponent(_logs, GroupLayout.PREFERRED_SIZE, h(.1), GroupLayout.PREFERRED_SIZE)
                                        .addComponent(_update, GroupLayout.PREFERRED_SIZE, h(.1), GroupLayout.PREFERRED_SIZE)
                                        .addComponent(_launch, GroupLayout.PREFERRED_SIZE, h(.1), GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        _news.init(_newsContainer, frame);
        pack();
    }

    public void loadUrl(final String url) {
        _news.load(url);
    }

    abstract void launchBtnAction(ActionEvent evt);

    abstract void sendLogsBtnAction(ActionEvent evt);

    abstract void updateBtnAction(ActionEvent evt);
}