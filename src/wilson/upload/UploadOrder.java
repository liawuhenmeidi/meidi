package wilson.upload;

import product.ProductService;
import utill.StringUtill;

public class UploadOrder {
	
	public UploadOrder() {
		super();
	}
	
	public UploadOrder(UploadOrder uo) {
		super();
		this.id = uo.getId();
		this.name = uo.getName();
		this.shop = uo.getShop();
		this.posNo = uo.getPosNo();
		this.saleManName = uo.getSaleManName();
		this.saleTime = uo.getSaleTime();
		this.type = uo.getType();
		this.num = uo.getNum();
		this.salePrice = uo.getSalePrice();
		this.backPoint = uo.getBackPoint();
		this.fileName = uo.getFileName();
		this.checked = uo.getChecked();
		this.checkedTime = uo.getCheckedTime();
		this.checkOrderId = uo.getCheckOrderId();
		//this.typeForCompare = uo.gettyp;
	}

	private int id = DEFAULT; //id
	
	private String name = ""; 
	
	private String shop = ""; //销售门店
	
	private String posNo = null; //pos(厂送)单号
	
	private String saleManName = "";//销售员姓名
	
	private String saleTime = ""; //销售日期

	private String type = ""; //票面型号

	private int num = 0; //票面数量
	
	private Double salePrice = 0.0; //供价
	
	private Double backPoint = 0.0;//扣点
	
	private String fileName = ""; //来自的文件名
	
	private int checked = UNCHECK; //0 为已经check  1为没有 check 2为已经与提成标准匹配好  3为已经消单  默认为1   
              
	public static final int DEFAULT = -1;
	public static final int CHECKED = 0;
	public static final int UNCHECK = 1;
	public static final int CALCED = 2;
	public static final int COMFIRMED = 3; 
	public static final int COMPARE_DEFAULT = 1;
	public static final int ORIGIN = 5;
	
	public String getPrintStatus(){
		switch (this.checked){
			case CHECKED:
				return "已结款";
			case UNCHECK:
				return "未结款";
			case CALCED:
				return "已提成";
			case COMFIRMED:
				return "已消";
			default:
				return "未知";
		}
			
	}
	
	public int getSendLength(){
		int result = 0;
		if(StringUtill.isNull(this.getSaleManName())){
			return result;
		}
		String content = this.getSaleManName();
		
		result = content.split(",").length;

		return result;
	}
	
	public String getSendTypeAndCountAndPrice(int a,int b){
		String result  = "";
		if(StringUtill.isNull(this.getSaleManName())){
			return result;
		}
		String content = this.getSaleManName();
		
		if(a >= content.split(",").length || b >= content.split(",")[a].split(":").length){
			return result;
		}
		
		result = content.split(",")[a].split(":")[b];
		if(0 == b){
			result = ProductService.getIDmap().get(Integer.parseInt(result)).getType();
		}
		

		return result;
	}
	
	public String getSendTypeAndCountAndPrice(){
		String result = "";
		if(StringUtill.isNull(this.getSaleManName())){
			return result;
		}
		String content = this.getSaleManName();
		
		for(int i = 0 ; i < content.split(",").length ; i ++){
			
			String type = content.split(",")[i].split(":")[0];
			type = ProductService.getIDmap().get(Integer.parseInt(type)).getType();
			String num = content.split(",")[i].split(":")[1];
			String price = content.split(",")[i].split(":")[2];

			result += type + ":" + num + "台:" + price +  "元,";
			
		}
		

		return result;
	}
	
	private String checkedTime = ""; //check的时间，默认为null
	
	private int checkOrderId = 0; //check后对应的orderID
	
	public static final int CHECK_ORDER_ID_DEFAULT = 0;
	public static final int CHECK_ORDER_ID_SEPARATE = -10;

	public Double getTotal(){
		return getSalePrice() * getNum();
	}
	
	public String getShop(){
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public String getSaleTime(){
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public String getType() {
		return type;
	}
	
	private String typeForCompare = null;
	
	public String getTypeForCompareWithOutCharactar(){
		if(typeForCompare == null){
			typeForCompare = getType().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "");
		}
		return typeForCompare;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public String getCheckedTime() {
		return checkedTime;
	}

	public void setCheckedTime(String checkedTime) {
		this.checkedTime = checkedTime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Double getSalePrice() {
		return Math.round(salePrice * 100)/100.0;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public String toString() {
		return "UploadOrder [shop=" + shop + ", posNo="
				+ posNo + ", saleTime=" + saleTime + ", type=" + type + ", num=" + num + ", salePrice="
				+ salePrice  + ", fileName="
				+ fileName + ", checked=" + checked + ", checkedTime="
				+ checkedTime + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSaleManName() {
		return saleManName;
	}
	
	public String getTypeForCalc(){
		if(StringUtill.isNull(this.saleManName)){
			return this.type;
		}else{
			String result = "";
			try{
				for(int i = 0 ; i < saleManName.split(",").length ; i ++){
					int id = Integer.parseInt(saleManName.split(",")[i].split(":")[0]);
					result += ProductService.getIDmap().get(id).getType();
					result += ",";
				}
				
			}catch(Exception e){
				return this.saleManName;
			}
			
			return result;
		}
	}
	
	public String getSendType(){
		String result = "";
		
		if(StringUtill.isNull(this.saleManName)){
			try{
				result += this.getType() + ":";
				result += this.getNum() + ":";
				result += this.getSalePrice();
						
			}catch(Exception e){
				e.printStackTrace();
				return this.type;
			}
		}else{
			try{
				for(int i = 0 ; i < saleManName.split(",").length ; i ++){
					int id = Integer.parseInt(saleManName.split(",")[i].split(":")[0]);
					result += ProductService.getIDmap().get(id).getType() + ":";
					result += saleManName.split(",")[i].split(":")[1] + ":";
					result += saleManName.split(",")[i].split(":")[2] + ",";
				}
				if(result.endsWith(",")){
					result = result.substring(0,result.length() -1);
				}
				
			}catch(Exception e){
				e.printStackTrace();
				return this.saleManName;
			}

		}
		return result;
	}

	public void setSaleManName(String saleManName) {
		this.saleManName = saleManName;
	}

	public int getCheckOrderId() {
		return checkOrderId;
	}

	public void setCheckOrderId(int checkOrderId) {
		this.checkOrderId = checkOrderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void clear() {
		this.id = -1; //id
		this.name = ""; 
		this.shop = ""; //销售门店
		this.posNo = ""; //pos(厂送)单号
		this.saleManName = "";//销售员姓名
		this.saleTime = ""; //销售日
		this.type = ""; //票面型
		this.num = 0; //票面数量	
		this.salePrice = 0.0; //供价	
		this.fileName = ""; //来自的文件名	
		this.checked = 1; //1为没有 check 0 为已经check 2为已经与提成标准匹配好  默认为1	
		this.checkedTime = ""; //check的时间，默认为null	
		this.checkOrderId = 0; //check后对应的orderID
	}

	public Double getBackPoint() {
		return backPoint;
	}

	public void setBackPoint(Double backPoint) {
		this.backPoint = backPoint;
	}

	public void removeCharecterFromType(){
		if(!StringUtill.isNull(saleManName)){
			setSaleManName(getSaleManName().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")","").replace("）", "").replace("（", ""));
		}
		setType(getType().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")","").replace("）", "").replace("（", ""));
	}
}
