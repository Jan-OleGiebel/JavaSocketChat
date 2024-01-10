package org.example;

public class Main {

    public static void main(String[] args) throws Exception {
        UI mainUI = null;
        MsgClient _mClient = new MsgClient(mainUI);
        mainUI = new UI(_mClient);

        mainUI.buildUI();
    }
}