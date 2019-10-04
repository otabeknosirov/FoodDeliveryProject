package delivery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class Delivery {
	
	private Map<String, Customer> customers = new LinkedHashMap<>();
	private List<Item> items = new ArrayList<>();
	private List<Order> orders = new LinkedList<>();
	
    public static enum OrderStatus{ NEW, CONFIRMED, PREPARATION, ON_DELIVERY, DELIVERED } 
    

    /**
     * Creates a new customer entry and returns the corresponding unique ID.
     * 
     * 
     * @param name name of the customer
     * @param address customer address
     * @param phone customer phone number
     * @param email customer email
     * @return unique customer ID (positive integer)
     */
    public int newCustomer(String name, String address, String phone, String email) throws DeliveryException {
    	if(customers.containsKey(email)) throw new DeliveryException();
    	Customer c = new Customer(name,address,phone,email);
        customers.put(email, c);
    	return customers.size();
    }
    
    /**
     * Returns a string description of the customer in the form:
     * <pre>
     * "NAME, ADDRESS, PHONE, EMAIL"
     * </pre>
     * 
     * @param customerId
     * @return customer description string
     */
    public String customerInfo(int customerId){
    	return customers.values().stream().skip(customerId - 1).findFirst().toString();
    }
    
    /**
     * Returns the list of customers, sorted by name
     * 
     */

    public List<String> listCustomers(){
    		
    	return  customers.values().stream()
    			                  .map(Customer::toString)
    			                  .sorted()
    			                  .collect(Collectors.toList());
    
    }
    
    /**
     * Add a new item to the delivery service menu
     * 
     * @param description description of the item (e.g. "Pizza Margherita", "Bunet")
     * @param price price of the item (e.g. 5 EUR)
     * @param category category of the item (e.g. "Main dish", "Dessert")
     * @param prepTime estimate preparation time in minutes
     */
    public void newMenuItem(String description, double price, String category, int prepTime){
    	Item i = new Item(description, price, category, prepTime);
    	items.add(i);
    }
    
    /**
     * Search for items matching the search string.
     * The items are sorted by category first and then by description.
     * 
     * The format of the items is:
     * <pre>
     * "[CATEGORY] DESCRIPTION : PRICE"
     * </pre>
     * 
     * @param search search string
     * @return list of matching items
     */
    public List<String> findItem(String search){
       return items.stream()  // qayerdan bilyapti "" hammasini chiqarishni?
    		       .filter(i->i.getDescription().toLowerCase().contains(search.toLowerCase()))
    		       .sorted(Comparator.comparing(Item::getCategory).thenComparing(Item::getDescription))
    		       .map(Item::toString)
    		       .collect(Collectors.toList());		    
    }
    
    /**
     * Creates a new order for the given customer.
     * Returns the unique id of the order, a progressive
     * integer number starting at 1.
     * 
     * @param customerId
     * @return order id
     */
    public int newOrder(int customerId){
    	Order o = new Order(customerId);
    	orders.add(o);
    	return orders.size();
    }
    
    /**
     * Add a new item to the order with the given quantity.
     * 
     * If the same item is already present in the order is adds the
     * given quantity to the current quantity.
     * 
     * The method returns the final total quantity of the item in the order. 
     * 
     * The item is found through the search string as in {@link #findItem}.
     * If none or more than one item is matched, then an exception is thrown.
     * 
     * @param orderId the id of the order
     * @param search the item search string
     * @param qty the quantity of the item to be added
     * @return the final quantity of the item in the order
     * @throws DeliveryException in case of non unique match or invalid order ID
     */
    public int addItem(int orderId, String search, int qty) throws DeliveryException {
        if(orders.size() < orderId)throw new DeliveryException();
        List<Item> itemList = items.stream()
        		              .filter(i->i.getDescription().toLowerCase().contains(search.toLowerCase()))
        		              .collect(Collectors.toList());
        
        if(itemList.size() != 1) throw new DeliveryException();
        
        orders.get(orderId-1).addItemToMenu(itemList.get(0),qty);
    	return orders.get(orderId-1).getItems().get(itemList.get(0));
    }
    
    /**
     * Show the items of the order using the following format
     * <pre>
     * "DESCRIPTION, QUANTITY"
     * </pre>
     * 
     * @param orderId the order ID
     * @return the list of items in the order
     * @throws DeliveryException when the order ID in invalid
     */
    public List<String> showOrder(int orderId) throws DeliveryException {
    	if(orderId > orders.size()) throw new DeliveryException();
    	
        return orders.get(orderId - 1).getItems()
        		     .entrySet()
        		     .stream()
        		     .map(o->o.getKey().getDescription()+", "+o.getValue())
        		     .collect(Collectors.toList());
    }
    
    /**
     * Retrieves the total amount of the order.
     * 
     * @param orderId the order ID
     * @return the total amount of the order
     * @throws DeliveryException when the order ID in invalid
     */
    public double totalOrder(int orderId) throws DeliveryException {
        if(orderId > orders.size()) throw new DeliveryException();
        
   
        return orders.get(orderId - 1).TotalOrderPrice();
    
    }
    
    /**
     * Retrieves the status of an order
     * 
     * @param orderId the order ID
     * @return the current status of the order
     * @throws DeliveryException when the id is invalid
     */
    public OrderStatus getStatus(int orderId) throws DeliveryException {
    	if(orderId > orders.size()) throw new DeliveryException();
    	return orders.get(orderId - 1).getStatus();
    }
    
    /**
     * Confirm the order. The status goes from {@code NEW} to {@code CONFIRMED}
     * 
     * Returns the delivery time estimate computed as the sum of:
     * <ul>
     * <li>start-up delay (conventionally 5 min)
     * <li>preparation time (max of all item preparation time)
     * <li>transportation time (conventionally 15 min)
     * </ul>
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code NEW}
     */
    public int confirm(int orderId) throws DeliveryException {
        if(orderId > orders.size()) throw new DeliveryException();
        if(orders.get(orderId - 1).getStatus() != OrderStatus.NEW) throw new DeliveryException();
        
        orders.get(orderId - 1).setStatus(OrderStatus.CONFIRMED);
    	return 5 + orders.get(orderId - 1)
    	                 .getItems()
    	                 .entrySet()
    	                 .stream()
    	                 .mapToInt(t->t.getKey().getPrepTime())
    	                 .max().orElse(0) + 15;
    }

    /**
     * Start the order preparation. The status goes from {@code CONFIRMED} to {@code PREPARATION}
     * 
     * Returns the delivery time estimate computed as the sum of:
     * <ul>
     * <li>preparation time (max of all item preparation time)
     * <li>transportation time (conventionally 15 min)
     * </ul>
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code CONFIRMED}
     */
    public int start(int orderId) throws DeliveryException {
       if(orderId > orders.size()) throw new DeliveryException();
       if(orders.get(orderId - 1).getStatus() != OrderStatus.CONFIRMED) throw new DeliveryException();
       
       orders.get(orderId - 1).setStatus(OrderStatus.PREPARATION);
       return orders.get(orderId - 1).getItems().keySet().stream()
    		        .mapToInt(t->t.getPrepTime())
    		        .max().orElse(0)
    		        + 15;
    }

    /**
     * Begins the order delivery. The status goes from {@code PREPARATION} to {@code ON_DELIVERY}
     * 
     * Returns the delivery time estimate computed as the sum of:
     * <ul>
     * <li>transportation time (conventionally 15 min)
     * </ul>
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code PREPARATION}
     */
    public int deliver(int orderId) throws DeliveryException {
    	if(orderId > orders.size()) throw new DeliveryException();
        if(orders.get(orderId - 1).getStatus() != OrderStatus.PREPARATION) throw new DeliveryException();
        
        orders.get(orderId - 1).setStatus(OrderStatus.ON_DELIVERY);
        
        return 15;
    }
    
    /**
     * Complete the delivery. The status goes from {@code ON_DELIVERY} to {@code DELIVERED}
     * 
     * 
     * @param orderId the order id
     * @return delivery time estimate in minutes
     * @throws DeliveryException when order ID invalid to status not {@code ON_DELIVERY}
     */
    public void complete(int orderId) throws DeliveryException {
    	if(orderId > orders.size()) throw new DeliveryException();
        if(orders.get(orderId - 1).getStatus() != OrderStatus.ON_DELIVERY) throw new DeliveryException();
     
        orders.get(orderId - 1).setStatus(OrderStatus.DELIVERED);
    }
    
    /**
     * Retrieves the total amount for all orders of a customer.
     * 
     * @param customerId the customer ID
     * @return total amount
     */
    public double totalCustomer(int customerId){
        return orders.stream().filter(c->c.getCustomerId() == customerId)
        		     .mapToDouble(o->o.TotalOrderPrice()).sum();
    }
    
    /**
     * Computes the best customers by total amount of orders.
     *  
     * @return the classification
     */
    public SortedMap<Double,List<String>> bestCustomers(){
//    	Map<Customer, Double> cp = orders.stream().collect(Collectors.groupingBy(Order::getCustomer, 
//    			Collectors.summingDouble(Order::getTotalPrice)));
//        return cp.entrySet().stream().collect(Collectors.groupingBy(e->e.getValue(), 
//        		()->new TreeMap<Double,List<String>>(Comparator.reverseOrder()), 
//        		Collectors.mapping(e->e.getKey().toString(), Collectors.toList())));
    	return null;
    }
    /**
     * Computes the best items by total amount of orders.
     *  
     * @return the classification
     */
//    public List<String> bestItems(){
//        return null;
//    }
//    
//    /**
//     * Computes the most popular items by total quantity ordered.
//     *  
//     * @return the classification
//     */
//    public List<String> popularItems(){
//        return null;
//    }

}
