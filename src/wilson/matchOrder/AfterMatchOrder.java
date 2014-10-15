package wilson.matchOrder;

import order.Order;
import wilson.upload.UploadOrder;

public class AfterMatchOrder {
	private Order dbOrder = new Order();
	private String DBSideShop = "";
	private String DBSidePosNo = "";
	private String DBSideSaleTime = "";
	private String DBSideDealTime = "";
	private String DBSideType = "";
	private String DBSideCount = "";
	private int DBSideOrderId = 0;
	
	private UploadOrder uploadOrder = new UploadOrder();
	private String UploadSideShop = "";
	private String UploadSidePosNo = "";
	private String UploadSideSaleTime = "";
	private String UploadSideDealTime = "";
	private String UploadSideType = "";
	private String UploadSideCount = "";
	private int UploadSideOrderId = 0;
	
	
	public void initDBSideOrder(Order dbOrder){
		setDBOrder(dbOrder);
		this.dbOrder = dbOrder;
		DBSideShop = dbOrder.getbranchName(dbOrder.getBranch()); 
		DBSidePosNo = dbOrder.getPos();
		DBSideSaleTime = dbOrder.getSaleTime();
		DBSideDealTime = dbOrder.getOdate();
		DBSideType = dbOrder.getSendType();
		DBSideCount = String.valueOf(dbOrder.getSendCount());
		DBSideOrderId = dbOrder.getId();
	}
	
	public void initUploadSideOrder(UploadOrder uploadOrder){
		this.uploadOrder = uploadOrder;
		UploadSideShop = uploadOrder.getShop();
		UploadSidePosNo = uploadOrder.getPosNo();
		UploadSideSaleTime = uploadOrder.getSaleTime();
		UploadSideDealTime = uploadOrder.getDealTime();
		UploadSideType = uploadOrder.getType();
		UploadSideCount = String.valueOf(uploadOrder.getNum());
		UploadSideOrderId = uploadOrder.getId();
	}
	
	public AfterMatchOrder() {
		super();
	}
	public AfterMatchOrder(UploadOrder uploadOrder, Order dbOrder) {
		setUploadOrder(uploadOrder);
		setDBOrder(dbOrder);
		this.dbOrder = dbOrder;
		DBSideShop = dbOrder.getbranchName(dbOrder.getBranch());
		DBSidePosNo = dbOrder.getPos();
		DBSideSaleTime = dbOrder.getSaleTime();
		DBSideDealTime = dbOrder.getOdate();
		DBSideType = dbOrder.getSendType();
		DBSideCount = String.valueOf(dbOrder.getSendCount());
		DBSideOrderId = dbOrder.getId();
		this.uploadOrder = uploadOrder;
		UploadSideShop = uploadOrder.getShop();
		UploadSidePosNo = uploadOrder.getPosNo();
		UploadSideSaleTime = uploadOrder.getSaleTime();
		UploadSideDealTime = uploadOrder.getDealTime();
		UploadSideType = uploadOrder.getType();
		UploadSideCount = String.valueOf(uploadOrder.getNum());
		UploadSideOrderId = uploadOrder.getId();
	}
	
	Double compareLevel = 0.0; //对比等级，每个对比项，完全相同，等级+1，部分相同，等级+0.5
	Double maxCompareLevel = 6.0; //最大等级，目前对比项有6个，默认最大等级为6
	
