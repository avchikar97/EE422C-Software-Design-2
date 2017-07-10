/*  * EE422C Project 2 (Mastermind) submission by
 * Akaash Chikarmane
 * avc536
 * Slip days used: 
 * Spring 2017
 */

package assignment2;

import java.util.Scanner;

public class Driver {
	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		System.out.println("Welcome to Mastermind.");
		while (true) {
			System.out.println("Do you want to play a new game? (Y/N):");
			String runMode = kb.nextLine();
			if (runMode.equals("Y")) {
				boolean isTesting;
				if (args.length != 0) {
					if (args[0].equals("1")) {
						isTesting = true;
					}
					else {
						isTesting = false;
					}
				}
				else {
					isTesting = false;
				}
				Game newGame = new Game(isTesting, kb);
				newGame.runGame();
			}
			else if (runMode.equals("N")) {
				break;
			}
		}
	}
}