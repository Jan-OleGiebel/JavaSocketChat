package org.example;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI {
    private final JFrame frame;
    private MsgClient ui_MsgClient;
    private JTextArea IncomingMsg;
    private String MsgHistory = "";
    private UI MainUI = this;
    private MsgServer MServer;
    private Thread serverThread;

    public UI(MsgClient _ui_MsgClient) {
        frame = new JFrame("JavaSocketChat - JSC");
        ui_MsgClient = _ui_MsgClient;
    }

    private JPanel settingsPanel() {
        JPanel _settingsPanel = new JPanel();

        _settingsPanel.setLayout(new GridBagLayout());

        GridBagConstraints _gbc = new GridBagConstraints();
        _gbc.gridx = 0;
        _gbc.gridy = 0;

        _settingsPanel.add(new JLabel("Peer address:"));
        JTextField _peerAddress = new JTextField(10);
        _peerAddress.setSize(10, 5);
        JButton _connectButton = new JButton("connect");
        JLabel _connectionStatus = new JLabel("disconnected");

        JButton _serverButton = new JButton("start server-mode");

        _serverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(_serverButton.getText() == "start server-mode") {
                    try {
                        MServer = new MsgServer(9060, MainUI);
                        serverThread = new Thread(MServer);
                        serverThread.start();
                        _serverButton.setText("stop server-mode");
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                } else {
                    MServer.stop();
                    _serverButton.setText("start server-mode");
                }
            }
        });

        _connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(_connectButton.getText() == "connect") {
                    boolean result = ui_MsgClient.connect(_peerAddress.getText());
                    if(result) {
                        _connectionStatus.setText("connected");
                        _connectButton.setText("disconnect");
                    } else {
                        _connectionStatus.setText("disconnected");
                    }
                } else {
                    boolean result = ui_MsgClient.disconnect();
                    if(result) {
                        _connectionStatus.setText("disconnected");
                        _connectButton.setText("connect");
                    } else {
                        _connectionStatus.setText("connected");
                    }
                }
            }
        });

        _gbc.gridx++;
        _gbc.gridy = 0;
        _settingsPanel.add(_peerAddress, _gbc);
        _gbc.gridx = 0;
        _gbc.gridy++;
        _settingsPanel.add(_connectButton, _gbc);
        _gbc.gridx++;
        _settingsPanel.add(_connectionStatus, _gbc);
        _gbc.gridx = 0;
        _gbc.gridy++;
        _settingsPanel.add(_serverButton, _gbc);

        return _settingsPanel;
    }

    private JPanel msgIn() {
        JPanel _msgInPanel = new JPanel();
        _msgInPanel.setLayout(new GridBagLayout());

        JLabel _infoLabel = new JLabel("Incoming messages:");

        IncomingMsg = new JTextArea(10,25);
        IncomingMsg.setEnabled(false);

        GridBagConstraints _gbc = new GridBagConstraints();
        _gbc.gridx = 0;
        _gbc.gridy = 0;
        _msgInPanel.add(_infoLabel, _gbc);
        _gbc.gridy++;
        _msgInPanel.add(IncomingMsg, _gbc);

        return  _msgInPanel;
    }

    private JPanel msgOut() {
        JPanel _msgInPanel = new JPanel();
        _msgInPanel.setLayout(new GridBagLayout());

        JLabel _infoLabel = new JLabel("Send message:");

        JTextField _outgoingMsg = new JTextField(20);
        _outgoingMsg.setSize(10, 5);

        JButton _sendMsgBtn = new JButton("send");

        _sendMsgBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ui_MsgClient.sendMsg(_outgoingMsg.getText());
                _outgoingMsg.setText("");
            }
        });

        GridBagConstraints _gbc = new GridBagConstraints();
        _gbc.gridx = 0;
        _gbc.gridy = 0;
        _msgInPanel.add(_infoLabel, _gbc);
        _gbc.gridy++;
        _msgInPanel.add(_outgoingMsg, _gbc);
        _gbc.gridy++;
        _msgInPanel.add(_sendMsgBtn, _gbc);

        return  _msgInPanel;
    }

    public void addNewMsg(String _msg) {
        MsgHistory = MsgHistory + _msg + "\n";
        IncomingMsg.setText(MsgHistory);
    }
    public void buildUI() {
        JPanel MainUIPanel = new JPanel();
        MainUIPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        MainUIPanel.setLayout(new BoxLayout(MainUIPanel, BoxLayout.Y_AXIS));
        JPanel _settingsPanel = settingsPanel();
        JPanel _msgIn = msgIn();
        JPanel _msgOut = msgOut();
        // 400 width and 500 height
        frame.setSize(300, 350);


        MainUIPanel.add(_settingsPanel);
        MainUIPanel.add(_msgIn);
        MainUIPanel.add(_msgOut);

        frame.add(MainUIPanel);

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        // making the frame visible
        //frame.setLayout(new FlowLayout());
        frame.setVisible(true);
    }
}
