package wilson.matchOrder;

import java.util.List;

import order.Order;

public class TestMatchOrder {
	public static void main(String[] args) {
		MatchOrder mo = new MatchOrder();
		List <Order> ll = mo.getOrdersFromDB();
		for(int i=0 ; i< ll.size();i++){
			System.out.println(ll.get(i).getPrintlnid());
		}
		System.out.println(ll.size());
		
	}
}