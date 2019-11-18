package com.pradela.clocksynchronization.utils;

import java.io.IOException;

public class ClockUpdater implements Runnable {
    private final String[] command;

    public ClockUpdater(String[] command) {
        super();
        this.command = command;
    }

    @Override
    public void run() {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
