/* ChatRoom ServerMain.java 
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ServerMain extends Observable {
	public ServerSocket serverSock;
	private ArrayList<PrintWriter> outputStreams;
	int counter;
	Object idLock;
	public HashMap<String, ClientObserver> allObservers; // could the value be the corresponding PrintWriter?
	public HashMap<String, ArrayList<String>> chatGroups;// the actual groups

	public static void main(String[] args) {
		ServerMain s = new ServerMain();
		s.setUp();
	}

	public void setUp() {
		try {
			serverSock = new ServerSocket(Constants.port);
			idLock = new Object();
			ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
			counter = 0;
			idLock = new Object();
			allObservers = new HashMap<String, ClientObserver>(); // could the value be the corresponding PrintWriter?
			chatGroups = new HashMap<String, ArrayList<String>>();// the actual groups
			while (true) {
				synchronized (idLock) {
					Socket clientSocket = serverSock.accept();
					String tempName;
					PrintWriter printer = new PrintWriter(clientSocket.getOutputStream());
					clientSocket.getOutputStream().flush();
					// key = generateID(); // generate unique ID for client
					// BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					// while ((key = reader.readLine()) != null) {
					// String[] splitMessage = key.split("##");
					// if (splitMessage[0].equalsIgnoreCase("new_user")) {
					// key = splitMessage[1];
					// break;
					// }
					// }
					// String message = "##system" + "##all" + "##" + key + " has come online!";
					// notifyObservers(message);
					tempName = generateID();
					outputStreams.add(printer);
					// ClientObserver client = new ClientObserver(clientSocket.getOutputStream(), tempName);
					// corrSocket.put(tempName, client);
					Thread t = new Thread(new ClientHandler(clientSocket)); // uses inner class with run function
					t.start();
				}
				// this.addObserver(client);
			}
		} catch (IOException e) {
			System.out.println("That's the wrong port number");
			e.printStackTrace();
		}
	}

	class ClientHandler implements Runnable {
		private BufferedReader reader;
		private PrintWriter writer;
		private Socket mySock;

		ClientHandler(Socket clientSocket) {
			mySock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
				writer = new PrintWriter(mySock.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			String message;
			try{
				while((message = reader.readLine())!= null){
					System.out.println("received message: " + message);//TODO debugging
					String[] splitMessage = message.split("##");
					switch (splitMessage.length) {
						case 3:
							if (splitMessage[1].equals("name_request")) {
								ArrayList<String> allNames;
								String username = splitMessage[2];
								boolean goodName = true;
								synchronized (allObservers) {
									allNames = new ArrayList<String>(allObservers.keySet());
								}
								for(String s : allNames){
									if (s.equals(username)) {
										goodName = false;
										break;
									}
								}
								if (goodName) {
									ClientObserver client = new ClientObserver(mySock.getOutputStream(), username);
									addObserver(client);
									allObservers.put(username, client);
									ArrayList<String> allClients;
									synchronized (allObservers) {
										allClients = new ArrayList<String>(allObservers.keySet());
									}
									message = "##system##all##all_clients##";
									for (String s : allClients) {
										message = message + s + ",";
									}
									notifyObservers(message);
									writer.println("##good_name##" + username);
								}
								else {
									writer.println("##bad_name");
								}
							}
							break;
						case 4:
							if (splitMessage[1].equalsIgnoreCase("new_group")) { // creating a new group
								String groupName = splitMessage[2]; // gets the group name
								String[] members = splitMessage[3].split(",");
								ArrayList<String> groupMem = new ArrayList<String>(Arrays.asList(members));
								chatGroups.put(groupName, groupMem);
								message = "##group_add" + "##" + groupName + "##" + splitMessage[3]
										+ "##You have been added to ";
								break;
							}
						case 5:
							if (splitMessage[1].equals("group_mess")) { // group chat
								String sender = splitMessage[2];
								String group_name = splitMessage[3];
								String main_message = splitMessage[4];
								ArrayList<String> groupNames = chatGroups.get(group_name);// gets all recipients
								message = "##group_message" + "##" + group_name + "##";
								for (String member : groupNames) { // add recipients
									message = message + member + ",";
								}
								message = message + "##" + sender + ": " + main_message;
							}
							else if (splitMessage[1].equals("single_mess")) {
								// do nothing -> the client observer will parse this
							}

						default:
							break;
					}
					setChanged();
					notifyObservers(message);
				}
			}
			catch (IOException i) {
				i.printStackTrace();
			}

		}

	}

	public String generateID() {
		synchronized (idLock) {
			counter++;
			int row = counter;
			String result = "";
			int log = 26;
			int tempInt = 1;
			while (log < row) {
				log = log * 26;
				tempInt++;
			}
			char[] tempArray = new char[tempInt];
			int[] tempInts = new int[tempInt];
			for (int i = counter; i >= 1; i--) {
				tempInts[0]++;
				for (int j = 0; j < tempInts.length; j++) {
					if (tempInts[j] > 26) {
						tempInts[j + 1]++;
						tempInts[j] = 1;
					}
				}
			}
			for (int i = tempArray.length - 1; i >= 0; i--) {
				tempArray[tempArray.length - 1 - i] = (char) (tempInts[i] + 64);// flip the order of the tempInt digits and make them capital letters
			}
			result = new String(tempArray);
			return result;
		}

	}
}
