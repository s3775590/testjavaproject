import java.util.ArrayList;
import java.util.List;

/**
 * Creates Business objects including storing menu items provided by the
 * business
 * 
 * @author David Bradbury
 *
 */

public abstract class Business {

	private String name;
	private double deliveryFee;
	private List<Food> foodList = new ArrayList<Food>();
	private List<Order> orderList = new ArrayList<Order>();

	public Business(String name, double deliveryFee, List<Food> foodList) {
		this.name = name;
		this.deliveryFee = deliveryFee;
		this.foodList = foodList;
		return;
	}

	public String getName() {
		return this.name;
	}

	public double getDeliveryFee() {
		return this.deliveryFee;
	}

	public void setFood(String item, double price) {
		this.foodList.add(new Food(item, price));
		return;
	}

	/*
	 * Display ArrayList of Food objects with counter for options
	 * 
	 * @return Integer value return
	 */
	public int printFoodList(int i) {

		for (Food food : this.foodList) {
			food.printItem(i);
			i++; // increment counter for option display
		}
		System.out.printf("%d) Return to menu%n", i);
		return i;

	}

	/*
	 * Method to check if item has been added to order and adjusts quantity if
	 * needed
	 * 
	 * @return boolean return value
	 */
	public boolean checkOrder(Food choice, int quant) {

		for (Order o : this.orderList) {
			if (o.getItemName().equals(choice.getItem())) {
				o.setOrderQuant(quant);

				return true;

			}
		}
		return false;
	}

	/*
	 * creates order objects and adds to ArrayList
	 * 
	 * @return no return value
	 */
	public void setOrder(int item, int quant) {

		item--; // decrement value by 1 to access index in array

		Food choice = this.foodList.get(item);

		if (checkOrder(choice, quant)) {
			return;

		} else {
			orderList.add(new Order(choice, quant));

			for (Order o : this.orderList) {
				System.out.println(o.getItemName() + " " + o.getQuant());
			}
			return;
		}

	}

	public int getOrderListSize() {
		return this.orderList.size();
	}

	/*
	 * Prints List of objects added to order and calculates sub total of cost of
	 * products
	 * 
	 * @return double return value of sub total
	 */
	public double printOrder(double subTot) {

		System.out.printf("-----------------------------------------\n");
		System.out.printf("%s%n", this.name);
		System.out.printf("-----------------------------------------\n\n");

		for (Order o : orderList) {
			double itemTot = o.getItemPrice() * o.getQuant();
			System.out.printf("%d %-25s$%.2f%n", o.getQuant(), o.getItemName(), itemTot);
			subTot = subTot + itemTot;

		}
		System.out.printf("%-27s$%.2f%n", "Delivery Fee:", this.deliveryFee);

		return subTot;
	}

}
