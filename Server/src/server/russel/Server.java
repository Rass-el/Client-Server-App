package server.russel;

import server.csv.CSVReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Server {
    private static int PORT;      // you may change this
    private static final String collectionFileString = "files/myCollection.csv";

    public static volatile PersonCollection myCollection;

    public static void main (String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java server.russel.Server <port>");
            System.exit(1);
        } else
            PORT = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            CSVReader input = new CSVReader(new File(collectionFileString));
            myCollection = new PersonCollection(collectionFileString);
            myCollection.initializationFromCSV(input);

            ExecutorService executor = Executors.newCachedThreadPool();

            while (true) {
                Socket msocket = serverSocket.accept();
                executor.execute(new ClientThread(msocket));
            }
        }catch (Throwable t) {
            System.out.println("Неизвестная ошибка");
        }
    }
}

