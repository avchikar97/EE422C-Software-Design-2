package assignment4;
/* CRITTERS Main.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {
    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
	public static ByteArrayOutputStream testOutputString; // if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }
        /* Do not alter the code above for your submission. */
        String[] userinputs = null;
        boolean quitflag = false;
        while(!quitflag){
        	userinputs = null;
        	System.out.print("critters> ");
        	userinputs = kb.nextLine().split(" ");
			if (userinputs[0].equals("quit") || userinputs[0].equals("show") || userinputs[0].equals("step")
					|| userinputs[0].equals("seed") || userinputs[0].equals("make") || userinputs[0].equals("stats")) {
        	switch(userinputs.length){
        	case 1: {
        		if(userinputs[0].equals("show")) {
        			Critter.displayWorld();
        			break;
        		}
        		else if(userinputs[0].equals("quit")){
        			quitflag = true;
        			break;
        		}
        		else if(userinputs[0].equals("step")){
        			Critter.worldTimeStep();
        			break;
        		}
        		else{
							System.out.print("error processing:");
						for (int j = 0; j < userinputs.length; j++) {
							System.out.print(" " + userinputs[j]);
						}
						System.out.println("");
        		}
					break;
        	}
        	case 2: {
        		if(userinputs[0].equals("step")){
        			String steptemp = userinputs[1];
						try {
								int numSteps = Integer.parseInt(steptemp);
								if (numSteps <= 0) {
									throw new Exception();
								}
								for (int i = 0; i < numSteps; i++) {
        				// parseInt throws NumberFormatException if not number
        				Critter.worldTimeStep();
        			}
						} catch (Exception e) {
							System.out.print("error processing:");
							for (int j = 0; j < userinputs.length; j++) {
								System.out.print(" " + userinputs[j]);
							}
							System.out.println("");
						}
        			break;
        		}
        		else if(userinputs[0].equals("seed")){
        			String seedtemp = userinputs[1];
						try {
								int seedNum = Integer.parseInt(seedtemp);
								if (seedNum <= 0) {
									throw new Exception();
								}
        			// is the input always gonna be an integer? Integer.parseInt();
								Critter.setSeed(seedNum); // parseInt throws NumberFormatException if not number
						} catch (Exception e) {
							System.out.print("error processing:");
							for (int j = 0; j < userinputs.length; j++) {
								System.out.print(" " + userinputs[j]);
							}
							System.out.println("");
						}
        			break;
        		}
        		else if(userinputs[0].equals("make")){
        			String classtemp = userinputs[1];
        			try {
						Critter.makeCritter(classtemp);
							} catch (Exception e) {
							System.out.print("error processing:");
							for (int j = 0; j < userinputs.length; j++) {
								System.out.print(" " + userinputs[j]);
							}
							System.out.println("");
					}
        			break;
        		}
        		else if(userinputs[0].equals("stats")){
        			String statstemp = userinputs[1];
        			Class<?> inputclass = null;
						List<Critter> existingCritters = null;
						try {
							inputclass = Class.forName(myPackage + "." + statstemp); // inputclass is the critter whose stats we are looking for
							existingCritters = Critter.getInstances(statstemp);// existingCritters is the List of all critter whose stats we're looking for
							Method stats = inputclass.getMethod("runStats", List.class);
							stats.invoke(inputclass, existingCritters);
					} catch (Exception e) {
							System.out.print("error processing:");
							for (int j = 0; j < userinputs.length; j++) {
								System.out.print(" " + userinputs[j]);
							}
							System.out.println("");
					}
        			break;
        		}
        		else {
							System.out.print("error processing:");
						for (int j = 0; j < userinputs.length; j++) {
							System.out.print(" " + userinputs[j]);
						}
						System.out.println("");
        		}
					break;
        	}
        	case 3:{
        		if(userinputs[0].equals("make")){
        			String makeclass = userinputs[1];
        			String makecount = userinputs[2];
        			try{
								int makeNum = Integer.parseInt(makecount);
								if (makeNum <= 0) {
									throw new Exception();
								}
								for (int i = 0; i < makeNum; i++) {
								try {
									Critter.makeCritter(makeclass);
								} catch (InvalidCritterException e) {
									System.out.print("error processing:");
									for (int j = 0; j < userinputs.length; j++) {
										System.out.print(" " + userinputs[j]);
									}
									System.out.println("");
								}
							}
						} catch (Exception e) {
							System.out.print("error processing:");
							for (int j = 0; j < userinputs.length; j++) {
								System.out.print(" " + userinputs[j]);
							}
							System.out.println("");
        			}
        		}
					else {
						System.out.print("error processing:");
						for (int j = 0; j < userinputs.length; j++) {
							System.out.print(" " + userinputs[j]);
						}
						System.out.println("");
					}
					break;
        	}
        	default:{
						System.out.print("error processing:");
					for (int i = 0; i < userinputs.length; i++) {
						System.out.print(" " + userinputs[i]);
					}
					System.out.println("");
        	}
        	}
        	}
			else {
				System.out.print("invalid command:");
				for (int i = 0; i < userinputs.length; i++) {
					System.out.print(" " + userinputs[i]);
				}
				System.out.println("");
			}
        System.out.flush();
    }
	}
}
