package rmi.clocksynchronization.server;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;

import rmi.clocksynchronization.common.IClient;
import rmi.clocksynchronization.utils.ClockUpdater;

class SynchronizationService implements Runnable {
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Server server;
    private final String serverName;
    private final JTextArea logsTextArea;

    SynchronizationService(Server server, String serverName, JTextArea logsTextArea) {
        super();
        this.server = server;
        this.serverName = serverName;
        this.logsTextArea = logsTextArea;
    }

    void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    void stop() {
        running.set(false);
    }

    @Override
    public void run() {
		/*
		  Synchronizacja zegarow
		 */
        while (running.get()) {
            if (server.getConnectedClients().isEmpty()) {
                logsTextArea.append("[Serwer: " + serverName + "] Czekam na połączenie klientów...\n");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
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

				/*
				  Obliczenie roznicy czasu pomiedzy serwerem a kazdym klientem oraz zsumowanie
				  tych roznic
				 */
                for (IClient client : server.getConnectedClients()) {
                    try {
                        differencesSum += client.getTimeDifference(serverTime);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

				/*
				  Obliczenie sredniej roznicy czasu
				 */
                long average = differencesSum / (server.getConnectedClients().size() + 1);

				/*
				  Obliczenie nowego czasu
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
                        e.printStackTrace();
                    }
                }

                logsTextArea.append("[Serwer: " + serverName + "] Ustawiam aktualny czas...\n");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date synchronizeDate = new Date(synchronizedTime);
                new Thread(new ClockUpdater(
                        new String[]{"bash", "-c", "date -s '" + simpleDateFormat.format(synchronizeDate) + "'"}))
                        .start();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {
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
                    e.printStackTrace();
                }
            }
        }
    }
}
