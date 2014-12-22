package wilson.matchOrder;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import utill.StringCompare;
import utill.StringUtill;
import wilson.upload.UploadOrder;

public class MatchOrder {
	
	//店名匹配等级
	public static final Double SHOPNAME_DISTANCE = 0.19999;
	
	//匹配的总个数
	public static final int MATCH_NUM = 5;
	
	//POS单号一样的时候的order的id
	public static final int SAME_POS_ID = -2;
	
	//这次匹配的个数
	private int MATCH_NUM_THISTIME = 5;
	
	//传入的MatchPara中各个位代表的意义
	//销售门店
	public static final int MATCHPARA_SHOP = 0;
	//pos(厂送)单号	
	public static final int MATCHPARA_POS = 1;
	//销售日期
	public static final int MATCHPARA_SALETIME = 2;
	//票面型号
	public static final int MATCHPARA_TYPE = 3;
	//票面数量	
	public static final int MATCHPARA_NUM = 4;
	
	
	//来自上传的匹配失败列表
	List<UploadOrder> unMatchedUploadOrders = new ArrayList<UploadOrder>();
	//来自DB的匹配失败列表
	List<Order> unMatchedDBOrders = new ArrayList<Order>();
	//匹配成功列表
	List<AfterMatchOrder> matchedOrders = new ArrayList<AfterMatchOrder>();
	
	//拥有重复posNo的upload单据列表
	//List<UploadOrder> samePosUploadOrders = new ArrayList<UploadOrder>();
	//拥有重复posNo的db单据列表
	List<Order> samePosDBOrders = new ArrayList<Order>();
	
