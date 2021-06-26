package com.pikkudev;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>Logger</h1>
 * The Logger class is used to record occurrences such as program start and end events,
 * errors, exceptions and custom messages in a log file, in HTML format, using a timestamp.
 *
 * @author Fábio Pereira
 */
public class Logger implements Closeable {

    private String fileName;
    private File logFile;
    private FileWriter writer;
    private Boolean isReady;
    private SimpleDateFormat dateFormat;
    private final String htmlTemplate;

    /**
     * Constructs a <i>Logger</i> object.
     */
    Logger() {
        fileName = "logfile.html";
        isReady = false;
        dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        htmlTemplate = "<!DOCTYPE html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> " +
                "<title>Log</title> <style> table { width: 90%; margin-left: auto; margin-right: auto; } " +
                "table,th,td { border: 1px solid black; border-collapse: collapse; } " +
                "tr.info { color: blue; } tr.status { color: green; } tr.error { color: red; }" +
                "th,td { padding: 10px; } .center { text-align: center; } </style> </head> <body> " +
                "<table> <tr> <th>Date</th> <th>Type</th> <th>Description</th> </tr> ";
    }

    /**
     * Sets this <i>Logger</i> object's file name field to the given string.
     * @param fileName a String to be mapped to the new file name
     */
    public void setFileName(String fileName) {
        if (fileName.endsWith(".html")) {
            this.fileName = fileName;
        } else {
            this.fileName = fileName + ".html";
        }
        isReady = false;
    }

    /**
     * Sets this <i>Logger</i> object's date format to the given string.
     * @param dateFormat a String to be mapped to the new date format
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    /**
     * Records an exception with a corresponding description in the log file.
     * @param exc an exception to logged in the log file
     * @param desc a description of the corresponding exception
     * @throws IOException if an I/O error occurs
     */
    public void logException(Exception exc, String desc) throws IOException {
        if (isReady) {
            writer.append("<tr class=\"error\"> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">" + exc + "</td> <td>" + desc +"</td> </tr> ");
        } else {
            start();
            logException(exc, desc);
        }
    }

    /**
     * Records an error with a corresponding description in the log file.
     * @param error an error to logged in the log file
     * @param desc a description of the corresponding error
     * @throws IOException if an I/O error occurs
     */
    public void logError(Error error, String desc) throws IOException {
        if (isReady) {
            writer.append("<tr class=\"error\"> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">" + error + "</td> <td>" + desc +"</td> </tr> ");
        } else {
            start();
            logError(error, desc);
        }
    }

    /**
     * Records an informative message in the log file.
     * @param desc a String to be logged in the log file
     * @throws IOException if an I/O error occurs
     */
    public void logMsg(String desc) throws IOException {
        if (isReady) {
            writer.append("<tr class=\"info\"> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">Info</td> <td>" + desc +"</td> </tr> ");
        } else {
            start();
            logMsg(desc);
        }
    }

    /**
     * Creates the <i>File</i> and <i>FileWriter</i> objects and records the start event in the log file.
     * @throws IOException if the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason, or if an I/O error occurs
     */
    public void start() throws IOException {
        logFile = new File(fileName);
        boolean logFileExists = logFile.exists();
        writer = new FileWriter(logFile, true);
        if (!logFileExists) {
            writer.write(htmlTemplate);
        }
        writer.append("<tr class=\"status\"> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">Status</td> <td>Program execution started</td> </tr> ");
        isReady = true;
    }

    /**
     * Records the close event in the log file and closes the <i>FileWriter</i> object.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (isReady) {
            writer.append("<tr class=\"status\"> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">Status</td> <td>Program execution stopped</td> </tr>");
            writer.close();
        }
    }

    /**
     * Clears the logs from the log file if it exists.
     * @throws IOException if the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void clearLog() throws IOException {
        if (logFile.exists()) {
            writer = new FileWriter(logFile, false);
            writer.write(htmlTemplate);
        }
    }

    /**
     * Gets the corresponding time value represented by this <i>Timestamp</i> object.
     * @return the corresponding <i>Timestamp</i> value from this date
     */
    private String getTime() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return dateFormat.format(timestamp);
    }

}