package main;

import java.util.List;

import delivery.Delivery;
import delivery.DeliveryException;

public class MainClass {

    public static void main(String[] args) throws DeliveryException {
        Delivery ds = new Delivery();
//R1
        int id1 = ds.newCustomer("Jon Snow", "1 Castle Black", "+0 610 555 555", "jon@night.watch.org");
        
        System.out.println(2);
        System.out.println(ds.customerInfo(2));
        List<String> l = ds.listCustomers();
        System.out.println(l);
        System.out.println(ds.findItem(""));
        
        ds.newMenuItem("Hamburger", 5.00, "Fastfood", 10);
        ds.newMenuItem("Cheeseburger", 5.50, "Fastfood", 10);
        ds.newMenuItem("Fries", 1.50, "Side", 16);        
        System.out.println(ds.findItem(""));
    // [[Fastfood] Cheeseburger : 5.50, [Fastfood] Hamburger : 5.00, [Side] Fries : 1.50]
//R2
        int idOrder1 = ds.newOrder(id1);
        try{
            ds.addItem(idOrder1, "burger", 1);
        }catch(DeliveryException e){
            System.out.println("Cannot add 'burger' since it is ambiguous.");
        }
        
        ds.addItem(idOrder1, "Hamburger", 1);
        ds.addItem(idOrder1, "Cheeseburger", 2);
        ds.addItem(idOrder1, "Fries", 3);
       // ds.addItem(idOrder2, "Hamburger", 3);
        
        System.out.println(ds.showOrder(idOrder1));
        // [Hamburger, 1, Cheeseburger, 2, Fries, 3]
        
        
       
        ds.confirm(idOrder1);
        
        System.out.println(String.format("%.2f",ds.totalOrder(idOrder1))); // 20.50

         
        ds.start(idOrder1);
        ds.deliver(idOrder1);
        
        System.out.println(ds.getStatus(idOrder1)); // ON_DELIVERY
        
        ds.complete(idOrder1);
        
        System.out.println(String.format("%.2f",ds.totalCustomer(id1))); // 20.50
        
        System.out.println(ds.bestCustomers());
        // {20.5=[Jon Snow, 1 Castle Black, +0 610 555 555, jon@night.watch.org]}

    }

}
