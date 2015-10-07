package impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import server.Spieler;

public class SpielImpl extends UnicastRemoteObject implements SpielInterface {
	// public List<String> playerList = new ArrayList<String>();
	private Map<String, Spieler> players = new HashMap<String, Spieler>();
	private Boolean gameOver = false;

	public SpielImpl() throws RemoteException {
	}

	public void login(String name) {
		players.put(name, new Spieler(name));
		Spieler player = players.get(name);
		System.out.println("Spieler " + player.getName() + " hat sich angemeldet.");
	}

	public void logout(String name) {
		Spieler player = players.remove(name);
		System.out.println("Spieler " + player.getName() + " hat das Spiel verlassen.");
	}

	public String getOpponentName(String name) {
		Iterator<String> it = players.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			if (!key.equals(name)) {
				return key;
			}
		}
		return null;
	}
	
	public Boolean clientsAlive() {
		if(players.size() == 2){
			return true;
		}
		return false;
	}

	public Boolean getScore(String name) {
		Spieler player = players.get(name);
		if (player.getHitCounter() == true) {
			gameOver = true;
			return true;
		} else {
			return false;
		}
	}

	public Boolean getGameOver() {
		if (gameOver == true) {
			return true;
		}
		return false;
	}

	public void setScore(String name) {
		Spieler player = players.get(name);
		player.setHitCounter();
	}

	public void setBombs(String name, String[] bombs) {
		Spieler player = players.get(name);
		player.setBombArray(bombs);
		System.out.println("Bomben von " + name + " erhalten.");

		for (int x = 0; x < bombs.length; x++) {
			System.out.println(bombs[x]);
		}
	}

	public Boolean checkShot(String name, String field) {
		Spieler player = players.get(name);
		if (Arrays.asList(player.getBombs()).contains(field)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean waitForBombs(String name) {
		Spieler player = players.get(name);
		if (player.getBombIndentificator() == false) {
			return false;
		} else {
			return true;
		}
	}

	public Boolean getPlayerStatus(String name) {
		Spieler player = players.get(name);
		return player.getStatus();
	}

	public void toggleStatus(String name, String oppName) {
		Spieler player = players.get(name);
		Spieler player2 = players.get(oppName);

		if (player.getStatus() == true) {
			player.setStatus(false);
			player2.setStatus(true);
		} else {
			player.setStatus(true);
			player2.setStatus(false);
		}
	}

	public void rollTheDice(String name, String oppName) {
		Spieler player = players.get(name);
		Spieler player2 = players.get(oppName);

		if (player.getStatus() == null) {
			Random rand = new Random();
			int dice = rand.nextInt(2);
			System.out.println("Würfel rollt: " + dice);
			if (dice == 0) {
				player.setStatus(true);
				player2.setStatus(false);
			} else {
				player.setStatus(false);
				player2.setStatus(true);
			}
		}
	}
}

/*
 * private Map<String, Spieler> players = new HashMap<String, Spieler>();
 * players.put("Oliver", new Spieler("Oliver")); Spieler player =
 * players.get("Oliver");
 */
