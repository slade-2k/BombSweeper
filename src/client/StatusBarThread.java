package client;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import impl.SpielInterface;

public class StatusBarThread extends Thread{
	
	private static final Logger LOGGER = LogManager.getLogger();
	private SpielGUI spielGUI;
	private String playerName;
	private SpielInterface intConn;
	private SpielClient splClient;
	
	@Override
	public void run(){
		LOGGER.info("Thread " + getClass() + " successfully started");
		try {
			while(intConn.getScore(playerName) == false && intConn.getScore(intConn.getOpponentName(playerName)) == false) {
				if(intConn.getPlayerStatus(playerName) == true) {
					spielGUI.setStatusText("Ready");
				}
				else {
					spielGUI.setStatusText("Warte auf Gegner");
				}
			}
			
			if(intConn.getScore(intConn.getOpponentName(playerName)) == true) {
				LOGGER.info("Player " + playerName + " lost the game");
				splClient.exitGame("GAME OVER!\nSchade, Sie haben verloren!");
			}
		}catch(RemoteException e){
			LOGGER.warn("Error occurred in Thread " + getClass());
		}
	}
	
	public void initStatusThread(SpielInterface intConn, String playerName, SpielClient splClient, SpielGUI spielGUI) throws RemoteException{
		this.playerName = playerName;
		this.intConn = intConn;
		this.splClient = splClient;
		this.spielGUI = spielGUI;
		this.start();
	}
}
