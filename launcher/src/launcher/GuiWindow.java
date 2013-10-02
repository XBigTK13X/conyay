package launcher;
/*
Created using the NetBeans GUI designer
*/

import javax.swing.*;

public abstract class GuiWindow extends javax.swing.JFrame {
    private BrowserComponent _news;
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

    public void init(JFrame frame) {
        _news = new BrowserComponent();
        _newsContainer = new JScrollPane();
        _logContainer = new JScrollPane();
        _log = new JEditorPane();
        _license = new JTextField();
        _licenseLbl = new javax.swing.JLabel();
        _launch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        _newsContainer.setViewportView(_news.getSwingComponent());

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
        _news.init(_newsContainer, frame);
        pack();
    }

    public void loadUrl(final String url) {
        _news.load(url);
    }

    abstract void launchBtnAction(java.awt.event.ActionEvent evt);
}
