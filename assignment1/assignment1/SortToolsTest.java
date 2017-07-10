package assignment1;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SortToolsTest {
	@Test(timeout = 2000) public void testFindFoundFull(){
		int[] x = new int[]{-2, -1, 0, 1, 2, 3};
		assertEquals(3, SortTools.find(x, 6, 1));
	}
	@Test(timeout = 2000) public void testInsertGeneralPartialEnd(){
		int[] x = new int[]{10, 20, 30, 40, 50};
		int[] expected = new int[]{10, 20, 30, 35};
		assertArrayEquals(expected, SortTools.insertGeneral(x, 3, 35));
	}
	@Test(timeout = 2000) public void testInsertSort(){
		int[] x = new int[]{45, 19, 21, 16, 17, 20, 2, 7};
		int[] expected = new int[]{2, 16, 17, 19, 20, 21, 45, 7};
		SortTools.insertSort(x, 7);
		assertArrayEquals(expected, x);
	}
}