package goodsreceipt;

import enums.ProductModel;
import enums.SaleModel;
import branch.Branch;
import branch.BranchService;
import product.Product;
import product.ProductService;
import utill.StringUtill;
  
public class GoodsReceipt extends OrderSNCommon { 
  

	private String receveid; // 苏宁收货单号  || 苏宁发货单号
	private String receveTime; // 收货日期  || 苏宁发货日期
	private String sendid; // 供应商发货单号
	private String buyid; // 采购订单号   || 退货订单号 
	private String ordertype; // 订单类型 
	
	
	private int recevenum; // 实收数量  || 实发数量
	private int refusenum;// 拒收数量
	
    
    private int statues ;  // 0 入库   1  退货  
   
    private int goodtypeStatues; //
     
       
    public int getGoodtypeStatues() {     
    	//System.out.println(ordertype);    
    	//System.out.println(ProductModel.Model.特价机采购订单.toString().equals(ordertype));  
		if(ProductModel.Model.标准采购订单.toString().equals(ordertype)){     
			goodtypeStatues = ProductModel.Model.标准采购订单.getValue();
		}else if(ProductModel.Model.特价机采购订单.toString().equals(ordertype)){
			goodtypeStatues = ProductModel.Model.特价机采购订单.getValue();
		}else if(ProductModel.Model.样机采购订单.toString().equals(ordertype)){
			goodtypeStatues = ProductModel.Model.样机采购订单.getValue();
		}   
		//System.out.println(goodtypeStatues);   
		return goodtypeStatues;
	}
    
	
	public String getReceveid() {
		return receveid;
	}

	public void setReceveid(String receveid) {
		this.receveid = receveid;
	}

	public String getReceveTime() {
		return receveTime;
	}

	public void setReceveTime(String receveTime) {
		this.receveTime = receveTime;
	}

	public String getSendid() {
		return sendid;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public String getBuyid() {
		return buyid;
	}

	public void setBuyid(String buyid) {
		this.buyid = buyid;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
    public String getStatuesName(){
    	String str = "";
    	if(1 == statues){ 
    		str = "退货";
    	}else if(0 == statues){
    		str = "入库";
    	} 
    	return str;
    }
    
	
	

	public int getRecevenum() {
		return recevenum;
	}

	public void setRecevenum(int recevenum) {
		this.recevenum = recevenum;
	}

	public int getRefusenum() {
		return refusenum;
	}

	public void setRefusenum(int refusenum) {
		this.refusenum = refusenum;
	}
 
	

	public int getStatues() {
		return statues;
	}

	public void setStatues(int statues) {
		this.statues = statues;
	}
  
}
