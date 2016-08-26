import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/*
 * Client class
 */
public class Click implements ActionListener {
	protected JButton _button;
	protected JMenuItem _item;
	public static String message;
	
	public Click(JButton button, JMenuItem item) {
		this._button = button;
		this._item = item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Client client = new Client(15500);

		if (e.getActionCommand() == "Exit") {
			client.disconnect();
			System.exit(0);
		}

		if (e.getActionCommand() == "Connect") {
			System.out.println("Username is:" + ClientGUI.getUsernameInput());
			if (ClientGUI.getUsernameInput().equals("")) {
				Component frame = null;
				JOptionPane.showMessageDialog(frame, "You have to insert your username");
			}
				else{
				System.out.println("Starting new client");
				client.start();
				_button.setText("Disconnect");
			}
		}
		
		if (e.getActionCommand() == "Disconnect") {
			client.disconnect();
			_button.setText("Connect");
		}
		if(e.getActionCommand()=="Send"){
			
		}
	}

}
