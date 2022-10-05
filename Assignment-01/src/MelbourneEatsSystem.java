import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console based application for a food ordering company.
 * 
 * @return No return value.
 * @author David Bradbury
 *
 */

public class MelbourneEatsSystem {
	private static List<Business> busList = new ArrayList<Business>();
	private static Scanner sc = new Scanner(System.in);
	private static int[] discountBrkt = new int[7]; // Discounts bracket values
	private static double[] discountPerc = new double[4]; // Discounts percentages
	private static int reqBusDiscountNO = 0; // Number of businesses in order to receive discount
	private static double deliveryDisc = 0; // Delivery discount if order meets requirements

	public static void main(String[] args) {
		createBusinessesFromFile();
		importDiscountRatesFromFile();
		showMenu();

	}

	/**
	 * Reads from file to instantiate business objects in an Array List This is done
	 * while instantiating Food Array Lists associated with each Business object
	 * 
	 * @return No return value
	 */
	public static void createBusinessesFromFile() {

		try {
			Scanner readFile = new Scanner(new FileReader("Restaurants.txt"));

			while (readFile.hasNext()) {

				String data = readFile.nextLine(); // Reads whole line of data from file

				int loc1 = data.indexOf(","); // looks and sets delimiter

				String busName = data.substring(0, loc1); // selects portion of string

				data = data.substring(loc1 + 1, data.length());// removes used data

				loc1 = data.indexOf(",");

				String cat = data.substring(0, loc1);

				data = data.substring(loc1 + 1, data.length());

				loc1 = data.indexOf(",");

				String sDevFee = data.substring(0, loc1); // Temporary String variable before conversion to double

				double deliveryFee = Double.parseDouble(sDevFee.replace("$", ""));

				data = data.substring(loc1 + 1, data.length());

				List<Food> tempFoodList = new ArrayList<Food>();

				do {

					if (data.contains(","))
						loc1 = data.indexOf(",");
					else
						loc1 = data.length();

					String foodName = data.substring(0, data.indexOf("-"));

					data = data.substring(data.indexOf("-") + 1, data.length());

					String sFoodPrice; // Temporary String variable before conversion to double
					if (data.contains(",")) {
						sFoodPrice = data.substring(0, data.indexOf(","));

					} else {
						sFoodPrice = data.substring(0, data.length());

					}

					double foodPrice = Double.parseDouble(sFoodPrice.replace("$", ""));

					tempFoodList.add(new Food(foodName, foodPrice));

					if (data.contains(","))
						data = data.substring(data.indexOf(",") + 1, data.length());
					else
						data = "";

				} while (!data.isEmpty());

				if (cat.equals("Restaurant")) {
					busList.add(new Restaurant(busName, deliveryFee, tempFoodList));

				} else if (cat.equals("Cafe")) {
					busList.add(new Cafe(busName, deliveryFee, tempFoodList));
				} else {
					busList.add(new FastFood(busName, deliveryFee, tempFoodList));
				}

			}
			readFile.close();

		} catch (IOException e) {
			System.out.printf("Error- could not read.%n");

		}

	}

	/**
	 * Reads from file to gather information regarding business rules for discounts
	 * applied. Applies the rates to static variables for use upon calculating order
	 * totals
	 * 
	 * @return No return value
	 */
	public static void importDiscountRatesFromFile() {

		int count = 0;
		int dCount = 0;

		try {
			Scanner readFile = new Scanner(new FileReader("Discounts.txt"));

			while (readFile.hasNext()) {
				String data = readFile.nextLine();
				int loc1;
				if (data.contains("[")) {
					loc1 = data.indexOf(",");
					discountBrkt[count] = Integer.parseInt(data.substring(1, loc1)); // store bracket value in array
					data = data.substring(loc1 + 1, data.length()); // Trim the string to removed spent data
					loc1 = data.indexOf(")");
					count++;

					if (data.substring(0, loc1).length() != 0) {

						discountBrkt[count] = Integer.parseInt(data.substring(0, loc1)); // store bracket 1 high value
																							// and avoid delimiter
						data = data.substring(loc1 + 1, data.length()); // Trim the string to removed spent data
						count++;
					}
					loc1 = data.indexOf(",");

					data = data.substring(loc1 + 1, data.length());
					loc1 = data.indexOf("%");

					String temp = data.substring(0, loc1); // temp variable to parse to double on next row
					discountPerc[dCount] = Double.parseDouble(temp) / 100;

					dCount++;

				} else {
					loc1 = data.indexOf(",");
					reqBusDiscountNO = Integer.parseInt(data.substring(0, loc1));
					data = data.substring(loc1 + 1, data.length());
					loc1 = data.indexOf("%");
					deliveryDisc = Double.parseDouble(data.substring(0, loc1)) / 100;
					data = "";
				}
			}

			readFile.close();

		} catch (IOException e) {
			System.out.printf("Error- could not read.%n");

		}

	}

