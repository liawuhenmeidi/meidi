package goodsreceipt;
 
public class OrderReceiptAll { 
    private String buyid ;
    private String checkNum;
    
    private String receveTime; // 退货订单日期 
    private String activeordertiem ; 
    
    
	public String getBuyid() {
		return buyid;
	}
	public void setBuyid(String buyid) {
		this.buyid = buyid;
	} 
	public String getCheckNum() {
		return checkNum; 
	}
	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}
	public String getReceveTime() {
		return receveTime;
	}
	public void setReceveTime(String receveTime) {
		this.receveTime = receveTime;
	}
	public String getActiveordertiem() {
		return activeordertiem;
	}
	public void setActiveordertiem(String activeordertiem) {
		this.activeordertiem = activeordertiem;
	}
    
    
}
