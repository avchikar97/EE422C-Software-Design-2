/* ChatRoom ClientUIController.java 
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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class ClientUIController {
	
	public TextField userinput;
	public TextArea textarea;
	public ListView userlist;
	public RadioButton singlechat, groupchat;
	public ChoiceBox selectchat, selectgroup;
	private String clientID;
	private PrintWriter writer;
	
	public void setClientID(String id) {
		clientID = id;
	}

	public void setWriter(PrintWriter p) {
		writer = p;
	}

	public void updatetextarea(String message) {
		textarea.appendText(message);
	}

	public void updateGroupSelection(String newGroup) {
		selectgroup.getItems().add(newGroup);
	}

	public void updateClientSelection(ArrayList<String> clients) {
		selectchat.getItems().clear();
		for (String client : clients) {
			selectchat.getItems().add(client);
		}
	}

	public void updateUserList(ArrayList<String> clients) {
		userlist.getItems().clear();
		for (String client : clients) {
			userlist.getItems().add(client);
		}
	}

	public void clicksend() {
		String message = userinput.getText();
		// textarea.appendText(message + "\n");
		if(singlechat.isSelected()){
			message = "##single_mess##" + (String) selectchat.getValue() + "##" + clientID + "##" + userinput.getText();
		}
		else if (groupchat.isSelected()) {
			message = "##group_mess##" + clientID + "##" + (String) selectgroup.getValue() + "##"
					+ userinput.getText();
		}
		userinput.clear();
		writer.print(message);
	}

	public void onEnter(ActionEvent event) {
		clicksend();
	}

	public void singlechataction() {
		selectgroup.setVisible(false);
		selectchat.setVisible(true);
	}

	public void groupchataction() {
		selectchat.setVisible(false);
		selectgroup.setVisible(true);
	}

	public void newGroup() {
		try {
			Stage temp = new Stage();
			Parent login = FXMLLoader.load(getClass().getResource("newgroup.fxml"));
			Scene loginscene = new Scene(login);
			temp.setScene(loginscene);
			temp.show();
		} catch (Exception e) {

		}
	}
}
