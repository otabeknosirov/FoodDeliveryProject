package delivery;

import java.util.HashMap;
import java.util.Map;

import delivery.Delivery.OrderStatus;

public class Order {

	private int customerId;
    private OrderStatus status;
	
	private Item item;
	private int qty;
	
	private Map<Item,Integer> items = new HashMap<>() ;
	
	public Order(int customerId) {
		this.customerId = customerId;
		this.status = OrderStatus.NEW;
	}
	
	public Order(Item item, int qty) {
		this.setItem(item);
		this.setQty(qty);
	}

	public int getCustomerId() {
		return customerId;
	}
	public Map<Item,Integer> getItems(){
		return items;
	}

	public void addItemToMenu(Item item, int qty) {
           if(!items.containsKey(item)) {
        	   items.put(item, qty);
           }
           else {
        	   items.put(item, items.get(item) + qty);
           }
	}
	public Double TotalOrderPrice() {
		return items.entrySet().stream().mapToDouble(p->p.getKey().getPrice() * p.getValue()).sum();
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	

}
