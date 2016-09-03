
package chat_client;
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
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;

/**
 * @author Denis Ievlev
 * @author Alexey Kurbatsky
 */

/**
 * The class represents a GUI for a client.<br>
 * This is only GUI items.
 */
public class ClientGUI {

	protected JFrame frame;
	protected static JTextField insertServerIP;
	protected static JTextField userNameInput;
	protected static JTextField port_field;
	protected static JTextField inputMessage;
	protected static JButton btnConnect;
	protected JButton btnSend;
	protected JMenuItem mntmExit;
	protected static JTextArea chatArea;
	protected static JTextArea onlineUsers;
	protected JLabel lblPort;
	protected JLabel lblChatBox;
	protected JLabel lblCurrentlyOnline;
	protected JLabel lblNewLabel;
	protected JLabel lblUserName;
	protected JMenuBar menuBar;
	protected JMenu mnFile;

	/**
	 * Constructor for the client's GUI<br>
	 * Just calls the method {@link #initialize()}
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Creates the client's GUI.
	 * Adding all GUI elements to the frame and setting the position
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client");
		lblNewLabel = new JLabel("Server IP");
		lblUserName = new JLabel("User name");

		insertServerIP = new JTextField();
		insertServerIP.setColumns(10);
		insertServerIP.setText("127.0.0.1");

		userNameInput = new JTextField("");
		userNameInput.setColumns(10);
		lblPort = new JLabel("Port");
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
		chatArea.setEditable(false);

		lblChatBox = new JLabel("Chat box");

		lblCurrentlyOnline = new JLabel("Currently online");

		onlineUsers = new JTextArea();
		onlineUsers.setEditable(false);
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new Click(null, mntmExit));
		inputMessage.addActionListener(new Click(inputMessage));
			
		mnFile.add(mntmExit);

		// creating the view
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
		frame.addWindowListener(new Click());
	}

	CopyAction copyAction = new DefaultEditorKit.CopyAction();
	CutAction cutAction = new DefaultEditorKit.CutAction();
	PasteAction pasteAction = new DefaultEditorKit.PasteAction();

	public static void refreshButtonState(String text) {
		btnConnect.setText(text);
	}

	/**
	 * Sets a new port that when we want to change
	 * 
	 * @param port
	 *            a new port
	 */
	public static void setPort(String port) {
		port_field.setText(port);
	}

	/**
	 * Returns a port
	 * 
	 * @return port in String
	 */
	public static String getPort() {
		return port_field.getText();
	}

	/**
	 * Returns a username of the current user
	 * 
	 * @return current username
	 */
	public static String getUsername() {
		return userNameInput.getText();
	}

	/**
	 * Sets a new username
	 * 
	 * @param nickName
	 *            The username of current client
	 */
	public void setUsernameInput(String nickName) {
		userNameInput.setText(nickName);
	}

	/**
	 * Returns the ip of the server
	 * 
	 * @return String ip
	 */
	public static String getServerIP() {
		return insertServerIP.getText();
	}

	/**
	 * Returns the sending message
	 * 
	 * @return String message
	 */
	public static String getInputMessage() {
		return inputMessage.getText();
	}

	/**
	 * Setting an input message in specific text field
	 * 
	 * @param message
	 *            message in String
	 */
	public static void setInputMessage(String message) {
		inputMessage.setText(message);
	}

	/**
	 * Setting all online users
	 * 
	 * @param result
	 *            Message that client received from server after parsing
	 */

	public static void setOnlineUsers(String[] result) {
		ClientGUI.onlineUsers.setText(null);
		String[] users = result[0].split(",");
		for (int i = 0; i < users.length; i++) {
			if (users[i].equals(""))
				;
			else
				ClientGUI.onlineUsers.append(users[i] + "\n");
		}
	}

	/**
	 * Returns all online users
	 * 
	 * @return online users
	 */
	public static String getOnlineUsers() {
		return onlineUsers.getText();
	}
}