	/*
	 * Entry/ main console menu for the system. All intercation stems from this
	 * console menu.
	 * 
	 * * @return No return value
	 */
	private static void showMenu() {

		int selection;

		System.out.printf("       Welcome to Melbourne Eats\n\n");
		System.out.printf("-----------------------------------------\n");
		System.out.printf(" > Main menu\n");
		System.out.printf("-----------------------------------------\n\n");
		System.out.printf("   1) Browse by category\n");
		System.out.printf("   2) Search by restaurant\n");
		System.out.printf("   3) Checkout\n");
		System.out.printf("   4) Exit\n");
		System.out.printf("Please select:");

		if (!sc.hasNextInt()) {
			System.out.printf("Please select a valid option\n\n"); // validation of input
			sc.next();
			showMenu();
		}
		selection = sc.nextInt();

		if (selection < 1 || selection > 4) {
			System.out.printf("Please select a valid option\n\n"); // validation of input
			showMenu();

		} else {

			switch (selection) {
			case 1:
				browseCat();
				break;
			case 2:
				sc.nextLine(); // consume trailing line
				search();
				break;
			case 3:
				sc.hasNextLine(); // consume trailing line
				checkout();
				break;
			case 4:
				System.exit(0);
				break;

			}

		}
	}

	/*
	 * Category selection menu
	 * 
	 * @return No return value
	 */
	private static void browseCat() {
		int selection;
		int cat;

		System.out.printf("-----------------------------------------\n");
		System.out.printf(" > Select category\n");
		System.out.printf("-----------------------------------------\n\n");
		System.out.printf("   1) Restaurant\n");
		System.out.printf("   2) Cafe\n");
		System.out.printf("   3) Fast Food\n");
		System.out.printf("   4) Return to main menu\n");
		System.out.printf("Please select:");

		if (!sc.hasNextInt()) {
			System.out.printf("Please select a valid option\n\n");
			sc.next();
			browseCat();
		}
		selection = sc.nextInt();

		if (selection < 1 || selection > 4) {
			System.out.printf("Please select a valid option\n\n");
			browseCat();

		} else {
			switch (selection) {
			case 1:
				cat = 1;
				catMenu(cat);
				break;
			case 2:
				cat = 2;
				catMenu(cat);
				break;
			case 3:
				cat = 3;
				catMenu(cat);
				break;
			case 4:
				showMenu();
				break;

			}
		}
	}

	/*
	 * Menu selection for chosen category selects category based on instance of
	 * object
	 * 
	 * @return No return value
	 */

	public static void catMenu(int cat) {
		String label;
		if (cat == 1) {
			label = "Restaurant";
		} else if (cat == 2) {
			label = "Cafe";
		} else {
			label = "Fast Food";
		}

		System.out.printf("-----------------------------------------\n");
		System.out.printf(" > Select from %s list\n", label);
		System.out.printf("-----------------------------------------\n\n");

		int i = 1; // counter for options

		for (Business bus : busList) {

			if (cat == 1 && bus instanceof Restaurant) {
				System.out.printf("   %s) %s \n", i, bus.getName());
				i++;
			}
			if (cat == 2 && bus instanceof Cafe) {
				System.out.printf("   %s) %s \n", i, bus.getName());
				i++;
			}
			if (cat == 3 && bus instanceof FastFood) {
				System.out.printf("   %s) %s \n", i, bus.getName());
				i++;
			}

		}
		int selection;
		System.out.printf("Please select:");
		if (!sc.hasNextInt()) {
			System.out.printf("Please select a valid option\n\n"); // validation of input
			sc.next();
			catMenu(cat);
		}
		selection = sc.nextInt();

		if (selection < 1 || selection >= i) {
			System.out.printf("Please select a valid option\n\n"); // validation of input
			catMenu(cat);

		}
		i = 1; // counter reset to access choice of user

		for (Business bus : busList) {
			if (cat == 1 && bus instanceof Restaurant) {
				if (i == selection) {
					foodMenu(bus);
					i++;
				} else {
					i++;
				}
			}
			if (cat == 2 && bus instanceof Cafe) {
				if (i == selection) {
					foodMenu(bus);
					i++;
				} else {
					i++;
				}

			}
			if (cat == 3 && bus instanceof FastFood) {
				if (i == selection) {
					foodMenu(bus);
					i++;
				} else {
					i++;
				}

			}

		}

	}

