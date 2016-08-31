package chat_client;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;


/**
 * class Client represents all client functions
 * 
 * @author Denis Ievlev
 * @author Alexey Kurbatsky
 */

public class Client implements Runnable {
	int _port;
	static Socket socket;
	public static String message;
	private static boolean running = true;
	private static PrintStream printStream = null;
	Scanner input;
	static PrintStream output;
	private static int counter;
	// private PrintWriter writer;

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
		while (running && socket == null && counter < 10) {
			counter++;
			System.out.println("Trying to connect to server..." + " time " + counter);

			try {
				socket = new Socket(ClientGUI.getServerIP(), this._port);
				System.out.println("Client socket is: " + socket);

			} catch (UnknownHostException e) {
				// e.printStackTrace();
				try {
					Thread.sleep(5000);
					System.out.println("Waiting for server...");
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
			System.out.println("Accepted socket from server");

			try {
				// int times = 5;
				String servMessage = "";
				String connectRequestMessage = "2" + ClientGUI.getUsername() + ":";
				String[] res;
				// sending request connection message to server

				// TODO change while to function

				printStream = new PrintStream(socket.getOutputStream());
				// printStream.println(connectRequestMessage);
				System.out.println("LOG: Connection request message is: " + connectRequestMessage);
				printStream.println(connectRequestMessage);
				printStream.flush();
				input = new Scanner(socket.getInputStream());
				servMessage = input.nextLine();

				System.out.println("LOG: connection request message from server: " + servMessage);
				res = new String[3];
				res = Protocol.parseMessage(servMessage);
				if (res[1].equals("fail"))
					JOptionPane.showMessageDialog(null, "Choose another username");
				System.out.println("LOG: The answer from server is " + res[1]);
				// if the respond message from server
				// times--;

				if (Protocol.getType(servMessage) == 2 && res[1].equals("success")) {
					ClientGUI.chatArea.append("You are connected to chat\n");
					// check for input messages
					checkStream();

				} else if (res[2] == null || res[2] == "") {
					System.out.println("haven't receive the answer from server");
				} else
					JOptionPane.showMessageDialog(null, res[2]);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks the input stream
	 */
	private void checkStream() {
		while (true)
			receive();
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
		// private message from result[2]
		case Protocol.broadcastMessage:
			ClientGUI.chatArea.append("[public] " + result[1] + ": " + result[0] + "\n");
			break;
		// broadcast message
		case Protocol.privateMessage:
			ClientGUI.chatArea.append("[private] " + result[2] + ": " + result[0] + "\n");
			break;
		case Protocol.refreshOnlineUsers:
			System.out.println("LOG: users online: " + msg);
			refreshOnlineUsers(result);

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
	 * Refreshing the online users
	 * 
	 * @param result
	 *            is parsed String from {@link Protocol #parseMessage(String)}
	 */
	private void refreshOnlineUsers(String[] result) {
		ClientGUI.chatArea.setText(null);
		String[] users = result[0].split(",");
		for (int i = 0; i < users.length; i++) {
			if (users[i] == "")
				;
			else
				ClientGUI.onlineUsers.append(users[i] + "\n");
		}

	}

	/**
	 * Sends a String message when button pressed Creates a message with
	 * {@link Protocol #createMessage(int, String, String)}
	 */
	public static void sendMessage() {

		if (socket != null) {
			if (!ClientGUI.getInputMessage().equals("")) {
				try {
					message = ClientGUI.getInputMessage();
					char firstChar = message.charAt(0);
					String from = "@" + ClientGUI.getUsername();
					String newMessage = "";
					if (firstChar == '@') {
						newMessage = Protocol.createMessage(1, from, message);
					} else {
						newMessage = Protocol.createMessage(0, from, message);
					}
					output = new PrintStream(socket.getOutputStream());
					output.println(newMessage);
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

				ClientGUI.chatArea.append(ClientGUI.getUsername() + " : " + ClientGUI.getInputMessage() + "\n");
				ClientGUI.setInputMessage("");
				output.flush();
				// output.close();

			} else
				JOptionPane.showMessageDialog(null, "Please, write a message");
		} else
			JOptionPane.showMessageDialog(null, "You are disconnected");
		// ClientGUI.chatArea.append("You are disconnected");
	}

	/**
	 * Making a new client when connect button is pressed
	 */
	public static void connect() {
		Client client = new Client(Integer.parseInt(ClientGUI.getPort()));
		Thread th = new Thread(client);
		th.start();
	}

	/**
	 * Disconnection from server and closing all open streams.
	 * Send a disconnect message to server
	 */
	public static void disconnect() {
		if (socket != null) {
			running = false;
			try {
				printStream = new PrintStream(socket.getOutputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			printStream.println(Protocol.createMessage(Protocol.disconnectMessage, "", ""));
			printStream.flush();
			printStream.close();

			System.out.println("Disconnecting..");
			ClientGUI.setOnlineUsers(new String[] { "" });
		}
		try {
			if (socket != null) {
				socket.close();
				System.out.println("Disconnected successfully from chat");
			} else {
				System.out.println("There is no socket");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main function of the client. 
	 * Starts a new GUI
	 * @param args no arguments from user needed
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Started thread " + Thread.currentThread());
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
