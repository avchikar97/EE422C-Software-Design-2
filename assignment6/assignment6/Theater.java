/* MULTITHREADING <Theater.java>
 * EE422C Project 6 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */
package assignment6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Theater {
	private int rowNum;
	private int seatsPerRow;
	private String show;
	private List<Ticket> transactionLog;
	private boolean[][] availableSeat;
	private Object seatLock;
	private Object printLock;
	private int clientNum;

	/*
	 * Represents a seat in the theater
	 * A1, A2, A3, ... B1, B2, B3 ...
	 */
	static class Seat {
		private int rowNum;
		private int seatNum;

		public Seat(int rowNum, int seatNum) {
			this.rowNum = rowNum;
			this.seatNum = seatNum;
		}

		public int getSeatNum() {
			return seatNum;
		}

		public int getRowNum() {
			return rowNum;
		}

		@Override
		public String toString() {
			int row = rowNum;
			String result = "";
			int log = 26;
			int tempInt = 1;
			while (log < row) {
				log = log * 26;
				tempInt++;
			}
			char[] tempArray = new char[tempInt];
			int[] tempInts = new int[tempInt];
			for (int i = rowNum; i >= 1; i--) {
				tempInts[0]++;
				for (int j = 0; j < tempInts.length; j++) {
					if (tempInts[j] > 26) {
						tempInts[j + 1]++;
						tempInts[j] = 1;
					}
				}
			}
			for (int i = tempArray.length - 1; i >= 0; i--) {
				tempArray[tempArray.length - 1 - i] = (char) (tempInts[i] + 64);// flip the order of the tempInt digits and make them capital letters
			}
			result = new String(tempArray) + seatNum;

			return result;

		}
	}

	/*
	 * Represents a ticket purchased by a client
	 */
	static class Ticket {
		private String show;
		private String boxOfficeId;
		private Seat seat;
		private int client;

		public Ticket(String show, String boxOfficeId, Seat seat, int client) {
			this.show = show;
			this.boxOfficeId = boxOfficeId;
			this.seat = seat;
			this.client = client;
		}

		public Seat getSeat() {
			return seat;
		}

		public String getShow() {
			return show;
		}

		public String getBoxOfficeId() {
			return boxOfficeId;
		}

		public int getClient() {
			return client;
		}

		@Override
		public String toString() {
			int spaces = 0;
			String firstLine, secondLine, thirdLine, fourthLine, fifthLine, sixthLine, result;
			firstLine = "-------------------------------\n";	//31 dashes

			spaces = 31 - "|Show: ".length() - show.length() - 1;
			secondLine = "|Show: " + show;// spaces = 31 - "|Show: ".length() - show.length() - "|"
			for(int i = 0; i < spaces; i++){
				secondLine = secondLine + " ";
			}
			secondLine = secondLine + "|\n";

			spaces = 31 - "|Box Office ID: ".length() - boxOfficeId.length() - 1;
			thirdLine = "|Box Office ID: " + boxOfficeId;
			for (int i = 0; i < spaces; i++) {
				thirdLine = thirdLine + " ";
			}
			thirdLine = thirdLine + "|\n";

			spaces = 31 - "|Seat: ".length() - seat.toString().length() - 1;
			fourthLine = "|Seat: " + seat;
			for (int i = 0; i < spaces; i++) {
				fourthLine = fourthLine + " ";
			}
			fourthLine = fourthLine + "|\n";

			Integer temp = new Integer(client);
			spaces = 31 - "|Client: ".length() - /* boxOfficeId.length() - */temp.toString().length() - 1;// 2 for the last bar and the dash in the client ID
			fifthLine = "|Client: " + /* boxOfficeId + "-" + */temp;
			for (int i = 0; i < spaces; i++) {
				fifthLine = fifthLine + " ";
			}
			fifthLine = fifthLine + "|\n";
			
			sixthLine = "-------------------------------\n";

			result = firstLine + secondLine + thirdLine + fourthLine + fifthLine + sixthLine;
			return result;
		}
	}

	public Theater(int nRows, int sPerRow, String title) {
		this.rowNum = nRows;
		this.seatsPerRow = sPerRow;
		this.show = title;
		this.transactionLog = new ArrayList<Ticket>();
		this.availableSeat = new boolean[rowNum][seatsPerRow];
		for (int i = 0; i < availableSeat.length; i++) {
			Arrays.fill(availableSeat[i], true);
		}
		this.seatLock = new Object();
		this.printLock = new Object();
		this.clientNum = 1;
	}

	/*
	 * Calculates the best seat not yet reserved
	 *
	 * @return the best seat or null if theater is full
	 */
	public synchronized Seat bestAvailableSeat() {
		synchronized (seatLock) {
			Seat best = null;
			int x, y;
			x = 0;
			y = 0;
			if (getTransactionLog().size() == (rowNum * seatsPerRow)) {
				best = null;
			}
			else {
				for (int i = 0; i < rowNum; i++) { // i is the x, j is the y
					for (int j = 0; j < seatsPerRow; j++) {
						if (availableSeat[i][j]) { // this group of for loops looks for the smallest row/column combination
							x = i + 1;
							y = j + 1;
							availableSeat[i][j] = false; // marks the seat as reserved
							break;
						}
					}
					if ((x != 0) || (y != 0)) {
						break;
					}
				}
				best = new Seat(x, y);
			}
			return best;
		}
	}


	/*
	 * Prints a ticket for the client after they reserve a seat
   * Also prints the ticket to the console
	 *
   * @param seat a particular seat in the theater
   * @return a ticket or null if a box office failed to reserve the seat
   */
	public synchronized Ticket printTicket(String boxOfficeId, Seat seat, int client) {
		synchronized (printLock) {
			Seat best = bestAvailableSeat();
			if (best != null) {
				Ticket result = new Ticket(show, boxOfficeId, best, clientNum);
				// Ticket result = new Ticket(show, boxOfficeId, best, client);
				clientNum++;
				transactionLog.add(result);
				System.out.print(result);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (transactionLog.size() == (rowNum * seatsPerRow)) {
					System.out.println("Sorry, we are sold out!");
				}
				return result;
			}
			else
				return null;
		}
	}

	/*
	 * Lists all tickets sold for this theater in order of purchase
	 *
	 * @return list of tickets sold
	 */
	public List<Ticket> getTransactionLog() {
		return transactionLog;
	}

	/**
	 * 
	 * @return the total number of rows in the theater
	 */
	public int getrowNum() {
		return rowNum;
	}

	/**
	 * 
	 * @return the total number of seats in each row (total number of columns)
	 */
	public int getseatsPerRow() {
		return seatsPerRow;
	}

	/**
	 * 
	 * @return the movie being shown
	 */
	public String getshow() {
		return show;
	}

	/**
	 * 
	 * @return indicates whether or not the seat is available
	 */
	public boolean[][] getavailableSeat() {
		return availableSeat;
	}

	/**
	 * 
	 * @return the Object used when getting the next best seat
	 */
	public Object getseatLock() {
		return seatLock;
	}

	/**
	 * 
	 * @return the Object used when printing the ticket
	 */
	public Object getPrintLock() {
		return printLock;
	}

	/**
	 * 
	 * @return the current client's number
	 */
	public int getclientNum() {
		return clientNum;
	}
}
