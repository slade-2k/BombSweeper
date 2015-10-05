package server;

import java.rmi.Naming;

import impl.SpielImpl;

public class SpielServer {
	public static void main(String args[]) {
		SpielServer serverObj = new SpielServer();
		serverObj.startServer();
	}

	private void startServer() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			SpielImpl implObj = new SpielImpl();
			Naming.bind("Spielbrett", implObj);

			System.out.println("Der Server wurde erfolgreich gestartet.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
