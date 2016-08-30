import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

import javax.print.attribute.standard.PrinterInfo;
import javax.swing.JOptionPane;

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
				int times = 5;
				String servMessage = "";
				String connectRequestMessage = "2" + ClientGUI.getUsername() + ":";
				String[] res;
				// sending request connection message to server

				do {
					if (times == 0)
						disconnect();
					printStream = new PrintStream(socket.getOutputStream());
					// printStream.println(connectRequestMessage);
					printStream.println(connectRequestMessage);
					printStream.flush();
					input = new Scanner(socket.getInputStream());
					servMessage = input.nextLine();
					res = new String[3];
					res = Protocol.parseMessage(servMessage);
					// if the respond message from server
					times--;
				} while (times > 0 && !res[1].equals("success"));

				if (Protocol.getType(servMessage) == 2 && res[1].equals("success")) {
					ClientGUI.chatArea.append("You are connected to chat");
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

	private void checkStream() {
		while (true)
			receive();
	}

	private void receive() {

		if (input.hasNext()) {
			String message = input.nextLine();
			processMessage(message);
		}
	}

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
			String[] users = result[0].split(",");
			for (int j = 0; j < users.length; j++) {
				ClientGUI.onlineUsers.append(users[j] + "\n");
			}
			break;
		// server message
		case Protocol.serverMessage:
			JOptionPane.showMessageDialog(null, result[0]);
		case Protocol.disconnectMessage:
			disconnect();
			break;
		}
	}

	public static void sendMessage() {

		if (socket != null) {
			if (!ClientGUI.getInputMessage().equals("")) {
				try {
					message = ClientGUI.getInputMessage();
					char firstChar = message.charAt(0);
					String from = "@" + ClientGUI.getUsername() + ":";
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

				ClientGUI.chatArea.append(ClientGUI.getUsername() + " : " + ClientGUI.getInputMessage());
				ClientGUI.setInputMessage("");
				output.flush();
				// output.close();

			} else
				JOptionPane.showMessageDialog(null, "Please, write a message");
		} else
			JOptionPane.showMessageDialog(null, "You are disconnected");
		// ClientGUI.chatArea.append("You are disconnected");
	}

	public static void connect() {
		Client client = new Client(Integer.parseInt(ClientGUI.getPort()));
		Thread th = new Thread(client);
		th.start();
	}

	public static void disconnect() {
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
