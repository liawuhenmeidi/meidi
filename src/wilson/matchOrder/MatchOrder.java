package wilson.matchOrder;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import wilson.upload.UploadOrder;

public class MatchOrder {
	
	//来自上传的匹配失败列表
	List<UploadOrder> unMatchedUploadOrders = new ArrayList<UploadOrder>();
	//来自DB的匹配失败列表
	List<Order> unMatchedDBOrders = new ArrayList<Order>();
	//匹配成功列表
	List<AfterMatchOrder> matchedOrders = new ArrayList<AfterMatchOrder>();
	
	
	public boolean startMatch(){
		//从上传列表取得需要匹配的Order
		List <UploadOrder> unCheckedUploadOrders = getUnCheckedUploadOrders();
		//从数据库中取到需要匹配的Order
		List <Order> unCheckedDBOrders = getUnCheckedDBOrders();		
		return matchOrder(unCheckedUploadOrders,unCheckedDBOrders);
	}
	
	private boolean matchOrder(List <UploadOrder> unCheckedUploadOrders,List <Order> unCheckedDBOrders) {
		boolean flag = false;
		//AfterMatchOrder amo = new AfterMatchOrder();
		
		
		for(int i = 0 ; i < unCheckedUploadOrders.size(); i ++){
			UploadOrder tempUo = unCheckedUploadOrders.get(i);
			
			for(int j = 0 ; j < unCheckedDBOrders.size() ; j ++ ){
				
				Order tempDBO = unCheckedDBOrders.get(j);
				
			}
		}
		return flag;
	}

	public static List<Order> getUnCheckedDBOrders(){
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
	
	public void HighLighter(String inputString,int start,int end){
		String a = "<redTag class=\"style\">red</redTag>black";
	}
}
