package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import server.ServeurInterface;

public class Client {
	ServeurInterface Serveur;
	private static Scanner sc = new Scanner(System.in);

	public Client() throws MalformedURLException, RemoteException, NotBoundException {
		Serveur = (ServeurInterface) Naming.lookup("//localhost/RmiServer");
	}

	public static void main(String args[]) {
		try {
			System.out.println("Bienvenu(e)! Pour commencer, entrez votre nom");
			String username = sc.nextLine();
			Client chatClient = new Client();
			ClientImpl callBackClient = new ClientImpl(username);

			// On vérifie que le client a un nom unique avant de l'enregistrer
			while (!chatClient.Serveur.join(callBackClient)) {
				System.out.println("Nom d'utilisateur déjà utilisé! Entrez un autre!");
				username = sc.nextLine();
				chatClient = new Client();
				callBackClient = new ClientImpl(username);
			}

			// On demande si l'utilisateur veut consulter l'historique
			System.out.println("Voulez vous un Historique des messages du chat? Y/N");
			if (sc.nextLine().equalsIgnoreCase("Y"))
				chatClient.Serveur.getMessageHistory(callBackClient);

			// Le chat peut commencer
			System.out.println("Vous pouvez à présent envoyer un message. Pour quitter, entrez @QUIT");
			String message = sc.nextLine();
			while (!message.equalsIgnoreCase("@QUIT")) {
				// On teste si l'entrée est non vide avant de l'envoyer!
				if (message.trim().length() > 1) {
					chatClient.Serveur.sendMessage(username + " : " + message);
				}
				message = sc.nextLine();
			}
			// On quitte le chat
			chatClient.Serveur.quit(callBackClient);
			System.out.println("Vous avez quitté le chat!");
		} catch (Exception e) {
			System.out.println("Une erreur inatendue est survenue");
			e.printStackTrace();
		}
	}
}