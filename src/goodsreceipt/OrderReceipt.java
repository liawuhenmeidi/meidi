package goodsreceipt;

import branch.Branch;
import branch.BranchService;
import product.Product;
import product.ProductService;
import utill.StringUtill;

public class OrderReceipt { 

	private int id;
	private String receveid; // 苏宁收货单号  || 苏宁发货单号
	private String receveTime; // 退货订单日期 
	private String sendid; // 供应商发货单号
	private String buyid;  // 退货订单号  
	private String ordertype; // 退货订单类型
	private String goodsnum; // 商品代码 
	private String goodsName; // 商品名称
	private int cid;  
	private int tid;  
	private int bid; 
	private int orderNum ; // 订单数量
	private int recevenum; //  实发数量
	private int refusenum;// 仍需数量
	private String branchid; // 地点
	private String branchName; // 地点名称   || 地点(仓库)名称
    private String uuid ;  // 唯一码  
    private int statues ;  // 0 入库   1  退货   
    private int disable ;   //  0 起作用  1  不起作用 
    private String queryNum ; //  送货确认书编号 
    private String checkNum;   // 校验码   
    private String ordertime ; // 退货订单日期 
    private String activeordertiem ; // 退货订单有效期
    private String pici; //  批次 
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
 
	public String getGoodsnum() {
		if(StringUtill.isNull(goodsnum)){
			Branch b = BranchService.getNameMap().get(goodsName);
			if(null != b){
				goodsnum = b.getEncoded();
			} 
		}
		return goodsnum;
	}

	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	} 

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
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
 
	public int getBid() { 
		Branch b = BranchService.getNumMap().get(getBranchid()); 
		if(null != b){
			bid = b.getId();  
		} 
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid; 
	}

	public String getBranchid() {
		if(StringUtill.isNull(branchid)){
			Branch b = BranchService.getNameMap().get(branchName); 
			if(null != b ){
				branchid = b.getEncoded();
			}
		} 
		return branchid;
	}

	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getCid() {
		Product p = ProductService.gettypeNUmmap().get(goodsnum);
		if(null != p){
			cid = p.getCategoryID(); 
		} 
		return cid;
	}

	public void setCid(int cid) {
		
		this.cid = cid;
	}
 
	public int getTid() {
		Product p = ProductService.gettypeNUmmap().get(goodsnum);
		if(null != p){
			tid = p.getId();
		}
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}
 
	public int getDisable() {
		return disable;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}

	public int getStatues() {
		return statues;
	}

	public void setStatues(int statues) {
		this.statues = statues;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getQueryNum() {
		return queryNum;
	}

	public void setQueryNum(String queryNum) {
		this.queryNum = queryNum;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public String getActiveordertiem() {
		return activeordertiem;
	}

	public void setActiveordertiem(String activeordertiem) {
		this.activeordertiem = activeordertiem;
	}

	public String getPici() {
		return pici;
	} 

	public void setPici(String pici) {
		this.pici = pici;
	}
  
}
