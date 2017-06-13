/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DownloadManagerGUI.java
 *
 * Created on Sep 13, 2011, 8:34:17 PM
 */
package GUI;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import downloadmanager.Download;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author rayat
 */
public class DownloadManagerGUI extends javax.swing.JFrame implements Observer {

    private DownloadsTableModel tableModel = new DownloadsTableModel();
    private Download selectedDownload;
    private boolean toClear;

    /** Creates new form DownloadManagerGUI */
    public DownloadManagerGUI() throws FileNotFoundException, IOException, ClassNotFoundException {
        initComponents();
        this.setTitle("IUT DOWNLOAD MANAGER v1.0");
        ContainingFolderButton.setEnabled(false);
        OpenFileButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        cancelButton.setEnabled(false);
        clearButton.setEnabled(false);
        loadDestination();
        loadList();
        DownloadTable.setModel(tableModel);
        DownloadTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
        DownloadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true); // show progress text
        DownloadTable.setDefaultRenderer(JProgressBar.class, renderer);
    }

    private void tableSelectionChanged() {
        if (selectedDownload != null) {
            selectedDownload.deleteObserver(this);
        }

        if (!toClear && DownloadTable.getSelectedRow() > -1) {
            selectedDownload = tableModel.getDownload(DownloadTable.getSelectedRow());
            selectedDownload.addObserver(this);
            ChangeButtonState();
        }
    }

    private void ChangeButtonState() {
        if (selectedDownload != null) {
            int status = selectedDownload.getStatus();
            switch (status) {
                case Download.DOWNLOADING:
                    ContainingFolderButton.setEnabled(true);
                    OpenFileButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case Download.PAUSED:
                    ContainingFolderButton.setEnabled(true);
                    OpenFileButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(true);
                    break;
                case Download.ERROR:
                    ContainingFolderButton.setEnabled(true);
                    OpenFileButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
                    break;
                case Download.CANCELLED:
                    ContainingFolderButton.setEnabled(true);
                    OpenFileButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
                    break;
                default: // COMPLETE
                    ContainingFolderButton.setEnabled(true);
                    OpenFileButton.setEnabled(true);
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
            }
        } else {
            OpenFileButton.setEnabled(false);
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            cancelButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fileLabel1 = new javax.swing.JLabel();
        addURLTextField = new javax.swing.JTextField();
        SaveToButton = new javax.swing.JButton();
        addURLButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        DownloadTable = new javax.swing.JTable();
        resumeButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        OpenFileButton = new javax.swing.JButton();
        ContainingFolderButton = new javax.swing.JButton();
        ClearCompletedButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        ExitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        fileLabel1.setText("C:\\Users\\rayat\\Documents");

        addURLTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addURLTextFieldActionPerformed(evt);
            }
        });

        SaveToButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        SaveToButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/save.gif"))); // NOI18N
        SaveToButton.setText("Save To");
        SaveToButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveToButtonActionPerformed(evt);
            }
        });

        addURLButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        addURLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Capture (Custom).PNG"))); // NOI18N
        addURLButton.setText("Download");
        addURLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addURLButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        DownloadTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        DownloadTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DownloadTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DownloadTable);

        resumeButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        resumeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Play-Hot-icon (Custom).png"))); // NOI18N
        resumeButton.setText("Resume");
        resumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeButtonActionPerformed(evt);
            }
        });

        pauseButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/pause (Custom).jpg"))); // NOI18N
        pauseButton.setText("Pause");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Stop-Normal-Red-icon (Custom).png"))); // NOI18N
        cancelButton.setText("Stop Download");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        clearButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/remove (Custom).jpg"))); // NOI18N
        clearButton.setText("Remove Download");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setText("Download Location : ");

        OpenFileButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        OpenFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/openfiles (Custom).jpg"))); // NOI18N
        OpenFileButton.setText("Open File");
        OpenFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenFileButtonActionPerformed(evt);
            }
        });

        ContainingFolderButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        ContainingFolderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/images (Custom).jpg"))); // NOI18N
        ContainingFolderButton.setText("Open Containing Folder");
        ContainingFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContainingFolderButtonActionPerformed(evt);
            }
        });

        ClearCompletedButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        ClearCompletedButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/clear2 (Custom).PNG"))); // NOI18N
        ClearCompletedButton.setText("Clear Completed Downloads");
        ClearCompletedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearCompletedButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel2.setText("Add URL :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(addURLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(31, 31, 31)
                                        .addComponent(fileLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(SaveToButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addURLButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(6, 6, 6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ClearCompletedButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 369, Short.MAX_VALUE)
                                .addComponent(ContainingFolderButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(OpenFileButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addComponent(resumeButton)
                                .addGap(40, 40, 40)
                                .addComponent(pauseButton)
                                .addGap(33, 33, 33)
                                .addComponent(cancelButton)
                                .addGap(26, 26, 26)
                                .addComponent(clearButton))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fileLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SaveToButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(addURLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addURLButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ClearCompletedButton)
                    .addComponent(ContainingFolderButton))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(resumeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pauseButton)
                        .addComponent(cancelButton)
                        .addComponent(clearButton))
                    .addComponent(OpenFileButton))
                .addGap(39, 39, 39))
        );

        jMenu1.setText("File");

        ExitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        ExitMenuItem.setFont(new java.awt.Font("Segoe UI", 1, 12));
        ExitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Exit (Custom).png"))); // NOI18N
        ExitMenuItem.setText("Exit");
        ExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(ExitMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMenuItemActionPerformed
        try {
            // TODO add your handling code here:
            saveList();
            savedestination();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(1);
    }//GEN-LAST:event_ExitMenuItemActionPerformed

    private void addURLTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addURLTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addURLTextFieldActionPerformed

    private void addURLButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addURLButtonActionPerformed
        // TODO add your handling code here:
        addURL();
    }//GEN-LAST:event_addURLButtonActionPerformed
    private void addURL() {
        URL verifiedUrl = verifyUrl(addURLTextField.getText());

        if (verifiedUrl != null) {
            Download download = new Download(verifiedUrl, fileLabel1.getText());
            tableModel.addDownload(download);
            addURLTextField.setText(""); // reset add text field
            DownloadDiag d = new DownloadDiag(null, false);
            // DownloadFrame d = new DownloadFrame();
            d.setDownload(download);
            d.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private URL verifyUrl(String url) {
        if (!url.toLowerCase().startsWith("http://")) {
            return null;
        }

        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }

        if (verifiedUrl.getFile().length() < 2) {
            return null;
        }

        return verifiedUrl;
    }
    private void SaveToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveToButtonActionPerformed
        // TODO add your handling code here:
        saveTo();
    }//GEN-LAST:event_SaveToButtonActionPerformed

    private void resumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeButtonActionPerformed
        // TODO add your handling code here:

        downloadResume();
    }//GEN-LAST:event_resumeButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        // TODO add your handling code here:
        downloadPause();
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        downloadRemove();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        downloadCancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        try {
            try {
                saveList();
                savedestination();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        try {
            try {
                saveList();
                savedestination();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void OpenFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenFileButtonActionPerformed
        // TODO add your handling code here:
        openFile();
    }//GEN-LAST:event_OpenFileButtonActionPerformed

    private void ContainingFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContainingFolderButtonActionPerformed
        // TODO add your handling code here:
        OpenFolder();
    }//GEN-LAST:event_ContainingFolderButtonActionPerformed

    private void DownloadTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DownloadTableMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == MouseEvent.BUTTON1) {
            if (evt.getClickCount() == 2) {
                if (selectedDownload != null) {
                    selectedDownload.deleteObserver(this);
                }
                if (!toClear && DownloadTable.getSelectedRow() > -1) {
                    selectedDownload = tableModel.getDownload(DownloadTable.getSelectedRow());
                    selectedDownload.addObserver(this);
                    ChangeButtonState();

                    DownloadDiag d = new DownloadDiag(null, false);
                    d.setDownload(selectedDownload);
                    d.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_DownloadTableMouseClicked

    private void ClearCompletedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearCompletedButtonActionPerformed
        // TODO add your handling code here:
        clearCompletedDownload();
    }//GEN-LAST:event_ClearCompletedButtonActionPerformed

    private void OpenFolder() {
        Runtime rt = Runtime.getRuntime();
        String destination = "explorer " + selectedDownload.getSAVEDIR();
        System.out.println(destination);


        try {
            Process p = rt.exec(destination);



        } catch (IOException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private void openFile() {
        Runtime rt = Runtime.getRuntime();
        String dest = selectedDownload.getSAVEDIR();


        if (dest.length() == 3) {
            dest = dest.substring(0, 2);
            System.out.println(dest);


        }
        String destination = "explorer " + dest;
        String fileName = selectedDownload.getFILENAME();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        System.out.println(fileName);
        destination = destination + File.separator + fileName;
        System.out.println(destination);


        try {
            Process p = rt.exec(destination);



        } catch (IOException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private void downloadResume() {
        selectedDownload.resume();


    }

    private void downloadPause() {
        selectedDownload.pause();


    }

    private void downloadCancel() {
        selectedDownload.cancel();


    }

    private void downloadRemove() {
        toClear = true;
        tableModel.clearDownload(DownloadTable.getSelectedRow());
        toClear = false;
        selectedDownload = null;
        ChangeButtonState();


    }

    private void clearCompletedDownload() {
        tableModel.clearCompletedDownload();


    }

    private void saveTo() {
        JFileChooser filechooser = new JFileChooser();

        filechooser.setApproveButtonText("OK");

        filechooser.setDialogTitle("Save To");
        filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        filechooser.showOpenDialog(this);
        //int result = filechooser.showOpenDialog(this);
        File newFile = filechooser.getSelectedFile();
        System.out.println("new destination:" + newFile);


        if (newFile != null) {
            this.fileLabel1.setText(newFile.getPath());


        }
        System.out.println(fileLabel1.getText());


    }

    private void saveList() throws FileNotFoundException, IOException, ClassNotFoundException {
        DataOutputStream dstream = new DataOutputStream(new FileOutputStream("save.txt"));
        ArrayList<Download> downloadList = tableModel.getDownloadList();
        //System.out.println(downloadList.get(0).getUrl());
        //System.out.println(downloadList.get(1).getUrl());


        for (Download obj : downloadList) {
            if(obj.getStatus()==Download.ERROR)
            {
                obj.setFileName("");
                obj.setBytesDownloaded(0);
                obj.setElapsedTime(0);
                obj.setFileSize(0);
                obj.setRemainingTime(0);
                obj.setSaveDir("");
                obj.setStartTime(0);
            }
            dstream.writeUTF(obj.getUrl());
            dstream.writeUTF(obj.getSAVEDIR());
            dstream.writeInt(obj.getFILESIZE());
            dstream.writeInt(obj.getBYTESDOWNLOADED());
            dstream.writeInt(obj.getSTATUS());
            dstream.writeLong(obj.getSTARTTIME());
            dstream.writeUTF(obj.getFILENAME());
            dstream.writeLong(obj.getElapsedTime());
            dstream.writeLong(obj.getRemainingTime());
            dstream.writeLong(obj.getDownloadResumeTime());

        }
        System.out.println("list saved");


    }

    private void loadList() throws FileNotFoundException, IOException, ClassNotFoundException {
        DataInputStream dstream = new DataInputStream(new FileInputStream("save.txt"));
        //Object[] data = new Object[5];

        String s;
        int i;
        long l;
        try {
            while (true) {
                Download temp = new Download();
                temp.addObserver(this);
                s = dstream.readUTF();
                temp.setUrl(new URL(s));
                s = dstream.readUTF();
                temp.setSaveDir(s);
                i = dstream.readInt();
                temp.setFileSize(i);
                i = dstream.readInt();
                temp.setBytesDownloaded(i);
                i = dstream.readInt();
                temp.setStatus(i);
                l = dstream.readLong();
                temp.setStartTime(l);
                s = dstream.readUTF();
                temp.setFileName(s);
                l = dstream.readLong();
                temp.setElapsedTime(l);
                l = dstream.readLong();
                temp.setRemainingTime(l);
                l = dstream.readLong();
                temp.setDownloadResumeTime(l);
                tableModel.addDownload(temp);


            }

        } catch (EOFException ex) {
            //Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
            return;


        }
    }

    private void savedestination() {
        try {
            DataOutputStream dstream = new DataOutputStream(new FileOutputStream("dir.txt"));
            dstream.writeUTF(fileLabel1.getText());



        } catch (IOException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private void loadDestination() {
        DataInputStream dstream = null;


        try {
            dstream = new DataInputStream(new FileInputStream("dir.txt"));


            while (true) {
                try {
                    String dest = dstream.readUTF();
                    fileLabel1.setText(dest);


                } catch (EOFException ex) {
                    return;



                } catch (IOException ex) {
                    Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }



            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dstream.close();



            } catch (IOException ex) {
                Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

    }

    public void update(Observable o, Object arg) {
        if (selectedDownload != null && selectedDownload.equals(o)) {
            ChangeButtonState();


        }
    }
    //  private

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    try {
                        UIManager.setLookAndFeel(new NimbusLookAndFeel());



                    } catch (UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    new DownloadManagerGUI().setVisible(true);



                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DownloadManagerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }


            }
        });


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ClearCompletedButton;
    private javax.swing.JButton ContainingFolderButton;
    private javax.swing.JTable DownloadTable;
    private javax.swing.JMenuItem ExitMenuItem;
    private javax.swing.JButton OpenFileButton;
    private javax.swing.JButton SaveToButton;
    private javax.swing.JButton addURLButton;
    private javax.swing.JTextField addURLTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel fileLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton resumeButton;
    // End of variables declaration//GEN-END:variables
}


