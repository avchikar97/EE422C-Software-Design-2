/* CRITTERS GUI Critter.java
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static java.util.Random rand = new java.util.Random();

	private int x_coord;
	private int y_coord;
	private int energy = 0;
	/*********************** Newly created variables here ***************************/
	private boolean isAlive = true; // used to stop
	private boolean canMove = true; // will decide if it can move
	private static String[][] worlddisplay = new String[Params.world_height][Params.world_width];
	private int old_x;
	private int old_y;
	private boolean functionCall = false; // true for doTimeStep, false for fight

	/************************ End of newly created variables **************************/

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	

	protected final String look(int direction, boolean steps) {
		if (functionCall)
			setWorld(false);
		else
			setWorld(true);
		String result = null;
		int temp_x = 0;
		int temp_y = 0;
		int steps_int = 0;
		if (steps)
			steps_int = 2;
		else
			steps_int = 1;
		switch (direction) {
			case 0: {
				temp_x = (x_coord + steps_int) % Params.world_width;
				break;
			}
			case 1: {
				temp_x = (x_coord + steps_int) % Params.world_width;
				temp_y = (y_coord + Params.world_height - steps_int) % Params.world_height;
				break;
			}
			case 2: {
				temp_y = (y_coord + Params.world_height - steps_int) % Params.world_height;
				break;
			}
			case 3: {
				temp_x = (x_coord + Params.world_width - steps_int) % Params.world_width;
				temp_y = (y_coord + Params.world_height - steps_int) % Params.world_height;
				break;
			}
			case 4: {
				temp_x = (x_coord + Params.world_width - steps_int) % Params.world_width;
				break;
			}
			case 5: {
				temp_x = (x_coord + Params.world_width - steps_int) % Params.world_width;
				temp_y = (y_coord + steps_int) % Params.world_height;
			}
			case 6: {
				temp_y = (y_coord + steps_int) % Params.world_height;
				break;
			}
			case 7: {
				temp_x = (x_coord + steps_int) % Params.world_width;
				temp_y = (y_coord + steps_int) % Params.world_height;
				break;
			}
		}
		energy = energy - Params.look_energy_cost;
		// result = new String(worlddisplay[temp_y][temp_x]);
		result = worlddisplay[temp_y][temp_x];
		if (functionCall)
			resetWorld(false);
		else
			resetWorld(true);
		return result;
	}
	
	/* rest is unchanged from Project 4 */
	
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	protected int getEnergy() { return energy; }
	
	protected final void walk(int direction) {
		int temp_x = 0;
		int temp_y = 0;
		boolean move = true;
		if (canMove) {
			switch (direction) {
				case 0: {
					temp_x = (x_coord + 1) % Params.world_width;
					break;
				}
				case 1: {
					temp_x = (x_coord + 1) % Params.world_width;
					temp_y = (y_coord + Params.world_height - 1) % Params.world_height;
					break;
				}
				case 2: {
					temp_y = (y_coord + Params.world_height - 1) % Params.world_height;
					break;
				}
				case 3: {
					temp_x = (x_coord + Params.world_width - 1) % Params.world_width;
					temp_y = (y_coord + Params.world_height - 1) % Params.world_height;
					break;
				}
				case 4: {
					temp_x = (x_coord + Params.world_width - 1) % Params.world_width;
					break;
				}
				case 5: {
					temp_x = (x_coord + Params.world_width - 1) % Params.world_width;
					temp_y = (y_coord + 1) % Params.world_height;
				}
				case 6: {
					temp_y = (y_coord + 1) % Params.world_height;
					break;
				}
				case 7: {
					temp_x = (x_coord + 1) % Params.world_width;
					temp_y = (y_coord + 1) % Params.world_height;
					break;
				}
			}
			if (!functionCall) {
				setWorld(true);
				if (worlddisplay[temp_y][temp_x] == null) { // only need to check this if called from fight
					move = true;
				}
				else
					move = false;
				resetWorld(true);
			}
			if (move) {
				x_coord = temp_x;
				y_coord = temp_y;
			}
			canMove = false;
		}
		energy = energy - Params.walk_energy_cost;
	}
	
	protected final void run(int direction) {
		int temp_x = 0;
		int temp_y = 0;
		boolean move = true;
		if (canMove) {
			switch (direction) {
				case 0: {
					temp_x = (x_coord + 2) % Params.world_width;
					break;
				}
				case 1: {
					temp_x = (x_coord + 2) % Params.world_width;
					temp_y = (y_coord + Params.world_height - 2) % Params.world_height;
					break;
				}
				case 2: {
					temp_y = (y_coord + Params.world_height - 2) % Params.world_height;
					break;
				}
				case 3: {
					temp_x = (x_coord + Params.world_width - 2) % Params.world_width;
					temp_y = (y_coord + Params.world_height - 2) % Params.world_height;
					break;
				}
				case 4: {
					temp_x = (x_coord + Params.world_width - 2) % Params.world_width;
					break;
				}
				case 5: {
					temp_x = (x_coord + Params.world_width - 2) % Params.world_width;
					temp_y = (y_coord + 2) % Params.world_height;
				}
				case 6: {
					temp_y = (y_coord + 2) % Params.world_height;
					break;
				}
				case 7: {
					temp_x = (x_coord + 2) % Params.world_width;
					temp_y = (y_coord + 2) % Params.world_height;
					break;
				}
			}
			if (!functionCall) {
				setWorld(true);
				if (worlddisplay[temp_y][temp_x] == null) { // only need to check this if called from fight
					move = true;
				}
				else
					move = false;
				resetWorld(true);
			}
			if (move) {
				x_coord = temp_x;
				y_coord = temp_y;
			}
			canMove = false;
		}
		energy = energy - Params.run_energy_cost;
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if (this.energy < Params.min_reproduce_energy)
			return;
		else {
			if (this.energy % 2 == 1) {
				offspring.energy = this.energy / 2;
				this.energy = (this.energy / 2) + 1;
			}
			else {
				offspring.energy = this.energy / 2;
				this.energy = this.energy / 2;
			}
			offspring.x_coord = this.x_coord;
			offspring.y_coord = this.y_coord;
			offspring.walk(direction);
			offspring.energy = offspring.energy + Params.walk_energy_cost;
			babies.add(offspring);
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);

	/**
	 * @param time
	 *            false for old and true for new
	 *
	 */
	private static void setWorld(boolean time) {
		Critter temp;
		if (time) {
			for (int i = 0; i < population.size(); i++) {
				temp = population.get(i);
				worlddisplay[temp.y_coord][temp.x_coord] = temp.toString();
			}
		}
		else {
			for (int i = 0; i < population.size(); i++) {
				temp = population.get(i);
				worlddisplay[temp.old_y][temp.old_x] = temp.toString();
			}
		}
	}

	private static void resetWorld(boolean time) {
		Critter temp;
		if (time) {
			for (int i = 0; i < population.size(); i++) {
				temp = population.get(i);
				worlddisplay[temp.y_coord][temp.x_coord] = null;
			}
		}
		else {
			for (int i = 0; i < population.size(); i++) {
				temp = population.get(i);
				worlddisplay[temp.old_y][temp.old_x] = null;
			}
		}
	}

	public static void encounter(Critter a, Critter b) {
		boolean AfightB = a.fight(b.toString());
		boolean BfightA = b.fight(a.toString());
		if ((a.x_coord == b.x_coord) && (a.y_coord == b.y_coord)) { // the secret Joestar Family technique
			int aRoll = 0;
			int bRoll = 0;
			if (AfightB) {
				aRoll = getRandomInt(20);
			}
			else
				aRoll = 0;
			if (BfightA) {
				bRoll = getRandomInt(20);
			}
			else
				bRoll = 0;
			if (aRoll >= bRoll) {
				a.energy = a.energy + (b.energy / 2);
				b.energy = 0;
				b.isAlive = false;
			}
			else {
				b.energy = b.energy + (a.energy / 2);
				a.energy = 0;
				a.isAlive = false;
			}
			return;
		}
		else {
			if (a.getEnergy() <= 0)
				a.isAlive = false;
			if (b.getEnergy() <= 0)
				b.isAlive = false;
			return;
		}
	}
	
	public static void worldTimeStep() {
		Critter critStep;
		int bound_step = population.size();
		for (int i = 0; i < bound_step; i++) {
			critStep = population.get(i);
			critStep.functionCall = true;
			critStep.old_x = critStep.x_coord;
			critStep.old_y = critStep.y_coord;
			critStep.doTimeStep();// carries out time step for each existing critter
			critStep.functionCall = false;
			critStep.energy = critStep.energy - Params.rest_energy_cost;
			if (critStep.energy <= 0)
				critStep.isAlive = false;
			if (!critStep.isAlive) {
				population.remove(i);
				i--;
				bound_step--;
			}
		}
		removeDead();
		// look for encounters
		int bound_fight = population.size();
		for (int i = 0; i < bound_fight; i++) {
			Critter critA = population.get(i);
			for (int j = 0; j < bound_fight; j++) {
				if (j != i) { // don't want a "why are you hitting yourself" situation
					Critter critB = population.get(j);
					if ((critA.x_coord == critB.x_coord) && (critA.y_coord == critB.y_coord) && critB.isAlive) {
						encounter(critA, critB);
						if (!critB.isAlive) {
							population.remove(j);
							j--;
							bound_fight--;
						}
						if (!critA.isAlive) {
							population.remove(i);
							i--;
							bound_fight--;
							break;
						}
					}
				}
			}
		}
		removeDead();
		int j = babies.size();
		for (int i = 0; i < j; i++) {
			Critter temp = babies.get(0);
			population.add(temp);
			babies.remove(0);
		}
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			try {
				makeCritter("Algae");
			} catch (InvalidCritterException e) {

			}
		}
		for (Critter setMove : population) {
			setMove.canMove = true;
		}
	}
	
	private static void removeDead() {
		Iterator<Critter> cullDead = population.iterator();// cleanup crew
		while (cullDead.hasNext()) {
			if (!cullDead.next().isAlive) {
				cullDead.remove();
			}
		}
	}

	public static void displayWorld(Object pane) {
		GridPane world = (GridPane) pane;
		world.getChildren().clear();
		Critter temp;
		Rectangle blankspace;
		final Double borderwidth = 1.0;
		Double cellHeight = ((600.0 - 2*Params.world_height) / Params.world_height);
		Double cellWidth = ((600.0 - 2*Params.world_width) / Params.world_width);
		for(int i = 0; i < Params.world_height; i++){
			for(int j = 0; j < Params.world_width; j++){
				blankspace = new Rectangle();				
				blankspace.setWidth(cellWidth + 2*borderwidth);
				blankspace.setHeight(cellHeight + 2*borderwidth);
				blankspace.setFill(javafx.scene.paint.Color.TRANSPARENT);
				blankspace.setStroke(javafx.scene.paint.Color.BLACK);
				world.add(blankspace, j, i);
			}
		}
		// Print Critter Shapes
		for (int i = 0; i < population.size(); i++) {
			temp = population.get(i);
			// Circle
			if (temp.viewShape() == CritterShape.CIRCLE) {
				Circle c = new Circle();
				Double heightRadius = cellHeight / 2 - borderwidth;
				Double widthRadius = cellWidth / 2 - borderwidth;
				if (heightRadius > widthRadius) {
					c = new Circle(widthRadius, temp.viewFillColor());
				}
				else {
					c = new Circle(heightRadius, temp.viewFillColor());
				}
				c.setStroke(temp.viewOutlineColor());
				world.add(c, temp.x_coord, temp.y_coord);
				GridPane.setHalignment(c, HPos.CENTER);
				GridPane.setValignment(c, VPos.CENTER);
			}
			// Diamond
			else if (temp.viewShape() == CritterShape.DIAMOND) {
				Polygon diamond = new Polygon();
				diamond.getPoints().addAll(
					cellWidth/2 - borderwidth, 0.0,
					0.0, cellHeight/2 - borderwidth,
					cellWidth/2 - borderwidth, cellHeight - 2*borderwidth,
					cellWidth - 2*borderwidth, cellHeight/2  - borderwidth
				);
				diamond.setFill(temp.viewFillColor());
				diamond.setStroke(temp.viewOutlineColor());
				world.add(diamond, temp.x_coord, temp.y_coord);
				GridPane.setHalignment(diamond, HPos.CENTER);
				GridPane.setValignment(diamond, VPos.CENTER);
			}
			// Square
			else if (temp.viewShape() == CritterShape.SQUARE) {
				Rectangle square = new Rectangle();
				if (cellHeight > cellWidth) {
					square = new Rectangle(cellWidth - 2*borderwidth, cellWidth - 2*borderwidth, temp.viewFillColor());
				}
				else {
					square = new Rectangle(cellHeight - 2*borderwidth, cellHeight - 2*borderwidth, temp.viewFillColor());
				}
				square.setStroke(temp.viewOutlineColor());
				world.add(square, temp.x_coord, temp.y_coord);
				GridPane.setHalignment(square, HPos.CENTER);
				GridPane.setValignment(square, VPos.CENTER);
			}
			else if (temp.viewShape() == CritterShape.STAR) {
				Polygon star = new Polygon();
				star.getPoints().addAll(
					cellWidth/2.0 - borderwidth, 0.0,	//1
					(cellWidth - borderwidth)*(2.0/5.0), cellHeight*(2.0/5.0) - borderwidth,	//2
					0.0, (cellHeight*(2.0/5.0)) - borderwidth,	//3
					cellWidth/3.0 - borderwidth, cellHeight*(3.0/5.0) - borderwidth,	//4
					(cellWidth/5.0) - borderwidth, cellHeight - 2*borderwidth,	//5
					cellWidth/2.0 - borderwidth, cellHeight*(3.0/4.0) - borderwidth,	//6
					cellWidth*(4.0/5.0) - borderwidth, cellHeight - 2*borderwidth,	//7
					cellWidth*(2.0/3.0) - borderwidth, cellHeight*(3.0/5.0) - borderwidth,	//8
					cellWidth - 2*borderwidth, cellHeight*(2.0/5.0) - borderwidth,	//9
					cellWidth*(3.0/5.0) - borderwidth, cellHeight*(2.0/5.0) - borderwidth	//10
				);
				star.setFill(temp.viewFillColor());
				star.setStroke(temp.viewOutlineColor());
				world.add(star, temp.x_coord, temp.y_coord);
				GridPane.setHalignment(star, HPos.CENTER);
				GridPane.setValignment(star, VPos.CENTER);
			}
			else if (temp.viewShape() == CritterShape.TRIANGLE) {
				Polygon triangle = new Polygon();
				triangle.getPoints().addAll(
					cellWidth/2 - borderwidth, 0.0,
					0.0, cellHeight - 2*borderwidth,
					cellWidth - 2*borderwidth, cellHeight - 2*borderwidth
				);
				triangle.setFill(temp.viewFillColor());
				triangle.setStroke(temp.viewOutlineColor());
				world.add(triangle, temp.x_coord, temp.y_coord);
				GridPane.setHalignment(triangle, HPos.CENTER);
				GridPane.setValignment(triangle, VPos.CENTER);
			}
		}
	} 
	/* Alternate displayWorld, where you use Main.<pane> to reach into your
	   display component.
	   // public static void displayWorld() {}
	*/
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		Class<?> temp = null;
		Constructor<?> critterConstructor = null;
		// Object instanceOfCritter = null;
		Critter newCritter = null;
		try {
			try {
				temp = Class.forName(myPackage + "." + critter_class_name);
			} catch (ClassNotFoundException e) {
				throw new InvalidCritterException(critter_class_name);
			}
			// critterConstructor = temp.getConstructor(String.class);
			critterConstructor = temp.getConstructor();
			try {
				newCritter = (Critter) critterConstructor.newInstance();
			} catch (InstantiationException e) {
				throw new InvalidCritterException(critter_class_name);
			}
			population.add(newCritter);
			newCritter.x_coord = getRandomInt(Params.world_width);
			newCritter.y_coord = getRandomInt(Params.world_height);
			newCritter.energy = Params.start_energy;
		} catch (Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * 
	 * @param critter_class_name
	 *            What kind of Critter is to be listed. Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class<?> input = null;
		Constructor<?> critterConstructor = null;
		// Object instanceOfCritter = null;
		Critter newCritter = null;
		Critter temp = null;
		try {
			input = Class.forName(myPackage + "." + critter_class_name);
			critterConstructor = input.getConstructor();
			newCritter = (Critter) critterConstructor.newInstance();
		} catch (Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
		for (int i = 0; i < population.size(); i++) {
			temp = population.get(i);
			if (temp.toString().equals(newCritter.toString())) {
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * 
	 * @param critters
	 *            List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		String out = null;
		out = "" + critters.size() + " critters as follows -- ";
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string, 1);
			}
			else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			out = out + prefix + s + ":" + critter_count.get(s);
			prefix = ", ";
		}
		out = out + "\n";
		return out;
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		Critter temp;
		for (int i = 0; i < population.size(); i++) {
			temp = population.get(i);
			worlddisplay[temp.y_coord][temp.x_coord] = null;
		}
		population.clear();
	}
	
	
}
