// SortTools.java 
/*
 * EE422C Project 1 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Spring 2017
 * Slip days used: 
 */

package assignment1;
import java.util.Arrays;
public class SortTools {
	/**
	  * This method tests to see if the first n elements of given array are sorted.
	  * @param x is the array
	  * @param n is the size of the input to be checked
	  * @return true if array is sorted
	  */
	public static boolean isSorted(int[] x, int n) {
		boolean a = false;
		if((x.length == 0) || (n == 0)){
			return false;
		}
		for(int i = 0; i < (n-1); i++){
			if(x[i]<=x[i+1]){
				a = true;
			}
			else{
				a = false;
				break;
			}
		}
		return a;
	}
	
	/** 
	 * Purpose: to find the value v in the first n elements of the array
	 * @param x is the array
	 * @param n is the size of the input to be checked
	 * @param v is the value to find
	 * @return index of x with value v
	 * */
	public static int find(int[] x, int n, int v){
		int[] y = Arrays.copyOfRange(x, 0, n);
		int low = 0;		//indicates the lower bound in which to search
		int high = y.length-1;		//indicated the upper bound in which to search
		int pos = (low+high)/2;		//indicates the midpoint between the two
		while((y[pos] != v) && (low <= high)){		//leave while loop if: current midpoint holds v or if low == high in which case v is not present
			if(y[pos] > v){		//if v is less than the value at pos, move upper bound below pos and continue with new midpoint and upper bound
				high = pos - 1;
			}
			else{		//if v is higher than value of pos, move lower bound above pos and continue with new midpoint and lower bound
				low = pos + 1;
			}
			pos = (low + high)/2;
		}
		if(y[pos] == v){		//by end of loop loop if: v is found (it will always be indicated at pos) or v is not present
			return pos;
		}
		else{
			return -1;
		}
	}
	
	/**
	 * Purpose: create new array of n elements with value v if previously non-existent
	 * @param x is the array
	 * @param n is the number of elements to copy
	 * @param v is the value to insert into the array if it doesn't already exist
	 * @return sorted array of first n elements of array x with at least one copy of value v
	 */
	public static int[] insertGeneral(int[] x, int n, int v){
		int[] y = Arrays.copyOfRange(x,  0,  n);
		boolean addv = false;
		int insert = -1;
		for(int i = 0; i < y.length - 1; i++){	//checks to see if v is already in the array
			if(y[i] == v){
				addv = true;
			}
		}
		if(addv){		//if it is, return y and be done with it
			return y;
		}
		else{		//if not, create a new array of length + 1 and insert v where it needs to be
			for(int i = 0; i < y.length; i++){		//sets insert to the index in which v should go
				if(y[i] > v){
					insert = i;
					break;
				}
			}
			if(insert == -1){		//if insert wasn't changed, it must be the last element in the new array
				insert = y.length;
			}
			int[] z = new int[y.length + 1];
			for(int i = 0; i < insert; i++){	//copies all the elements before v to preserve order
				z[i] = y[i];
			}
			z[insert] = v;		//insert v
			for(int i = insert + 1; i < z.length; i++){		//copy the rest of y
				z[i] = y[i-1];
			}
			return z;
		}
	}
	
	/**
	 * Purpose: insert value v correctly into a sorted array if not already included
	 * @param x is the sorted array
	 * @param n is the number of elements
	 * @param v is the value to be inserted if necessary
	 * @return the length of the set of integers including v
	 */
	public static int insertInPlace(int[] x, int n, int v){
		boolean addv = false;
		int insert = -1;
		for(int i = 0; i < n; i++){	//checks to see if v is already in the number of elements
			if(x[i] == v){
				addv = true;
			}
		}
		if(addv){	//if v is already present, don't add anything and just return n
			return n;
		}
		else{		//begin insertion process
			for(int i = 0; i < n; i++){		//check the first n elements of x. Insert will be -1 if it is the last element or the index in which it should go
				if(x[i] > v){
					insert = i;
					break;
				}
			}
			if(insert == -1){		//if insert is unchanged, it must be the last element in the new list within the array with index n
				insert = n;
			}
			for(int i = n; i > insert; i--){		//copy the rest of y
				x[i] = x[i-1];
			}
			x[insert] = v;
			return (n+1);
		}
	}
	
	/**
	 * Purpose: sort the first n elements in non-decreasing order
	 * @param x is the array to be sorted
	 * @param n is the number of elements to be sorted
	 * @return nothing
	 */
	public static void insertSort(int[] x, int n){
		int tempnum = 0;		//will be used to store number when switching
		for(int i = 1; i < n; i++){		//decides which number to start moving
			for(int j = i; ((j > 0) && (x[j] < x[j-1])); j--){		//switches the number formerly at index i to the left until it is higher than all to its left 
				tempnum = x[j-1];
				x[j-1] = x[j];
				x[j] = tempnum;
			}
		}
	}
}
