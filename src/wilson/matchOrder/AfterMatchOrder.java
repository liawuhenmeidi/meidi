package wilson.matchOrder;

import order.Order;
import wilson.upload.UploadOrder;

public class AfterMatchOrder {
	private Order dbOrder = new Order();
	private String DBSideShop = "";
	private String DBSidePosNo = "";
	private String DBSideSaleTime = "";
	private String DBSideType = "";
	private String DBSideCount = "";
	private int DBSideOrderId = 0;
	
	private UploadOrder uploadOrder = new UploadOrder();
	private String UploadSideShop = "";
	private String UploadSidePosNo = "";
	private String UploadSideSaleTime = "";
	private String UploadSideType = "";
	private String UploadSideCount = "";
	private int UploadSideOrderId = 0;

	
	public void initDBSideOrder(Order dbOrder){
		setDBOrder(dbOrder);
		this.dbOrder = dbOrder;
		DBSideShop = dbOrder.getbranchName(dbOrder.getBranch()); 
		DBSidePosNo = dbOrder.getPos();
		DBSideSaleTime = dbOrder.getSaleTime();
		DBSideType = dbOrder.getSendType();
		DBSideCount = String.valueOf(dbOrder.getSendCount());
		DBSideOrderId = dbOrder.getId();
	}
	
	public void initUploadSideOrder(UploadOrder uploadOrder){
		this.uploadOrder = uploadOrder;
		UploadSideShop = uploadOrder.getShop();
		UploadSidePosNo = uploadOrder.getPosNo();
		UploadSideSaleTime = uploadOrder.getSaleTime();
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
		if(uploadOrder.getId() != -1){
			this.uploadOrder = uploadOrder;
			UploadSideShop = uploadOrder.getShop();
			UploadSidePosNo = uploadOrder.getPosNo();
			UploadSideSaleTime = uploadOrder.getSaleTime();
			UploadSideType = uploadOrder.getType();
			UploadSideCount = String.valueOf(uploadOrder.getNum());
			UploadSideOrderId = uploadOrder.getId();
		}else{
			this.uploadOrder = uploadOrder;
			UploadSideShop = "";
			UploadSidePosNo = "";
			UploadSideSaleTime = "";
			UploadSideType = "";
			UploadSideCount = "";
			UploadSideOrderId = uploadOrder.getId();
			
			this.uploadOrder.clear();
		}
		
		if(dbOrder.getId() != -1){
			this.dbOrder = dbOrder;
			DBSideShop = dbOrder.getbranchName(dbOrder.getBranch());
			DBSidePosNo = dbOrder.getPos();
			DBSideSaleTime = dbOrder.getSaleTime();
			DBSideType = dbOrder.getSendType();
			DBSideCount = String.valueOf(dbOrder.getSendCount());
			DBSideOrderId = dbOrder.getId();
		}else{
			this.dbOrder = dbOrder;
			DBSideShop = "";
			DBSidePosNo = "";
			DBSideSaleTime = "";
			DBSideType = "";
			DBSideCount = "";
			DBSideOrderId = dbOrder.getId();
			
			this.dbOrder.clear();
		}
		
		
	}
	
	Double compareLevel = 0.0; //对比等级，每个对比项，完全相同，等级+1，部分相同，等级+0.5
	Double maxCompareLevel = 5.0; //最大等级，目前对比项有5个，默认最大等级为5
	
	String compareResult = "00000";  //0为没对比上，1是对比上了  依次对应: 门店>型号>时间>pos>数量
	
	private static final int SHOP = 0;
	private static final int TYPE = 1;
	private static final int SALETIME = 2;
	private static final int POSNO = 3;
	private static final int NUM = 4;
	
	public void setCompareResult(int name,boolean result){
		this.compareResult = compareResult.substring(0,name) + (result?"1":"0") + compareResult.substring(name + 1,this.compareResult.length());
	}
	
	public int getCompareResult(){
		return Integer.parseInt(this.compareResult);
	}
	
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
	//HighLighter("abcde","3,5") => abc<redTag class=\"style\">de</redTag>
	//接受参数index的类型为String，值为"1,2,4,5"这样的类型
	public static String HighLighter(String inputString,String index){
		if(index == null || index.length() == 0 || index.split(",").length % 2 != 0){
			return inputString;
		}
		String outPut = inputString.substring(0,Integer.parseInt(index.split(",")[0]));
		for(int i = 0 ; i< index.split(",").length; i+=2){
			outPut += "<redTag class=\"style\">";
			outPut += inputString.substring(Integer.parseInt(index.split(",")[i]),Integer.parseInt(index.split(",")[i + 1]));
			outPut += "</redTag>";
			if(i + 2 != index.split(",").length){
				outPut += inputString.substring(Integer.parseInt(index.split(",")[i + 1]),Integer.parseInt(index.split(",")[i + 2]));
			}else{
				outPut += inputString.substring(Integer.parseInt(index.split(",")[i + 1]),inputString.length());
			}
			
		}
		
		return outPut;
	}
		
