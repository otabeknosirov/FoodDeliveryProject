package delivery;


public class Item {

	private String description;
	private double price;
	private String category;
	private int prepTime;
	
	public Item(String description, double price, String category, int prepTime) {
		this.description = description;
		this.price = price;
		this.category = category;
		this.prepTime = prepTime;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	public int getPrepTime() {
		return prepTime;
	}
	@Override
	public String toString() {
		return String.format("[%s] %s: %.2f",category,description,price);
	}
	
	
	
}
