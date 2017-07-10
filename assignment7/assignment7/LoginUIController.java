/* ChatRoom LoginUIController.java 
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

import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class LoginUIController {

	public TextField logintextfield;
	public Text usernametaken;
	// public Stage primarystage;
	private boolean canClose;
	public String name;
	private PrintWriter writer;
	
	public void displayWarning() {
		usernametaken.setVisible(true);
	}

	public void setcanClose(boolean b) {
		canClose = b;
	}

	public void setWriter(PrintWriter p) {
		writer = p;
	}

	public void onEnter(ActionEvent event) {
		canClose = false;
		name = logintextfield.getText();
		// if username is taken or "all", display usernametaken and
		logintextfield.clear();
		writer.print("##name_request##" + name);
		writer.flush();
		if (canClose) {
			closebutton();
			Parent root;
			try {
				// root = FXMLLoader.load(getClass().getResource("userinterface.fxml"));
				Stage secondaryStage = new Stage();
				FXMLLoader c = new FXMLLoader(getClass().getResource("userinterface.fxml"));
				LoginUIController chatControl = c.getController();
				chatControl.setWriter(writer);
				Scene chatScene = new Scene(c.load());
				secondaryStage.setScene(chatScene);
				secondaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return; 
	}

	public void closebutton() {
		Stage stage = (Stage) logintextfield.getScene().getWindow();
		stage.close();
	}
}
