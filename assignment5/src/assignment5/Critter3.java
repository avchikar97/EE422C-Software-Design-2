/* CRITTERS Critter3.java
 * EE422C Project 5 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Nicholas Sutanto
 * nds729
 * 16220
 * Slip days used: 0
 * Git URL: https://github.com/AkaashChikarmane/ee422c_project4.git
 * Spring 2017
 * 
 * Critter3 is different from the other critters because it can both walk or run in any direction, and reproduces when
 * energy > 90. It is also different from the other critters because it loses fights with Craig and Critter1, walks
 * away from encounters with other Critter3, but fights with all other kinds of Critters. 
 */

package assignment5;

public class Critter3 extends Critter {
	
	@Override
	public String toString () { return "3";	}
	
	@Override
	public void doTimeStep() {
		int steps = Critter.getRandomInt(8);
		int speed = Critter.getRandomInt(3);
		if(speed < 2) walk(steps);
		else run(steps);
		
		if(getEnergy() > 90){
			Critter3 child = new Critter3();
			reproduce(child, Critter.getRandomInt(8));
		}
	}

	@Override
	public boolean fight(String opponent) {
		if(opponent.equals("C") || opponent.equals("1")) return false;
		else if(opponent.equals("3")){
			int temp = Critter.getRandomInt(8);
			if(look(temp, false).equals(null)){
				walk(Critter.getRandomInt(8));
			}
			return false;
		}
		else return true;
	}

	@Override
	public CritterShape viewShape() { return CritterShape.TRIANGLE; }
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.CORAL; }
	@Override
	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.YELLOW; }
}
