package assignment6;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import assignment6.Theater.Seat;
import assignment6.Theater.Ticket;

public class Test {

	private static void joinAllThreads(List<Thread> threads) throws InterruptedException {
		for (Thread t: threads) {
			t.join();
		}
	}
	
	@BeforeClass
	public static void setupBeforeClass() throws InterruptedException {
		ByteArrayOutputStream st = new ByteArrayOutputStream();
	    PrintStream ps = new PrintStream(st);
	    PrintStream old = System.out;
	    System.setOut(ps);
	}


	@org.junit.Test
	public void testTicketShow() {
		Theater t = new Theater(3,5,"Ouija");
		Seat s = new Seat(3,5);
		Ticket t1 = new Ticket("Ouija", "BX1", s, 1);
		
		assertEquals("Ouija", t1.getShow());
	}
	
	@org.junit.Test
	public void testTicketBO() {
		Theater t = new Theater(3,5,"Ouija");
		Seat s = new Seat(3,5);
		Ticket t1 = new Ticket("Ouija", "BX1", s, 1);
		
		assertEquals("BX1", t1.getBoxOfficeId());
	}
	
	@org.junit.Test
	public void testTicketRow_Seat_Num() {
		Theater t = new Theater(3,5,"Ouija");
		Seat s = new Seat(3,5);
		Ticket t1 = new Ticket("Ouija", "BX1", s, 1);
		
		assertEquals(3, t1.getSeat().getRowNum());
		assertEquals(5, t1.getSeat().getSeatNum());
	}
	
	@org.junit.Test
	public void testTicketClient() {
		Theater t = new Theater(3,5,"Ouija");
		Seat s = new Seat(3,5);
		Ticket t1 = new Ticket("Ouija", "BX1", s, 1);
		
		assertEquals(1, t1.getClient());
	}
	
	@org.junit.Test
	public void testThreadSize() {
		
		Theater t = new Theater(3,5,"Ouija");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("BX1", 3);
		m.put("BX3", 3);
		m.put("BX2", 4);
		m.put("BX5", 3);
		m.put("BX4", 3);
		BookingClient b = new BookingClient(m,t);
		List<Thread> l = b.simulate();
		assertEquals(5, l.size());
		
	}
	
	@org.junit.Test
	public void testClientIds() {
		Theater t = new Theater(100,2,"Ouija");
		boolean noDuplicate = true;
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("BX1", 20);
		m.put("BX3", 20);
		m.put("BX2", 20);
		m.put("BX5", 20);
		m.put("BX4", 20);
		BookingClient b = new BookingClient(m,t);
		try {
			joinAllThreads(b.simulate());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		List<Ticket> t_l = t.getTransactionLog();
		Set<Integer> s = new TreeSet<Integer>();
		for (Ticket ticket: t_l) {
			if (!s.add(ticket.getClient())) {
				System.out.println("you know it: " +ticket.getClient());
				noDuplicate = false;
			}
		}
		assertEquals(true, noDuplicate);
	}
	
	@org.junit.Test
	public void testSeatNums() {
		Theater t = new Theater(100,2,"Ouija");
		boolean noDuplicate = true;
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("BX1", 20);
		m.put("BX3", 20);
		m.put("BX2", 20);
		m.put("BX5", 20);
		m.put("BX4", 20);
		BookingClient b = new BookingClient(m,t);
		try {
			joinAllThreads(b.simulate());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		List<Ticket> t_l = t.getTransactionLog();
		Set<String> s = new TreeSet<String>();
		for (Ticket ticket: t_l) {
			if (!s.add(ticket.getSeat().toString())) {
				noDuplicate = false;
			}
		}
		assertEquals(true, noDuplicate);
	}
	
	@org.junit.Test
	public void testSeatOrder() {
		Theater t = new Theater(100,2,"Ouija");
		boolean noDuplicate = true;
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("BX1", 20);
		m.put("BX3", 20);
		m.put("BX2", 20);
		m.put("BX5", 20);
		m.put("BX4", 20);
		BookingClient b = new BookingClient(m,t);
		try {
			joinAllThreads(b.simulate());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		List<Ticket> list = t.getTransactionLog();
		
	    boolean sorted = true;        
	    for (int i = 1; i < list.size(); i++) {
	    	String s1 = list.get(i-1).getSeat().toString();
	    	String s2 = list.get(i).getSeat().toString();
	    	if (s1.length() < s2.length()) {
	    		sorted = true;
	    	}
	    	else if (s1.compareTo(s2) > 0) sorted = false;
	    }
	    assertEquals(true, sorted);    
	}
}
