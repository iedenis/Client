package chat_client;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Class Client represents all client functions<br>
 * This is the main class for the client
 * 
 * @author Denis Ievlev
 * @author Alexey Kurbatsky
 * 
 */

public class Client implements Runnable {
	private int _port;
	private static Socket socket;
	protected static String message;
	private static boolean running = true;
	private static PrintStream printStream = null;
	private Scanner input;
	private static PrintStream output;
	private static int counter;
	protected static boolean isConnected = false;
	String userName = ClientGUI.getUsername();

	/**
	 * Constructor for Client
	 * 
	 * @param port
	 *            integer
	 */
	public Client(int port) {
		this._port = port;
		socket = null;
	}

	public void run() {
		while (running) {
			while (socket == null && counter < 10) {
				counter++;
				ClientGUI.chatArea.append("Trying to connect to server..." + " time " + counter);

				try {
					socket = new Socket(ClientGUI.getServerIP(), this._port);

				} catch (UnknownHostException e) {
					// e.printStackTrace();
					try {
						Thread.sleep(5000);
						ClientGUI.chatArea.append("Waiting for server...\n");
					} catch (InterruptedException e1) {
						// e1.printStackTrace();
					}
				} catch (IOException e) {
					// e.printStackTrace();
					try {
						Thread.sleep(5000);
						ClientGUI.chatArea.append("Still waiting for server\n");
					} catch (InterruptedException e1) {
						// e1.printStackTrace();
						running = false;
					}
				}
			}
			if (socket != null) {
				try {
					String servMessage = "";
					String connectRequestMessage = "2" + userName + ":";
					String[] res;

					// sending request connection message to server

					printStream = new PrintStream(socket.getOutputStream());
					printStream.println(connectRequestMessage);
					printStream.flush();
					input = new Scanner(socket.getInputStream());
					servMessage = input.nextLine();

					res = new String[3];
					res = Protocol.parseMessage(servMessage);

					if (res[1].equals("fail")) {
						userName = JOptionPane.showInputDialog(null, "Please choose another username", "Input Dialog",
								JOptionPane.PLAIN_MESSAGE);
					}

					if (Protocol.getType(servMessage) == 2 && res[1].equals("success")) {
						isConnected = true;
						ClientGUI.chatArea.setText("You are connected to chat\n");
						ClientGUI.refreshButtonState("Disconnect");

						checkStream(); // check for input messages

					} else if (res[2] == null || res[2].equals("")) {
						ClientGUI.chatArea.append("Haven't received any answer from server\n");
					} else
						JOptionPane.showMessageDialog(null, res[2]);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Checks the input stream
	 */
	private void checkStream() {
		while (running) {
			receive();
		}
	}

	/**
	 * receives line by line input messages
	 */
	private void receive() {
		if (input.hasNext()) {
			String message = input.nextLine();
			processMessage(message);
		}
	}

	/**
	 * Handles input messages
	 * 
	 * @param msg
	 *            message that represented by String the received message is
	 *            parsed by Protocol
	 *            {@link chat_client.Protocol #parseMessage(String)} method
	 */
	private void processMessage(String msg) {
		String[] result = Protocol.parseMessage(msg);
		switch (Protocol.getType(msg)) {

		// broadcast message from result[2]
		case Protocol.broadcastMessage:
			ClientGUI.chatArea.append("[public] " + result[1] + ": " + result[0] + "\n");
			break;

		// private message
		case Protocol.privateMessage:
			ClientGUI.chatArea.append("[private] " + result[2] + ": " + result[0] + "\n");
			break;

		case Protocol.refreshOnlineUsers:
			ClientGUI.setOnlineUsers(result);
			break;

		// server message
		case Protocol.serverMessage:
			JOptionPane.showMessageDialog(null, result[0]);
			break;
		case Protocol.disconnectMessage:
			disconnect();
			break;
		}
	}

	/**
	 * Sends a String message when button pressed Creates a message with
	 * {@link Protocol #createMessage(int, String, String)}
	 */
	public static void sendMessage() {
		if (isConnected) {
			if (socket != null) {
				if (!ClientGUI.getInputMessage().equals("")) {
					try {
						message = ClientGUI.getInputMessage();
						char firstChar = message.charAt(0);
						String from = "@" + ClientGUI.getUsername();
						String newMessage = "";
						if (firstChar == '@') {
							newMessage = Protocol.createMessage(Protocol.privateMessage, from, message);
							ClientGUI.chatArea.append("[private] " + message + "\n");
						} else {
							newMessage = Protocol.createMessage(Protocol.broadcastMessage, from, message);
							ClientGUI.chatArea.append("[public] " + message + "\n");
						}
						output = new PrintStream(socket.getOutputStream());
						output.println(newMessage);
						output.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}

					ClientGUI.setInputMessage("");
					output.flush();
				} else
					JOptionPane.showMessageDialog(null, "Please, write a message");
			} else
				JOptionPane.showMessageDialog(null, "You are disconnected");
		} else
			JOptionPane.showMessageDialog(null,
					"You can't send messages when you are offline\nPlease connect to the chat");
	}

	/**
	 * Making a new client when connect button is pressed
	 */
	public static void connect() {
		running = true;
		Client client = new Client(Integer.parseInt(ClientGUI.getPort()));
		Thread clientThread = new Thread(client);
		clientThread.start();
	}

	/**
	 * Disconnection from server and closing all open streams. Send a disconnect
	 * message to server
	 */
	public static void disconnect() {
		try {
			if (socket != null) {
				running = false;
				printStream.println(Protocol.createMessage(Protocol.disconnectMessage, "", ""));
				printStream.flush();
				printStream.close();
				socket.close();
				ClientGUI.setOnlineUsers(new String[] { "" });
				ClientGUI.chatArea.append("You are disconnected from chat");
				isConnected = false;
			} else {
				ClientGUI.chatArea.append("You were not connected to chat");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main function of the client. Starts a new GUI
	 * 
	 * @param args
	 *            no arguments from user needed
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
