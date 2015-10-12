package client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.SpielClient.Zustand;

public class SpielGUI extends AbstractSpielClient {

	private JButton[][] btnField = new JButton[10][10];
	private SpielClient splClient;
	private JPanel pnlField = new JPanel();

	public void createGUI(SpielClient caller) {

		this.splClient = caller;

		createBtnField();
		this.setSize(600, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("BombSearch - Client");
		pnlField.setLayout(new GridLayout(10, 10));
		this.addWindowListener(this);
		this.add(pnlField);
		//getContentPane().add(statBar, java.awt.BorderLayout.SOUTH);
	}

	private void createBtnField() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				btnField[x][y] = new JButton("");
				btnField[x][y].addActionListener(this);
				btnField[x][y].setActionCommand(x + "|" + y);
				pnlField.add(btnField[x][y]);
			}
		}
	}

	public void clearField() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				btnField[x][y].setEnabled(true);
				btnField[x][y].setBackground(new Color(150, 150, 150));
			}
		}
	}

//	public void setStatusBar(Boolean status) {
//		statBar.toggleStatusBar(status);
//	}
	
	public void setButtonImage(String path, JButton btn) {
		try {
			BufferedImage img = ImageIO.read(new File(path));
			btn.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (splClient.getCondition() == Zustand.Setzen) {
			try {
				((JButton) e.getSource()).setBackground(new Color(4, 99, 4));
				((JButton) e.getSource()).setEnabled(false);
				splClient.setBombs(((JButton) e.getSource()).getActionCommand());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		else if (splClient.getCondition() == Zustand.Schießen) {
			splClient.isGameOver();
			splClient.setShot(((JButton) e.getSource()));
		}
		
		else if (splClient.getCondition() == Zustand.Warten) {
			
		}
	}
	
	public void paintShot(JButton e) {
		this.repaint();
		this.invalidate();
		e.setEnabled(false);
	}

	
	public void windowClosing(WindowEvent e) {
		splClient.exitGame("Spiel wird beendet.");
	}

}