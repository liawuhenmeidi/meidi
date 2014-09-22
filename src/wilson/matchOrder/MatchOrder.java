package wilson.matchOrder;

import group.Group;

import java.util.ArrayList;
import java.util.List;
import order.*;
import user.User;

public class MatchOrder {
	
	//匹配成功列表
	List<Order> ordersMatched = new ArrayList<Order>();
	//匹配失败列表
	List<Order> ordersUnMatched = new ArrayList<Order>();
	
	MatchOrderManager mom = new MatchOrderManager();
	
	public boolean startMatch(){
		
		//从数据库中取到需要匹配的Order
		List <Order> ordersFromDB = getOrdersFromDB();
		//从上传列表取得需要匹配的Order
		List <Order> ordersFromUpload = getOrdersFromUpload ();
		
		matchOrder(ordersFromDB,ordersFromUpload);
		
		mom.saveOrder(this.getOrdersMatched());
		mom.saveOrder(this.getOrdersUnMatched());
		
		return false;
	}
	
	private void matchOrder(List <Order> ordersFromDB,List <Order> ordersFromUpload) {
		//set
		//set
	}

	public List<Order> getOrdersFromDB(){
		List <Order> ordersFromDB = new ArrayList<Order>();
		
		ordersFromDB = OrderManager.getOrderlist(new User());
		
		
		//对照好了消除
		//int statues = OrderManager.updateStatues(user,"orderCharge",Order.query, id); 
		/**
		 * User user = (User)session.getAttribute("user");
		 * 
		 */
		
		return ordersFromDB;
	}
	
	public List<Order> getOrdersFromUpload(){
		List <Order> ordersFromDB = new ArrayList<Order>();
		return ordersFromDB;
	}
	
	public List<Order> getOrdersMatched() {
		return ordersMatched;
	}

	public void setOrdersMatched(List<Order> ordersMatched) {
		this.ordersMatched = ordersMatched;
	}

	public List<Order> getOrdersUnMatched() {
		return ordersUnMatched;
	}

	public void setOrdersUnMatched(List<Order> ordersUnMatched) {
		this.ordersUnMatched = ordersUnMatched;
	}
}
