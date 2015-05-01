package goodsreceipt;

import java.util.Set;

import utill.StringUtill;
 
public class OrderReceiptAll { 
    private String buyid ;
    private String checkNum;
    
    private String receveTime; // 退货订单日期 
    private String activeordertiem ; 
      
    private Set<Integer> printstatues; 
    private String printName;
       
	public String getPrintName() {   
		//System.out.println(StringUtill.GetJson(printstatues));
		if(null != printstatues && printstatues.size() != 0 ){
			if(printstatues.contains(0) ){ 
				if(printstatues.size() >1){  
					printName = "部分打印"; 
				}else { 
					printName = "未打印"; 
				}
			}else { 
				printName = "已打印";
			}
		}else {
			printName=""; 
		}
		return printName;
	}
	public void setPrintName(String printName) {
		this.printName = printName;
	}
	public Set<Integer> getPrintstatues() {
		return printstatues;
	}
	public void setPrintstatues(Set<Integer> printstatues) {
		this.printstatues = printstatues;
	}
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