	//给字符串加入<redTag class=\"style\"></redTag>标签，参数为加入的位置，比如:
	//HighLighter("abcde",3,5) => abc<redTag class=\"style\">de</redTag>
	public static String HighLighter(String inputString,int start,int end){
		if(start<0||start>end||end>inputString.length()){
			return inputString;
		}
		//System.out.println("inputString = " + inputString + "start = " + start + "end = " + end);
		return inputString.substring(0,start) + "<redTag class=\"style\">" + inputString.substring(start,end) + "</redTag>" + inputString.substring(end);
	}
	
	//给字符串加入<redTag class=\"style\"></redTag>标签，比如:
	//HighLighter("abcde) => <redTag class=\"style\">abcde</redTag>
	public static String HighLighter(String inputString){
		return "<redTag class=\"style\">" + inputString + "</redTag>";
	}
	
	
	//计算相似度
	public Double calcLevel(){
		//清空
		this.setCompareLevel(0.0);
		String key = "";
		
		
		//对比posNo
		
		if(comparePosNo(this.getDBOrder().getPos(), this.getUploadOrder().getPosNo())){
			
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setCompareResult(AfterMatchOrder.POSNO, true);
			
			this.setDBSidePosNo(HighLighter(this.getDBSidePosNo()));
			this.setUploadSidePosNo(HighLighter(this.getUploadSidePosNo()));
			
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

		if(compareSaleTime(this.getDBSideSaleTime(), this.getUploadSideSaleTime())){	
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setCompareResult(AfterMatchOrder.SALETIME, true);
			
			this.setDBSideSaleTime(HighLighter(this.getDBSideSaleTime()));
			this.setUploadSideSaleTime(HighLighter(this.getUploadSideSaleTime()));
		}else{
		//无模糊对比
		}
		
		//对比票面数量

		if(compareNum(this.getDBSideCount(), this.getUploadSideCount())){
			
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setCompareResult(AfterMatchOrder.NUM, true);
			
			this.setDBSideCount(HighLighter(this.getDBSideCount()));
			this.setUploadSideCount(HighLighter(this.getUploadSideCount()));
		}else{
		//无模糊对比
		}
			
		//对比门店名称		
		
		if(compareShop(dbOrder.getShopNameForCompare(), this.getUploadOrder().getShop()) > MatchOrder.SHOPNAME_DISTANCE){
			
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setCompareResult(AfterMatchOrder.SHOP, true);
			
			this.setDBSideShop(HighLighter(this.getDBSideShop()));
			this.setUploadSideShop(HighLighter(this.getUploadSideShop()));
			
//			//精准对比
//			if(tempUpLoad.equals(tempDB)){
//				this.setDBSideShop(HighLighter(tempDB));
//				this.setUploadSideShop(HighLighter(tempUpLoad));
//			}else{
//			//模糊对比
//				this.setDBSideShop(HighLighter(tempDB,tempDB.indexOf(key),tempDB.indexOf(key) + key.length()));
//				this.setUploadSideShop(HighLighter(tempUpLoad,tempUpLoad.indexOf(key),tempUpLoad.indexOf(key) + key.length()));
//			}
		}
		
		//对比型号
		
		if(compareType(this.getDBOrder().getSendType(), this.getUploadSideType(), true)){
			
			this.setCompareLevel(this.getCompareLevel() + 1.0);
			this.setCompareResult(AfterMatchOrder.TYPE, true);
		}	
		
		
		
					
		
		return this.getCompareLevel();	
	}

	
	
	
	
	
	//以下为对比用工具方法
	
	public int simpleCompare(){
		int result = 0 ;
		//pos单号
		if(comparePosNo(this.getDBSidePosNo(), this.getUploadSidePosNo())){
			result += 1;
		}
				
		//销售时间
		if(compareSaleTime(this.getDBSideSaleTime(), this.getUploadSideSaleTime())){
			result += 1;
		}

		//票面数量
		if(compareNum(this.getDBSideCount(), this.getUploadSideCount())){
			result += 1;
		}
		
		//销售门店
		if(compareShop(this.getDBSideShop(), this.getUploadSideShop()) >= 0.2){
			result += 1;
		}
			
		//型号
		if(compareType(this.getDBSideType(), this.getUploadSideType(), false)){
			result += 1;
		}
		
		return result;
	}
	
	//对比门店名称
	public double compareShop(String dbSide,String uploadSide){
		double result = 0.0;
		dbSide = dbSide.trim();
		uploadSide = uploadSide.trim();
		
		result = utill.StringCompare.getSimilarityRatio(dbSide, uploadSide);
		
		return result;
	}
	
	
	//对比posno
	public boolean comparePosNo(String dbSide,String uploadSide){
		boolean result = false;
		String tempDB = dbSide.toUpperCase().trim();
		String tempUpLoad = uploadSide.toUpperCase().trim();
		if(tempDB.equals(tempUpLoad)){
			result = true;
		}
		return result;
	}
	
	//对比saleTime
	public boolean compareSaleTime(String dbSide,String uploadSide){
		boolean result = false;
		dbSide = dbSide.length() > 10 ? dbSide.substring(0,10):dbSide;
		if(dbSide.replace("-", "").equals(uploadSide)){
			result = true;
		}
		return result;
	}
	
	//对比票面数量
	public boolean compareNum(String dbSide,String uploadSide){
		boolean result = false;
		
		if(dbSide.replace("|", "").equals(uploadSide.replace("|", ""))){
			
			result = true;
		}else{
		//无模糊对比
		}
		return result;
	}
	
	
	//对比型号
	public boolean compareType(String dbSide,String uploadSide,boolean HighLight){
		boolean result = false;
		dbSide = dbSide.trim();
		uploadSide = uploadSide.trim();
		
		//快速对比，是否完全相同?
		if(dbSide.replace("|", "").equals(uploadSide)){
			if(HighLight){
				this.setDBSideType(HighLighter(dbSide));
				this.setUploadSideType(HighLighter(uploadSide));
			}
			return true;
		}
		
		//有括号吗？
		boolean isBlock = false;

		
		String tempDB = "";
		String tempUpload = "";
		
		//括号内部分
		String tempDBInBlock = "";
		String tempUploadInBlock = "";

		
		//判断是否有括号
		if((dbSide.contains("(") && dbSide.contains(")"))||(dbSide.contains("（") && dbSide.contains("）"))){
			if((uploadSide.contains("(") && uploadSide.contains(")"))||(uploadSide.contains("（") && uploadSide.contains("）"))){
				isBlock = true;
			}
		}

		
		
		//如果没括号
		if(!isBlock){
			tempUpload = uploadSide.replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "");
			if(tempDB.contains(tempUpload)){
				result = true;
				return result;
			}

			if(dbSide.replace("|", "").equals(uploadSide)){
				result = true;
				return result;
			}
		}else{
		//如果有括号	
			
			//分离括号内外
			if(dbSide.contains("(") && dbSide.contains(")")){
				tempDB = dbSide.substring(dbSide.indexOf("(") + 1,dbSide.indexOf(")"));
				tempDBInBlock = dbSide.substring(0,dbSide.indexOf("(")) + dbSide.substring(dbSide.indexOf(")") + 1,dbSide.length());
			}else if(dbSide.contains("（") && dbSide.contains("）")){
				tempDB = dbSide.substring(dbSide.indexOf("（")+ 1,dbSide.indexOf("）"));
				tempDBInBlock = dbSide.substring(0,dbSide.indexOf("（")) + dbSide.substring(dbSide.indexOf("）") + 1,dbSide.length());
			}
			
			
			if(uploadSide.contains("(") && uploadSide.contains(")")){
				tempUpload = uploadSide.substring(uploadSide.indexOf("(")+ 1,uploadSide.indexOf(")"));
				tempUploadInBlock = uploadSide.substring(0,uploadSide.indexOf("(")) + uploadSide.substring(uploadSide.indexOf(")") + 1,uploadSide.length());
			}else if(uploadSide.contains("（") && uploadSide.contains("）")){
				tempUpload = uploadSide.substring(uploadSide.indexOf("（")+ 1,uploadSide.indexOf("）"));
				tempUploadInBlock = uploadSide.substring(0,uploadSide.indexOf("（")) + uploadSide.substring(uploadSide.indexOf("）") + 1,uploadSide.length());
			}
			
			
			tempDB = tempDB.replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "");
			tempUpload = tempUpload.replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "");
			tempDBInBlock = tempDBInBlock.replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "");
			tempUploadInBlock = tempUploadInBlock.replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "");
					
			
			//括号外的部分
			if(tempDB.contains(tempUpload)){
				result = true;//括号外相同，暂时先true
			}else{
				return result; //此时为false;
			}
		
			//括号内部分
			if(tempDBInBlock.contains(tempUploadInBlock)){
				if(result == true){ 
					//括号内外都能匹配上
					
					//是否上色
					if(HighLight){
						try{	
							String dbSideIndex = String.valueOf(dbSide.indexOf(tempUpload)) + "," + String.valueOf(dbSide.indexOf(tempUpload) + tempUpload.length()) + "," + String.valueOf(dbSide.indexOf(tempUploadInBlock)) + "," + String.valueOf(dbSide.indexOf(tempUploadInBlock) + tempUploadInBlock.length());
							String uploadSideIndex = String.valueOf(uploadSide.indexOf(tempDB)) + "," + String.valueOf(uploadSide.indexOf(tempDB) + tempDB.length()) + "," + String.valueOf(uploadSide.indexOf(tempDBInBlock)) + "," + String.valueOf(uploadSide.indexOf(tempDBInBlock) + tempDBInBlock.length());
							
							this.setDBSideType(HighLighter(dbSide, dbSideIndex));
							this.setUploadSideType(HighLighter(uploadSide, uploadSideIndex));
						}catch(Exception e){
							this.setDBSideType(HighLighter(dbSide));
							this.setUploadSideType(HighLighter(uploadSide));
						}
						
					}
					return result;
				}else{
					result = false;
					return result;//这里能匹配上，但是括号外不能，返回false;
				}
				
			}else{
				result = false;
				return result;//这里不能匹配上，返回false;
			}
			
		}
		return result;

	}
	
}
