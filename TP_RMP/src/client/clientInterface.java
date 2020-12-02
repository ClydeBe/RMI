package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface clientInterface extends Remote {
	String getUsername() throws RemoteException;

	void getNewMessage(String message) throws RemoteException;

	void getOldMessages(List<String> messages) throws RemoteException; // Pour les nouveaux utilisateurs
}
