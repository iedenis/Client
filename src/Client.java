import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Client extends Thread {
	int _port;
	Socket socket;
	protected static String message;
	private boolean running = true;
	private PrintStream printStream = null;
	Scanner input;
	
	// private static int counter;
	public Client(int port) {
		this._port = port;
		socket=null;
	}

	public void run() {
		int counter = 0;
		while (running) {
			System.out.println("Started thread with client" + Thread.currentThread().getName());
			while (socket == null && counter < 10 && running) {
				counter++;
				System.out.println("Trying to connect to server...");
				try {
					this.socket = new Socket(ClientGUI.getServerIP(), 15500);
					System.out.println("Client socket is: "+ this.socket);
				} catch (UnknownHostException e) {
					// e.printStackTrace();
					try {
						Thread.sleep(5000);
						System.out.println("Waiting for server...");

					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					// e.printStackTrace();
					try {
						Thread.sleep(5000);
						// System.out.println("Still waiting for server");
					} catch (InterruptedException e1) {
						running = false;
					}
				}

			}
			if (socket != null) {

				// servSocket = new ServerSocket(_port);

				System.out.println("Accepted socket from server");
				try {
					input = new Scanner(socket.getInputStream());
					printStream = new PrintStream(socket.getOutputStream());
					printStream.println(ClientGUI.getUsernameInput() + " is connected to chat");
					printStream.flush();
					this.checkStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// message=
				// printStream.println(ClientGUI.getUsernameInput()+ " is
				// connected to chat");

			}
			//disconnect();
		}
	}

	private  void  checkStream() {
		while (true)
			receive();
	}

	private void receive() {

		if (input.hasNext()) {
			message = input.nextLine();
			ClientGUI.chatArea.append(message);
		}
	}

	public   void sendMessage() {
		System.out.println("Send message in thread "+Thread.currentThread());
		if (socket != null) {
			if (!ClientGUI.getInputMessage().equals("")) {
				printStream.println(ClientGUI.getInputMessage());
				ClientGUI.chatArea.append(ClientGUI.getInputMessage());
			} else
				JOptionPane.showMessageDialog(null, "Please write a message");
		} else 
			ClientGUI.chatArea.append("You are disconnected");
	}

	public  void disconnect() {
		try {
			if (socket != null) {
				socket.close();
				running = false;
			} else {
				System.out.println("There is no socket");
				running = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public  static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Started thread " + Thread.currentThread());
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
					// Client client=new Client(15500);
					// client.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
