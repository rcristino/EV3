package comms;

import javax.swing.JOptionPane;

public class CommsClient {

	public static void main(String[] args) throws InterruptedException {
		String addressIp = JOptionPane
				.showInputDialog("Enter IP Address of a machine that is\n" + "running the date service on port 9090:");
		SocketClient cli = new SocketClient(addressIp, 9090);

		boolean isWaiting = true;
		while (isWaiting) {
			String msgSend = JOptionPane.showInputDialog("Enter the message:");
			RemoteEvent evt = new RemoteEvent(0,msgSend);
			if (msgSend.contains("EXIT")) {
				isWaiting = false;
				
			}
			cli.sendMessage(evt.convertToString());

		}

		System.exit(0);

	}
}
