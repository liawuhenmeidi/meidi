package comparator;

import inventory.InventoryMessage;

import java.util.Comparator;

import utill.StringUtill;

 
public class InventoryMessageComparator implements Comparator<InventoryMessage> {

	@Override 
	public int compare(InventoryMessage o1, InventoryMessage o2) {
		String o1t = StringUtill.getStringNocn(o1.getProductname());
		String o2t = StringUtill.getStringNocn(o2.getProductname());
		
		if(o1t.hashCode() > o2t.hashCode()){
			return 1 ;
		}else {
			return 0;
		}
	}
   

}
