/* CRITTERS GUI Main.java
 * EE422C Project 5 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Nicholas Sutanto
 * nds729
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */

package assignment5;
	
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Main extends Application {
	private static String myPackage; // package of Critter file. Critter cannot be in default pkg.
	// Gets the package name. The usage assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	static Pane worldbackground = new Pane();
	static GridPane world = new GridPane();
	static Timeline animation = new Timeline();
	static int skip = 0;
	static int track = 0;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Critter Controller");
		GridPane grid = new GridPane();
		FlowPane statsPane = new FlowPane();
		Stage statsStage = new Stage();
		statsStage.setTitle("Statistics Window");

		// Display World Grid
		Stage worldstage = new Stage();
		Scene worldscene = new Scene(worldbackground, 650, 650);
		ColumnConstraints limitc = new ColumnConstraints((600.0 / Params.world_width));
		RowConstraints limitr = new RowConstraints((600.0 / Params.world_height));
		for (int i = 0; i < Params.world_height; i++) {
			world.getRowConstraints().add(limitr);
		}
		for (int j = 0; j < Params.world_width; j++) {
			world.getColumnConstraints().add(limitc);
		}
		world.setAlignment(Pos.CENTER);
		worldbackground.getChildren().add(world);
		Critter.displayWorld(world);
		Rectangle blankspace;
		for (int i = 0; i < Params.world_height; i++) {
			for (int j = 0; j < Params.world_width; j++) {
				blankspace = new Rectangle();
				blankspace.setWidth(600.0 / Params.world_height);
				blankspace.setHeight(600.0 / Params.world_width);
				blankspace.setFill(javafx.scene.paint.Color.TRANSPARENT);
				world.add(blankspace, j, i);
			}
		}
		world.setMaxSize(10, 10);
		statsStage.setScene(new Scene(statsPane, 600, 400));
		statsStage.show();
		worldstage.setTitle("Critter World!");
		worldstage.setScene(worldscene);
		worldstage.show();

		//add general buttons (commands)
		makeButtons(grid, statsPane);
		primaryStage.setScene(new Scene(grid, 440, 500));
		primaryStage.show();

	}

	private static ChoiceBox<String> getCritterBox() {
		String temp = new String();
		Class<?> crittercheck;
		ArrayList<String> filenames = new ArrayList<String>();

		File folder2 = new File(System.getProperty("user.dir") + "\\" + myPackage);
		File[] filelist2 = folder2.listFiles();
		if (filelist2 != null) {
			for (int i = 0; i < filelist2.length; i++) {
				temp = filelist2[i].getName();
				if (temp.contains(".java")) {
					temp = temp.substring(0, temp.indexOf(".java"));
					try {
						crittercheck = Class.forName(myPackage + "." + temp);
					} catch (ClassNotFoundException e) {
						continue;
					}
					if (Critter.class.isAssignableFrom(crittercheck) && !filenames.contains(temp)) {
						filenames.add(temp);
					}
				}
			}
		}

		if (filenames.size() == 0) {
			File folder1 = new File("./src/" + myPackage);
			File[] filelist1 = folder1.listFiles();

			for (int i = 0; i < filelist1.length; i++) {
				temp = filelist1[i].getName();
				if (temp.contains(".java")) {
					temp = temp.substring(0, temp.indexOf(".java"));
					try {
						crittercheck = Class.forName(myPackage + "." + temp);
					} catch (ClassNotFoundException e) {
						continue;
					}
					if (Critter.class.isAssignableFrom(crittercheck)) {
						filenames.add(temp);
					}
				}
			}
		}

		filenames.remove("Critter");
		ChoiceBox<String> cb = new ChoiceBox<String>(FXCollections.observableArrayList(filenames));
		return cb;
	}

	private static ArrayList<String> getCritterList() {
		String temp;
		Class<?> crittercheck;
		ArrayList<String> filenames = new ArrayList<String>();

		File folder2 = new File(System.getProperty("user.dir") + "\\" + myPackage);
		File[] filelist2 = folder2.listFiles();

		if (filelist2 != null) {
			for (int i = 0; i < filelist2.length; i++) {
				temp = filelist2[i].getName();
				if (temp.contains(".java")) {
					temp = temp.substring(0, temp.indexOf(".java"));
					try {
						crittercheck = Class.forName(myPackage + "." + temp);
					} catch (ClassNotFoundException e) {
						continue;
					}
					if (Critter.class.isAssignableFrom(crittercheck) && !filenames.contains(temp)) {
						filenames.add(temp);
					}
				}
			}
		}

		if (filenames.size() == 0) {
			File folder1 = new File("./src/" + myPackage);
			File[] filelist1 = folder1.listFiles();

			for (int i = 0; i < filelist1.length; i++) {
				temp = filelist1[i].getName();
				if (temp.contains(".java")) {
					temp = temp.substring(0, temp.indexOf(".java"));
					try {
						crittercheck = Class.forName(myPackage + "." + temp);
					} catch (ClassNotFoundException e) {
						continue;
					}
					if (Critter.class.isAssignableFrom(crittercheck)) {
						filenames.add(temp);
					}
				}
			}
		}

		filenames.remove("Critter");
		return filenames;
	}

	private static String statsWrapper(String input) {
		String[] userinputs = null;
		userinputs = input.split(" ");
		String result = null;
		String statstemp = userinputs[1];
		Class<?> inputclass = null;
		List<Critter> existingCritters = null;
		try {
			inputclass = Class.forName(myPackage + "." + statstemp); // inputclass is the critter whose stats we are looking for
			existingCritters = Critter.getInstances(statstemp);// existingCritters is the List of all critter whose stats we're looking for
			Method stats = inputclass.getMethod("runStats", List.class);
			result = (String) stats.invoke(inputclass, existingCritters);
		} catch (Exception e) {

		}
		return result;
	}

	private static void processInput(String input) {
		String[] userinputs = null;
		userinputs = null;
		userinputs = input.split(" ");
		if (userinputs[0].equals("step") || userinputs[0].equals("seed") || userinputs[0].equals("make")
				|| userinputs[0].equals("stats")) {
			switch (userinputs.length) {
				case 1: {
					if (userinputs[0].equals("step")) {
						Critter.worldTimeStep();
						break;
					}
					else {

					}
					break;
				}
				case 2: {
					if (userinputs[0].equals("step")) {
						String steptemp = userinputs[1];
						try {
							int numSteps = Integer.parseInt(steptemp);
							if (numSteps <= 0) {
								throw new Exception();
							}
							for (int i = 0; i < numSteps; i++) {
								Critter.worldTimeStep();
							}
						} catch (Exception e) {

						}
						break;
					}
					else if (userinputs[0].equals("seed")) {
						String seedtemp = userinputs[1];
						try {
							int seedNum = Integer.parseInt(seedtemp);
							if (seedNum <= 0) {
								throw new Exception();
							}
							// is the input always gonna be an integer? Integer.parseInt();
							Critter.setSeed(seedNum); // parseInt throws NumberFormatException if not number
						} catch (Exception e) {

						}
						break;
					}
					else if (userinputs[0].equals("make")) {
						String classtemp = userinputs[1];
						try {
							Critter.makeCritter(classtemp);
						} catch (Exception e) {

						}
						break;
					}
					else if (userinputs[0].equals("stats")) {
						String statstemp = userinputs[1];
						Class<?> inputclass = null;
						List<Critter> existingCritters = null;
						try {
							inputclass = Class.forName(myPackage + "." + statstemp); // inputclass is the critter whose stats we are looking for
							existingCritters = Critter.getInstances(statstemp);// existingCritters is the List of all critter whose stats we're looking for
							Method stats = inputclass.getMethod("runStats", List.class);
							stats.invoke(inputclass, existingCritters);
						} catch (Exception e) {

						}
						break;
					}
					else {

					}
					break;
				}
				case 3: {
					if (userinputs[0].equals("make")) {
						String makeclass = userinputs[1];
						String makecount = userinputs[2];
						try {
							int makeNum = Integer.parseInt(makecount);
							if (makeNum <= 0) {
								throw new Exception();
							}
							for (int i = 0; i < makeNum; i++) {
								try {
									Critter.makeCritter(makeclass);
								} catch (InvalidCritterException e) {

								}
							}
						} catch (Exception e) {

						}
					}
					else {

					}
					break;
				}
				default: {

				}
			}
		}
		else {

		}
		System.out.flush();
	}

	private static void makeButtons(GridPane buttonGrid, FlowPane f) {
		Label title = new Label();
		title.setText("Critters");
		title.setFont(Font.font("System", 25));
		title.setAlignment(Pos.CENTER);

		Button show = new Button(); // Display world button
		show.setText("Show world");
		show.setLayoutX(Params.world_width / 2);
		show.setLayoutY(world.getHeight() + 40);
		show.setPadding(new Insets(5));
		show.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Critter.displayWorld(world);
			}
		});

		// add step variable selector
		TextField stepInput = new TextField();
		stepInput.setText("1");
		stepInput.setEditable(true);
		stepInput.setPadding(new Insets(5));

		Button step = new Button();// Step button
		step.setText("Step");
		step.setPadding(new Insets(5));
		step.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String steps = stepInput.getText();
				String input = "step " + steps;
				processInput(input);
				Critter.displayWorld(world);
			}
		});

		// add seed variable selector
		TextField seedInput = new TextField();
		seedInput.setText("1");
		seedInput.setEditable(true);
		seedInput.setPadding(new Insets(5));

		Button seed = new Button(); // Set seed button
		seed.setText("Set seed");
		seed.setPadding(new Insets(5));
		seed.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String seedNum = seedInput.getText();
				String input = "seed " + seedNum;
				processInput(input);
			}
		});

		// add number of Critter to make
		TextField makeNum = new TextField();
		makeNum.setText("1");
		makeNum.setEditable(true);
		makeNum.setPadding(new Insets(5));

		ChoiceBox<String> critterchoice = getCritterBox();
		critterchoice.getSelectionModel().select(0);

		Button make = new Button();// Make new critter button
		make.setText("Make new: ");
		make.setPadding(new Insets(5));
		make.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String totalMake = makeNum.getText();
				String choiceCritter = critterchoice.getValue();
				String input = "make " + choiceCritter + " " + totalMake;
				processInput(input);
			}
		});
		
		ArrayList<String> listOfCritters = getCritterList();
		ArrayList<CheckBox> statsCheck = new ArrayList<CheckBox>();
		VBox statsCheckBoxes = new VBox(listOfCritters.size());
		statsCheckBoxes.setVisible(false);
		statsCheckBoxes.setPadding(new Insets(5));
		for(int i = 0; i<listOfCritters.size(); i++){
			CheckBox temp = new CheckBox(listOfCritters.get(i));
			statsCheck.add(temp);
			statsCheckBoxes.getChildren().add(temp);
		}

		Button showChoices = new Button(); // Show critters drop down for stats
		showChoices.setText("Show/hide stats choices");
		showChoices.setPadding(new Insets(5));
		showChoices.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				statsCheckBoxes.setVisible(!statsCheckBoxes.isVisible());
			}
		});

		TextArea statsPrint = new TextArea();
		statsPrint.setEditable(false);
		statsPrint.setWrapText(true);
		statsPrint.setPrefSize(600, 400);
		f.getChildren().add(statsPrint);

		Button stats = new Button(); // Show stats button
		stats.setText("Show stats of:");
		stats.setPadding(new Insets(5));
		stats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String choiceCritter = critterchoice.getValue();
				String input = "make " + choiceCritter;
				String statsReturn = null;
				statsPrint.setText("");
				for (int i = 0; i < statsCheck.size(); i++) {
					CheckBox temp = statsCheck.get(i);
					if (temp.isSelected()) {
						choiceCritter = temp.getText();
						input = "stats " + choiceCritter;
						statsReturn = choiceCritter + "\n" + statsWrapper(input);
						statsPrint.setText(statsPrint.getText() + "\n" + statsReturn);
					}
				}
			}
		});
		
		Label emptyLabel = new Label("");
		emptyLabel.setFont(new Font(16));
		
		Label aniLabel = new Label("Animation:");
		aniLabel.setFont(new Font(16));
		aniLabel.setPadding(new Insets(5));

		Slider animationSpeed = new Slider(1, 200, 1);
		animationSpeed.setPadding(new Insets(5));
		Label aniSpeedLabel = new Label("1 (steps/second)");
		aniSpeedLabel.setPadding(new Insets(5));
		animationSpeed.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
				aniSpeedLabel.textProperty()
						.setValue((String.valueOf((int) animationSpeed.getValue())) + " (steps/second)");

			}
		});

		Button startAnimation = new Button();
		startAnimation.setText("Start");
		startAnimation.setPadding(new Insets(5));
		startAnimation.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				double refreshRate = animationSpeed.getValue();
				refreshRate = 1 / refreshRate;
				// disable all other buttons
				show.setDisable(!show.isDisabled());
				step.setDisable(!step.isDisabled());
				stepInput.setDisable(!stepInput.isDisabled());
				seed.setDisable(!seed.isDisabled());
				seedInput.setDisable(!seedInput.isDisabled());
				make.setDisable(!make.isDisabled());
				critterchoice.setDisable(!critterchoice.isDisabled());
				makeNum.setDisable(!makeNum.isDisabled());
				stats.setDisable(!stats.isDisabled());
				showChoices.setDisable(!showChoices.isDisabled());
				statsCheckBoxes.setDisable(!statsCheckBoxes.isDisabled());
				animationSpeed.setDisable(!animationSpeed.isDisabled());

				if ((1 / refreshRate) > 20) {
					skip =((int) (1/refreshRate))/4;
					track = 0;
				}
				else {
					skip = 0;
					track = 0;
				}

				animation = new Timeline(new KeyFrame(Duration.seconds(refreshRate), new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						String doSteps = "step 1";
						processInput(doSteps);
						if (skip != 0) { // slow it down so it doesn't lag (only shows every fourth frame)
							track++;
							if (track == skip) {
								track = 0;
								// display world
								Critter.displayWorld(world);
								// display runStats
								String choiceCritter = critterchoice.getValue();
								String input = "make " + choiceCritter;
								String statsReturn = null;
								statsPrint.setText("");
								for (int i = 0; i < statsCheck.size(); i++) {
									CheckBox temp = statsCheck.get(i);
									if (temp.isSelected()) {
										choiceCritter = temp.getText();
										input = "stats " + choiceCritter;
										statsReturn = choiceCritter + "\n" + statsWrapper(input);
										statsPrint.setText(statsPrint.getText() + "\n" + statsReturn);
									}
								}
							}
						}
						else{
							// display world
							Critter.displayWorld(world);
							// display runStats
							String choiceCritter = critterchoice.getValue();
							String input = "make " + choiceCritter;
							String statsReturn = null;
							statsPrint.setText("");
							for (int i = 0; i < statsCheck.size(); i++) {
								CheckBox temp = statsCheck.get(i);
								if (temp.isSelected()) {
									choiceCritter = temp.getText();
									input = "stats " + choiceCritter;
									statsReturn = choiceCritter + "\n" + statsWrapper(input);
									statsPrint.setText(statsPrint.getText() + "\n" + statsReturn);
								}
							}
						}
					}
				}));
				animation.setCycleCount(Timeline.INDEFINITE);
				animation.play();
			}
		});

		Button stopAnimation = new Button();
		stopAnimation.setText("Stop");
		stopAnimation.setPadding(new Insets(5));
		stopAnimation.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// disable all other buttons
				show.setDisable(!show.isDisabled());
				step.setDisable(!step.isDisabled());
				stepInput.setDisable(!stepInput.isDisabled());
				seed.setDisable(!seed.isDisabled());
				seedInput.setDisable(!seedInput.isDisabled());
				make.setDisable(!make.isDisabled());
				critterchoice.setDisable(!critterchoice.isDisabled());
				makeNum.setDisable(!makeNum.isDisabled());
				stats.setDisable(!stats.isDisabled());
				showChoices.setDisable(!showChoices.isDisabled());
				statsCheckBoxes.setDisable(!statsCheckBoxes.isDisabled());
				animationSpeed.setDisable(!animationSpeed.isDisabled());

				animation.stop();
				skip = 0;
				track = 0;
			}
		});

		Button quit = new Button(); // Quit game button
		quit.setText("Quit game");
		quit.setPadding(new Insets(5));
		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

		title.setMaxWidth(Double.MAX_VALUE);
		make.setMaxWidth(Double.MAX_VALUE);
		critterchoice.setMaxWidth(Double.MAX_VALUE);
		makeNum.setMaxWidth(Double.MAX_VALUE);
		stats.setMaxWidth(Double.MAX_VALUE);
		showChoices.setMaxWidth(Double.MAX_VALUE);
		statsCheckBoxes.setMaxWidth(Double.MAX_VALUE);
		step.setMaxWidth(Double.MAX_VALUE);
		stepInput.setMaxWidth(Double.MAX_VALUE);
		show.setMaxWidth(Double.MAX_VALUE);
		startAnimation.setMaxWidth(Double.MAX_VALUE);
		startAnimation.setPrefWidth(70);
		stopAnimation.setMaxWidth(Double.MAX_VALUE);
		animationSpeed.setMaxWidth(Double.MAX_VALUE);
		seed.setMaxWidth(Double.MAX_VALUE);
		seedInput.setMaxWidth(Double.MAX_VALUE);
		quit.setMaxWidth(Double.MAX_VALUE);

		buttonGrid.add(title, 2, 0, 2, 1);

		buttonGrid.add(make, 0, 1, 2, 1);
		buttonGrid.add(critterchoice, 2, 1, 2, 1);
		buttonGrid.add(makeNum, 4, 1, 1, 1);

		buttonGrid.add(stats, 0, 2, 2, 1);
		buttonGrid.add(showChoices, 2, 2, 2, 1);
		buttonGrid.add(statsCheckBoxes, 2, 3, 2, 1);

		buttonGrid.add(step, 1, listOfCritters.size() + 3, 1, 1);
		buttonGrid.add(stepInput, 2, listOfCritters.size() + 3, 1, 1);

		buttonGrid.add(show, 1, listOfCritters.size() + 4, 1, 1);

		buttonGrid.add(emptyLabel, 0, listOfCritters.size() + 5);

		buttonGrid.add(aniLabel, 0, listOfCritters.size() + 6, 2, 1);
		buttonGrid.add(startAnimation, 0, listOfCritters.size() + 7, 1, 1);
		buttonGrid.add(stopAnimation, 1, listOfCritters.size() + 7, 1, 1);
		buttonGrid.add(animationSpeed, 0, listOfCritters.size() + 8, 2, 1);
		buttonGrid.add(aniSpeedLabel, 2, listOfCritters.size() + 8, 1, 1);

		buttonGrid.add(seed, 0, listOfCritters.size() + 9, 2, 1);
		buttonGrid.add(seedInput, 2, listOfCritters.size() + 9, 1, 1);

		buttonGrid.add(quit, 3, listOfCritters.size() + 10, 2, 1);

		ObservableList<Node> a = buttonGrid.getChildren();
		for (Node temp : a) {
			buttonGrid.setFillWidth(temp, true);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
