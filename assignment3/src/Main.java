/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Nicholas Sutanto
 * nds729
 * 16220
 * Slip days used: 0
 * Git URL: https://github.com/AkaashChikarmane/ee422c_project3
 * Spring 2017
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static ArrayList<String> dict;
	static boolean[] dfsVisited; // all values in nodeVisited correspond to the entry at the same index in dict, static to save memory and close off branches that have been found to not work - used in DFS
	static String[] dfsParent; // index corresponds to index in dict, used to keep track of parent strings in DFS
	static String[] bfsParent; // values in dictParent correspond to same index in dict, used to keep track of the parent - used in BFS
	static boolean[] bfsVisited; // used to see if the node has been visited - used in BFS


	public static void main(String[] args) throws Exception {
		ArrayList<String> mainList = new ArrayList<String>();
		ArrayList<String> input = new ArrayList<String>();
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		while (true) {
			initialize();
			input = parse(kb);
			if (input == null) {
				break;
			}
			mainList = getWordLadderDFS(input.get(0), input.get(1));
			printLadder(mainList);
			mainList = getWordLadderBFS(input.get(0), input.get(1));
			printLadder(mainList);
		}
	}
	
	public static void initialize() {
		dict = new ArrayList<String>(makeDictionary());
		
		dfsVisited = new boolean[dict.size()];
		Arrays.fill(dfsVisited, true);

		dfsParent = new String[dict.size()];
		Arrays.fill(dfsParent, null);

		bfsParent = new String[dict.size()];
		Arrays.fill(bfsParent, null);
		
		bfsVisited = new boolean[dict.size()];
		Arrays.fill(bfsVisited, false);

		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	

	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		ArrayList<String> parseList = new ArrayList<String>();
		String commandOne = new String();
		String commandTwo = new String();
		if (keyboard.hasNext()) {
			commandOne = keyboard.next();
			if (commandOne.equals("/quit"))
				return null;
			parseList.add(commandOne);
		}
		if (keyboard.hasNext()) {
			commandTwo = keyboard.next();
			if (commandTwo.equals("/quit"))
				return null;
			parseList.add(commandTwo);
		}
		return parseList;
	}

	/**
	 * 
	 * @param start
	 *            is the first word inputted from console
	 * @param end
	 *            is the second word inputted from console
	 * @return first working word ladder or null if there is no working word ladder
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		if (start.equalsIgnoreCase("/quit") || end.equalsIgnoreCase("/quit"))
			return null;
		boolean found = myWordLadderDFS(start, end);
		ArrayList<String> toReturn = new ArrayList<String>();
		if (found) {
			int addToLadder = dict.indexOf(end.toUpperCase());
			String temp = end;
			boolean a = true;
			toReturn.add(0, temp);
			while ((dfsParent[addToLadder] != start) || a) {
				addToLadder = dict.indexOf(temp.toUpperCase());
				toReturn.add(0, dfsParent[addToLadder]);
				temp = dfsParent[addToLadder];
				a = false;
			}
			Iterator<String> correct = toReturn.iterator();
			ArrayList<String> correctedBFSList = new ArrayList<String>();
			while (correct.hasNext()) {
				correctedBFSList.add(correct.next().toLowerCase());
			}
			toReturn = correctedBFSList;
		}
		else {
			toReturn = new ArrayList<String>(); // return an empty list
			toReturn.add(start);
			toReturn.add(end);
		}
		return toReturn;
	}
	
	/**
	 * 
	 * @param start
	 *            is the first inputted word from console
	 * @param end
	 *            is the second inputted word from console
	 * @param dfsVisited
	 *            is a boolean array corresponding to the dictionary used to say if the word is already in the word ladder
	 * @param record
	 *            is a record of the word ladder thus far
	 * @return completed word ladder or null if there is no existing word ladder
	 */
	public static boolean myWordLadderDFS(String start, String end) {
		int strikeOut = dict.indexOf(start); // sets the start to false so that it doesn't circle back
		strikeOut = dict.indexOf(start.toUpperCase()); // finds the index of the starting word so that it can be set to false
		boolean nextReturn = false;
		dfsVisited[strikeOut] = false; // sets corresponding starting boolean to false
		if (start.equalsIgnoreCase(end)) { // base case (if it finds a working word ladder)
			return true;
		}
		for (int i = 0; i < dict.size(); i++) {
			if ((oneLetterDifference(start, dict.get(i))) && (dfsVisited[i])) { // if there is only a one letter difference between the starting word AND the word is not already part of the ladder
				dfsParent[i] = start;
				nextReturn = myWordLadderDFS(dict.get(i), end);
				if (nextReturn == true) { // condition will be false if it has come to the last branch and there is no other possible ladder
					return true;

				}
			}
		}
		return false;
	}

    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		if (start.equalsIgnoreCase("/quit") || end.equalsIgnoreCase("/quit"))
			return null;
		boolean found = myWordLadderBFS(start, end);
		ArrayList<String> toReturn = new ArrayList<String>();
		if (found) {
			int addToLadder = dict.indexOf(end.toUpperCase());
			String temp = end;
			boolean a = true;
			toReturn.add(0, temp);
			while ((bfsParent[addToLadder] != start) || a) {
				addToLadder = dict.indexOf(temp.toUpperCase());
				toReturn.add(0, bfsParent[addToLadder]);
				temp = bfsParent[addToLadder];
				a = false;
			}
			Iterator<String> correct = toReturn.iterator();
			ArrayList<String> correctedBFSList = new ArrayList<String>();
			while (correct.hasNext()) {
				correctedBFSList.add(correct.next().toLowerCase());
			}
			toReturn = correctedBFSList;
		}
		else {
			toReturn = new ArrayList<String>(); // return an empty list
			toReturn.add(start);
			toReturn.add(end);
		}
		return toReturn;

	}
    
	public static boolean myWordLadderBFS(String start, String end) { // main function is to set the parent strings of each word in the dictionary
		LinkedList<String> wordQueue = new LinkedList<String>();
		boolean found = false; // sets default value to return, will return true if there is a word ladder
		String tempParent = null;
		String temp = null;
		bfsParent[dict.indexOf(start.toUpperCase())] = null; // sets the start parent to null
		wordQueue.add(start); // add the starting word to the queue
		bfsVisited[dict.indexOf(start.toUpperCase())] = true; // set start word to visited so that the BFS doesn't loop through start
		while (!wordQueue.isEmpty()) { // if the queue is empty and it has not returned, all branches have been checked and there is no link between start word and end word
			tempParent = wordQueue.remove(); // remove the top word from the queue for analysis
			if (tempParent.equalsIgnoreCase(end)) { // if the word is the end word, you can exit having found the word
				found = true;
				return found;
			}
			for (int i = 0; i < dict.size(); i++) {// places all neighbors (one letter difference) on the queue for anaylsis
				temp = dict.get(i);
				if (oneLetterDifference(tempParent, temp) && !bfsVisited[i]) {
					bfsVisited[i] = true; // sets that they've been visited so that the tree doesn't loop
					if (tempParent != null) {
						bfsParent[i] = tempParent; // sets the parent so that it can be traced back when creating word ladder to print
					}
					wordQueue.add(temp); // adds neighbor to queue
				}
			}
		}
		return false;
	}

	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder.equals(null))
			return;
		if (ladder.size() > 2) {
			System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " + ladder.get(0)
					+ " and " + ladder.get(ladder.size() - 1) + ".");
			Iterator<String> toPrint = ladder.iterator();
			while (toPrint.hasNext()) {
				System.out.println(toPrint.next());
			}
		}
		else if (ladder.size() == 2) {
			System.out.println("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(1) + ".");
		}
	}

	/**
	 * 
	 * @param w1
	 *            is the starting word
	 * @param w2
	 *            is the word to be tested
	 * @return true iff there is only a one letter difference
	 */
	public static boolean oneLetterDifference(String w1, String w2) {
		int difference = 0;
		if (w1.length() != w2.length())
			return false;
		for (int i = 0; i < w1.length(); i++) {
			if (!w1.substring(i, i + 1).equalsIgnoreCase(w2.substring(i, i + 1))) {
				difference++;
			}
		}
		if (difference == 1) {
			return true;
		}
		else
			return false;
	}
}
