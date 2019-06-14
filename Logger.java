package com.company;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger implements Closeable {

    private String fileName;
    private File logFile;
    private FileWriter writer;
    private Boolean isReady;
    private SimpleDateFormat sdf;

    Logger(){
        fileName = "logfile.html";
        isReady = false;
        sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
    }

    public void setFileName(String fileName) {
        if (fileName.endsWith(".html")) {
            this.fileName = fileName;
        } else {
            this.fileName = fileName + ".html";
        }
        isReady = false;
    }

    public void setDateFormat(String sdf) {
        this.sdf = new SimpleDateFormat(sdf);
    }

    public void log(Exception error, String desc) throws IOException {
        if (isReady) {
            writer.write("<tr> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">" + error + "</td> <td>" + desc +"</td> </tr> ");
        } else {
            start();
            log(error, desc);
        }
    }

    public void start() throws IOException {
        logFile = new File(fileName);
        if (!logFile.exists()) {
            final String htmlTemplate = "<!DOCTYPE html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> <title>Log</title> " +
                    "<style> table { width: 80%; margin-left: auto; margin-right: auto; } table,th,td { border: 1px solid black; border-collapse: collapse; } th,td { padding: 15px; } .center { text-align: center; } </style> " +
                    "</head> <body> <table> <tr> <th>Date</th> <th>Type</th> <th>Description</th> </tr> ";
            writer = new FileWriter(logFile, true);
            writer.write(htmlTemplate);
            writer.write("<tr> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">Status</td> <td>Program execution started</td> </tr> ");
        } else {
            writer = new FileWriter(logFile, true);
            writer.write("<tr> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">Status</td> <td>Program execution started</td> </tr> ");
        }
        isReady = true;
    }

    @Override
    public void close() throws IOException {
        if (isReady) {
            writer.append("<tr> <td class=\"center\">" + getTime() + "</td> <td class=\"center\">Status</td> <td>Program execution stopped</td> </tr>");
            writer.close();
        }
    }

    private String getTime() {
        Date date = new Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        return sdf.format(timestamp);
    }

}