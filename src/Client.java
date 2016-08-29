import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Client extends Thread {
	int _port;
	static Socket socket;
	protected static String message;
	private static boolean running = true;
	private static PrintStream printStream = null;
	Scanner input;
	static PrintStream output;
	private static int counter;

	public Client(int port) {
		this._port = port;
		socket = null;
	}

	public void run() {
		counter = 0;

		System.out.println("Started thread with client" + Thread.currentThread().getName());
		while (socket == null && counter < 10 && running) {
			counter++;
			System.out.println("Trying to connect to server...");
			try {
				socket = new Socket(ClientGUI.getServerIP(), 15500);
				System.out.println("Client socket is: " + socket);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				try {
					Thread.sleep(5000);
					System.out.println("Waiting for server...");

				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					Thread.sleep(5000);
					ClientGUI.chatArea.append("Still waiting for server");
				} catch (InterruptedException e1) {
					running = false;
				}
			}

		}
		if (socket != null) {
			System.out.println("Accepted socket from server");
			try {
				input = new Scanner(socket.getInputStream());
				printStream = new PrintStream(socket.getOutputStream());
				printStream.println(ClientGUI.getUsername() + " is connected to chat\n");
				printStream.flush();
				checkStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// message=
			// printStream.println(ClientGUI.getUsernameInput()+ " is
			// connected to chat");

		}
		// disconnect();
	}

	private void checkStream() {
		while (true)
			receive();
	}

	private void receive() {

		if (input.hasNext()) {
			String Mmessage = input.nextLine();
			ClientGUI.chatArea.append(Mmessage);
		}
	}

	public static void sendMessage() {
		String message;
		if (socket != null) {
			if (!ClientGUI.getInputMessage().equals("")) {
				try {
					output = new PrintStream(socket.getOutputStream());
					output.println(ClientGUI.getInputMessage());

				} catch (IOException e) {
					e.printStackTrace();
				}
				
				ClientGUI.chatArea.append(ClientGUI.getUsername()+" : " + ClientGUI.getInputMessage());
				ClientGUI.setInputMessage("");
				output.flush();
				//output.close();

			} else
				JOptionPane.showMessageDialog(null, "Please write a message");
		} else
			JOptionPane.showMessageDialog(null, "You are disconnected");
		// ClientGUI.chatArea.append("You are disconnected");
	}

	public static void connect() {
		Client client = new Client(15500);
		Thread th = new Thread(client);
		th.start();
	}

	public static void disconnect() {
		try {
			if (socket != null) {
				socket.close();
				System.out.println("Disconnected successfully");
				// running = false;
			} else {
				System.out.println("There is no socket");
				// running = false;
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
