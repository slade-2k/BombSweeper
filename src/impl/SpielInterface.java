package impl;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SpielInterface extends Remote {
	public void login(String name) throws RemoteException;
	/*
	 * Spieler �bergibt seinen gew�hlten Namen an den Server und meldet sich
	 * �ber diesen an Entspricht quasi setName
	 */

	public void logout(String name) throws RemoteException;
	/*
	 * Spieler name wird abgemeldet und vom Server stack gel�scht
	 */

	public String getOpponentName(String name) throws RemoteException;
	/*
	 * Spieler name holt sich den Namen des Gegenspielers
	 */

	public void setBombs(String name, String[] bombs) throws RemoteException;
	/*
	 * Spieler name �bertr�gt gesetzte Bomben in Array bombs[10]
	 */

	public Boolean checkShot(String name, String field) throws RemoteException;
	/*
	 * Spieler name schie�t auf Feld field. Returns String um "treffer" und
	 * "fehlsch�sse" anzuzeigen. String damit auch die Gewinnauswertung mit
	 * dieser Methode realisiert werden kann Nach Schuss wird das Feld auf dem
	 * Client local disabled
	 */
	
	public Boolean waitForBombs(String name) throws RemoteException;
	public void toggleStatus(String name, String oppName) throws RemoteException;
	public Boolean getPlayerStatus(String name) throws RemoteException;
	public void rollTheDice(String name, String oppname) throws RemoteException;
	public Boolean getScore(String name) throws RemoteException;
	public void setScore(String name) throws RemoteException;
	public Boolean getGameOver() throws RemoteException;
	public Boolean clientsAlive() throws RemoteException;
}
