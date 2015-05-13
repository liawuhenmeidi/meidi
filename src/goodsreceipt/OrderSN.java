package goodsreceipt;

import enums.ProductModel;

public class OrderSN {
	private int id ;
	  private String orderNum ;   // 订单编号
	  private String branchName;  // 库位名称 
	  
	  
	  private String goodType ;  // 商品类别
	 
	  private int goodtypeStatues; //
	  
	  private String goodpName ;  // 商品名称
	  private String goodNum ;  // 商品代码
	
	  private int num;  // 订单数量
	  private int inNum;  // 收货数量
	   
	  private int statues ;   //收货状态
	  
	  private String statuesName ; // 
      
	  public String getStatuesName() {
		return statuesName;
	}

	public void setStatuesName(String statuesName) {
		this.statuesName = statuesName;
	}

	private int disable ; 
	  
	  private String uuid ;
	   
	  private String starttime ;
	  
	  private String endtime ;
 

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDisable() {
		return disable;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getGoodType() {
		return goodType;
	}

	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}

	public String getGoodpName() {
		return goodpName;
	}

	public void setGoodpName(String goodpName) {
		this.goodpName = goodpName;
	}

	public String getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getInNum() {
		return inNum;
	}

	public void setInNum(int inNum) {
		this.inNum = inNum;
	}

	public int getStatues() {
		return statues;
	}

	public void setStatues(int statues) {
		this.statues = statues;
	}
 
	public int getGoodtypeStatues() {
		if(ProductModel.Model.标准采购订单.equals(goodType)){  
			goodtypeStatues = ProductModel.Model.标准采购订单.getValue();
		}else if(ProductModel.Model.特价机采购订单.equals(goodType)){
			goodtypeStatues = ProductModel.Model.特价机采购订单.getValue();
		}
		return goodtypeStatues;
	}

	
	  
	  
}