	public String getDBSideShop() {
		return DBSideShop;
	}
	public void setDBSideShop(String dBSideShop) {
		DBSideShop = dBSideShop;
	}
	public String getDBSidePosNo() {
		return DBSidePosNo;
	}
	public void setDBSidePosNo(String dBSidePosNo) {
		DBSidePosNo = dBSidePosNo;
	}
	public String getDBSideSaleTime() {
		return DBSideSaleTime;
	}
	public void setDBSideSaleTime(String dBSideSaleTime) {
		DBSideSaleTime = dBSideSaleTime;
	}
	public String getDBSideDealTime() {
		return DBSideDealTime;
	}
	public void setDBSideDealTime(String dBSideDealTime) {
		DBSideDealTime = dBSideDealTime;
	}
	public String getDBSideType() {
		return DBSideType;
	}
	public void setDBSideType(String dBSideType) {
		DBSideType = dBSideType;
	}
	public String getDBSideCount() {
		return DBSideCount;
	}
	public void setDBSideCount(String dBSideCount) {
		DBSideCount = dBSideCount;
	}
	public int getDBSideOrderId() {
		return DBSideOrderId;
	}
	public void setDBSideOrderId(int dBSideOrderId) {
		DBSideOrderId = dBSideOrderId;
	}
	public String getUploadSideShop() {
		return UploadSideShop;
	}
	public void setUploadSideShop(String uploadSideShop) {
		UploadSideShop = uploadSideShop;
	}
	public String getUploadSidePosNo() {
		return UploadSidePosNo;
	}
	public void setUploadSidePosNo(String uploadSidePosNo) {
		UploadSidePosNo = uploadSidePosNo;
	}
	public String getUploadSideSaleTime() {
		return UploadSideSaleTime;
	}
	public void setUploadSideSaleTime(String uploadSideSaleTime) {
		UploadSideSaleTime = uploadSideSaleTime;
	}
	public String getUploadSideDealTime() {
		return UploadSideDealTime;
	}
	public void setUploadSideDealTime(String uploadSideDealTime) {
		UploadSideDealTime = uploadSideDealTime;
	}
	public String getUploadSideType() {
		return UploadSideType;
	}
	public void setUploadSideType(String uploadSideType) {
		UploadSideType = uploadSideType;
	}
	public String getUploadSideCount() {
		return UploadSideCount;
	}
	public void setUploadSideCount(String uploadSideCount) {
		UploadSideCount = uploadSideCount;
	}
	public int getUploadSideOrderId() {
		return UploadSideOrderId;
	}
	public void setUploadSideOrderId(int uploadSideOrderId) {
		UploadSideOrderId = uploadSideOrderId;
	}
	
	public Double getCompareLevel() {
		return compareLevel;
	}
	public void setCompareLevel(Double compareLevel) {
		this.compareLevel = compareLevel;
	}
	public Double getMaxCompareLevel() {
		return maxCompareLevel;
	}
	public void setMaxCompareLevel(Double maxCompareLevel) {
		this.maxCompareLevel = maxCompareLevel;
	}
	public Order getDBOrder() {
		return dbOrder;
	}
	public void setDBOrder(Order dbOrder) {
		this.dbOrder = dbOrder;
	}
	public UploadOrder getUploadOrder() {
		return uploadOrder;
	}
	public void setUploadOrder(UploadOrder uploadOrder) {
		this.uploadOrder = uploadOrder;
	}
	
	//给字符串加入<redTag class=\"style\"></redTag>标签，参数为加入的位置，比如:
	//HighLighter("abcde",3,5) => abc<redTag class=\"style\">de</redTag>
	//接受参数start和end的类型为String，值为",1,2,3" ,"4,3,2"这样的类型
	public static String HighLighter(String inputString,String start,String end){
		if(start == null || end == null || start.length()*end.length() == 0){
			return inputString;
		}
		String outPut = inputString.substring(0,Integer.parseInt(start.split(",")[1]));
		for(int i = 0 ; i + 1< start.split(",").length;i++){
			outPut += "<redTag class=\"style\">";
			outPut += inputString.substring(Integer.parseInt(start.split(",")[i+1]),Integer.parseInt(end.split(",")[i+1]));
			outPut += "</redTag>";
		}
		return outPut;
	}
		
	//给字符串加入<redTag class=\"style\"></redTag>标签，参数为加入的位置，比如:
	//HighLighter("abcde",3,5) => abc<redTag class=\"style\">de</redTag>
	public static String HighLighter(String inputString,int start,int end){
		if(start<0||start>end||end>inputString.length()){
			return inputString;
		}
		System.out.println("inputString = " + inputString + "start = " + start + "end = " + end);
		return inputString.substring(0,start) + "<redTag class=\"style\">" + inputString.substring(start,end) + "</redTag>" + inputString.substring(end);
	}
	
