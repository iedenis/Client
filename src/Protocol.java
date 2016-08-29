
public class Protocol {

	private static final int broadcastMessage = 0, privateMessage = 1, connectRequestMessage = 2, disconnectMessage = 3,
			refreshOnlineUsers = 4;
	
	public static int getType(String message) {
		return Character.getNumericValue(message.charAt(0));
	}

	public static String createMessage(int type, String to, String message) {
		return type + to + message;
	}

	// res[0] message
	// res[1] client name

	public static String[] parseMessage(String message) {
		int messageType = getType(message);
		String[] res = new String[2];
		switch (messageType) {
		case broadcastMessage:
			res[0] = message.substring(1);
			return res;
		case privateMessage:
			String[] s = message.split(":", 2);
			res[1] = s[0].substring(2);
			res[0] = s[1];
			return res;

		case refreshOnlineUsers:
			return res;

		case connectRequestMessage:
			// res[0] the requested name from client
			// res[1] the answer from server success/fail
			
			String[] st = message.split(":", 2);
			res[0] = st[0];
			res[1] = st[1];
			return res;
			
		case disconnectMessage:
			// return in res[0] the answer

		default:
			res[0] = "error";
			return res;
		}
	}
}
