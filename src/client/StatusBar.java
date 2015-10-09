package client;

import java.rmi.RemoteException;

import impl.SpielInterface;

public class StatusBar extends Thread{
	
	private StatusBarGUI sbGUI = new StatusBarGUI();
	
	public void run(String playerName, SpielInterface intConn) {
		do {
			try {
				if(intConn.getPlayerStatus(playerName)){
					sbGUI.toggleStatusBar(true);
				}
				else {
					sbGUI.toggleStatusBar(false);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while(true);
	}
}
