package chat_client;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.NoSuchElementException;
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
			while (socket == null && counter < 10 && running) {
				ClientGUI.refreshButtonState("Stop");
				counter++;
				appendTextToChatArea("Trying to connect to server..." + " time " + counter + "\n");

				try {
					socket = new Socket(ClientGUI.getServerIP(), this._port);

				} catch (UnknownHostException e) {
					// e.printStackTrace();
					try {
						Thread.sleep(5000);
						appendTextToChatArea("Waiting for server...\n");
					} catch (InterruptedException e1) {
						// e1.printStackTrace();
					}
				} catch (IOException e) {
					// e.printStackTrace();
					try {
						Thread.sleep(5000);
						// appendTextToChatArea("Still waiting for server\n");
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
        try {
            String message = input.nextLine();
            processMessage(message);
        }
        catch (NoSuchElementException e) {
            disconnect(false);
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
			appendTextToChatArea("[public] " + result[1] + ": " + result[0] + "\n");
			break;

		// private message
		case Protocol.privateMessage:
			appendTextToChatArea("[private] " + result[1] + ": " + result[0] + "\n");
			break;

		case Protocol.refreshOnlineUsers:
			ClientGUI.setOnlineUsers(result);
			break;

		// server message
		case Protocol.serverMessage:
			JOptionPane.showMessageDialog(null, result[0]);
			break;
		case Protocol.disconnectMessage:
			disconnect(true);
			break;
		}
	}

	/**
	 * Sends a String message when button pressed Creates a message with
	 * {@link Protocol#createMessage(int, String, String)}
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
						output = new PrintStream(socket.getOutputStream());
						if (firstChar == '@') {
							if (message.indexOf(':') < 0) {
								JOptionPane.showMessageDialog(null, "Wrong message format\n Use ':' after nickname");
							} else {
								newMessage = Protocol.createMessage(Protocol.privateMessage, from, message);
								appendTextToChatArea("[private] " + message + "\n");
								output.println(newMessage);
							}
						} else {
							newMessage = Protocol.createMessage(Protocol.broadcastMessage, from, message);
							appendTextToChatArea("[public] " + message + "\n");
							output.println(newMessage);
						}
						//output.flush();
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
	 * @param disconnectingMessage if got disconnected message so it prevents to send more unneeded message to server
	 */
	public static void disconnect(boolean disconnectingMessage) {
		try {
			if (socket != null) {
				running = false;
				if (!disconnectingMessage) {
					printStream.println(Protocol.createMessage(Protocol.disconnectMessage, "", ""));
				}
				printStream.flush();
				printStream.close();
				socket.close();
				ClientGUI.setOnlineUsers(new String[] { "" });
				appendTextToChatArea("You are disconnected from chat\n");
				isConnected = false;
				ClientGUI.refreshButtonState("Connect");
			} else {
				appendTextToChatArea("You were not connected to chat\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void stop() {
		running = false;
		appendTextToChatArea("Stopped\n");
		ClientGUI.refreshButtonState("Connect");
	}

	/**
	 * Appending the needed text to the chat window
	 * 
	 * @param text
	 *            - text to append
	 */
	public static void appendTextToChatArea(String text) {
		ClientGUI.chatArea.append(text);
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
