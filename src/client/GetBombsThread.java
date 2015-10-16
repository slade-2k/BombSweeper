package client;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import client.SpielClient.Zustand;
import impl.SpielInterface;

public class GetBombsThread extends Thread {

	private SpielClient splClient;
	private SpielInterface intConn;
	private String playerName;
	private SpielGUI spielGUI;
	private StatusBarThread statusThread = new StatusBarThread();
	private String oppName = null;
	
	@Override
	public void run() {
		try {
			while(oppName == null){
				this.oppName = intConn.getOpponentName(playerName);
			}
			while (!intConn.waitForBombs(intConn.getOpponentName(playerName))) {
				
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			splClient.exitGame("Die Verbindung zum Server wurde unterbrochen.");
		} catch (Exception e) {
			e.printStackTrace();
			splClient.exitGame("Es wurde kein anderer Spieler gefunden!\nDas Spiel wird beendet.");
		}
		try {
			intConn.rollTheDice(playerName, oppName);
			JOptionPane.showMessageDialog(spielGUI, "Alle Spieler sind bereit.");
			if (intConn.getPlayerStatus(playerName)) {
				JOptionPane.showMessageDialog(spielGUI, "Sie beginnen.");
			}
			splClient.setOpponentName(oppName);
			splClient.setCondition(Zustand.Schieﬂen);
			statusThread.initStatusThread(intConn, playerName, splClient, spielGUI);
		} catch (RemoteException e) {
		}
	}
	
	public void initGetBombsThread(SpielInterface intConn, SpielClient splClient, String playerName, SpielGUI spielGUI) {
		this.splClient = splClient;
		this.intConn = intConn;
		this.playerName = playerName;
		this.spielGUI = spielGUI;
		
		start();
	}
}
