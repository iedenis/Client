import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;

public class Client extends Thread {
	int _port;
	Socket socket = null;
	protected static String message;
	private boolean running = true;

	// private static int counter;
	public Client(int port) {
		this._port = port;
	}

	public void run() {
		int counter = 0;
		while(running){
			System.out.println("Started thread " + Thread.currentThread().getName());
			while (socket == null&&counter<10&&running) {
				counter++;
				System.out.println("Trying to connect to server...");
				try {
					socket = new Socket(ClientGUI.getServerIP(), 15500);
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
				PrintStream printStream = null;
				try {
					printStream = new PrintStream(socket.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				// message=
				printStream.println("hello to server");

			}
			disconnect();
		}
	}

	public void disconnect() {
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

	public static void main(String[] args) {
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
