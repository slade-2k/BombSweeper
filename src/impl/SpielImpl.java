package impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpielImpl extends UnicastRemoteObject implements SpielInterface {
	public List<String> playerList = new ArrayList<String>();
	public String[] bombArrayPlayer1 = new String[10];
	public String[] bombArrayPlayer2 = new String[10];
	public String bomb1 = "empty";
	public String bomb2 = "empty";
	
	public SpielImpl() throws RemoteException {
		
	}
	
	public void login(String name){
		if(playerList.size() < 2) {
			playerList.add(name);
			System.out.println("Spieler "+ playerList.get(playerList.size() -1) + " hat sich registriert.");
		}
	}
	
	public void logout(String name) {
		playerList.remove(name);
	}
	
	public String getName(String name) {
		((ArrayList<String>)playerList).trimToSize();
		
		if (playerList.indexOf(name) == 0) {
			return playerList.get(1);
		}
		else {
			return playerList.get(0);
		}
	}
	
	public void setBombs(String name, String[] bombs) {					//Gedachtes Format bombs = {1|0, 4|5, 2|3, ...}
		if(playerList.indexOf(name) == 0) {
			bombArrayPlayer1 = Arrays.copyOf(bombs, 10);
			System.out.println("Bomben von "+name+" erhalten.");
			bomb1 = "set";
			
			for (int x = 0; x < 10; x++) {
				System.out.println(bombArrayPlayer1[x]);
			}
		}
		else {
			bombArrayPlayer2 = Arrays.copyOf(bombs, 10);
			System.out.println("Bomben von "+name+" erhalten.");
			bomb2 = "set";
			
			for (int x = 0; x < 10; x++) {
				System.out.println(bombArrayPlayer2[x]);
			}
		}
	}
	
	public int shotField(String name, String field) {
		if(playerList.indexOf(name) == 0) {
			if(Arrays.asList(bombArrayPlayer1).contains(field)) {
				return 1;
			}
			else {
				return 2;
			}
		}
		else {
			if(Arrays.asList(bombArrayPlayer2).contains(field)) {		//Gedachtes Format bombs = {1|0, 4|5, 2|3, ...}
				return 1;
			}
			else {
				return 2;
			}
		}
	}
	
	public int waitForAction(String name) {
		if(playerList.indexOf(name) == 0) {
			if(bomb2.equals("set")) {
				return 1;
			}
			else {
				return 0;
			}
		}
		else {
			if(bomb1.equals("set")) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}
}
