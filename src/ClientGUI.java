import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ClientGUI {

	protected JFrame frame;
	protected static JTextField insertServerIP;
	private static JTextField userNameInput;
	protected static JTextField port_field;
	private static JTextField inputMessage;
	protected JButton btnConnect;
	protected JButton btnSend;
	protected JMenuItem mntmExit;
	protected static JTextArea chatArea;
	protected static JTextArea onlineUsers;

	public ClientGUI() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client");
		JLabel lblNewLabel = new JLabel("Server IP");

		JLabel lblUserName = new JLabel("User name");

		insertServerIP = new JTextField();
		insertServerIP.setColumns(10);
		insertServerIP.setText("127.0.0.1");

		userNameInput = new JTextField("");
		userNameInput.setColumns(10);

		JLabel lblPort = new JLabel("Port");

		port_field = new JTextField();
		port_field.setColumns(10);
		port_field.setText("55555");

		btnSend = new JButton("Send");
		btnSend.addActionListener(new Click(btnSend, null));

		inputMessage = new JTextField("");
		inputMessage.setColumns(10);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new Click(btnConnect, null));

		chatArea = new JTextArea();

		JLabel lblChatBox = new JLabel("Chat box");

		JLabel lblCurrentlyOnline = new JLabel("Currently online");

		onlineUsers = new JTextArea();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
						.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel).addComponent(lblUserName))
						.addGap(5)
						.addGroup(groupLayout
								.createParallelGroup(
										Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout
										.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(userNameInput, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(70).addComponent(btnConnect))
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(insertServerIP, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(26).addComponent(lblPort).addGap(30).addComponent(port_field,
														GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)))
										.addGap(16))
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
										.addComponent(lblChatBox)
										.addPreferredGap(ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(onlineUsers, GroupLayout.PREFERRED_SIZE, 161,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblCurrentlyOnline))
										.addGap(31))))
						.addGroup(groupLayout.createSequentialGroup().addComponent(btnSend).addGap(18)
								.addComponent(inputMessage, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
						.addComponent(chatArea, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel)
								.addComponent(insertServerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPort).addComponent(port_field, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblUserName)
								.addComponent(userNameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnConnect))
						.addGap(26)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblChatBox)
								.addComponent(lblCurrentlyOnline))
						.addGap(29)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(onlineUsers)
								.addComponent(chatArea, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
						.addGap(14)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(inputMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSend))
						.addContainerGap(20, Short.MAX_VALUE)));
		frame.getContentPane().setLayout(groupLayout);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				Client.disconnect();
				System.exit(0);
			}
		});

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new Click(null, mntmExit));

		mnFile.add(mntmExit);
	}

	public static void setPort(String port){
		port_field.setText(port);
	}
	public static String getPort(){
		return port_field.getText();
	}
	public static String getUsername() {
		return userNameInput.getText();
	}

	public void setUsernameInput(String nickName) {
		userNameInput.setText(nickName);
	}

	public static String getServerIP() {
		return insertServerIP.getText();
	}

	public static String getInputMessage() {
		return inputMessage.getText();
	}

	public static void setInputMessage(String message) {
		inputMessage.setText(message);
	}

	public static void setOnlineUsers(String[] s) {
		for (String W : s)
			onlineUsers.append(W);
	}

	public String getOnlineUsers() {
		return onlineUsers.getText();
	}
}
