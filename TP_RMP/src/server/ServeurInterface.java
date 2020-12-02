package server;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientInterface;

public interface ServeurInterface extends Remote {

	boolean join(clientInterface client) throws RemoteException, MalformedURLException, NotBoundException;

	void quit(clientInterface client) throws RemoteException;

	void sendMessage(String message) throws RemoteException;

	void getMessageHistory(clientInterface client) throws RemoteException;
}
