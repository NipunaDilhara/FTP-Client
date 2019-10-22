package org.nipuna.sampleproject;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;

import java.io.*;
import java.sql.Time;
import java.util.Scanner;
import java.sql.Timestamp;

import static javax.print.attribute.standard.ReferenceUriSchemesSupported.FTP;


public class FTPClientConnector {

    private static Scanner scanner;
    private static Timestamp timestamp1, timestamp2;
    public static void main(String[] args){
        try{
            boolean conitnue = true;
            FTPClient ftp=null;

            System.out.println("===================================================================");
            System.out.println("FTP CLIENT");
            System.out.println("===================================================================");
            System.out.println("MENU");
            System.out.println("1. Connect");
            System.out.println("2. Exit");
            System.out.println("===================================================================");


            scanner = new Scanner(System.in);
            System.out.print("Selected Option: ");
            int selectedOption = scanner.nextInt();
            scanner.nextLine();

            switch (selectedOption){
                case 1:
                    System.out.print("Enter ServerName: ");
                    String serverName= scanner.nextLine();
                    ftp=connectToFTP(serverName);
                    break;
                case 2:
                    conitnue = false;
                    break;
                default:
                    conitnue = false;
                    break;
            }

            String server = "ftp.support.wso2.com";
            String server2 = "speedtest.tele2.net";
            //String server3 = "test.rebex.net";
            String server4 = "c64.rulez.org";
            //3ftp.login("demo","password");
            //4ftp.login("anonymous","");
            //ftp.login("fisglobalsub", "#rraKn7I0YT8d1-Z+duH");
            //FTPListParseEngine engine = ftp.initiateListParsing("/pub/");
            //FTPClient ftp = connectToFTP(server2);
            //listFiles(ftp);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FTPClient connectToFTP(String serverName) throws Exception{
        System.out.print("Enter UserName: ");
        String userName= scanner.nextLine();
        System.out.print("Enter Password: ");
        String password= scanner.nextLine();
        System.out.println("Connecting to the server...");
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.connect(serverName);
        boolean isLoggedIn = false;
        isLoggedIn = ftp.login(userName, password);
        if(isLoggedIn){
            System.out.println("Logged in to the server");
            serverOptions(ftp);
        } else{
            System.out.println("Failed the loging to server");
        }

        return ftp;
    }

    public static void serverOptions(FTPClient ftp) throws Exception{
        boolean conitnue = true;

        while (conitnue){
            System.out.println("===================================================================");
            System.out.println("MENU:");
            System.out.println("1. List Files");
            System.out.println("2. Download File");
            System.out.println("3. Upload File");
            System.out.println("4. Exit");
            System.out.println("===================================================================");
            System.out.print("Selected Option:");
            int selectedOption = scanner.nextInt();
            scanner.nextLine();

            switch (selectedOption){
                case 1:
                    listFiles(ftp);
                    break;
                case 2:
                    System.out.print("Enter File Name: ");
                    String remoteFileToDownload= scanner.nextLine();
                    System.out.print("Enter File Name to Save: ");
                    String nametoSaveTheDownloaded= scanner.nextLine();
                    downloadFile(ftp, remoteFileToDownload, nametoSaveTheDownloaded);
                    break;
                case 3:
                    System.out.print("Enter File Name with the Location: ");
                    String localFileName= scanner.nextLine();
                    System.out.print("Enter the Destination File Name: ");
                    String remoteFileName= scanner.nextLine();
                    uploadFile(ftp, localFileName, remoteFileName);
                    break;
                case 4:
                    conitnue=false;
                    break;
                default:
                    conitnue = false;
                    break;
            }
        }
    }

    public static void listFiles(FTPClient ftp) throws Exception{
        /*FTPFile[] files = ftp.listFiles("/upload");
        for(FTPFile file: files){
            System.out.println(file.getName());
        }*/
        FTPListParseEngine engine = ftp.initiateListParsing("/ftp/files");
        while (engine.hasNext()){
            FTPFile[] files = engine.getNext(20);
            for(FTPFile file: files){
                System.out.println(file.getName());
            }
        }
        //System.out.println();
    }

    public static void downloadFile (FTPClient ftp, String remoteFileToDownload, String nametoSaveTheDownloaded) throws Exception{
        boolean success = false;
        String remoteFile2 = "/ftp/files/"+remoteFileToDownload;
        //File downloadFile2 = new File("/home/nipuna/test.txt");
        File downloadFile2 = new File("/home/nipuna/"+nametoSaveTheDownloaded);
        OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
        InputStream inputStream = ftp.retrieveFileStream(remoteFile2);
        byte[] bytesArray = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(bytesArray)) != -1) {
            outputStream2.write(bytesArray, 0, bytesRead);
        }

        success = ftp.completePendingCommand();
        if (success) {
            System.out.println("File #2 has been downloaded successfully.");
        }
        outputStream2.close();
        inputStream.close();
    }

    public static void uploadFile (FTPClient ftp, String localFileName, String remoteFileName) throws Exception{

        //File fileName = new File ("/home/nipuna/projects/sample.txt");
        File fileToUpload = new File(localFileName);
        String remoteFile = "/ftp/files/sample.txt";
        InputStream inputStream = new FileInputStream(fileToUpload);
        System.out.println("Start uploading second file");
        OutputStream outputStream = ftp.storeFileStream("/ftp/files/"+remoteFileName);
        byte[] bytesIn = new byte[4096];
        int read = 0;

        while ((read = inputStream.read(bytesIn)) != -1) {
            outputStream.write(bytesIn, 0, read);
        }
        inputStream.close();
        outputStream.close();

        boolean completed = ftp.completePendingCommand();
        if (completed) {
            System.out.println("The file is uploaded successfully.");
        }

        /*String filename = "home/nipuna2/ftp/files/sample.txt";
        ftp.enterLocalPassiveMode();
        byte[] buffer = new byte[4096];
        ftp.setFileType(1);
        FileInputStream fis = new FileInputStream(filename);
        ftp.storeFile(filename, fis);
        */

        //------------------------
        //OutputStream out = ftp.put(filename);

        /*int counter = 0;
        while (true) {
            int bytes = fis.read(buffer);
            if (bytes < 0)
                break;
            out.write(buffer, 0, bytes);
            counter += bytes;
            System.out.println(counter);
        }
*/

    }

    public static void  logout(FTPClient ftp) throws Exception{
        if (ftp.isConnected()) {
            ftp.logout();
            ftp.disconnect();
        }
    }
}
