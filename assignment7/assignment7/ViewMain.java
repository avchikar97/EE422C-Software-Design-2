/* ChatRoom ViewMain.java
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

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ViewMain extends Application implements Runnable {
	
	String[] args;
	PrintWriter writer;
	LoginUIController logController;

	ViewMain(String[] s, PrintWriter p) {
		args = s;
		writer = p;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader l = new FXMLLoader(getClass().getResource("login.fxml"));
			LoginUIController logControl = l.getController();
			logController = logControl;
			logControl.setWriter(writer);
			Scene loginscene = new Scene(l.load());
			primaryStage.setScene(loginscene);
			primaryStage.show();

			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		} catch (Exception e) {
			System.out.println("Something went wrong: ViewMain, while loading login.fxml");
			e.printStackTrace();
		}
	}
	
	public void startapplication(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		launch(args);
	}
}
