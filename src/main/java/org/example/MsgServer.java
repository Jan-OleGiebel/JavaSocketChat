package org.example;

import java.io.*;
import java.net.*;

public class MsgServer implements Runnable {
    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private UI MainUI;

    public MsgServer(int port, UI _mainUI) throws IOException {
        MainUI = _mainUI;
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        System.out.println("Server started. Waiting for connections...");
        MainUI.addNewMsg("Server started. Waiting for connections...");

        while (isRunning) {
            try {
                // Listen for incoming connections
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
                MainUI.addNewMsg("Client connected: " + socket.getInetAddress().getHostAddress());

                // Handle the client in a new thread
                Thread clientHandler = new Thread(new ClientHandler(socket, MainUI));
                clientHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private UI MainUI;

    public ClientHandler(Socket socket, UI _mainUI) {
        MainUI = _mainUI;
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Handle client communication here
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                MainUI.addNewMsg(inputLine);
            }

            // Close resources
            reader.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}