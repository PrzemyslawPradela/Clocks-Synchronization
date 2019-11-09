package rmi.clocks.synchronization.swing.server;

import rmi.clocks.synchronization.swing.common.IClient;
import rmi.clocks.synchronization.swing.common.IServer;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Server extends UnicastRemoteObject implements IServer {
    private static final long serialVersionUID = 1L;

    private Vector<IClient> connectedClients;
    private int clockSyncFreq;
    private JTextArea logsTextArea;
    private String serverName;

    Server(JTextArea logsTextArea, String serverName) throws RemoteException {
        super();
        this.connectedClients = new Vector<>();
        this.clockSyncFreq = 0;
        this.logsTextArea = logsTextArea;
        this.serverName = serverName;
    }

    @Override
    public String registerClient(IClient client) {
        this.connectedClients.add(client);
        logsTextArea.append("[Serwer: " + serverName + "] Połączył sie nowy klient!\n");
        return "[System] Połaczyłeś się z serwerem!\n";
    }

    @Override
    public String unregisterClient(IClient client) {
        this.connectedClients.remove(client);
        logsTextArea.append("[Serwer: " + serverName + "] Jeden klient zakonczył połaczenie z serwerem!\n");
        return "[System] Rozłączyłeś się z serwerem!\n";
    }

    Vector<IClient> getConnectedClients() {
        return connectedClients;
    }

    int getClockSyncFreq() {
        return clockSyncFreq;
    }

    void setClockSyncFreq(int clockSyncFreq) {
        this.clockSyncFreq = clockSyncFreq;
    }
}
