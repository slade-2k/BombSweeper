package server;

import java.util.Arrays;

public class Spieler{

	private String name;
	private String[] bombArray = null;
	private Boolean bombsSet = false;
	private Boolean status = null;
	private int hitCounter = 0;
	private int maxBombs = 10;

	public Spieler(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public Boolean getHitCounter() {
		if(hitCounter == 10){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setHitCounter() {
		hitCounter++;
	}

	
	public int getMaxBombs(){
		return maxBombs;
	}
	
	public void setBombArray(String bombs[]) {
		this.bombArray = new String[maxBombs];
		this.bombArray = Arrays.copyOf(bombs, maxBombs);
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
