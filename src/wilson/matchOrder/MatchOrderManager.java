package wilson.matchOrder;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import order.OrderManager;
import wilson.upload.UploadOrder;
import wilson.upload.UploadOrderManager;

public class MatchOrderManager {

	public void saveOrder(List<Order> ordersUnMatched) {
		// TODO Auto-generated method stub
		
	}
	
	public static List <UploadOrder> getUnCheckedUploadOrders(){
		List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
		unCheckedUploadOrders = UploadOrderManager.getUnCheckedUploadOrders();
		if(unCheckedUploadOrders != null && unCheckedUploadOrders.size() >= 0){
			return unCheckedUploadOrders;
		}else{
			return null;
		}
	}
	
	public static List<Order> getUnCheckedDBOrders(){
		List <Order> unCheckedDBOrders = new ArrayList<Order>();
		unCheckedDBOrders = OrderManager.getUnCheckedDBOrders();
		if(unCheckedDBOrders != null && unCheckedDBOrders.size() >= 0){
			return unCheckedDBOrders;
		}else{
			return null;
		}
	}
	
	public static boolean checkOrder(int DBOrderID,int UploadOrderID){
		//消除DB中的Order
		//a.b();
		
		
		//消除Upload中的Order
		if(UploadOrderManager.checkOrder(UploadOrderID, DBOrderID)){
			return true;
		}

		return false;
	}
	
	//接受{"1,2","2,3"}类型的ID输入,前一个是DB的Order，后一个是upload的Order哦~
	public static boolean checkOrder(String[] idString){
		
		//消除Upload中的Order和消除DB中的Order(还没有添加完)
		if(UploadOrderManager.checkOrder(idString)){
			return true;
		}
		
		return false;
	}

}
