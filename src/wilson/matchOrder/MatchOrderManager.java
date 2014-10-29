package wilson.matchOrder;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import order.OrderManager;
import wilson.upload.UploadOrder;
import wilson.upload.UploadManager;

public class MatchOrderManager {
	
	public static List <UploadOrder> getUnCheckedUploadOrders(){
		List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
		unCheckedUploadOrders = UploadManager.getUnCheckedUploadOrders();
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
		//对照好了消除
		//OrderManager.updateStatues("orderCharge",Order.query, String.valueOf(DBOrderID)); 
		//消除Upload中的Order
		if(UploadManager.checkOrder(UploadOrderID,DBOrderID )){
			return true;
		}

		return false;
	}
	
	//接受{"1,2","2,3"}类型的ID输入,前一个是DB的Order，后一个是upload的Order哦~
	public static boolean checkOrder(String[] idString){
//		int DBOrderID = 0;
//		int UploadOrderID = 0;
//		for(int i = 0 ; i < idString.length ; i ++){
//			DBOrderID = Integer.parseInt(idString[i].split(",")[0]);
//			UploadOrderID = Integer.parseInt(idString[i].split(",")[1]);
//			
//		}
		
		return UploadManager.checkOrder(idString);
	}
	
	public static boolean checkDBOrder(int DBOrderID){
		
		if(UploadManager.checkDBOrder(DBOrderID )){
			return true;
		}

		return false;
	}
	
	public static boolean checkUploadOrder(int UploadOrderID){

		if(UploadManager.checkUploadOrder(UploadOrderID)){
			return true;
		}

		return false;
	}
	
	public static boolean checkDBOrder(String dbOrderIdStrList){
		
		if(UploadManager.checkDBOrderStrList(dbOrderIdStrList)){
			return true;
		}

		return false;
	}
	
	public static boolean checkUploadOrder(String uploadOrderIdStrList){

		if(UploadManager.checkUploadOrderStrList(uploadOrderIdStrList)){
			return true;
		}

		return false;
	}

}
