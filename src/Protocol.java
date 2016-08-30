
public class Protocol {

	private static final int broadcastMessage = 0, privateMessage = 1, connectRequestMessage = 2, disconnectMessage = 3,
			refreshOnlineUsers = 4, serverMessage = 5;

	public static int getType(String message) {
		return Character.getNumericValue(message.charAt(0));
	}

	public static String createMessage(int type, String from, String message) {
		return type + from + message;
	}

	// result from connection request
	// answer = "success"/"fail"
	public static void setResultFromServer(String[] ParsedMessage, String answer) {
		ParsedMessage[1] = answer;
	}

	// res[0] message
	// res[1] client name

	public static String[] parseMessage(String message) {
		int messageType = getType(message);
		String[] res = new String[3];
		switch (messageType) {
		case broadcastMessage:
			// res[0] -> message
			// res[1] -> from
			String str[] = message.split(":", 2);
			res[0] = str[1];
			res[1] = str[0].substring(1);
			return res;
		case privateMessage:
			// res[0] -> message
			// res[1] -> from
			// res[2] -> to
			String[] s = message.split(":", 3);
			res[1] = s[0].substring(2);
			res[2] = s[1].substring(1);
			res[0] = s[2];
			return res;

		// all users separated by comma (,) in one String
		case refreshOnlineUsers:
			res[0] = message.substring(1);
			return res;

		case connectRequestMessage:
			// res[0] the requested name from client
			// res[1] the answer from server "success"/"fail"
			// res[2] the message from server (like try to choose another username)
			String[] st = message.split(":", 2);
			res[0] = st[0];
			res[1] = st[1];
			return res;

		case disconnectMessage:
			// return in res[0] the answer
		case serverMessage:
			res[0] = message.substring(1);
			return res;
		default:
			res[0] = "error";
			return res;
		}
	}
	
	//parsing client message
	public static void parseUserMessage(String message){
		
	}
	/*
	public static void main(String[] args) {
		String message = "1@denis:@alexey:whats up?";
		String messageB = "0denis:hello to all";
		String messageServer = "5user is offline";
		String str[] = parseMessage(messageB);
		String message1="@alexey:Hello";
		
		char firstChar = message1.charAt(0);
		String[] str1 = message1.split(":", 2);
		String to = str1[0] + ":";
		String newMessage="";
		if (firstChar == '@') {
			newMessage=Protocol.createMessage(1, "@denis:", message1);
		}
		String str2[]=parseMessage(newMessage);
		for (int i = 0; i < str2.length; i++) {
			System.out.println(str2[i]);
		}
		
	}
	*/
}