	public boolean startMatch(List<Order> dbOrders,List<UploadOrder> uploadOrders,String matchPara){
		unMatchedUploadOrders = new ArrayList<UploadOrder>();
		unMatchedDBOrders = new ArrayList<Order>();
		matchedOrders = new ArrayList<AfterMatchOrder>();
		MATCH_NUM_THISTIME = getRequeredLevel(matchPara);
		
		boolean flag = false;

		
		try{
	
			//取两个源数据中,店名相似度最高的位置i。如14,23
			String position = "";
			
			//临时使用的匹配用列表容器
			List<Order> tempDBOrderList = new ArrayList<Order>();
			List<UploadOrder> tempUploadOrderList = new ArrayList<UploadOrder>();
					
			while(dbOrders.size()> 0 ){
				//循环前，清空变量
				position = "";
				tempDBOrderList = new ArrayList<Order>();
				tempUploadOrderList = new ArrayList<UploadOrder>();
				
				
				//取两个源数据中,店名相似度最高的位置i。如14,23
				position = getSimilarPosFromList(dbOrders,uploadOrders);
				
				
				//出问题了!!
				if(position.equals("")){
					break;
				}
				
				
				//根据位置取出对应要匹配的列表
				
				tempDBOrderList = getSameShopNameSubListFromdbOrders(dbOrders,position.split(",")[0]);   
				tempUploadOrderList = getSameShopNameSubListFromUploadOrders(uploadOrders,position.split(",")[1]);
				
				//赋值本轮需要进行匹配的双方列表
				unMatchedUploadOrders = tempUploadOrderList;
				unMatchedDBOrders = tempDBOrderList;
				//取相同pos的列表
				//如果开启则右侧也会处理相同POS单号的情况
				//samePosUploadOrders = getSamePosUploadOrdersList(unMatchedUploadOrders);
				samePosDBOrders = getSamePosDBOrdersList(unMatchedDBOrders);
				matchOrder(unMatchedUploadOrders,unMatchedDBOrders,matchPara);
			}
			
			while(uploadOrders.size() > 0 ){
				Order o = new Order();
				o.setId(-1);
				AfterMatchOrder tempAmo = new AfterMatchOrder(uploadOrders.get(0),o);
				matchedOrders.add(tempAmo);
				uploadOrders.remove(0);
			}
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return flag;
	}
	
	private List<UploadOrder> getSameShopNameSubListFromUploadOrders(
			List<UploadOrder> uploadOrders, String index) {
		
		
		List<UploadOrder> result = new ArrayList<UploadOrder>();
		int i = Integer.parseInt(index);
		if(i >= 0){
			String shopName = uploadOrders.get(i).getShop();
			while(uploadOrders.get(i).getShop().equals(shopName)){
				result.add(uploadOrders.get(i));
				uploadOrders.remove(uploadOrders.get(i));
				if(i >= uploadOrders.size()){
					break;
				}
			}
			
		}else{
			//相当与返回空
		}
		
		return result;
	}

	private List<Order> getSameShopNameSubListFromdbOrders(
			List<Order> dbOrders, String index) {
		List<Order> result = new ArrayList<Order>();
		int i = Integer.parseInt(index);
		if(i >= 0 ){
			String shopName = dbOrders.get(i).getShopNameForCompare();
			while(dbOrders.get(i).getShopNameForCompare().equals(shopName)){
				result.add(dbOrders.get(i));
				dbOrders.remove(dbOrders.get(i));
				if(i >= dbOrders.size()){
					break;
				}
			}
		}else{
			//相当与返回空
		}
		
		return result;
	}

	private String getSimilarPosFromList(List<Order> dbOrders,
			List<UploadOrder> uploadOrders) {
		String result = "";
		
		//取不重复的店名的list
		List<String> dbOrderShopNames = getShopNamesFromdbOrderList(dbOrders);
		List<String> uploadOrderShopNames = getShopNamesFromUploadOrderList(uploadOrders);
		double distance = 0.0;
		String resultDBShopName = "";
		String resultUploadShopName="";
		
		double similarityRatio = 0.0;
		
		//找出相似度最高的店名组合
		for(int i = 0 ; i < dbOrderShopNames.size() ; i ++){
			for(int j = 0 ; j < uploadOrderShopNames.size() ; j ++){
				similarityRatio = (double)StringCompare.getSimilarityRatio(dbOrderShopNames.get(i), uploadOrderShopNames.get(j));
				if(similarityRatio > distance){
					distance = similarityRatio;
					resultDBShopName = dbOrderShopNames.get(i);
					resultUploadShopName = uploadOrderShopNames.get(j);
				}
			}
		}

		if(resultDBShopName.equals("")){
			resultDBShopName = dbOrderShopNames.get(0);
		}
		
		//找出这个组合对应在DBList中的开始位置
		for(int i = 0 ; i < dbOrders.size() ; i ++){
			if(dbOrders.get(i).getShopNameForCompare().equals(resultDBShopName)){
				result += String.valueOf(i) + ",";
				break;
			}
		}
		
		//没找到?
		if(!result.endsWith(",")){
			return "";
		}
		
		if(distance < MatchOrder.SHOPNAME_DISTANCE){
			return result + "-1";
		}
		
		//找出这个组合对应在UploadList中的开始位置
		for(int i = 0 ; i < uploadOrders.size() ; i ++){
			if(uploadOrders.get(i).getShop().equals(resultUploadShopName)){
				result += String.valueOf(i);
				break;
			}
		}
		
		//没找到?
		if(result.endsWith(",")){
			return "";
		}
		
		return result;
	}

	private List<String> getShopNamesFromUploadOrderList(
			List<UploadOrder> uploadOrders) {
		List<String> result = new ArrayList<String>();
		String tempName = "";
		for(int i = 0  ; i < uploadOrders.size() ; i ++){
			if(!uploadOrders.get(i).getShop().equals(tempName)){
				tempName = uploadOrders.get(i).getShop();
				result.add(tempName);
			}
		}
		return result;
	}

	private List<String> getShopNamesFromdbOrderList(List<Order> dbOrders) {
		List<String> result = new ArrayList<String>();
		String tempName = "";
		for(int i = 0  ; i < dbOrders.size() ; i ++){
			if(!dbOrders.get(i).getShopNameForCompare().equals(tempName)){
				tempName = dbOrders.get(i).getShopNameForCompare();
				result.add(tempName);
			}
		}
		return result;
	}

	private void matchOrder(List <UploadOrder> unCheckedUploadOrders,List <Order> unCheckedDBOrders,String matchPara) {
		AfterMatchOrder amo ;
		Double tempDouble = 0.0;
		int tempInt = 0 ;
		
		if(matchPara.charAt(MATCHPARA_POS) == '1'){
			//先过滤掉大多数posNo相等的
			for(int i = 0 ; i < unCheckedUploadOrders.size(); i ++){
				
				UploadOrder tempUo = unCheckedUploadOrders.get(i);

				for(int j = 0 ; j < unCheckedDBOrders.size() ; j ++){	
					Order tempDBO = unCheckedDBOrders.get(j);
					
					if(tempUo.getPosNo().toUpperCase().trim().equals(tempDBO.getPos().toUpperCase().trim())){
						
						//对比双方都没有重复posno的情况
						if(!samePosDBOrders.contains(tempDBO)){
							amo = new AfterMatchOrder(tempUo,tempDBO);
							if(amo.calcLevel(matchPara) == MATCH_NUM_THISTIME){
								matchedOrders.add(amo);
								
								unCheckedUploadOrders.remove(tempUo);
								unCheckedDBOrders.remove(tempDBO);
								unMatchedUploadOrders.remove(tempUo);
								unMatchedDBOrders.remove(tempDBO);
								
								i --;
							}
							break;
							
							
						//左侧有重复的posno，右侧没有
						}else if(samePosDBOrders.contains(tempDBO)){
							
							List<Order> tempList = getSamePosDBOrdersFromList(samePosDBOrders, tempDBO.getPos());		
							tempDouble = 0.0;
							
							//取出comparelevel最大的一对作为第一对
							int maxCompareLevelIterator = 0 ;

							for(int m = 0 ; m <tempList.size() ; m++){
								amo = new AfterMatchOrder(tempUo,tempList.get(m));
								if(amo.calcLevel(matchPara)>tempDouble){
									tempDouble = amo.calcLevel(matchPara);
									maxCompareLevelIterator = m;
								}
							}
							
							//第一单
							amo = new AfterMatchOrder(tempUo,tempList.get(maxCompareLevelIterator));
							tempDouble = amo.calcLevel(matchPara);
							//为了让前台不勾选这个
							amo.setCompareLevel(0.0);
							matchedOrders.add(amo);
							
							unMatchedUploadOrders.remove(tempUo);
							unMatchedDBOrders.remove(tempList.get(maxCompareLevelIterator));
							unCheckedUploadOrders.remove(tempUo);
							unCheckedDBOrders.remove(tempList.get(maxCompareLevelIterator));
							
							tempList.remove(maxCompareLevelIterator);
							
							//其他对
							for(int k = 0 ; k < tempList.size() ; k ++){							
								
								//其他单据
								//对应处设置一个新单据与他对应，ID为-2
								UploadOrder o = new UploadOrder();
								o.setId(MatchOrder.SAME_POS_ID);
								amo = new AfterMatchOrder(o,tempList.get(k));
								//amo.setCompareLevel(tempDouble);
								matchedOrders.add(amo);
								
								unMatchedDBOrders.remove(tempList.get(k));
								unCheckedDBOrders.remove(tempList.get(k));
							}
							i --;
							break;					
							
						}
						
					}
				}
			}
		}
		
		
		//最高对比等级
		int requeredLevel = MATCH_NUM_THISTIME;
		//最低对比等级
		int minRequeredLevel = 1;
		
		
		
		//再用模糊对比进行过滤(日期相同，型号，数量和门店对的上的)
		for(int i = 0 ; i < unCheckedUploadOrders.size(); i ++){
			
			UploadOrder tempUo = unCheckedUploadOrders.get(i);
			
			//最优compareLevel
			tempInt = 0;
			//最优结果
			AfterMatchOrder tempAfterMatchOrder = new AfterMatchOrder();
			//是否找到
			boolean find = false;
			
			
			for(int j = 0 ; j < unCheckedDBOrders.size() ; j ++){	
				Order tempDBO = unCheckedDBOrders.get(j);		
				
				
				int level = fuzzyCompare(tempUo,tempDBO,matchPara);
				
				if(level == requeredLevel ){				
					find = true;
					amo = new AfterMatchOrder(tempUo,tempDBO);
					amo.calcLevel(matchPara);
					if(amo.getCompareResult() > tempInt){
						tempAfterMatchOrder = amo;
						tempInt = amo.getCompareResult();
					}
				}
			}
			
			
			if(find){
				matchedOrders.add(tempAfterMatchOrder);
				
				unMatchedUploadOrders.remove(tempAfterMatchOrder.getUploadOrder());
				unMatchedDBOrders.remove(tempAfterMatchOrder.getDBOrder());
				unCheckedUploadOrders.remove(tempAfterMatchOrder.getUploadOrder());
				unCheckedDBOrders.remove(tempAfterMatchOrder.getDBOrder());
				
				find = false;
				i --;
			}
			
			if(i == unCheckedUploadOrders.size() - 1 ){
				if(requeredLevel != minRequeredLevel){
					requeredLevel -= 1;
					i = -1;
				}
			}
			
		}
		
		
		
		//去除两边没有匹配的项
		for(int i = 0 ; ;){
			
			if(i < unMatchedDBOrders.size()){
				UploadOrder uo = new UploadOrder();
				uo.setId(-1);
				AfterMatchOrder tempAmo = new AfterMatchOrder(uo,unMatchedDBOrders.get(i));
				matchedOrders.add(tempAmo);
			}
			
			if(i < unMatchedUploadOrders.size()){
				Order o = new Order();
				o.setId(-1);
				AfterMatchOrder tempAmo = new AfterMatchOrder(unMatchedUploadOrders.get(i),o);
				matchedOrders.add(tempAmo);
			}
			
			i ++;
			if(i >= unMatchedDBOrders.size() && i>= unMatchedUploadOrders.size()){
				unMatchedDBOrders.clear();
				unMatchedDBOrders = new ArrayList<Order>();
				unMatchedUploadOrders.clear();
				unMatchedUploadOrders = new ArrayList<UploadOrder>();
				break;
			}
			
		}
	}
	
	public  static int getRequeredLevel(String matchPara) {
		int result = MatchOrder.MATCH_NUM; 
		if(StringUtill.isNull(matchPara)){
			return result;
		}
		try{
			int t = 0 ; 
			for(int i = 0 ; i < 5 ; i ++){
				if(matchPara.charAt(i) == '1'){
					t++;
				}
			}
			result = t;
			
		}catch(Exception e){
			e.printStackTrace();
			result = MatchOrder.MATCH_NUM;
		}
		return result;
	}

	//模糊对比，如果认为对比成功则返回true，否则返回false
	private int fuzzyCompare(UploadOrder tempUo, Order tempDBO,String matchPara) {
		
		AfterMatchOrder tempAfterMatchOrder = new AfterMatchOrder(tempUo,tempDBO);
		
		return tempAfterMatchOrder.simpleCompare(matchPara);
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
					if(!result.contains(uploadOrders.get(i))){
						result.add(uploadOrders.get(i));
					}
					
					if(!result.contains(uploadOrders.get(j))){
						result.add(uploadOrders.get(j));
					}
					
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
					if(!result.contains(DBOrders.get(i))){
						result.add(DBOrders.get(i));
					}
					
					
					if(!result.contains(DBOrders.get(j))){
						result.add(DBOrders.get(j));
					}
					
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
