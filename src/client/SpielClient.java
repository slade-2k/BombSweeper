package client;

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
	private SpielInterface interfaceConn = getConn();
	private List<String> setFields = new ArrayList<String>();
	private String playerName = null;

	public enum Zustand {
		Setzen, Schieﬂen
	}

	public Zustand zustand;

	public void createGUI() {
		pnlField.setLayout(new GridLayout(10, 10));
		createBtnField(); // Ruft die Funktion CreateBtnField auf.
		add(pnlField); // Fuegt dem JFrame das Panel 'pnlField' hinzu.

		setSize(600, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("BombSearch - Client");
	}

	private void createBtnField() { // Erstellt das Button Array und fuegt es
									// dem Panel 'pnlField' hinzu.
		JButton[][] btnField = new JButton[10][10]; // Erzeugt ein
													// Mehrdimensionales (10*10)
													// JButton Array

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				// btnField[x][y] = new JButton("" + x + y + "");
				btnField[x][y] = new JButton("");
				btnField[x][y].addActionListener(this);
				btnField[x][y].setActionCommand(x + "|" + y);
				pnlField.add(btnField[x][y]);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (zustand == Zustand.Setzen) {
			try {
				setBombs(((JButton) e.getSource()).getActionCommand());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		else if (zustand == Zustand.Schieﬂen) {
			try {
				if(interfaceConn.shotField(playerName, ((JButton) e.getSource()).getActionCommand()) == 1){
					JOptionPane.showMessageDialog(pnlField, "Treffer!");
				}
				else {
					JOptionPane.showMessageDialog(pnlField, "Daneben!");
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private static SpielInterface getConn() {
		try {
			SpielInterface interfaceConn = (SpielInterface) Naming.lookup("Spielbrett"); // TODO
																							// LAN
																							// verf¸gbarkeit
			System.out.println("Die Verbindung zum Server wurde erfolgreich hergestellt.");

			return interfaceConn;
		} catch (Exception e) {
			System.out.println("Die Verbindung zum Server konnte nicht hergestellt werden.");
		}
		return null;
	}

	private void setBombs(String fields) throws RemoteException {
		setFields.add(fields);
		if (setFields.size() == 10) {
			String[] tempArr = new String[10];

			for (int x = 0; x < 10; x++) {
				tempArr[x] = setFields.get(x);
			}

			interfaceConn.setBombs(playerName, tempArr);
			pnlField.setEnabled(false);
			JOptionPane.showMessageDialog(pnlField, "Setzphase beendet");
			while (interfaceConn.waitForAction(playerName) == 0) {

			}
			JOptionPane.showMessageDialog(pnlField, "Spieler bereit.");
			zustand = Zustand.Schieﬂen;
		}
	}
	

	private String userLogin() {

		String s = JOptionPane.showInputDialog("Geben Sie Ihren Namen ein: ");
		try {
			interfaceConn.login(s);
			zustand = Zustand.Setzen;
			return s;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(pnlField, "Login am Sever fehlgeschlagen!");
			return null;
		}
	}

	private void setPlayerName(String name) {
		playerName = name;
	}

	public static void main(String args[]) {
		SpielClient spielClient = new SpielClient();
		spielClient.createGUI();
		spielClient.setPlayerName(spielClient.userLogin());
		// spielClient.setBombs();

	}
}
