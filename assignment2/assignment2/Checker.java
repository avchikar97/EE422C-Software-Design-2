/*  * EE422C Project 2 (Mastermind) submission by
 * Akaash Chikarmane
 * avc536
 * Slip days used: 
 * Spring 2017
 */

package assignment2;

import java.util.Arrays;

public class Checker {
	public boolean validity(String inputGuess) {
		boolean[] validPeg = new boolean[GameConfiguration.pegNumber];
		boolean finalValid = true;
		String pegI = new String();
		if (inputGuess.length() == GameConfiguration.pegNumber) {
			for (int i = 0; i < inputGuess.length(); i++) {
				pegI = inputGuess.substring(i, i + 1);
				for (int j = 0; j < GameConfiguration.colors.length; j++) {
					if (GameConfiguration.colors[j].equals(pegI)) {
						validPeg[i] = true;
						break;
					}
					if (j == GameConfiguration.colors.length - 1) {
						validPeg[i] = false;
					}
				}
			}
		}
		else
			return false;
		for (int i = 0; i < inputGuess.length(); i++) {
			finalValid = (finalValid) && (validPeg[i]);
		}
		return finalValid;
	}

	public String addFeedback(String code, String inputGuess) {
		int black = 0;
		int white = 0;
		String guessSub;
		String codeSub;
		boolean[] analyzeCode = new boolean[code.length()];// used as a method to "strike out pegs" after feedback has been given
		Arrays.fill(analyzeCode, true);
		boolean[] analyzeGuess = new boolean[inputGuess.length()];
		Arrays.fill(analyzeGuess, true);
		if (code.equals(inputGuess)) {
			black = GameConfiguration.pegNumber;
			white = 0;
		}
		else {
			for (int i = 0; i < inputGuess.length(); i++) { // sets black pegs
				guessSub = inputGuess.substring(i, i + 1);
				codeSub = code.substring(i, i + 1);
				if (guessSub.equals(codeSub)) {
					black++;
					analyzeCode[i] = false;
					analyzeGuess[i] = false;
				}
			}
			for (int i = 0; i < inputGuess.length(); i++) { // sets white pegs
				if (analyzeGuess[i]) { // if the guess peg hasn't already been given feedback
					guessSub = inputGuess.substring(i, i + 1);
					for (int j = 0; j < code.length(); j++) {
						if (analyzeCode[j]) { // if the code peg hasn't already been given feedback
							codeSub = code.substring(j, j + 1);
							if (guessSub.equals(codeSub)) {
								white++;
								analyzeGuess[i] = false;
								analyzeCode[j] = false;
								break; // leave once feedback is given
							}
						}
					}
				}
			}
		}
		inputGuess = inputGuess + " -> " + black + "b_" + white + "w";
		return inputGuess;
	}
  
	public boolean matchCode(String code, String inputGuess) {
		if (code.equals(inputGuess)) {
			return true;
		}
		return false;
	}
}
