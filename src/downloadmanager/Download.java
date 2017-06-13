/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloadmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.Observable;
import javax.swing.JOptionPane;

/**
 *
 * @author rayat
 */
public class Download extends Observable implements Runnable {

    private static final int MAX_BUFFER_SIZE = 1024;
    public static final String[] STATUSES = {"Downloading", "Paused", "Complete", "Cancelled",
        "Error"};
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    private URL url;
    private String saveDir; // Directory to save file
    private int fileSize;
    private int status;
    private int bytesDownloaded;
    private long startTime;
    private String fileName;
    private int intermediateBytesDownloaded;
    private long ElapsedTime;
    private long RemainingTime;
    private long ResumeStartTime;
    private long downloadResumeTime;

    public Download() {
    }

    public Download(URL url, String saveDir) {
        this.url = url;
        this.saveDir = saveDir;
        fileSize = -1;
        bytesDownloaded = 0;
        status = DOWNLOADING;
        ElapsedTime = 0;
        RemainingTime = 0;
        startTime = System.currentTimeMillis();
        downloadResumeTime = 0;
        System.out.println("Constructor is called");
        // Beginning Download
        download();

    }

    public URL getURL() {
        return url;
    }

    public String getUrl() {
        return url.toString();
    }

    public int getFILESIZE() {
        return fileSize;
    }

    public int getSTATUS() {
        return status;
    }

    public String getSAVEDIR() {
        return saveDir;
    }

    public String getFILENAME() {
        return fileName;
    }

    public int getBYTESDOWNLOADED() {
        return bytesDownloaded;
    }

    public long getSTARTTIME() {
        return startTime;
    }

    public long getRemainingTime() {
        return RemainingTime;
    }

    public String getFileSize() {
        float fileSizeinKB = (float) fileSize / (float) 1024;
        float fileSizeinMB = 0;
        String fileKB;
        String fileMB;
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        if (fileSizeinKB >= 1024) {
            fileSizeinMB = (float) fileSizeinKB / (float) 1024;
            fileMB = formatter.format(fileSizeinMB);
            if(fileMB.startsWith("-"))
            {
                fileMB = fileMB.substring(fileMB.lastIndexOf("-")+1);
            }
            return " " + fileMB + " MB ";
        } else {
            fileKB = formatter.format(fileSizeinKB);
             if(fileKB.startsWith("-"))
            {
                fileKB = fileKB.substring(fileKB.lastIndexOf("-")+1);
            }
            return " " + fileKB + " KB ";
        }
    }

    public int getStatus() {
        return status;
    }

    public float getDownloadProgress() {
        return ((float) bytesDownloaded / fileSize) * 100;
    }

    public String getBytesDownloaded() {
        float bytesDownloadedinKB = (float) bytesDownloaded / (float) 1024;
        float bytesDownloadedinMB = 0;
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        if (bytesDownloadedinKB >= 1024) {
            bytesDownloadedinMB = (float) bytesDownloadedinKB / (float) 1024;
            return " " + formatter.format(bytesDownloadedinMB) + " MB ";
        } else {
            return " " + formatter.format(bytesDownloadedinKB) + " KB ";
        }

    }

    public String getFileName(URL url) {
        setFileName(url.getFile());
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

// NOTIFYING THE OBSERVERS
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }

    public String getDownloadSpeed() {

        float currentSpeed;

        if (bytesDownloaded > 0) {
            currentSpeed = (float) (intermediateBytesDownloaded / (float) (getDownloadResumeTime()));
        } else {
            currentSpeed = 0;
        }
       
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);

