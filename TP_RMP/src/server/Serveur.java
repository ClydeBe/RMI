package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.clientInterface;

//import client.ClientInterf;

public class Serveur extends UnicastRemoteObject implements ServeurInterface {
	private static final long serialVersionUID = 1L;
	private List<String> messages = new ArrayList<String>();
	private List<clientInterface> clientsCallback = new ArrayList<clientInterface>();

	public Serveur() throws RemoteException {
		super(0);
	}

	@Override
	public synchronized boolean join(clientInterface clientCallback) // Rejoindre le chat
			throws RemoteException, MalformedURLException, NotBoundException {
		if (isValidUser(clientCallback)) { // L'utilisateur a t-il un nom valide?
			clientsCallback.add(clientCallback);
			notifyNewUser(clientCallback.getUsername());
			return true;
		}
		return false;
	}

	@Override
	public synchronized void quit(clientInterface client) throws RemoteException {
		clientsCallback.remove(clientsCallback.indexOf(client));
		// On notifie les autres utilisateurs du départ
		notifyLeavingUser(client.getUsername());
	}

	@Override
	public synchronized void sendMessage(String message) throws RemoteException {
		messages.add(message);
		// On fait un broadcast une fois un message reçu

		for (clientInterface client : clientsCallback) {
			try {
				// Détecter une éventuelle déconnexion inadéquate et supprimer le client
				if (!message.startsWith(client.getUsername()))
					client.getNewMessage(message);
			} catch (RemoteException e) {
				clientsCallback.remove(clientsCallback.indexOf(client));
//				notifyLeavingUser(client.getUsername());
			}
		}
	}

	@Override
	public void getMessageHistory(clientInterface client) throws RemoteException {
		client.getOldMessages(messages);
	}

	private String notifyNewUser(String username) throws RemoteException {
		return username + " vient de rejoindre le serveur!";
	}

	private void notifyLeavingUser(String username) throws RemoteException {
		messages.add("Serveur : " + username + " a quité le chat!");
	}

	private synchronized boolean isValidUser(clientInterface client) {
		for (clientInterface i : clientsCallback)
			try {
				if (i.getUsername().contentEquals(client.getUsername()))
					return false;
			} catch (RemoteException e) {
				// On détecte une éventuelle déconnexion et on met à jour la liste
				// d'utilisateurs
				clientsCallback.remove(clientsCallback.indexOf(i));
			}
		return true;
	}

	public static void main(String args[]) throws Exception {
		try {
			LocateRegistry.createRegistry(1099);
			Serveur chatServeur = new Serveur();
			Naming.rebind("//localhost/RmiServer", chatServeur);
			System.out.println("Serveur prêt!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
