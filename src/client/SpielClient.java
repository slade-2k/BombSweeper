package client;

import java.awt.Color;
import java.awt.HeadlessException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JOptionPane;

import impl.SpielInterface;

public class SpielClient {
	private SpielInterface intConn;
	private List<String> setFields = new ArrayList<String>();
	private String playerName = null;
	private String oppName = null;
	private SpielGUI spielGUI = new SpielGUI(); 
	
	public enum Zustand {
		Setzen, Schießen, Warten
	}

	public Zustand zustand;

	
	public void exitGame(String message) {
		try {
			intConn.logout(playerName);
			JOptionPane.showMessageDialog(spielGUI, message);
			System.exit(0);
		} catch (RemoteException logout) {
			JOptionPane.showMessageDialog(spielGUI,
					"Bei dem beenden des Spiels ist ein Fehler aufgetreten!\nIst der Server offline?");
			System.exit(0);
		}
	}

	public Zustand getCondition() {
		return zustand;
	}

	private SpielInterface getConn() {
		try {
			String ip = JOptionPane.showInputDialog(null, "Geben Sie die IP des Severs ein:", "192.168.5.41");
			if(ip == null){
				System.exit(0);
			}
			SpielInterface interfaceConn = (SpielInterface) Naming.lookup("rmi://"+ip+"/server"); //" + ip + ":" +Registry.REGISTRY_PORT + "\\server"
			return interfaceConn;
		} catch (Exception e) {
			//System.exit(0);
			return null;
		}
	}

	private void userLogin() throws HeadlessException, RemoteException {
		do {
			String input = JOptionPane.showInputDialog("Geben Sie Ihren Namen ein: ");
			if(input == null) {
				System.exit(0);
			}
			playerName = input.toUpperCase();
		} while (!intConn.login(playerName));
		zustand = Zustand.Setzen;
	}

	public void isAlive() {
		try {
			if (intConn.clientsAlive() == false) {
				this.exitGame("Ihr Gegenspieler hat das Spiel verlassen.\nSie haben gewonnen!");
			}
		} catch (RemoteException e) {
			this.exitGame("Die Verbindung zum Server wurde untebrochen.");
			e.printStackTrace();
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
						this.exitGame("Herzlichen Glückwunsch!\nSie haben gewonnen.");
					}

				} else {
					shot.setBackground(new Color(0, 150, 255));
				}
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
			this.exitGame("Die Verbindung zum Server wurde unterbrochen.");
		}
	}
	
	public void initGame() {
		try {
			intConn = this.getConn();
			spielGUI.createGUI(this);
			this.userLogin();
		} catch (RemoteException e) {
			this.exitGame("Fehler bei dem Erstellen der Verbindung!");
			e.printStackTrace();
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
			zustand = Zustand.Warten;
			spielGUI.clearField();
			GetBombsThread gbThread = new GetBombsThread();
			gbThread.initGetBombsThread(intConn, this, playerName, spielGUI);
			spielGUI.setStatusText("Warte auf Gegner");
		}
	}
		
	public void setOpponentName(String name) {
		this.oppName = name;
	}
	public void setCondition(Zustand zustand) {
		this.zustand = zustand;
	}
	
	public static void main(String args[]) {
		SpielClient spielClient = new SpielClient();
		spielClient.initGame();
	}

}