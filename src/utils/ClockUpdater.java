package utils;

import java.io.IOException;

public class ClockUpdater implements Runnable {
	String[] command;

	public ClockUpdater(String[] command) {
		super();
		this.command = command;
	}

	@Override
	public void run() {
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
