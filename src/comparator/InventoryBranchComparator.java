package comparator;


import inventory.InventoryBranch;

import java.util.Comparator;

import utill.StringUtill;

 
public class InventoryBranchComparator implements Comparator<InventoryBranch> {

	@Override  
	public int compare(InventoryBranch o1, InventoryBranch o2) {
		String o1t = StringUtill.getStringNocn(o1.getType());
		String o2t = StringUtill.getStringNocn(o1.getType());
		
		if(o1t.hashCode() > o2t.hashCode()){
			return 1 ;
		}else {
			return 0;
		}
	}
   

}
