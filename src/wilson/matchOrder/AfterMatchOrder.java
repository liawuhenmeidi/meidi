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
	
	public AfterMatchOrder() {
		super();
	}
	public AfterMatchOrder(UploadOrder uploadOrder, Order dbOrder) {
		setUploadOrder(uploadOrder);
		setDBOrder(dbOrder);
		this.dbOrder = dbOrder;
		DBSideShop = dbOrder.getBranch();
		DBSidePosNo = dbOrder.getPos();
		DBSideSaleTime = dbOrder.getSaleTime();
		DBSideDealTime = dbOrder.getOdate();
		DBSideType = dbOrder.getSendType();
		DBSideCount = dbOrder.getSendCount();
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
	public static String HighLighter(String inputString,int start,int end){
		if(start<0||start>end||end>inputString.length()){
			return inputString;
		}
		return inputString.substring(0,start) + "<redTag class=\"style\">" + inputString.substring(start,end) + "</redTag>" + inputString.substring(end);
	}
	
	public void calcLevel(){
		
	}
	
	
}
