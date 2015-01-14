package wilson.matchOrder;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import order.OrderManager;
import user.User;
import utill.StringUtill;
import wilson.upload.UploadOrder;
import wilson.upload.UploadManager;

public class MatchOrderManager {
	
	//将uploadOrder临时转换为Order，仅供uploadCheckout.jsp使用
	public static List<Order> transferUploadOrder(List<UploadOrder> input){
		List<Order> result = new ArrayList<Order>();
		Order tmp = new Order();
		for(int i = 0 ; i < input.size() ; i ++){
			tmp = new Order();
			tmp.setFromUploadOrder(true);
			tmp.setId(input.get(i).getId());
			tmp.setShopname_upload(input.get(i).getShop());
			tmp.setPos(input.get(i).getPosNo());
			tmp.setSaleTime(input.get(i).getSaleTime());
			tmp.setSalenum_upload(input.get(i).getNum());
			tmp.setSaleType_upload(input.get(i).getType());
			result.add(tmp);
		}
		return result;
	}
	
	//根据条件取order(仅提供给manualCheckout.jsp使用)
	public static List<Order> getUnCheckedDBOrders(String selectBranchType,String selectBranch,String deadline){
		List<Order> unCheckedDBOrders = new ArrayList<Order>();
		
		//查询条件提交后，左侧侧显示内容
		if(selectBranchType != null && !selectBranchType.equals("") ){
			//第一级选择的是否是all
			if(selectBranchType.equals("all")){
				unCheckedDBOrders = MatchOrderManager.getUnCheckedDBOrders(deadline);
			}else{
				
				if(selectBranch != null && !selectBranch.equals("")){
					//第二级选择的是否是all
					if(selectBranch.equals("all")){ 
						unCheckedDBOrders = OrderManager.getUnCheckedDBOrdersbyBranchType(selectBranchType,deadline);
					}else{
						unCheckedDBOrders = OrderManager.getUnCheckedDBOrdersbyBranch(selectBranch,deadline);
					}
				}
				
			}
		}
		return unCheckedDBOrders;
	}
	
	//根据条件取order(仅提供给manualCheckout.jsp使用)
	public static List<Order> getUnConfirmedDBOrders(String selectBranchType,String selectBranch,String deadline){
		List<Order> unConfirmedDBOrders = new ArrayList<Order>();
		
		//查询条件提交后，左侧侧显示内容
		if(selectBranchType != null && !selectBranchType.equals("") ){
			//第一级选择的是否是all
			if(selectBranchType.equals("all")){
				unConfirmedDBOrders = MatchOrderManager.getUnConfirmedDBOrders(deadline);
			}else{
				
				if(selectBranch != null && !selectBranch.equals("")){
					//第二级选择的是否是all
					if(selectBranch.equals("all")){ 
						unConfirmedDBOrders = OrderManager.getUnConfirmedDBOrdersbyBranchType(selectBranchType,deadline);
					}else{
						unConfirmedDBOrders = OrderManager.getUnConfirmedDBOrdersbyBranch(selectBranch,deadline);
					}
				}
				
			}
		}
		return unConfirmedDBOrders;
	}
	//根据条件取Uploadorder(仅提供给manualCheckout.jsp使用)
	public static List<UploadOrder> getUnCheckedUploadOrders(String selectOrderName){
		List<UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
		//查询条件提交后，右侧显示内容
		if(selectOrderName != null && !selectOrderName.equals("")){
			if(selectOrderName.equals("all")){
				unCheckedUploadOrders = UploadManager.getUnCheckedUploadOrders();
			}else{
				unCheckedUploadOrders = UploadManager.getUnCheckedUploadOrdersByName(selectOrderName);
			}
		}
		return unCheckedUploadOrders;
	}
	
	//根据条件取order(仅提供给manualCheckout.jsp使用)
	public static List<Order> getCheckedDBOrders(String selectBranchType,String selectBranch,String deadline){
		List<Order> unCheckedDBOrders = new ArrayList<Order>();
		if(selectBranchType != null && !selectBranchType.equals("") ){
			//第一级选择的是否是all
			if(selectBranchType.equals("all")){
				unCheckedDBOrders = OrderManager.getCheckedDBOrders(deadline);
			}else{
				
				if(selectBranch != null && !selectBranch.equals("")){
					//第二级选择的是否是all
					if(selectBranch.equals("all")){ 
						unCheckedDBOrders = OrderManager.getCheckedDBOrdersbyBranchType(selectBranchType,deadline);
					
					}else{
						unCheckedDBOrders = OrderManager.getCheckedDBOrdersbyBranch(selectBranch,deadline);
					}
				}
				
			}
		}
		return unCheckedDBOrders;
	}
	//根据条件取Uploadorder(仅提供给manualCheckout.jsp使用)
	public static List<UploadOrder> getCheckedUploadOrders(String selectOrderName){
		List<UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
		
		
		//查询条件提交后，右侧显示内容
		if(selectOrderName != null && !selectOrderName.equals("")){
			if(selectOrderName.equals("all")){
				unCheckedUploadOrders = UploadManager.getCheckedUploadOrders();
			}else{
				unCheckedUploadOrders = UploadManager.getCheckedUploadOrdersByName(selectOrderName);
			}
		}
		return unCheckedUploadOrders;
	}
	
