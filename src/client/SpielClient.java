package client;

import java.awt.Color;
import java.awt.HeadlessException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import impl.SpielInterface;

public class SpielClient {
	private SpielInterface intConn;
	private List<String> setFields = new ArrayList<String>();
	private String playerName = null;
	private String oppName = null;
	private SpielGUI spielGUI = new SpielGUI();
	private static final Logger LOGGER = LogManager.getLogger();
	
	public enum Zustand {
		Setzen, Schießen, Warten
	}

	public Zustand zustand;

	
	public static void main(String args[]) {
		SpielClient spielClient = new SpielClient();
		spielClient.initGame();
	}
	
	public void initGame() {
		try {
			intConn = this.getConn();
			spielGUI.createGUI(this);
			this.userLogin();
		} catch (RemoteException e) {
			LOGGER.error("Error initializing the Game error: " + e);
			e.printStackTrace();
			this.exitGame("Fehler bei dem Erstellen der Verbindung!");
		}
	}
	
	private SpielInterface getConn() {
		String ip = JOptionPane.showInputDialog(null, "Geben Sie die IP des Severs ein:", "localhost");
		if(ip == null){
			System.exit(0);
		}
		try {
			SpielInterface interfaceConn = (SpielInterface) Naming.lookup("rmi://"+ip+"/server"); //" + ip + ":" +Registry.REGISTRY_PORT + "\\server"
			LOGGER.info("Connection established with " + ip);
			return interfaceConn;
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			LOGGER.error("Error establishing connection with "+ ip +" error: "+ e);
			e.printStackTrace();
			this.exitGame("Der Server konnte nicht gefunden werden!");
			return null;
		}
	}
	
	private void userLogin() throws HeadlessException, RemoteException {
		do {
			String input = JOptionPane.showInputDialog("Geben Sie Ihren Namen ein: ");
			if(input == null) {
				LOGGER.warn("No Name entered");
				System.exit(0);
			}
			playerName = input.toUpperCase();
		} while (!intConn.login(playerName));
		zustand = Zustand.Setzen;
		LOGGER.info("State changed to " + zustand);
	}
	

	public Zustand getCondition() {
		return zustand;
	}



	public void isAlive() {
		try {
			if (intConn.clientsAlive() == false) {
				LOGGER.info("Oppenent has left the game");
				this.exitGame("Ihr Gegenspieler hat das Spiel verlassen.\nSie haben gewonnen!");
			}
		} catch (RemoteException e) {
			LOGGER.error("Lost connection to the server error: " + e);
			e.printStackTrace();
			this.exitGame("Die Verbindung zum Server wurde untebrochen.");
		}
	}

	
	public void setShot(JButton shot) {
		try {
			if (intConn.getPlayerStatus(playerName) == true) {
				spielGUI.paintShot(shot);
				intConn.toggleStatus(playerName, oppName);
				if (intConn.checkShot(oppName, shot.getActionCommand())) {
					spielGUI.setButtonImage(".\\Sonstiges\\bomb.jpeg", shot);

					if (intConn.getScore(playerName) == true) {
						LOGGER.info("Game successfully finished");
						this.exitGame("Herzlichen Glückwunsch!\nSie haben gewonnen.");
					}

				} else {
					shot.setBackground(new Color(0, 150, 255));
				}
			}
		} catch (RemoteException e1) {
			LOGGER.error("Lost connection to the server error: " + e1);
			e1.printStackTrace();
			this.exitGame("Die Verbindung zum Server wurde unterbrochen.");
		}
	}
	

	public void setBombs(String fields) throws RemoteException, InterruptedException {
		setFields.add(fields);
		int maxBombs = intConn.getMaxBombs();
		if (setFields.size() == maxBombs) {
			String[] tempArr = new String[maxBombs];

			for (int x = 0; x < maxBombs; x++) {
				tempArr[x] = setFields.get(x);
			}

			intConn.setBombs(playerName, tempArr);
			LOGGER.info("Bombs successfully set");
			zustand = Zustand.Warten;
			LOGGER.info("State changed to: " + zustand);
			spielGUI.clearField();
			GetBombsThread gbThread = new GetBombsThread();
			gbThread.initGetBombsThread(intConn, this, playerName, spielGUI);
			spielGUI.setStatusText("Warte auf Gegner");
		}
	}
		
	public void setOpponentName(String name) {
		this.oppName = name;
		LOGGER.info("Received Opponent name");
	}
	
	public void setCondition(Zustand zustand) {
		this.zustand = zustand;
	}
	

	public void exitGame(String message) {
		try {
			intConn.logout(playerName);
			JOptionPane.showMessageDialog(spielGUI, message);
			LOGGER.info("Game closed");
			System.exit(0);
		} catch (RemoteException logout) {
			JOptionPane.showMessageDialog(spielGUI,
					"Bei dem Beenden des Spiels ist ein Fehler aufgetreten!\nIst der Server offline?");
			LOGGER.fatal("Game crashed while beeing closed! error: " + logout);
			System.exit(0);
		}
	}
}