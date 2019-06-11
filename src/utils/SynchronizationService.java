package utils;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;

import common.IClient;
import server.Server;

public class SynchronizationService implements Runnable {
	private Thread thread;
	private AtomicBoolean running = new AtomicBoolean(true);
	private Server server;
	private String serverName;
	private JTextArea logsTextArea;

	public SynchronizationService(Server server, String serverName, JTextArea logsTextArea) {
		super();
		this.server = server;
		this.serverName = serverName;
		this.logsTextArea = logsTextArea;
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		running.set(false);
	}

	@Override
	public void run() {
		/**
		 * Synchronizacja zegarow
		 */
		while (running.get()) {
			if (server.getConnectedClients().isEmpty()) {
				logsTextArea.append("[Serwer: " + serverName + "] Czekam na połączenie klientów...\n");
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

				logsTextArea.append("[Serwer: " + serverName + "] Rozpoczynam synchronizację czasu...\n");
				long differencesSum = 0;
				long serverTime = System.currentTimeMillis();
				logsTextArea.append("[System] Aktualny czas serwera " + serverName + ": "
						+ displayDateFormat.format(new Date(serverTime)) + "\n");
				logsTextArea.append("[Serwer: " + serverName + "] Czekam na czas klientów...\n");

				/**
				 * Obliczenie roznicy czasu pomiedzy serwerem a kazdym klientem oraz zsumowanie
				 * tych roznic
				 */
				for (IClient client : server.getConnectedClients()) {
					try {
						differencesSum += client.getTimeDifference(serverTime);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				/**
				 * Obliczenie sredniej roznicy czasu
				 */
				long average = differencesSum / (server.getConnectedClients().size() + 1);

				/**
				 * Obliczenie nowego czasu
				 */
				long synchronizedTime = serverTime + average;

				Date dateTime = new Date(synchronizedTime);

				logsTextArea.append(
						"[Serwer: " + serverName + "] Aktualny czas to: " + displayDateFormat.format(dateTime) + "\n");
				logsTextArea.append("[Serwer: " + serverName + "] Wysyłam aktualny czas do klientów...\n");
				for (IClient client : server.getConnectedClients()) {
					try {
						client.setSynchronizedTime(synchronizedTime);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				logsTextArea.append("[Serwer: " + serverName + "] Ustawiam aktualny czas...\n");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date synchronizeDate = new Date(synchronizedTime);
				new Thread(new ClockUpdater(
						new String[] { "bash", "-c", "date -s '" + simpleDateFormat.format(synchronizeDate) + "'" }))
								.start();
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Date currentDate = new Date(System.currentTimeMillis());
				logsTextArea.append("[Serwer: " + serverName + "] Synchronizacja czasu zakończona.\n");
				logsTextArea.append(
						"[Serwer: " + serverName + "] Aktulany czas: " + displayDateFormat.format(currentDate) + "\n");
				logsTextArea.append("[Serwer: " + serverName + "] Kolejna synchronizacja czasu  za "
						+ server.getClockSyncFreq() + " sekund.\n");
				logsTextArea.append("\n");
				try {
					TimeUnit.SECONDS.sleep(server.getClockSyncFreq());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
