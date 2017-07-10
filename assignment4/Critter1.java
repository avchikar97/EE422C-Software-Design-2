/* CRITTERS Critter1.java
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

/*
 * Critter1 only attacks Critter2 (crabs) and algae. It will run away from others of itself or any other Critter.
 * In addition, it will only walk everywhere so it would be possible for Critter1 to die by simply not
 * getting algae because other Critters take it. Critter1 is based on the vegetarian sloth. Like the
 * sloth, Critter1 also has a 50% chance of not doing anything so it will move very slowly.
 */

package assignment4;

import java.util.*;

public class Critter1 extends Critter {
	@Override
	public void doTimeStep() {
		int action = getRandomInt(2);
		if (action == 0) {
			return;
		}
		else {
			walk(getRandomInt(8));
		}
	}

	@Override
	public boolean fight(String opponent) {
		if (opponent.equals("@")) {
			return true;
		}
		else{
			run(getRandomInt(8));
			return false;
		}
	}
	
	public String toString() {
		return "1";
	}
	
	public void test (List<Critter> l) {
		
	}
}
