package server.russel;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private final String collectionFile = "files/myCollection.csv";


    private Socket socket;
    private volatile Scanner clientIn = null;
    private volatile PrintWriter clientOut = null;

    public ClientThread(Socket incoming) {
        socket = incoming;
    }

    public void importFileFromClientToServer() {
        System.out.println("Прием файла от клиента...");
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(collectionFile), true)) {
            while (clientIn.hasNextLine()) {

                String line = clientIn.nextLine();
                if (line.trim().equals("END")) {
                    System.out.println("Готово!");
                    return;
                }
                fileWriter.println(line);
            }

        } catch (IOException ex) {
            System.out.println("Ошибка импорта файла");
        }

    }

    public void importFileFromServerToClient() {
        System.out.println("Передача файла клиенту...");
        try (Scanner fileReader = new Scanner(new File(collectionFile))) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                clientOut.println(line);
            }
            clientOut.println("END");
            System.out.println("Готово!");
        } catch (FileNotFoundException ex) {
            System.out.println("Ошибка экспорта файла");
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Подключился новый клиент!");

            clientOut = new PrintWriter(socket.getOutputStream(), true);
            clientIn = new Scanner(socket.getInputStream());

            try {
                while (clientIn.hasNextLine()) {
                    String command = clientIn.nextLine().trim();
                    System.out.println("Поступил новый запрос: " + command);

                    switch (command) {
                        case "import":
                            clientOut.println(command);
                            importFileFromClientToServer();
                            break;
                        case "load":
                            clientOut.println(command);
                            importFileFromServerToClient();
                            break;
                        case "info":
                            clientOut.println(command);
                            clientOut.println(Server.myCollection.info());
                            break;
                        case "show":
                            clientOut.println(command);
                            clientOut.println(Server.myCollection.show());
                            break;
                        case "help": case "?":
                            clientOut.println(command);
                            clientOut.println(Server.myCollection.help());
                            break;
                        default:
                            //Даем коллекции обработать команду
                            String success = Server.myCollection.start(command);
                            clientOut.println(success);
                    }
                }
            } finally {
                clientIn.close();
                clientOut.close();
                socket.close();
            }

        } catch (IOException ex) {
            System.out.println("Ошибка инициализации клиента или закрытия сокета");
        }
    }

}
