/* CRITTERS Critter2.java
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
 * Critter2 is meant to be like a crab. It will only run right or left depending on the random integer
 * and it will fight every other Critter it comes across, regardless of its chance of winning. Just like a
 * real crab, it will also reproduce very rarely.
 */
package assignment5;

import assignment5.Critter.CritterShape;
import javafx.scene.paint.Color;

public class Critter2 extends Critter {
	
	@Override
	public void doTimeStep() {
		int decide = getRandomInt(20);
		if (decide >= 17) {
			Critter2 baby = new Critter2();
			reproduce(baby, 2);
		}
		else if (decide % 2 == 1) {
			run(0);
		}
		else {
			run(4);
		}
	}

	@Override
	public boolean fight(String opponent) {
		return true;
	}

	@Override
	public String toString () {
		return "2";
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return Color.CORNFLOWERBLUE;
	}

	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return Color.CRIMSON;
	}
}
