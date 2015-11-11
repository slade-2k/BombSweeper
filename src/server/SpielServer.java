package server;

import java.rmi.Naming;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import impl.SpielImpl;

public class SpielServer {
	private static final Logger LOGGER = LogManager.getLogger();
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
			Naming.bind("server", implObj);
			LOGGER.info("Server successfully started");
			System.out.println("Der Server wurde erfolgreich gestartet.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			LOGGER.fatal("Error occurred while starting the server");
		}
	}
}
