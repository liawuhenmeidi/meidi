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
		AfterMatchOrder amo ;
		
		//先过滤掉大多数posNo相等的
		for(int i = 0 ; i < unCheckedUploadOrders.size(); i ++){
			UploadOrder tempUo = unCheckedUploadOrders.get(i);
			
			for(int j = 0 ; j < unCheckedDBOrders.size() ; j ++){	
				Order tempDBO = unCheckedDBOrders.get(j);
				
				if(tempUo.getPosNo().equals(tempDBO.getPos())){
					amo = new AfterMatchOrder(tempUo,tempDBO);
					amo.calcLevel();
					matchedOrders.add(amo);
					unMatchedUploadOrders.remove(tempUo);
					unMatchedDBOrders.remove(tempDBO);
					i --;
					break;
				}
			}
		}
		
		//再用模糊对比进行过滤(日期相同，型号，数量和门店对的上的)
		for(int i = 0 ; i < unCheckedUploadOrders.size(); i ++){
			UploadOrder tempUo = unCheckedUploadOrders.get(i);
			
			for(int j = 0 ; j < unCheckedDBOrders.size() ; j ++){	
				Order tempDBO = unCheckedDBOrders.get(j);
				
				if(fuzzyCompare(tempUo,tempDBO)){
					amo = new AfterMatchOrder(tempUo,tempDBO);
					amo.calcLevel();
					matchedOrders.add(amo);
					unMatchedUploadOrders.remove(tempUo);
					unMatchedDBOrders.remove(tempDBO);
					i --;
					break;
				}
			}
		}
		return flag;
	}
	
	//模糊对比，如果认为对比成功则返回true，否则返回false
	private boolean fuzzyCompare(UploadOrder tempUo, Order tempDBO) {
		// TODO Auto-generated method stub
		return false;
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
}