        return " " + formatter.format(currentSpeed) + " KB/s ";

    }

    public long getElapsedTime() {
        return ElapsedTime;
    }

    public String getElapsedtime() {
        return " " + millisecondsToString(ElapsedTime) + " ";
    }

    public String getRequiredTime() {
       
        if (bytesDownloaded > 0) {         
                RemainingTime =  (long)((fileSize *ElapsedTime)/bytesDownloaded) - ElapsedTime;
        } else {
            RemainingTime = 0;
        }

        return " " + millisecondsToString(RemainingTime) + " ";

    }

    public void pause() {
        setStatus(PAUSED);
        stateChanged();
    }

    public void resume() {
        setStatus(DOWNLOADING);
        stateChanged();
        download();
    }

    public void cancel() {
        setStatus(CANCELLED);
        stateChanged();
    }

    private void error() {
        setStatus(ERROR);
        stateChanged();
    }

    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;     
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + bytesDownloaded + "-");

            System.out.println("Making connection");
            //Connecting to the internet server
            connection.connect();
            System.out.println("Connected!");
            int responseCode = connection.getResponseCode();
            System.out.println("Response code from server=" + responseCode);

            //     responseCode must be within 200
            if (responseCode / 100 != 2) {
                error();
            }
            // Check for valid content length.
            
            int contentLength = connection.getContentLength();
            System.out.println("Content length=" + contentLength);
            if (contentLength < 1) {
                error();
            }
            if (fileSize == -1) {
                setFileSize(contentLength);
                stateChanged();
            }
            // Open file and seek to the end of it.
            file = new RandomAccessFile(saveDir + File.separator + this.getFileName(url),"rw");
            file.seek(bytesDownloaded);
            System.out.println("Get InputStream");
            stream = connection.getInputStream();                             
            intermediateBytesDownloaded = 0;
            ResumeStartTime = System.currentTimeMillis();
            ElapsedTime+=ElapsedTime;
            while (status == DOWNLOADING) {
                /*
                 * Size of buffer according to how much of the file is left to download.
                 */
                byte buffer[];
                if (fileSize - bytesDownloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[fileSize - bytesDownloaded];
                }

                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }
                // Write buffer to file.
                file.write(buffer, 0, read);
                bytesDownloaded+= read;
                // System.out.println(bytesDownloaded);
                intermediateBytesDownloaded+=read;
                ElapsedTime = System.currentTimeMillis() - startTime;
                System.out.println(System.currentTimeMillis());
                System.out.println(startTime);
                System.out.println(ElapsedTime);
                setDownloadResumeTime(System.currentTimeMillis() - ResumeStartTime);
                stateChanged();
            }
           
            if (status == DOWNLOADING) {
                setStatus(COMPLETE);
                stateChanged();
            }
        } catch (UnknownHostException ex) {
            System.out.println("Error=" + ex);
            error();
            // ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot Connect to the Host");
        }catch(ConnectException ex)
        {
            System.out.println("Error=" + ex);
            ex.printStackTrace();
            error();
            JOptionPane.showMessageDialog(null, "Connection Timed Out");
        }
        catch (Exception ex) {
            System.out.println("Error=" + ex);
            ex.printStackTrace();
            error();
        } finally {

// Close file.
            if (file != null) {
                try {               
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

// Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String millisecondsToString(long time) {
        int milliseconds = (int) (time % 1000);
        int seconds = (int) ((time / 1000) % 60);
        int minutes = (int) ((time / 60000) % 60);
        int hours = (int) ((time / 3600000) % 24);

        String millisecondsStr = (milliseconds < 10 ? "00" : (milliseconds < 100 ? "0" : "")) + milliseconds;
        String secondsStr = (seconds < 10 ? "0" : "") + seconds;
        String minutesStr = (minutes < 10 ? "0" : "") + minutes;
        String hoursStr = (hours < 10 ? "0" : "") + hours;

        return new String(hoursStr + ":" + minutesStr + ":" + secondsStr);
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * @param saveDir the saveDir to set
     */
    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @param bytesDownloaded the bytesDownloaded to set
     */
    public void setBytesDownloaded(int bytesDownloaded) {
        this.bytesDownloaded = bytesDownloaded;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the intermediateBytesDownloaded
     */
    public int getIntermediateBytesDownloaded() {
        return intermediateBytesDownloaded;
    }

    /**
     * @param intermediateBytesDownloaded the intermediateBytesDownloaded to set
     */
    public void setIntermediateBytesDownloaded(int intermediateBytesDownloaded) {
        this.intermediateBytesDownloaded = intermediateBytesDownloaded;
    }

    /**
     * @return the ElapsedTime
     */
    /**
     * @param ElapsedTime the ElapsedTime to set
     */
    public void setElapsedTime(long ElapsedTime) {
        this.ElapsedTime = ElapsedTime;
    }

    /**
     * @return the RemainingTime
     */
    /**
     * @param RemainingTime the RemainingTime to set
     */
    public void setRemainingTime(long RemainingTime) {
        this.RemainingTime = RemainingTime;
    }

    /**
     * @return the downloadResumeTime
     */
    public long getDownloadResumeTime() {
        return downloadResumeTime;
    }

    /**
     * @param downloadResumeTime the downloadResumeTime to set
     */
    public void setDownloadResumeTime(long downloadResumeTime) {
        this.downloadResumeTime = downloadResumeTime;
    }

    /**
     * @return the storeTime
     */



}
