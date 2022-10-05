/**
 * Instantiates food objects to be stored in ArrayList of businesses
 * 
 * @author David Bradbury
 *
 */
public class Food {

	private String item;
	private double price;

	public Food(String item, double price) {
		this.item = item;
		this.price = price;
	}

	public String getItem() {
		return this.item;
	}

	public double getPrice() {
		return this.price;
	}

	/*
	 * Prints object to console
	 * 
	 * @return no return value
	 */
	public void printItem(int i) {
		System.out.printf("%d) %-24s$%.2f%n", i, this.item, this.price);
	}

}
