package wilson.matchOrder;

import java.util.List;

import order.Order;

public class TestMatchOrder {
	public static void main(String[] args) {
//		MatchOrder mo = new MatchOrder();
//		List <Order> ll = mo.getOrdersFromDB();
//		for(int i=0 ; i< ll.size();i++){
//			System.out.println(ll.get(i).getPos());
//		}
//		System.out.println(ll.size());
		for(int i = 0 ; i < 5 ; i ++){
			for(int j = 0 ; j < 5 ; j ++){
				if(j == 2 ){
					continue;
				}
				System.out.println(" j = " + j);
			}
			System.out.println("i = " + i);
		}
		
	}
}