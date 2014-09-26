package orderproduct;

public class OrderProduct {
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
