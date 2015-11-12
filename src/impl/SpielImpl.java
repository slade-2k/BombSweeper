package impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Spieler;

public class SpielImpl extends UnicastRemoteObject implements SpielInterface {
	

	// public List<String> playerList = new ArrayList<String>();
	private static final Logger LOGGER = LogManager.getLogger();
	private Map<String, Spieler> players = new HashMap<String, Spieler>();
	private int maxBombs = 10;

	public SpielImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
	}
	
	public Boolean login(String name) {
		Iterator<String> it = players.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			if (name.equals(key)) {
				LOGGER.warn("Tried to register a playername that is already registered");
				return false;
			}
		}
		if (name.equals("")) {
			LOGGER.warn("Tried to register a empty playername");
			return false;
		}
		players.put(name, new Spieler(name));
		Spieler player = players.get(name);
		System.out.println("Spieler " + player.getName() + " hat sich angemeldet.");
		LOGGER.info("Player " + player.getName() + " successfully registered on the server");
		return true;
	}

	public int getMaxBombs() {
		return this.maxBombs;
	}
	
	public void logout(String name) {
		Spieler player = players.remove(name);
		System.out.println("Spieler " + player.getName() + " hat das Spiel verlassen.");
		LOGGER.warn("Player " + name + " left the game");
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
		if (players.size() == 2) {
			return true;
		}
		return false;
	}

	public Boolean getScore(String name) {
		if (players.get(name).getHitCounter() == maxBombs) {
			LOGGER.info("Hitcounter reached");
			return true;
		} else {
			return false;
		}
	}

	public void setScore(String name) {
		Spieler player = players.get(name);
		player.setHitCounter();
		LOGGER.info("Score of player " + name + " got incremented");
	}

	public void setBombs(String name, String[] bombs) {
		Spieler player = players.get(name);
		player.setBombArray(bombs);
		System.out.println("Bomben von " + name + " erhalten.");
		LOGGER.info("Bombs received from " + name);

		for (int x = 0; x < bombs.length; x++) {
			System.out.println(bombs[x]);
		}
	}

	public Boolean checkShot(String name, String field) {
		Spieler player = players.get(name);
		if (Arrays.asList(player.getBombs()).contains(field)) {
			setScore(getOpponentName(name));
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
			LOGGER.info("Dice rolled " + dice);
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
