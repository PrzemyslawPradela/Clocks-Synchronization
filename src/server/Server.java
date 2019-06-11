package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import javax.swing.JTextArea;

import common.IClient;
import common.IServer;

public class Server extends UnicastRemoteObject implements IServer {
	private static final long serialVersionUID = 1L;

	private Vector<IClient> connectedClients;
	private int clockSyncFreq;
	private JTextArea logsTextArea;
	private String serverName;

	protected Server(JTextArea logsTextArea, String serverName) throws RemoteException {
		super();
		this.connectedClients = new Vector<IClient>();
		this.clockSyncFreq = 0;
		this.logsTextArea = logsTextArea;
		this.serverName = serverName;
	}

	@Override
	public String registerClient(IClient client) throws RemoteException {
		this.connectedClients.add(client);
		logsTextArea.append("[Serwer: " + serverName + "] Połączył sie nowy klient!\n");
		return "[System] Połaczyłeś się z serwerem!\n";
	}

	@Override
	public String unregisterClient(IClient client) throws RemoteException {
		this.connectedClients.remove(client);
		logsTextArea.append("[Serwer: " + serverName + "] Jeden klient zakonczył połaczenie z serwerem!\n");
		return "[System] Rozłączyłeś się z serwerem!\n";
	}

	public Vector<IClient> getConnectedClients() {
		return connectedClients;
	}

	public int getClockSyncFreq() {
		return clockSyncFreq;
	}

	public void setClockSyncFreq(int clockSyncFreq) {
		this.clockSyncFreq = clockSyncFreq;
	}
}
