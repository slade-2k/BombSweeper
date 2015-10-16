package client;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import impl.SpielInterface;

public class StatusBarThread extends Thread{
	
	private SpielGUI spielGUI;
	private String playerName;
	private SpielInterface intConn;
	private SpielClient splClient;
	
	@Override
	public void run(){
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
				splClient.exitGame("GAME OVER!\nSchade, Sie haben verloren!");
			}
		}catch(RemoteException e){
			
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
