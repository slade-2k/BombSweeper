package server;

import java.util.Arrays;

public class Spieler{

	private String name;
	private String[] bombArray = null;
	private Boolean bombsSet = false;
	private Boolean status = null;
	private int hitCounter = 0;

	public Spieler(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public int getHitCounter() {
		return this.hitCounter;
	}
	
	public void setHitCounter() {
		this.hitCounter++;
	}
	
	public void setBombArray(String bombs[]) {
		this.bombArray = new String[bombs.length];
		this.bombArray = Arrays.copyOf(bombs, bombs.length);
		this.bombsSet = true;
	}

	public Boolean getBombIndentificator() {
		return this.bombsSet;
	}

	public String[] getBombs() {
		return this.bombArray;
	}

	public Boolean getStatus() {
		return this.status;
	}

	public void setStatus(Boolean stat) {
		this.status = stat;
	}
}
