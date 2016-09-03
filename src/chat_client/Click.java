
package chat_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * The class represents a listener for buttons<br>
 * from client GUI. Connect between {@link Client} class and {@link ClientGUI}
 * class
 * 
 * @author Denis Ievlev
 * @author Alexey Kurbatsky
 */

public class Click implements ActionListener, WindowListener {
	protected JButton _button;
	protected JMenuItem _item;
	public static String message;
	private JTextField _field;

	public Click(JButton button, JMenuItem item) {
		this._button = button;
		this._item = item;
	}

	/**
	 * default constructor for window listener.
	 */
	public Click() {
	}

	/**
	 * Creates an object for for listener to send message by striking "Enter"
	 * key
	 * 
	 * @param field
	 *            - JTexfield
	 */
	public Click(JTextField field) {
		_field = field;
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

		if (e.getActionCommand().equals("Connect")) {
			if (ClientGUI.getUsername().equals("")) {
				JOptionPane.showMessageDialog(null, "You have to insert your username");
			} else {
				Client.connect();
				if (Client.isConnected)
					ClientGUI.refreshButtonState("Disconnect");
			}
		}
		if (e.getActionCommand().equals("Disconnect")) {
			Client.disconnect();
			if (!Client.isConnected) {
				ClientGUI.refreshButtonState("Connect");
			}
		}
		if(e.getActionCommand().equals("Stop")){
			Client.stop();
		}
		if (e.getActionCommand().equals("Send") || e.getSource().equals(_field)) {
			Client.sendMessage();
		}
	}

	
	
	@Override
	public void windowClosing(WindowEvent e) {
		Client.disconnect();
		System.exit(0);
	}


	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
