/* ChatRoom ClientMain.java 
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

import java.awt.Checkbox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

	public BufferedReader reader;
	public PrintWriter writer;
	public ClientUIController clientController;
	public LoginUIController loginController;
	public NewGroupUI groupController;
	private Socket sock;
	private String clientID;
	public static String[] argarray;

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent login = FXMLLoader.load(getClass().getResource("login.fxml"));
			// FXMLLoader l = new FXMLLoader(getClass().getResource("login.fxml"));
			// LoginUIController logControl = l.getController();
			Scene loginscene = new Scene(login);
			primaryStage.setScene(loginscene);
			primaryStage.show();

			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		} catch (Exception e) {
			System.out.println("Something went wrong: ViewMain, while loading login.fxml");
			e.printStackTrace();
		}
	}

	public void run() throws Exception {
		setUpNetworking();
		setUpControllers();
		Thread t = new Thread(new IncomingReader());
		t.start();
	}

	public static void main(String[] args) {
		launch(args);
		try {
			new ClientMain().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ViewMain v = new ViewMain(args, writer);
		Thread t = new Thread(v);
		t.start();
	}

	public void setUpNetworking() throws Exception {
		this.sock = new Socket(Constants.IP, Constants.port);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("successful connection");
	}

	public void setUpControllers() {
		FXMLLoader controllerloader = new FXMLLoader(getClass().getResource("userinterface.fxml"));
		clientController = controllerloader.getController();

		FXMLLoader loginloader = new FXMLLoader(getClass().getResource("login.fxml"));
		loginController = loginloader.getController();

		FXMLLoader grouploader = new FXMLLoader(getClass().getResource("login.fxml"));
		groupController = grouploader.getController();

	}

	class IncomingReader implements Runnable {

		@Override
		public void run() {
			String message;
			clientController.setWriter(writer);
			loginController.setWriter(writer);
			groupController.setWriter(writer);
			try {
				while ((message = reader.readLine()) != null) {
					String[] splitMessage = message.split("##");
					switch (splitMessage.length) {
						case 2:
							if (splitMessage[1].equals("bad_name")) {
								loginController.displayWarning();// display red "bad username" text
							}
							break;
						case 3:
							if (splitMessage[1].equals("normal")) {
								clientController.updatetextarea(splitMessage[2]);// append in text area
							}
							else if (splitMessage[1].equals("global")) {
								clientController.updatetextarea(splitMessage[2]);// append in text area
								
							}
							else if (splitMessage[1].equals("good_name")) {
								String username = splitMessage[2];
								clientID = username;
								clientController.setClientID(username);
								loginController.setcanClose(true);
							}
							break;
						case 4:
							if (splitMessage[1].equals("invisible")) {
								if (splitMessage[2].equals("all_clients")) {
									String[] clients = splitMessage[3].split(",");// update 1-on-1 list
									ArrayList<String> clientList = new ArrayList<String>(Arrays.asList(clients));
									clientController.updateClientSelection(clientList);
									clientController.updateUserList(clientList);// update ListView
									// TODO update group generation boxes
									Constants.allClients = clientList;
								}
							}
							else if (splitMessage[1].equals("group_add")) {
								String groupName = splitMessage[2];// output join message
								clientController.updateGroupSelection(groupName);// update group name dropdown
								clientController.updatetextarea(splitMessage[3]);
							}
							break;
					}

				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