	//list中找到与target相似的订单
	public static List<UploadOrder> searchUploadOrderList(List<UploadOrder> uploadOrders,UploadOrder target){
		
		//店名
		String tempName = target.getShop();
		if(!StringUtill.isNull(tempName)){
			for(int i = 0 ; i < uploadOrders.size() ; i ++){
				if(!uploadOrders.get(i).getShop().contains(tempName)){
					uploadOrders.remove(i);
					i --;
				}
				if(i >= uploadOrders.size()){
					break;
				}
			}
		}
		
		//pos单号
		tempName = target.getPosNo();
		if(!StringUtill.isNull(tempName)){
			for(int i = 0 ; i < uploadOrders.size() ; i ++){
				if(!uploadOrders.get(i).getPosNo().contains(tempName)){
					uploadOrders.remove(i);
					i --;
				}
				if(i >= uploadOrders.size()){
					break;
				}
			}
		}
		
		//销售日期
		tempName = target.getSaleTime();
		if(!StringUtill.isNull(tempName)){
			tempName = tempName.replace("-", "").replace("/", "");
			for(int i = 0 ; i < uploadOrders.size() ; i ++){
				if(!uploadOrders.get(i).getSaleTime().replace("-", "").replace("/", "").equals(tempName)){
					uploadOrders.remove(i);
					i --;
				}
				if(i >= uploadOrders.size()){
					break;
				}
			}
		}
		
		//型号
		tempName = target.getType();
		if(!StringUtill.isNull(tempName)){
			for(int i = 0 ; i < uploadOrders.size() ; i ++){
				if(!uploadOrders.get(i).getType().contains(tempName)){
					uploadOrders.remove(i);
					i --;
				}
				if(i >= uploadOrders.size()){
					break;
				}
			}
		}
		
		
		//数量
		int tempInt = target.getNum();
		if(tempInt > 0){
			for(int i = 0 ; i < uploadOrders.size() ; i ++){
				if(uploadOrders.get(i).getNum() != tempInt){
					uploadOrders.remove(i);
					i --;
				}
				if(i >= uploadOrders.size()){
					break;
				}
			}
		}
		
		return uploadOrders;
	}
	
	//list中找到与target相似的订单
	public static List<Order> searchOrderList(List<Order> orders,UploadOrder target){
		if(target == null){
			return null;
		}
		String tmp = "";
		//店名
		String tempName = target.getShop();
		if(!StringUtill.isNull(tempName)){
			for(int i = 0 ; i < orders.size() ; i ++){
				if(orders.get(i).isFromUploadOrder()){
					tmp = orders.get(i).getShopname_upload();
				}else{
					tmp = orders.get(i).getShopNameForCompare();
				}
				if(!tmp.contains(tempName)){
					orders.remove(i);
					i --;
				}
				if(i >= orders.size()){
					break;
				}
			}
		}
		
		//pos单号
		tempName = target.getPosNo();
		if(!StringUtill.isNull(tempName)){
			for(int i = 0 ; i < orders.size() ; i ++){
				if(!orders.get(i).getPos().contains(tempName)){
					orders.remove(i);
					i --;
				}
				if(i >= orders.size()){
					break;
				}
			}
		}
		
		//销售日期
		tempName = target.getSaleTime();
		if(!StringUtill.isNull(tempName)){
			tempName = tempName.replace("-", "").replace("/", "");
			for(int i = 0 ; i < orders.size() ; i ++){
				if(!orders.get(i).getSaleTime().replace("-", "").replace("/", "").equals(tempName)){
					orders.remove(i);
					i --;
				}
				if(i >= orders.size()){
					break;
				}
			}
		}
		
		//型号
		tempName = target.getType();
		if(!StringUtill.isNull(tempName)){
			for(int i = 0 ; i < orders.size() ; i ++){
				if(orders.get(i).isFromUploadOrder()){
					tmp = orders.get(i).getSaleType_upload();					
				}else{
					tmp = orders.get(i).getSendType();
				}
				if(!tmp.contains(tempName)){
					orders.remove(i);
					i --;
				}
				if(i >= orders.size()){
					break;
				}
			}
		}
		
		
		//数量
		int tempInt = target.getNum();
		if(tempInt > 0){
			for(int i = 0 ; i < orders.size() ; i ++){
				if(orders.get(i).isFromUploadOrder()){
					tmp = String.valueOf(orders.get(i).getSalenum_upload());
				}else{
					tmp = orders.get(i).getSendCount().replace("|", "").trim();
				}
				
				if(!tmp.equals(String.valueOf(tempInt))){
					orders.remove(i);
					i --;
				}
				if(i >= orders.size()){
					break;
				}
			}
		}
		
		return orders;
	}
	
