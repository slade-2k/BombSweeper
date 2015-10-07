package client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import impl.SpielInterface;

public class SpielClient extends JFrame implements ActionListener {
	private JPanel pnlField = new JPanel();
	private SpielInterface intConn = getConn();
	private List<String> setFields = new ArrayList<String>();
	private String playerName = null;
	private String oppName = null;
	private JButton[][] btnField = new JButton[10][10];

	public enum Zustand {
		Setzen, Schieﬂen
	}

	public Zustand zustand;

	public void createGUI() {

		createBtnField();
		this.setSize(600, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("BombSearch - Client");
		pnlField.setSize(500, 500);
		pnlField.setLayout(new GridLayout(10, 10));
		this.add(pnlField);
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

	public void exitGame(String message) {
		try {
			intConn.logout(playerName);
			JOptionPane.showMessageDialog(pnlField, message);
			System.exit(0);
		} catch (Exception logout) {
			JOptionPane.showMessageDialog(pnlField, "Bei dem beenden des Spiels ist ein Fehler aufgetreten!\nIst der Server offline?");
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (zustand == Zustand.Setzen) {
			try {
				((JButton) e.getSource()).setBackground(new Color(4, 99, 4));
				((JButton) e.getSource()).setEnabled(false);
				setBombs(((JButton) e.getSource()).getActionCommand());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		else if (zustand == Zustand.Schieﬂen) {
			try {
				if (intConn.getGameOver() == true) {
					this.exitGame("Game over!\nSchade, Sie haben verloren.");
				}
				
				if (intConn.clientsAlive() == false) {
					this.exitGame("Ihr Gegenspieler hat das Spiel verlassen.\nSie haben gewonnen!");
				}

				if (intConn.getPlayerStatus(playerName) == true) {
					intConn.toggleStatus(playerName, oppName);
					if (intConn.checkShot(oppName, ((JButton) e.getSource()).getActionCommand()) == true) {
						//((JButton) e.getSource()).setIcon(new ImageIcon());
						((JButton) e.getSource()).setBackground(new Color(255, 0, 0));
						intConn.setScore(playerName);

						if (intConn.getScore(playerName) == true) {
							this.exitGame("Herzlichen Gl¸ckwunsch!\nSie haben gewonnen.");
						}

					} else {
						((JButton) e.getSource()).setBackground(new Color(0, 150, 255));
					}
					this.repaint();
					this.invalidate();
					((JButton) e.getSource()).setEnabled(false);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
				this.exitGame("Die Verbindung zum Server wurde unterbrochen.");
			}
		}
	}

	private SpielInterface getConn() {
		try {
			SpielInterface interfaceConn = (SpielInterface) Naming.lookup("Spielbrett");
			return interfaceConn;
		} catch (Exception e) {
			this.exitGame("Die Verbindung zu dem Server konnte nicht hergestellt werden.\nIst der Server online?");
		}
		return null;
	}

	private void setBombs(String fields) throws RemoteException, InterruptedException {
		setFields.add(fields);
		if (setFields.size() == 10) {
			String[] tempArr = new String[10];

			for (int x = 0; x < 10; x++) {
				tempArr[x] = setFields.get(x);
			}

			intConn.setBombs(playerName, tempArr);
			JOptionPane.showMessageDialog(pnlField, "Setzphase beendet");
			try {
				oppName = intConn.getOpponentName(playerName);
				while (intConn.waitForBombs(oppName) == false) {
					Thread.sleep(5000);
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.exitGame("Es wurde kein anderer Spieler gefunden!\nDas Spiel wird beendet.");
			}
			this.clearField();
			intConn.rollTheDice(playerName, oppName);
			JOptionPane.showMessageDialog(pnlField, "Spieler bereit.");
			if (intConn.getPlayerStatus(playerName)) {
				JOptionPane.showMessageDialog(pnlField, "Ihr Zug.");
			}
			zustand = Zustand.Schieﬂen;
			// pnlField.setEnabled(intConn.getPlayerStatus(playerName));
		}
	}

	private void userLogin() {

		playerName = JOptionPane.showInputDialog("Geben Sie Ihren Namen ein: ");
		try {
			intConn.login(playerName);
			zustand = Zustand.Setzen;
		} catch (Exception e) {
			e.printStackTrace();
			this.exitGame("Login am Sever fehlgeschlagen! Ist der Server online?");
		}
	}

	private void clearField() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				btnField[x][y].setEnabled(true);
				btnField[x][y].setBackground(new Color(150, 150, 150));
			}
		}
	}

	public static void main(String args[]) {
		SpielClient spielClient = new SpielClient();
		spielClient.createGUI();
		spielClient.userLogin();
	}
}
