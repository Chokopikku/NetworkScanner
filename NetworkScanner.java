package com.pikkudev;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkScanner {

    private static Logger logger = new Logger();

    public void scan(String subnet) throws IOException {
        int timeout = 1000;
        boolean isReachable = false;
        for (int i=1; i<16; i++) {
            String host = subnet + "." + i;
            try {
                isReachable = InetAddress.getByName(host).isReachable(timeout);
            } catch (IOException ex) {
                logger.logException(ex, "Target address does not belong to the network's range");
            }
            if (isReachable) {
                System.out.println(host + " is reachable");
            } else {
                System.out.println("Couldn't reach " + host);
            }
        }
    }

    public void local() throws IOException {
        try {
            InetAddress myAddr = InetAddress.getLocalHost();
            System.out.println("localhost InetAddress: " + myAddr);
            System.out.println("localhost host address: " + myAddr.getHostAddress());
            System.out.println("localhost host name: " + myAddr.getHostName());
        } catch (UnknownHostException ex) {
            logger.logException(ex, "The IP address of this host could not be determined");
        }
    }

    public static void main(String[] args) throws IOException {

        logger.setFileName("log");
        logger.setDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
        logger.start();
        logger.clearLog();
        logger.logMsg("Dev: Pikku");
        Scanner inputScanner = new Scanner(System.in);
        NetworkScanner netScanner = new NetworkScanner();
        netScanner.local();
        System.out.println("\nSubnet address (e.g. 127.0.0):");
        String subnet = inputScanner.nextLine();
        netScanner.scan(subnet);
        inputScanner.close();
        logger.close();

    }

}