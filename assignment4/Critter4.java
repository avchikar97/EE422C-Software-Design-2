/* CRITTERS Critter4.java
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
 * 
 * Critter4 is different from the other Critters because it only runs in diagonal directions (e.g. directions
 * 1, 3, 5, 7). It is also different because it fights with all kinds of Critters except other Critter4, for
 * which it walks away from the encounter.
 */

package assignment4;

public class Critter4 extends Critter {
	
	@Override
	public void doTimeStep() {
		int steps = Critter.getRandomInt(8);
		switch(steps){
		case 0: run(steps + 1);
		case 2: run(steps + 1);
		case 4: run(steps + 1);
		case 6: run(steps + 1);
		default: run(steps);
		}
		
		if(getEnergy() > 150){
			Critter4 hatchling = new Critter4();
			reproduce(hatchling, Critter.getRandomInt(8));
		}
	}

	@Override
	public boolean fight(String opponent) {
		if(opponent.equals("4")){
			walk(Critter.getRandomInt(8));
			return false;
		}
		else return true;
	}

	@Override
	public String toString () {
		return "4";
	}
}
