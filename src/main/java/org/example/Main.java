package org.example;

public class Main {
    public static void main(String[] args) throws Exception {
        MsgClient _mClient = new MsgClient();
        UI mainUI = new UI(_mClient);

        mainUI.buildUI();
    }
}