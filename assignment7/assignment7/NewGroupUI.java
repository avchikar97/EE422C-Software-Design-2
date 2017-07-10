/* ChatRoom NewGroupUI.java 
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

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NewGroupUI implements Initializable {
	
	public MenuButton groupmembers;
	public Button closewindow, creategroup;
	public ArrayList<String> selected;
	public TextField groupName;
	public PrintWriter writer;
	private CheckBox something;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<String> clients = Constants.allClients;
		CustomMenuItem onlineuser;
		ArrayList<CustomMenuItem> onlineusers = new ArrayList<CustomMenuItem>();
		for (int i = 0; i < clients.size(); i++) {
			something = new CheckBox(clients.get(i));
			something.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if (something.isSelected()) {
						selected.add(something.getText());
					}
					else if (selected.contains(something.getText())) {
						selected.remove(something.getText());
					}
				}
				
			});
			onlineusers.add(new CustomMenuItem(something));
		}
		groupmembers.getItems().setAll(onlineusers);
	}
	
	public void creategroup(){
		String message = "##new_group##";
		message = message + groupName.getText() + "##";
		for (String member : selected) {
			message = message + member + ",";
		}
		writer.print(message);
	}

	public void setWriter(PrintWriter p) {
		writer = p;
	}
	//handleCloseButtonAction
	public void closebutton() {
	    Stage stage = (Stage) closewindow.getScene().getWindow();
	    stage.close();
	}
}
