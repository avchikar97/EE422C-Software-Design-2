package assignment6;

import static org.junit.Assert.*;

import org.junit.Test;

import assignment6.Theater.Seat;

public class assignment6Test {

	@Test
	public void test1() {
		Theater.Ticket t = new Theater.Ticket("Ouija", "BX4", new Seat(651, 1), 3);
		System.out.println(t.toString());
	}

	// @Test
	public void test2(){
		Theater.Ticket t = new Theater.Ticket("Ouija", "BX4", null, 2);
		System.out.println(t.toString());
	}

}
