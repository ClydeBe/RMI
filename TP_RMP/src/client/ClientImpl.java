package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientImpl extends UnicastRemoteObject implements clientInterface {

	private static final long serialVersionUID = 1L;
	private String username;

	public ClientImpl(String username) throws RemoteException {
		super(0);
		this.username = username;
	}

	@Override
	public String getUsername() throws RemoteException {
		return username;
	}

	@Override
	public void getNewMessage(String message) throws RemoteException {
		System.out.println(message);
	}

	@Override
	public void getOldMessages(List<String> messages) throws RemoteException {
		if (messages.size() > 1)
			for (String message : messages)
				System.out.println(message);
		else
			System.out.println("Pas d'historique!");
	}

}
