import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MelbourneEatsSystemTest {

	private Restaurant bus1;
	private FastFood bus2;
	private Cafe bus3;
	private Food food1;
	private Food food2;
	private Food food3;
	private List<Food> foodList = new ArrayList<Food>();
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	/*
	 * Load objects and business rules parameters for testing
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MelbourneEatsSystem.createBusinessesFromFile();
		MelbourneEatsSystem.importDiscountRatesFromFile();
	}

	/*
	 * Instantiate objects for testing
	 */
	@Before
	public void setUp() throws Exception {
		food1 = new Food("Sausage", 5.00);
		food2 = new Food("Pancake", 7.50);
		food3 = new Food("Martini", 18.50);
		foodList.add(food1);
		foodList.add(food2);
		foodList.add(food3);

		bus1 = new Restaurant("Joels Grub", 10.00, foodList);
		bus2 = new FastFood("Maccas", 5.00, foodList);
		bus3 = new Cafe("Espresso inc.", 6.50, foodList);

		System.setOut(new PrintStream(outputStreamCaptor));

	}

	@Test
	public void restaurantInstantiationTest() {
		assertTrue(bus1.getName().equals("Joels Grub"));
	}

	@Test
	public void fastFoodInstantiationTest() {
		assertTrue(bus2.getName().equals("Maccas"));
	}

	@Test
	public void cafeInstantiationTest() {
		assertTrue(bus3.getName().equals("Espresso inc."));
	}

	@Test
	public void foodInstantiationTest() {
		assertTrue(food1.getItem().equals("Sausage"));
	}

	/*
	 * Test of print out method.
	 * 
	 */
	@Test
	public void foodPrintTest() {
		bus1.printFoodList(1);
		assertEquals(
				"1) Sausage                 $5.00\r\n" + "2) Pancake                 $7.50\r\n"
						+ "3) Martini                 $18.50\r\n" + "4) Return to menu",
				outputStreamCaptor.toString().trim());
	}

	/*
	 * Test of discount calculations performed on typical values and boundary values
	 * of each discount bracket. In conjunction, tests importDiscountRatesFromFile()
	 * function.
	 */
	@Test
	public void orderDiscountTest() {

		assertEquals(5, MelbourneEatsSystem.calculateOrderDiscount(5), 0.001); // Typical value
		assertEquals(9.99, MelbourneEatsSystem.calculateOrderDiscount(9.99), 0.001);// Boundary test
		assertEquals(9.00, MelbourneEatsSystem.calculateOrderDiscount(10.0), 0.001);
		assertEquals(13.5, MelbourneEatsSystem.calculateOrderDiscount(15), 0.001);
		assertEquals(17.99, MelbourneEatsSystem.calculateOrderDiscount(19.99), 0.001);
		assertEquals(17, MelbourneEatsSystem.calculateOrderDiscount(20), 0.001);
		assertEquals(21.25, MelbourneEatsSystem.calculateOrderDiscount(25), 0.001);
		assertEquals(25.49, MelbourneEatsSystem.calculateOrderDiscount(29.99), 0.001);
		assertEquals(24, MelbourneEatsSystem.calculateOrderDiscount(30), 0.001);
		assertEquals(28, MelbourneEatsSystem.calculateOrderDiscount(35), 0.001);

	}

	/*
	 * Test of delivery fee discount application upon differing
	 * "numbers of businesses" being ordered from. In conjunction, tests
	 * importDiscountRatesFromFile() function.
	 */
	@Test
	public void deliveryDiscountTest() {

		assertEquals(10.00, MelbourneEatsSystem.calculateDeliveryFee(1, 10.00), 0.001);
		assertEquals(5.00, MelbourneEatsSystem.calculateDeliveryFee(2, 10.00), 0.001);

	}

	/*
	 * Test if same item is ordered multiple times, the system recognises and acts
	 * accordingly.
	 */
	@Test
	public void checkOrderTest() {
		bus1.setOrder(1, 1);
		assertTrue(bus1.checkOrder(food1, 1));
	}

}
