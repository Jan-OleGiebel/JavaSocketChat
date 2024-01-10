package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MsgClient {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private UI MainUI;
    public MsgClient(UI _mainUI) {
        MainUI = _mainUI;
    }

    public boolean connect(String _peerAddress, int _peerPort) {
        this.serverAddress = _peerAddress;
        this.serverPort = _peerPort;
        try {
            // Connect to the server
            socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server.");

            // Initialize reader and writer for communication
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to listen for server messages
            Thread serverListenerThread = new Thread(new ServerMessageListener(socket, MainUI));
            serverListenerThread.start();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disconnect() {
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Disconnected from server.");
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendMsg(String message) {
        if (writer != null) {
            writer.println(message);
        } else {
            System.out.println("Writer is not initialized.");
        }
    }

}

class ServerMessageListener implements Runnable {
    private final Socket socket;
    private UI MainUI;

    public ServerMessageListener(Socket socket, UI _mainUI) {
        MainUI = _mainUI;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverResponse;
            while ((serverResponse = reader.readLine()) != null) {
                System.out.println("Received from server: " + serverResponse);
                MainUI.addNewMsg(serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}