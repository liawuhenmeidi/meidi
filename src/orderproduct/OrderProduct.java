package orderproduct;


public class OrderProduct {
	public static int query = 1;
	public static int unquery = 0  ;
	public static int submit = 2  ;
	
	private int id ;
    private int categoryId ;
    private String categoryName;
    
    private String sendType;
    
	private String saleType;
	
    private  int orderid ;
    
    private  int count ;
    
    private int statues ;    // 1 顶码   0 非顶码
     
    private int salestatues ; //  1 需配送     0  已自提      2 只安装门店提货    3 只安装顾客已提
    
    private String subtime;   
    
    private String typeName ;
    
    private double price ;
    
    private  String batchNumber;  //批号
    
    private  String Barcode;  // 条码
    
    private  int isSubmit;  // 是否提交到苏宁售后      0 表示忽略  1 表示已经确认
    
    
	public int getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(int isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getBarcode() {
		return Barcode;
	}

	public void setBarcode(String barcode) {
		Barcode = barcode;
	}

	public double getPrice() {
		return price; 
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSalestatues(int i){
    	String str = "";
    	if(i == 1){
    		str = "需配送";
    	}else if(i == 0 ){
    		str = "已自提";
    	}else if(i == 2){
    		str = "只安装门店提货"; 
    	}else if(i == 3){
    		str = "只安装顾客已提";
    	} 
    	return str;
    }
    
	public String getSubtime() {
		return subtime; 
	}
	public void setSubtime(String subtime) {
		this.subtime = subtime;
	}
	public int getSalestatues() {
		return salestatues;
	}
	public void setSalestatues(int salestatues) {
		this.salestatues = salestatues;
	}
	public String getCategoryName() {
  		return categoryName;
  	}
  	public void setCategoryName(String categoryName) {
  		this.categoryName = categoryName;
  	}
    public int getCategoryId() {
		return categoryId;
	} 
	public int getStatues() {
		return statues;
	}
	public void setStatues(int statues) {
		this.statues = statues;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	
   
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
  public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
