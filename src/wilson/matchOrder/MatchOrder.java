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
	
	//拥有重复posNo的upload单据列表
	List<UploadOrder> samePosUploadOrders = new ArrayList<UploadOrder>();
	//拥有重复posNo的db单据列表
	List<Order> samePosDBOrders = new ArrayList<Order>();
	
	public boolean startMatch(List<Order> dbOrders,List<UploadOrder> uploadOrders){
		boolean flag = false;
		try{
			//从上传列表取得需要匹配的Order
			unMatchedUploadOrders = uploadOrders;
			//从数据库中取到需要匹配的Order
			//unMatchedDBOrders = getUnCheckedDBOrders();
			unMatchedDBOrders = dbOrders;
			
			//取相同pos的列表
			samePosUploadOrders = getSamePosUploadOrdersList(unMatchedUploadOrders);
			samePosDBOrders = getSamePosDBOrdersList(unMatchedDBOrders);
			
			matchOrder(unMatchedUploadOrders,unMatchedDBOrders);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return flag;
	}
	
	private void matchOrder(List <UploadOrder> unCheckedUploadOrders,List <Order> unCheckedDBOrders) {
		AfterMatchOrder amo ;
		Double tempDouble = 0.0;
		
		//先过滤掉大多数posNo相等的
		for(int i = 0 ; i < unCheckedUploadOrders.size(); i ++){
			UploadOrder tempUo = unCheckedUploadOrders.get(i);
			
			for(int j = 0 ; j < unCheckedDBOrders.size() ; j ++){	
				Order tempDBO = unCheckedDBOrders.get(j);
				
				if(tempUo.getPosNo().toUpperCase().equals(tempDBO.getPos().toUpperCase())){
					
					if(tempUo.getPosNo().equals("D01982456")){
						System.out.println("a");
					}
					
					//对比双方都没有重复posno的情况
					if(!samePosDBOrders.contains(tempDBO) && !samePosUploadOrders.contains(tempUo)){
						amo = new AfterMatchOrder(tempUo,tempDBO);
						amo.calcLevel();
						matchedOrders.add(amo);
						unMatchedUploadOrders.remove(tempUo);
						unMatchedDBOrders.remove(tempDBO);
						i --;
						break;
						
						
					//左侧有重复的posno，右侧没有
					}else if(samePosDBOrders.contains(tempDBO) && !samePosUploadOrders.contains(tempUo)){
						
						List<Order> tempList = getSamePosDBOrdersFromList(samePosDBOrders, tempDBO.getPos());		
						tempDouble = 0.0;
						
						//取出comparelevel最大的一对作为第一对
						int maxCompareLevelIterator = 0 ;

						for(int m = 0 ; m <tempList.size() ; m++){
							amo = new AfterMatchOrder(tempUo,tempList.get(m));
							if(amo.calcLevel()>tempDouble){
								tempDouble = amo.calcLevel();
								maxCompareLevelIterator = m;
							}
						}
						
						//第一单
						amo = new AfterMatchOrder(tempUo,tempList.get(maxCompareLevelIterator));
						tempDouble = amo.calcLevel();
						matchedOrders.add(amo);
						unMatchedUploadOrders.remove(tempUo);
						unMatchedDBOrders.remove(tempList.get(maxCompareLevelIterator));
						tempList.remove(maxCompareLevelIterator);
						
						//其他对
						for(int k = 0 ; k < tempList.size() ; k ++){							
							
							//其他单据
							//对应处设置一个新单据与他对应，ID为-1
							UploadOrder o = new UploadOrder();
							o.setId(-1);
							amo = new AfterMatchOrder(o,tempList.get(k));
							amo.setCompareLevel(tempDouble);
							matchedOrders.add(amo);
							unMatchedDBOrders.remove(tempList.get(k));
						}
						i --;
						break;					
						
						
					//右侧有重复的posno，左侧没有	
					}else if(!samePosDBOrders.contains(tempDBO) && samePosUploadOrders.contains(tempUo)){
						
						List<UploadOrder> tempList = getSamePosUploadOrdersFromList(samePosUploadOrders, tempUo.getPosNo());
						tempDouble = 0.0;
										
						//取出comparelevel最大的一对作为第一对
						int maxCompareLevelIterator = 0 ;

						for(int m = 0 ; m <tempList.size() ; m++){
							amo = new AfterMatchOrder(tempList.get(m),tempDBO);
							if(amo.calcLevel()>tempDouble){
								tempDouble = amo.calcLevel();
								maxCompareLevelIterator = m;
							}
						}
						
						//第一单
						amo = new AfterMatchOrder(tempList.get(maxCompareLevelIterator),tempDBO);
						tempDouble = amo.calcLevel();
						matchedOrders.add(amo);
						unMatchedUploadOrders.remove(tempList.get(maxCompareLevelIterator));
						unMatchedDBOrders.remove(tempDBO);
						tempList.remove(maxCompareLevelIterator);
						
						
						for(int k = 0 ; k < tempList.size() ; k ++){

							//其他单
							//对应处设置一个新单据与他对应，ID为-1
							Order o = new Order();
							o.setId(-1);
							amo = new AfterMatchOrder(tempList.get(k),o);
							amo.setCompareLevel(tempDouble);
							matchedOrders.add(amo);
							unMatchedUploadOrders.remove(tempList.get(k));
						}
						i --;
						break;	
						
						
					//左右都有重复的posNo的情况
					}else if(samePosDBOrders.contains(tempDBO) && samePosUploadOrders.contains(tempUo)){
						
						
						List<Order> tempDBOrder = getSamePosDBOrdersFromList(samePosDBOrders, tempDBO.getPos());
						List<UploadOrder> tempUploadOrder = getSamePosUploadOrdersFromList(samePosUploadOrders, tempUo.getPosNo());
						tempDouble = 0.0;
						
						//取出comparelevel最大的一对作为第一对
						int maxCompareLevelIteratorDB = 0 ;
						int maxCompareLevelIteratorUpload = 0 ;

						for(int m = 0 ; m <tempDBOrder.size() ; m++){
							for(int n = 0 ; n < tempUploadOrder.size() ; n ++){
								
								amo = new AfterMatchOrder(tempUploadOrder.get(n),tempDBOrder.get(m));
								if(amo.calcLevel()>tempDouble){
									tempDouble = amo.calcLevel();
									maxCompareLevelIteratorDB = m;
									maxCompareLevelIteratorUpload = n;
								}
								
							}
						}
						
						
						//第一单
						amo = new AfterMatchOrder(tempUploadOrder.get(maxCompareLevelIteratorUpload),tempDBOrder.get(maxCompareLevelIteratorDB));
						tempDouble = amo.calcLevel();
						matchedOrders.add(amo);
						unMatchedUploadOrders.remove(tempUploadOrder.get(maxCompareLevelIteratorUpload));
						unMatchedDBOrders.remove(tempDBOrder.get(maxCompareLevelIteratorDB));
						tempDBOrder.remove(maxCompareLevelIteratorDB);
						tempUploadOrder.remove(maxCompareLevelIteratorUpload);
							
						
						
						for(int k = 0;;){				
							
							//其他单
							//对应处设置一个新单据与他对应，ID为-1
							if(k >= tempDBOrder.size()){
								tempDBO = new Order();
								tempDBO.setId(-1);
							}else{
								tempDBO = tempDBOrder.get(k);
								unMatchedDBOrders.remove(tempDBO);
							}
							
							if(k >= tempUploadOrder.size()){
								tempUo = new UploadOrder();
								tempUo.setId(-1);
							}else{
								tempUo = tempUploadOrder.get(k);
								unMatchedUploadOrders.remove(tempUo);
							}
							amo = new AfterMatchOrder(tempUo,tempDBO);
							amo.setCompareLevel(tempDouble);
							matchedOrders.add(amo);	
							
							k++;
							if( k>= tempDBOrder.size() && k >= tempUploadOrder.size()){
								break;
							}
						}
						i --;
						break;	
					}
					
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
	
	public List<UploadOrder> getSamePosUploadOrdersList(List<UploadOrder> uploadOrders){
		List<UploadOrder> result = new ArrayList<UploadOrder>(); 
		String tempPos = "";
		for(int i = 0 ; i < uploadOrders.size() ; i ++){
			tempPos = uploadOrders.get(i).getPosNo();
			for(int j = i + 1 ; j < uploadOrders.size() ; j++){
				if(tempPos.equals(uploadOrders.get(j).getPosNo())){
					result.add(uploadOrders.get(i));
					result.add(uploadOrders.get(j));
					break;
				}
			}
		}
		return result;
	}
	
	public List<Order> getSamePosDBOrdersList(List<Order> DBOrders){
		List<Order> result = new ArrayList<Order>(); 
		String tempPos = "";
		for(int i = 0 ; i < DBOrders.size() ; i ++){
			tempPos = DBOrders.get(i).getPos();
			for(int j = i + 1 ; j < DBOrders.size() ; j++){
				if(tempPos.equals(DBOrders.get(j).getPos())){
					result.add(DBOrders.get(i));
					result.add(DBOrders.get(j));
					break;
				}
			}
		}
		return result;
	}
	
	public List <Order> getSamePosDBOrdersFromList(List<Order> DBOrders,String pos){
		List <Order> result = new ArrayList<Order>();
		for(int i = 0 ; i < DBOrders.size() ; i++){
			if(DBOrders.get(i).getPos().equals(pos)){
				result.add(DBOrders.get(i));
			}
		}
		return result;
	}
	
	public List<UploadOrder> getSamePosUploadOrdersFromList(List<UploadOrder> uploadOrder,String pos){
		List<UploadOrder> result = new ArrayList<UploadOrder>();
		for(int i = 0 ; i < uploadOrder.size() ; i++){
			if(uploadOrder.get(i).getPosNo().equals(pos)){
				result.add(uploadOrder.get(i));
			}
		}
		return result;
	}
	
}