	public static List <UploadOrder> getUnCheckedUploadOrders(){
		List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
		unCheckedUploadOrders = UploadManager.getUnCheckedUploadOrders();
		if(unCheckedUploadOrders != null && unCheckedUploadOrders.size() >= 0){
			return unCheckedUploadOrders;
		}else{
			return null;
		}
	}
	
	public static List<Order> getUnCheckedDBOrders(String time){
		List <Order> unCheckedDBOrders = new ArrayList<Order>();
		unCheckedDBOrders = OrderManager.getUnCheckedDBOrders(time);
		if(unCheckedDBOrders != null && unCheckedDBOrders.size() >= 0){
			return unCheckedDBOrders;
		}else{
			return null;
		}
	}
	
	public static List<Order> getUnConfirmedDBOrders(String time){
		List <Order> unConfirmedDBOrders = new ArrayList<Order>();
		unConfirmedDBOrders = OrderManager.getUnConfirmedDBOrders(time);
		if(unConfirmedDBOrders != null && unConfirmedDBOrders.size() >= 0){
			return unConfirmedDBOrders;
		}else{
			return null;
		}
	}
	
	public static boolean checkOrder(User user ,int DBOrderID,int UploadOrderID,String checkName){
		//消除DB中的Order
		//a.b();
		//对照好了消除
		//OrderManager.updateStatues("orderCharge",Order.query, String.valueOf(DBOrderID)); 
		//消除Upload中的Order
		if(UploadManager.checkOrder(user,UploadOrderID,DBOrderID,checkName)){
			return true;
		}

		return false;
	}
	
	//接受{"1,2","2,3"}类型的ID输入,前一个是DB的Order，后一个是upload的Order哦~
	public static boolean checkOrder(User user,String[] idString,String checkName){
//		int DBOrderID = 0;
//		int UploadOrderID = 0;
//		for(int i = 0 ; i < idString.length ; i ++){
//			DBOrderID = Integer.parseInt(idString[i].split(",")[0]);
//			UploadOrderID = Integer.parseInt(idString[i].split(",")[1]);
//			
//		}
		
		return UploadManager.checkOrder(user,idString,checkName);
	}
	
	public static boolean checkDBOrder(User user,int DBOrderID,String checkName){
		
		if(UploadManager.checkDBOrder(user,DBOrderID,checkName)){
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
	
	public static boolean checkDBOrderList(User user ,String[] dbOrderIdStrList,String checkName){
		String idList = "";
		for(int i = 0 ; i < dbOrderIdStrList.length ; i ++ ){
			idList += dbOrderIdStrList[i] + ",";
		}
		idList = idList.substring(0,idList.length()-1);
		 
		if(UploadManager.checkDBOrderStrList(user,idList,checkName)){
			return true;
		}

		return false;
	}
	
	public static boolean confirmDBOrderList(User user ,String[] dbOrderIdStrList){
		String idList = "";
		for(int i = 0 ; i < dbOrderIdStrList.length ; i ++ ){
			idList += dbOrderIdStrList[i] + ",";
		}
		idList = idList.substring(0,idList.length()-1);
		 
		if(UploadManager.confirmDBOrderStrList(user,idList,"1")){
			return true;
		}

		return false;
	}
	
	public static boolean checkUploadOrderList(String[] uploadOrderIdStrList){
		String idList = "";
		for(int i = 0 ; i < uploadOrderIdStrList.length ; i ++ ){
			idList += uploadOrderIdStrList[i] + ",";
		}
		idList = idList.substring(0,idList.length()-1);
		if(UploadManager.checkUploadOrderStrList(idList)){
			return true;
		}

		return false;
	}

}
