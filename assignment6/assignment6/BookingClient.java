/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Akaash Chikarmane
 * avc536
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */
package assignment6;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.Thread;

public class BookingClient {
	private Map<String, Integer> office;
	private Theater theater;
  /*
   * @param office maps box office id to number of customers in line
   * @param theater the theater where the show is playing
   */
	public BookingClient(Map<String, Integer> o, Theater t) {
		this.office = o;
		this.theater = t;
	}

  /*
   * Starts the box office simulation by creating (and starting) threads
   * for each box office to sell tickets for the given theater
   *
   * @return list of threads used in the simulation,
   *         should have as many threads as there are box offices
   */
	public List<Thread> simulate() {
		Set<String> k = office.keySet(); // client ID will be (boxOfficeID)-(client#)
		String[] s = (String[]) k.toArray(new String[0]);
		ArrayList<Thread> allThreads = new ArrayList<Thread>();
		for (int i = 0; i < s.length; i++) {
			Runnable tempTask = new BoxOffice(s[i], office.get(s[i]), theater);
			Thread tempThread = new Thread(tempTask);
			allThreads.add(tempThread);
		}
		for (int i = 0; i < allThreads.size(); i++) {
			allThreads.get(i).start();
		}
		for (Thread threadTemp : allThreads) {
			try {
				threadTemp.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return allThreads;
	}

	public static void main(String[] arg) {
		Map<String, Integer> input1 = new Hashtable<String, Integer>();
		input1.put("BX1", new Integer(100));
		input1.put("BX3", new Integer(100));
		Theater input2 = new Theater(101, 2, "Ouija");
		BookingClient tester = new BookingClient(input1, input2);
		tester.simulate();
	}
}
