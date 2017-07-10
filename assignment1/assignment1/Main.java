	/*
	 * This file is how you might test out your code.  Don't submit this, and don't
	 * have a main method in SortTools.java.
	 */
package assignment1;

public class Main {
	public static void main(String [] args) {
		int[] x = new int[]{21, 22, 23, 24, 45, 46, 47};
		SortTools.insertSort(x, 7);
		SortTools.insertInPlace(x, 7, 21);
		SortTools.insertGeneral(x, 7, 21);
		SortTools.find(x, 7, 21);
		SortTools.isSorted(x, 7);
	}
}
	