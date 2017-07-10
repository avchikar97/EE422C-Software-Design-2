/*  * EE422C Project 2 (Mastermind) submission by
 * Akaash Chikarmane
 * avc536
 * Slip days used: 
 * Spring 2017
 */

package assignment2;

import java.util.Scanner;

public class Game {
	boolean gameMode; // true if in testing mode
	Scanner keyboard;
	String code;
	HumanPlayer challenger;
	Checker check;

	public Game(boolean mode, Scanner k) {
		this.gameMode = mode;
		this.keyboard = k;
		this.code = SecretCodeGenerator.getInstance().getNewSecretCode();
		this.challenger = new HumanPlayer(this.keyboard);
		this.check = new Checker();
	}

	public void runGame() {
		boolean isGuess = true; // false if player requested history
		boolean isValid = false; // true if length and string characters are legitimate
		boolean isCorrect = false; // true if player guess matches code
		String withBWPegs = new String(); // used to store guess with feedback appended
		if (this.gameMode) { // checks
			System.out.println("Secret code: " + this.code);
		}
		while ((isCorrect == false) && (this.challenger.numguess > 0))
{
			System.out.println("");
			System.out.println("You have " + this.challenger.numguess + " guess(es) left.");
			System.out.println("Enter guess:");
			isGuess = this.challenger.nextGuess();
			if (isGuess) {
				isValid = this.check.validity(this.challenger.guess);
				if (isValid) {
					withBWPegs = this.check.addFeedback(this.code, this.challenger.guess);
					isCorrect = this.check.matchCode(this.code, this.challenger.guess);
					this.challenger.addToHistory(withBWPegs);
				}
				else {
					System.out.println("INVALID_GUESS");
				}
			}
		}
		if (isCorrect == true) {
			System.out.println("");
			System.out.println("You win!");
			System.out.println("");
		}
		else if (this.challenger.numguess == 0) {
			System.out.println("");
			System.out.println("You lose! The pattern was " + this.code);
		}
	}
}
