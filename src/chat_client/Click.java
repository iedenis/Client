
package chat_client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
/**
 * The class represents  a listener for buttons<br>
 * from client GUI. Connect between {@link Client} class and {@link ClientGUI} class
 * @author Denis Ievlev
 * @author Alexey Kurbatsky
 */

public class Click implements ActionListener {
	protected JButton _button;
	protected JMenuItem _item;
	public static String message;
	
	public Click(JButton button, JMenuItem item) {
		this._button = button;
		this._item = item;
	}

	/**
	 * All possible actions for buttons or menu items
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand() == "Exit") {
			Client.disconnect();
			System.exit(0);
		}

		if (e.getActionCommand() == "Connect") {
			//client=new Client(15500);
			Client.connect();

			System.out.println("Username is:" + ClientGUI.getUsername());
			if (ClientGUI.getUsername().equals("")) {
			
				JOptionPane.showMessageDialog(null, "You have to insert your username");
			}
				else{
				System.out.println("Starting new client");
			//	client.start();
				_button.setText("Disconnect");
			}
		}
		
		if (e.getActionCommand() == "Disconnect") {
			Client.disconnect();
			_button.setText("Connect");
		}
		if(e.getActionCommand()=="Send"){
			Client.sendMessage();
		}
	}

}
