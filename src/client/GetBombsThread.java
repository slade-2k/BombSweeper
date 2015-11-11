package client;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.SpielClient.Zustand;
import impl.SpielInterface;

public class GetBombsThread extends Thread {

	private Logger LOGGER = LogManager.getLogger();
	private SpielClient splClient;
	private SpielInterface intConn;
	private String playerName;
	private SpielGUI spielGUI;
	private StatusBarThread statusThread = new StatusBarThread();
	private String oppName = null;
	
	@Override
	public void run() {
		LOGGER.info("Thread started successfully");
		try {
			while(oppName == null){
				this.oppName = intConn.getOpponentName(playerName);
			}
			while (!intConn.waitForBombs(intConn.getOpponentName(playerName))) {
				
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			LOGGER.fatal("Lost connection to the server error: " + e);
			splClient.exitGame("Die Verbindung zum Server wurde unterbrochen.");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Couldn't find an opponent player error: " + e);
			splClient.exitGame("Es wurde kein anderer Spieler gefunden!\nDas Spiel wird beendet.");
		}
		try {
			intConn.rollTheDice(playerName, oppName);
			JOptionPane.showMessageDialog(spielGUI, "Alle Spieler sind bereit.");
			LOGGER.info("All players are ready");
			if (intConn.getPlayerStatus(playerName)) {
				JOptionPane.showMessageDialog(spielGUI, "Sie beginnen.");
				LOGGER.info("Player " + playerName + " begins");
			}
			splClient.setOpponentName(oppName);
			splClient.setCondition(Zustand.Schieﬂen);
			statusThread.initStatusThread(intConn, playerName, splClient, spielGUI);
		} catch (RemoteException e) {
			LOGGER.warn("Error occurred in Thread " + getClass());
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
