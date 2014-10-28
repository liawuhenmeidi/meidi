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
	
	
	public boolean startMatch(List<Order> dbOrders,List<UploadOrder> uploadOrders){
		boolean flag = false;
		try{
			//从上传列表取得需要匹配的Order
			unMatchedUploadOrders = uploadOrders;
			//从数据库中取到需要匹配的Order
			//unMatchedDBOrders = getUnCheckedDBOrders();
			unMatchedDBOrders = dbOrders;
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
				
				if(tempUo.getPosNo().toUpperCase().equals(tempDBO.getPos().toUpperCase())){
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
		String key = "";
		
		if(tempDBO.getPos().equals("D01949302")  && tempUo.getPosNo().equals("D01851930")){
			System.out.println("a");
		}
			
			
		int level = 0 ; //相似等级 2项相同，就给弄一起吧
		//如果销售时间相同
		if(tempDBO.getSaleTime().replace("-", "").equals(tempUo.getSaleTime())){
			level += 1;
			
		}
		
		//而且票面数量一样
		if(String.valueOf(tempUo.getNum()).replace("|", "").equals(tempDBO.getSendCount().replace("|", ""))){
			level += 1;
		}
		
		//而且销售门店一样
		key = tempDBO.getbranchName(tempDBO.getBranch()).replace("苏宁", "").replace("店", "");
		if(tempUo.getShop().contains(key)){
			level += 1;
		}
		
		//而且型号一样
		key = tempUo.getType().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "");
		
		if(tempDBO.getSendType().contains(key)){
			level += 1;
		}
		
		if(level >= 2){
			flag = true;
		}			
			
		return flag;
	}

	public static List<Order> getUnCheckedDBOrders(){
		List <Order> unCheckedDBOrders = new ArrayList<Order>();
		unCheckedDBOrders = MatchOrderManager.getUnCheckedDBOrders();
		return unCheckedDBOrders;
		
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
