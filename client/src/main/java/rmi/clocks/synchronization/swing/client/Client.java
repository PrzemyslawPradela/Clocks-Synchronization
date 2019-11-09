package rmi.clocks.synchronization.swing.client;

import rmi.clocks.synchronization.swing.common.IClient;
import rmi.clocks.synchronization.swing.utils.ClockUpdater;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Client extends UnicastRemoteObject implements IClient {
    private static final long serialVersionUID = 1L;

    private JTextArea logsTextArea;
    private String ipAddr;

    Client(JTextArea logsTextArea, String ipAddr) throws RemoteException {
        super();
        this.logsTextArea = logsTextArea;
        this.ipAddr = ipAddr;
    }

    @Override
    public long getTimeDifference(long serverTime) {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dateTime = new Date(System.currentTimeMillis());
        logsTextArea.append("[System] Aktualny czas to: " + displayDateFormat.format(dateTime) + "\n");
        logsTextArea.append("[Klient: " + ipAddr + "] Wysłano czas do serwera. Trwa synchronizacja czasu...\n");
        return System.currentTimeMillis() - serverTime;
    }

    @Override
    public void setSynchronizedTime(long synchronizedTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date synchronizeDate = new Date(synchronizedTime);
        new Thread(new ClockUpdater(
                new String[]{"bash", "-c", "date -s '" + simpleDateFormat.format(synchronizeDate) + "'"})).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dateTime = new Date(System.currentTimeMillis());
        logsTextArea.append("[Klient: " + ipAddr + "] Otrzymano zsynchronizowany czas od serwera.\n");
        logsTextArea.append("[System] Aktualny czas to: " + displayDateFormat.format(dateTime) + "\n");
        logsTextArea.append("\n");
    }
}
