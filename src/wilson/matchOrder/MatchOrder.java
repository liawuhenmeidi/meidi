package wilson.matchOrder;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import wilson.upload.UploadOrder;

public class MatchOrder {
	
	//匹配成功列表
	List<Order> ordersMatched = new ArrayList<Order>();
	//匹配失败列表
	List<Order> ordersUnMatched = new ArrayList<Order>();
	
	MatchOrderManager mom = new MatchOrderManager();
	
	public boolean startMatch(){
		
		//从数据库中取到需要匹配的Order
		List <Order> unCheckedDBOrders = getOrdersFromDB();
		//从上传列表取得需要匹配的Order
		List <UploadOrder> unCheckedUploadOrders = getUnCheckedUploadOrders();
		
		//matchOrder(ordersFromDB,ordersFromUpload);
		
		//mom.saveOrder(this.getOrdersMatched());
		//mom.saveOrder(this.getOrdersUnMatched());
		
		return false;
	}
	
	private void matchOrder(List <Order> ordersFromDB,List <Order> ordersFromUpload) {
		//set
		//set
	}

	public static List<Order> getOrdersFromDB(){
		List <Order> unCheckedDBOrders = new ArrayList<Order>();
		unCheckedDBOrders = MatchOrderManager.getUnCheckedDBOrders();
		return unCheckedDBOrders;
		//对照好了消除
		//int statues = OrderManager.updateStatues(user,"orderCharge",Order.query, id); 
	}
	
	public static List<UploadOrder> getUnCheckedUploadOrders(){
		List<UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
		unCheckedUploadOrders = MatchOrderManager.getUnCheckedUploadOrders();
		return unCheckedUploadOrders;
	}
	
}
