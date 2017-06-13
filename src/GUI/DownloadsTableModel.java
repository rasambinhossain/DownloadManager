/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import downloadmanager.Download;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author rayat
 */
class DownloadsTableModel extends AbstractTableModel implements Observer {

    private static final String[] columnNames = {"URL", "Size", "Speed", "Progress", "Downloaded", "Time Left", "Status"};
    private static final Class[] columnClasses = {String.class, String.class, String.class, JProgressBar.class, String.class, String.class,
        String.class};
    private ArrayList<Download> downloadList = new ArrayList<Download>();

    public ArrayList<Download> getDownloadList() {
        return downloadList;
    }

    public void setDownloadList(ArrayList<Download> downloadList) {
        this.downloadList = downloadList;
    }

    public void addDownload(Download download) {
        download.addObserver(this);
        downloadList.add(download);

        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public Download getDownload(int row) {
        return (Download) downloadList.get(row);
    }

    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void clearCompletedDownload() {
        int i,count=0;
        System.out.println(downloadList.size());
        for (i = downloadList.size()-1; i>=0; i--) {
            if (downloadList.get(i).getStatus() == Download.COMPLETE) {
                downloadList.remove(i);
                count++;
                fireTableRowsDeleted(i,i);
            }
        }
        if(count==0)
        {
            JOptionPane.showMessageDialog(null,"No Completed Downloads");
            return;
        }
        JOptionPane.showMessageDialog(null,"Cleared All Completed Downloads");
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    public int getRowCount() {
        return downloadList.size();
    }

    public Object getValueAt(int row, int col) {
        Download download = downloadList.get(row);
        switch (col) {
            case 0:
                return download.getUrl();
            case 1:
                String size = download.getFileSize();
                return size;
            case 2:
                String downloadspeed = download.getDownloadSpeed();
                return downloadspeed;
            case 3:
                return new Float(download.getDownloadProgress());
            case 4:
                String downloaded = download.getBytesDownloaded();
                return downloaded;
            case 5:
                String timeLeft = download.getRequiredTime();
                return timeLeft;
            case 6:
                return Download.STATUSES[download.getStatus()];
        }
        return "";
    }

    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }
}