	/*
	 * Businesses food options console menu
	 * 
	 * @return No return value
	 */

	public static void foodMenu(Business bus) {
		int i = 1; // value for display order selection
		System.out.printf("-----------------------------------------\n");
		System.out.printf(" > Select item from %s \n", bus.getName());
		System.out.printf("-----------------------------------------\n\n");
		i = bus.printFoodList(i);

		System.out.printf("Please select:");
		if (!sc.hasNextInt()) {
			System.out.printf("Please select a valid option\n\n"); // input validation
			sc.next();
			foodMenu(bus);
		}
		int item = sc.nextInt();

		if (item < 1 || item > i) {
			System.out.printf("Please select a valid option\n\n"); // input validation
			foodMenu(bus);

		}

		if (item == i) {
			showMenu();
		} else {
			System.out.printf("Please enter a quantity:");
			int quant = sc.nextInt();

			bus.setOrder(item, quant);

			foodMenu(bus);
		}

	}

	/*
	 * Search function for finding business related to user input'
	 * 
	 * @return no return value
	 */
	public static void search() {

		System.out.printf("Please enter a restaurant name:%n");

		String sName = sc.nextLine();

		int i = 1; //

		for (Business bus : busList) {

			if (bus.getName().toLowerCase().contains(sName.toLowerCase())) {
				System.out.printf("   %d) %s%n", i, bus.getName());
				i++;
			}

		}

		if (i <= 1) {
			System.out.printf("Cannot find anything related to %s%n", sName);
			search();

		}

		System.out.printf("Please select:");
		int selection = sc.nextInt();
		sc.hasNextLine();
		i = 1;

		for (Business bus : busList) {
			if (bus.getName().toLowerCase().contains(sName.toLowerCase())) {
				if (i == selection) {
					foodMenu(bus);
				} else {
					i++;
				}
			}
		}

	}

	/*
	 * Display order and perform calculations for totals
	 * 
	 * @return no return value
	 */
	public static void checkout() {

		double subTot = 0;
		double delFee = 0;
		int busCount = 0;
		double amountSaved = 0;
		System.out.printf("-----------------------------------------\n");
		System.out.printf(" > You have ordered the following items\n");
		System.out.printf("-----------------------------------------\n\n");

		for (Business bus : busList) {
			if (bus.getOrderListSize() > 0) {
				subTot = bus.printOrder(subTot);
				delFee = delFee + bus.getDeliveryFee();
				busCount++;
			}

		}

		double discDelFee = calculateDeliveryFee(busCount, delFee);
		double discSubTot = calculateOrderDiscount(subTot);

		amountSaved = (subTot + delFee) - (discSubTot + discDelFee);

		System.out.printf("-----------------------------------------\n");
		System.out.printf("%-27s$%.2f%n", "Order Price", discSubTot);
		System.out.printf("%-27s$%.2f%n", "Delivery Fee:", discDelFee);
		System.out.printf("%-27s$%.2f%n", "Savings amount:", amountSaved);
		System.out.printf("%-27s$%.2f%n", "Amount owing:", discSubTot + discDelFee);
		System.out.printf("-----------------------------------------\n");
		System.out.printf("Thankyou for ordering with Mebourne Eats!");

	}

	/*
	 * Assess and calculate discount on delivery
	 * 
	 * @return double value return
	 */
	public static double calculateDeliveryFee(int busCount, double delFee) {
		double discDelFee = delFee;
		if (busCount >= reqBusDiscountNO) {
			discDelFee = delFee - delFee * deliveryDisc;
			return discDelFee;

		} else {

			return discDelFee;
		}

	}

	/*
	 * Perform calculations for totals from loaded business rules parameters
	 * 
	 * @return double value return
	 */

	public static double calculateOrderDiscount(double subTot) {
		double discSubTot;
		if (subTot >= discountBrkt[0] && subTot < discountBrkt[1]) {
			discSubTot = subTot - subTot * discountPerc[0];
		} else if (subTot >= discountBrkt[2] && subTot < discountBrkt[3]) {
			discSubTot = subTot - subTot * discountPerc[1];
			;

		} else if (subTot >= discountBrkt[4] && subTot < discountBrkt[5]) {
			discSubTot = subTot - subTot * discountPerc[2];
		} else if (subTot >= discountBrkt[6]) {
			discSubTot = subTot - subTot * discountPerc[3];
		} else {
			discSubTot = subTot;
		}
		discSubTot = Math.round(discSubTot * 100.0) / 100.0;
		return discSubTot;

	}

}
