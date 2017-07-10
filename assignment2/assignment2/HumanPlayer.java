/*  * EE422C Project 2 (Mastermind) submission by
 * Akaash Chikarmane
 * avc536
 * Slip days used: 
 * Spring 2017
 */

package assignment2;
import java.util.Scanner;

public class HumanPlayer {
	int numguess;
	String guess;
	String[] history;
	Scanner keyboard;
  
	public HumanPlayer(Scanner k) {
		this.numguess = GameConfiguration.guessNumber;
		this.guess = new String();
		this.history = new String[this.numguess];
		this.keyboard = k;
	}
  
	public boolean nextGuess() {
		this.guess = this.keyboard.nextLine();
		if (this.guess.equals("HISTORY")) {
			for (int i = 0; i < GameConfiguration.guessNumber - this.numguess; i++) {
				System.out.println(this.history[i]);
			}
			return false;
		}
		return true;
	}

	public void addToHistory(String inputGuess) {
		this.history[(GameConfiguration.guessNumber - this.numguess)] = inputGuess;
		this.numguess--;
		System.out.print(inputGuess);
	}
}
