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
		boolean flag = false;
		try{
			//从上传列表取得需要匹配的Order
			unMatchedUploadOrders = getUnCheckedUploadOrders();
			//从数据库中取到需要匹配的Order
			unMatchedDBOrders = getUnCheckedDBOrders();		
			matchOrder(unMatchedUploadOrders,unMatchedDBOrders);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return flag;
	}
	
	private void matchOrder(List <UploadOrder> unCheckedUploadOrders,List <Order> unCheckedDBOrders) {
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

	}
	
	//模糊对比，如果认为对比成功则返回true，否则返回false
	private boolean fuzzyCompare(UploadOrder tempUo, Order tempDBO) {
		boolean flag = false;
		AfterMatchOrder amo ;
		//如果销售时间相同
		if(tempDBO.getSaleTime().replace("-", "").equals(tempUo.getSaleTime())){
			//而且票面数量一样
			if(String.valueOf(tempUo.getNum()).equals(tempDBO.getSendCount())){
				//而且销售门店一样
				if(tempUo.getShop().contains(tempDBO.getBranch().replace("苏宁", "").replace("店", ""))){
					//而且型号一样
					if(tempUo.getType().replaceAll("(\\s[\u4E00-\u9FA5]+)|([\u4E00-\u9FA5]+\\s)", "").equals(tempDBO.getSendType().replaceAll("(\\s[\u4E00-\u9FA5]+)|([\u4E00-\u9FA5]+\\s)", ""))){
						amo = new AfterMatchOrder(tempUo,tempDBO);
						amo.calcLevel();
						matchedOrders.add(amo);
						unMatchedUploadOrders.remove(tempUo);
						unMatchedDBOrders.remove(tempDBO);
						return true;
					}
				}
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

	public List<UploadOrder> getUnMatchedUploadOrders() {
		return unMatchedUploadOrders;
	}

	public List<Order> getUnMatchedDBOrders() {
		return unMatchedDBOrders;
	}

	public List<AfterMatchOrder> getMatchedOrders() {
		return matchedOrders;
	}
}
