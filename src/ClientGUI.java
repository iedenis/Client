import java.awt.EventQueue;
import java.net.Socket;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientGUI {

	protected JFrame frame;
	protected static JTextField insertServerIP;
	private static JTextField userNameInput;
	protected JTextField port_field;
	private JTextField textField_3;
	private static JTextField inputMessage;
	protected JButton btnConnect;
	protected JMenuItem mntmExit;
	public ClientGUI() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(150, 150, 450, 300);
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
		port_field.setText("15000");
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		
		inputMessage = new JTextField();
		inputMessage.setColumns(10);
		
		 btnConnect = new JButton("Connect");
		 btnConnect.addActionListener(new Click(btnConnect,null));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_3, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblUserName))
							.addGap(5)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(userNameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(70)
									.addComponent(btnConnect))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(insertServerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(26)
									.addComponent(lblPort)
									.addGap(30)
									.addComponent(port_field, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)))
							.addGap(16))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnSend)
							.addGap(18)
							.addComponent(inputMessage, GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(insertServerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort)
						.addComponent(port_field, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUserName)
						.addComponent(userNameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnect))
					.addGap(23)
					.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(inputMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSend))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		 mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new Click(null, mntmExit));
			
		
		mnFile.add(mntmExit);
	}
	public static String getUsernameInput(){
		return userNameInput.getText();
	}
	public void setUsernameInput(String nickName){
		userNameInput.setText(nickName);
	}
	public static  String getServerIP(){
		return insertServerIP.getText();
	}
	public static String getInputMessage(){
		return inputMessage.getText();
	}
	public static void setInputMessage(String message){
		inputMessage.setText(message);
	}
}
