package client;

import java.awt.Dimension;

import javax.swing.JLabel;

public class StatusBar extends JLabel{
	public StatusBar() {
		super();
		super.setPreferredSize(new Dimension(100, 16));
		setText("");
	}
	
	public void toggleStatusBar(Boolean status) {
		if(status == true){
			this.setText("Ready");
		}
		else if(status == false){
			this.setText("Warte auf Gegner");
		}
	}
}
