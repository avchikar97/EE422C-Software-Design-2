/* ChatRoom ClientObserver.java 
 * EE422C Project 7 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Nicholas Sutanto
 * nds729
 * 16220
 * Slip days used: <1>
 * Spring 2017
 */

package assignment7;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer {
	public String clientID;

	ClientObserver(OutputStream out, String id) {
		super(out);
		clientID = id;
	}

	@Override
	public void update(Observable o, Object arg) {
		boolean toPrint = false;
		String[] splitMessage = ((String) arg).split("##");
		switch(splitMessage.length){
			case 4: {
				if (splitMessage[0].equals("system")) {
					if (splitMessage[1].equals("all")) {
						String message = "##normal##" + splitMessage[2];
						this.println(message);
					}
				}
				break;
			}
			case 5: {
				if (splitMessage[1].equals("group_add")) {
					String groupName = splitMessage[2];
					String message = "##group_add##" + "##" + groupName + "##" + splitMessage[4] + "\"" + groupName + "\"";// You have been added to xyz group
					String[] targets = splitMessage[3].split(",");
					for (String recipient : targets) {
						if (recipient.equalsIgnoreCase(clientID)) {
							toPrint = true;
						}
					}
					if (toPrint) {
						this.println(message);// you have been added to "xyz" group
						toPrint = false;
					}
				}
				else if (splitMessage[1].equals("global")) {
					if (splitMessage[2].equals("all")) {
						String sender = splitMessage[3];
						String main_message = splitMessage[4];
						String message;
						if (sender.equals("clientID"))
							sender = "You";
						message = "##global##" + sender + ": " + main_message;
						this.println(message);
					}
				}
				else if (splitMessage[1].equals("system")) {
					if (splitMessage[2].equals("all")) {
						if (splitMessage[3].equals("all_clients")) {
							String message = "##invisible##all_clients##" + splitMessage[4];
							this.println(message);
						}
					}
				}
				break;
			}
			case 6: {
				if (splitMessage[1].equalsIgnoreCase("group_mess")) {
					String message;
					String groupName = splitMessage[2];
					String[] targets = splitMessage[3].split(",");
					String sender = splitMessage[4];
					if (sender.equalsIgnoreCase(clientID)) {
						sender = "You";
					}
					String main_message = splitMessage[5];
					message = "##normal##" + groupName + ": " + sender + ": " + main_message;
					for (String recipient : targets) {
						if (clientID.equalsIgnoreCase(recipient)) {
							toPrint = true;
						}
					}
					if (toPrint) {
						this.println(message);
						toPrint = false;
					}
				}
				else if (splitMessage[1].equals("single_mess")) {
					String recipient = splitMessage[2];
					String sender = splitMessage[3];
					String main_message = splitMessage[4];
					String message;
					toPrint = false;
					if (sender.equals(clientID)) {
						sender = "You";
					}
					if(recipient.equals(clientID)){
						message = "##normal##" + sender + ": " + main_message;
					}
				}
				break;
			}
			default:
				System.out.println("Damn, bad roll: " + clientID);
				System.exit(0);
				break;
		}
		this.flush();
}
}
