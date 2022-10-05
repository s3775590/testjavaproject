/**
 * Instantiates order objects to be stored in ArrayList of businesses. Shopping
 * cart items.
 * 
 * @author David Bradbury
 *
 */
public class Order {
	private Food item;
	private int quant;

	public Order(Food item, int quant) {
		this.item = item;
		this.quant = quant;
	}

	public Food getItem() {
		return this.item;
	}

	public int getQuant() {
		return this.quant;
	}

	public String getItemName() {
		return this.item.getItem();
	}

	public Double getItemPrice() {
		return this.item.getPrice();
	}

	public void setOrderQuant(int quant) {
		this.quant = this.quant + quant;
		return;

	}

	public void printDetails() {
		System.out.printf("%5d x %s @ %5.2f ea%n", this.quant, this.item.getItem(), this.item.getPrice());
	}

}
