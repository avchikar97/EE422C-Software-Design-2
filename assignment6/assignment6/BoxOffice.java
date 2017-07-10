/* MULTITHREADING <BoxOffice.java>
 * EE422C Project 6 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */

package assignment6;

import assignment6.Theater.Seat;
import assignment6.Theater.Ticket;

public class BoxOffice implements Runnable {

	private Integer numClient;
	private String ID;
	private Theater theater;

	public BoxOffice(String s, Integer c, Theater t) {
		this.ID = s;
		this.numClient = c;
		this.theater = t;
	}

	@Override
	public void run() {
		for (int i = 0; i < numClient; i++) {
			Seat s = new Seat(0, 0);
			Ticket t = theater.printTicket(ID, s, i);
			if (t == null) {
				break;
			}
		}

	}

	/**
	 * 
	 * @return the number of clients left at that booth
	 */
	public Integer getnumClient() {
		return numClient;
	}

	/**
	 * 
	 * @return the box office ID
	 */
	public String getID() {
		return ID;
	}

	/**
	 * 
	 * @return the theater the box office services
	 */
	public Theater gettheater() {
		return theater;
	}
}