	//给字符串加入<redTag class=\"style\"></redTag>标签，比如:
	//HighLighter("abcde) => <redTag class=\"style\">abcde</redTag>
	public static String HighLighter(String inputString){
		return "<redTag class=\"style\">" + inputString + "</redTag>";
	}
	
	
	//计算相似度
	public void calcLevel(){
		
		String key = "";
		//对比posNo
		String tempDB = this.getDBOrder().getPos().trim();
		String tempUpLoad = this.getUploadOrder().getPosNo().trim();
		if(tempDB.equals(tempUpLoad)){
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setDBSidePosNo(HighLighter(tempDB));
			this.setUploadSidePosNo(HighLighter(tempUpLoad));
		}else{
		//模糊对比posNo
			
//			int start = 0;
//			int end = 0 ;
//			String startPoint = "";
//			String endPoint = "";
//			boolean combo = false;
//			for(int i = 0 ; i < tempUpLoad.length(); i ++ ){
//				if(tempDB.charAt(i) == tempUpLoad.charAt(i)){
//					if(combo == false){
//						start = i;
//					}
//					combo=true;
//				}else{
//					if(combo == true){
//						end = i;
//						startPoint += "," + start;
//						endPoint += "," + end;
//					}
//					combo=false;
//				}
//				if(i == tempUpLoad.length()-1){
//					if(combo == true){
//						end = tempUpLoad.length();
//						startPoint += "," + start;
//						endPoint += "," + end ;	
//					}
//				}
//			}
//		
//			this.setDBSidePosNo(HighLighter(tempDB,startPoint,endPoint));
//			this.setUploadSidePosNo(HighLighter(tempUpLoad,startPoint,endPoint));
			
		}
		
		//对比saleTime
		tempDB = this.getDBOrder().getSaleTime();
		tempUpLoad = this.getUploadOrder().getSaleTime();
		tempDB = tempDB.length() > 10 ? tempDB.substring(0,10):tempDB;
		if(tempDB.replace("-", "").equals(tempUpLoad)){
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setDBSideSaleTime(HighLighter(tempDB));
			this.setUploadSideSaleTime(HighLighter(tempUpLoad));
		}else{
		//无模糊对比
		}
		
		//对比DealTime
		tempDB = this.getDBOrder().getOdate();
		tempUpLoad = this.getUploadOrder().getDealTime();
		tempDB = tempDB.length() > 10 ? tempDB.substring(0,10):tempDB;
		if(tempDB.replace("-", "").equals(tempUpLoad)){
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setDBSideDealTime(HighLighter(tempDB));
			this.setUploadSideDealTime(HighLighter(tempUpLoad));
		}else{
		//无模糊对比
		}
		
		//对比票面数量
		tempDB = String.valueOf(this.getUploadOrder().getNum());
		tempUpLoad = String.valueOf(this.getDBOrder().getSendCount());
		if(tempDB.replace("|", "").equals(tempUpLoad.replace("|", ""))){
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setDBSideCount(HighLighter(tempDB));
			this.setUploadSideCount(HighLighter(tempUpLoad));
		}else{
		//无模糊对比
		}
			
		//对比门店名称
		tempDB = dbOrder.getbranchName(dbOrder.getBranch());
		tempUpLoad = this.getUploadOrder().getShop().trim();
		key = tempDB.replace("苏宁", "").replace("店", "");
		if(tempUpLoad.contains(key)){
			//精准对比
			if(tempUpLoad.equals(tempDB)){
				this.setCompareLevel(this.getCompareLevel() + 1.0);
				this.setDBSideShop(HighLighter(tempDB));
				this.setUploadSideShop(HighLighter(tempUpLoad));
			}else{
			//模糊对比
				this.setCompareLevel(this.getCompareLevel() + 0.5);
				this.setDBSideShop(HighLighter(tempDB,tempDB.indexOf(key),tempDB.indexOf(key) + key.length()));
				this.setUploadSideShop(HighLighter(tempUpLoad,tempUpLoad.indexOf(key),tempUpLoad.indexOf(key) + key.length()));
			}
		}
		
		//对比型号
		tempDB = this.getDBOrder().getSendType().trim();
		tempUpLoad = this.getUploadOrder().getType().trim();
		key = tempUpLoad.replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "");
		if(tempDB.contains(key)){
			//精准对比
			if(tempDB.equals(tempUpLoad)){
				this.setCompareLevel(this.getCompareLevel() + 1.0);
				this.setDBSideType(HighLighter(tempDB));
				this.setUploadSideType(HighLighter(tempUpLoad));
			}else{
				this.setCompareLevel(this.getCompareLevel() + 0.5);
				this.setDBSideType(HighLighter(tempDB,tempDB.indexOf(key),tempDB.indexOf(key) + key.length()));
				this.setUploadSideType(HighLighter(tempUpLoad,tempUpLoad.indexOf(key),tempUpLoad.indexOf(key) + key.length()));
			}
		}		
					
			
	}
	
	
}
