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

	public String getName(String name) throws RemoteException;
	/*
	 * Spieler name holt sich den Namen des Gegenspielers
	 */

	public void setBombs(String name, String[] bombs) throws RemoteException;
	/*
	 * Spieler name �bertr�gt gesetzte Bomben in Array bombs[10]
	 */

	public int shotField(String name, String field) throws RemoteException;
	/*
	 * Spieler name schie�t auf Feld field. Returns String um "treffer" und
	 * "fehlsch�sse" anzuzeigen. String damit auch die Gewinnauswertung mit
	 * dieser Methode realisiert werden kann Nach Schuss wird das Feld auf dem
	 * Client local disabled
	 */
	
	public int waitForAction(String name) throws RemoteException;
}
