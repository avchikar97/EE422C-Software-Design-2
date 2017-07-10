package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Nicholas Sutanto
 * nds729
 * 16220
 * Slip days used: 0
 * Git URL: https://github.com/AkaashChikarmane/ee422c_project4.git
 * Spring 2017
 */


import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	static String[][] worlddisplay = new String[Params.world_height][Params.world_width];

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private boolean isAlive = true; // used to stop

	private boolean canMove = true; // will decide if it can move
	
	protected final void walk(int direction) {
		if (canMove) {
			switch (direction) {
				case 0: {
					x_coord = (x_coord + 1) % Params.world_width;
					break;
				}
				case 1: {
					x_coord = (x_coord + 1) % Params.world_width;
					y_coord = (y_coord + Params.world_height - 1) % Params.world_height;
					break;
				}
				case 2: {
					y_coord = (y_coord + Params.world_height - 1) % Params.world_height;
					break;
				}
				case 3: {
					x_coord = (x_coord + Params.world_width - 1) % Params.world_width;
					y_coord = (y_coord + Params.world_height - 1) % Params.world_height;
					break;
				}
				case 4: {
					x_coord = (x_coord + Params.world_width - 1) % Params.world_width;
					break;
				}
				case 5: {
					x_coord = (x_coord + Params.world_width - 1) % Params.world_width;
					y_coord = (y_coord + 1) % Params.world_height;
					break;
				}
				case 6: {
					y_coord = (y_coord + 1) % Params.world_height;
					break;
				}
				case 7: {
					x_coord = (x_coord + 1) % Params.world_width;
					y_coord = (y_coord + 1) % Params.world_height;
					break;
				}
			}
			canMove = false;
			energy = energy - Params.walk_energy_cost;
		}
	}
	
	protected final void run(int direction) {
		if (canMove) {
			switch (direction) {
				case 0: {
					x_coord = (x_coord + 2) % Params.world_width;
					break;
				}
				case 1: {
					x_coord = (x_coord + 2) % Params.world_width;
					y_coord = (y_coord + Params.world_height - 2) % Params.world_height;
					break;
				}
				case 2: {
					y_coord = (y_coord + Params.world_height - 2) % Params.world_height;
					break;
				}
				case 3: {
					x_coord = (x_coord + Params.world_width - 2) % Params.world_width;
					y_coord = (y_coord + Params.world_height - 2) % Params.world_height;
					break;
				}
				case 4: {
					x_coord = (x_coord + Params.world_width - 2) % Params.world_width;
					break;
				}
				case 5: {
					x_coord = (x_coord + Params.world_width - 2) % Params.world_width;
					y_coord = (y_coord + 2) % Params.world_height;
				}
				case 6: {
					y_coord = (y_coord + 2) % Params.world_height;
					break;
				}
				case 7: {
					x_coord = (x_coord + 2) % Params.world_width;
					y_coord = (y_coord + 2) % Params.world_height;
					break;
				}
			}
			canMove = false;
			energy = energy - Params.run_energy_cost;
		}
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
	public abstract boolean fight(String opponent);
	
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
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
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
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
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
	public static void clearWorld() {
		Critter temp;
		for (int i = 0; i < population.size(); i++) {
			temp = population.get(i);
			worlddisplay[temp.y_coord][temp.x_coord] = null;
		}
		population.clear();
	}

	public static void worldTimeStep() {
		for (Critter critStep : population) {
			critStep.doTimeStep();// carries out time step for each existing critter
			critStep.energy = critStep.energy - Params.rest_energy_cost;
			if (critStep.energy <= 0)
				critStep.isAlive = false;
		}
		// look for encounters
		for (int i = 0; i < population.size(); i++) {
			Critter critA = population.get(i);
			for (int j = 0; j < population.size(); j++) {
				if (j != i) { // don't want a "why are you hitting yourself" situation
					Critter critB = population.get(j);
					if ((critA.x_coord == critB.x_coord) && (critA.y_coord == critB.y_coord) && critB.isAlive) { // TODO: Encounters, remember to set energy and isAlive as needed
						encounter(critA, critB);
					}
				}
			}
		}
		int j = babies.size();
		for (int i = 0; i < j; i++) {
			Critter temp = babies.get(0);
			population.add(temp);
			babies.remove(0);
		}
		Iterator<Critter> cullDead = population.iterator();// cleanup crew
		while (cullDead.hasNext()) {
			if (!cullDead.next().isAlive) {
				cullDead.remove();
			}
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
	
	public static void displayWorld() {
		Critter temp;
		for(int i = 0; i < population.size(); i++){
			temp = population.get(i);
			worlddisplay[temp.y_coord][temp.x_coord] = temp.toString();
		}
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		for(int i = 0; i < Params.world_height; i++){
			System.out.print("|");
			for(int j = 0; j < Params.world_width; j++){
				if (worlddisplay[i][j] == null)
					System.out.print(" ");
				else System.out.print(worlddisplay[i][j]);
			}
			System.out.println("|");
		}
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		for (int i = 0; i < population.size(); i++) {
			temp = population.get(i);
			worlddisplay[temp.y_coord][temp.x_coord] = null;
		}
	}
}
