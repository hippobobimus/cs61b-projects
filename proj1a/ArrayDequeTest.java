/** Performs some basic array tests. */
public class ArrayDequeTest {
	
	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out size checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out get checks. */
	public static boolean checkGet(int expected, int actual) {
		if (expected != actual) {
			System.out.println("get() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");
		ArrayDeque<String> ad = new ArrayDeque<String>();

		boolean passed = checkEmpty(true, ad.isEmpty());

		ad.addFirst("front");
		passed = checkSize(1, ad.size()) && passed;
		passed = checkEmpty(false, ad.isEmpty()) && passed;

		ad.addLast("middle");
		passed = checkSize(2, ad.size()) && passed;

		ad.addLast("back");
		passed = checkSize(3, ad.size()) && passed;

		System.out.println("Printing out deque: ");
		ad.printDeque();

		printTestStatus(passed);
	}

	/** Adds an item, then removes an item, and ensures that ArrayDeque is empty
      * afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, ad.isEmpty());

		ad.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, ad.isEmpty()) && passed;

		ad.removeFirst();
		// should be empty 
		passed = checkEmpty(true, ad.isEmpty()) && passed;

		printTestStatus(passed);
	}

	/** Populates a deque and then gets individual items, ensuring they are
      * returned correctly by the get method. */
	public static void getTest() {

		System.out.println("Running get test.");

		ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, ad.isEmpty());

		ad.addFirst(10);
		ad.addLast(20);
		ad.addLast(30);
		ad.addLast(40);
		ad.addLast(50);
		// should not be empty and size should be 5.
		passed = checkEmpty(false, ad.isEmpty()) && passed;
		passed = checkSize(5, ad.size()) && passed;

		System.out.println("Printing out deque: ");
		ad.printDeque();

        passed = checkGet(10, ad.get(0)) && passed;
        passed = checkGet(30, ad.get(2)) && passed;
        passed = checkGet(50, ad.get(4)) && passed;

		printTestStatus(passed);
	}

	/** Populates a deque beyond both the looping point of the front and back. */
	public static void cycleTest() {

		System.out.println("Running cycle test.");

		ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, ad.isEmpty());

		ad.addFirst(0);
		ad.addFirst(7);
		ad.addLast(1);
		ad.addLast(2);
		ad.addLast(3);
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
		ad.addLast(4);
		ad.addLast(5);
		ad.addLast(6);
		ad.addLast(7);
		ad.addLast(99);
        passed = checkGet(99, ad.get(5)) && passed;
        passed = checkGet(3, ad.get(0)) && passed;

		System.out.println("Printing out deque: ");
		ad.printDeque();

		printTestStatus(passed);
	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
        getTest();
        cycleTest();
	}
} 

