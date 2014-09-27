package wilson.matchOrder;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import wilson.upload.UploadOrder;

public class TestMatchOrder {
	public static void main(String[] args) {
//		MatchOrder mo = new MatchOrder();
//		List <Order> ll = mo.getOrdersFromDB();
//		for(int i=0 ; i< ll.size();i++){
//			System.out.println(ll.get(i).getPos());
//		}
//		System.out.println(ll.size());
		List<String> a = new ArrayList<String>();
		a.add("a1");
		a.add("a2");
		a.add("a3");
		List<String> b = new ArrayList<String>();
		b.add("b1");
		b.add("a2");
		b.add("b3");
		for(int i = 0 ; i < a.size(); i ++){
			System.out.println("外层循环次数" + i + "次数a.size=" + a.size());
			String tempUo = a.get(i);
			
			for(int j = 0 ; j < b.size() ; j ++ ){
				System.out.println("内层循环次数" + i + "次数b.size=" + b.size());
				String tempDBO = b.get(j);
				
				if(tempUo.equals(tempDBO)){
					b.remove(tempDBO);
					a.remove(tempUo);
					break;
				}
				continue;
			}
		}
		
	}
	
	
}