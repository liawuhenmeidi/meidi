package order;

import branch.Branch;
import branch.BranchService;

public class OrderService {
   
	public static String getBranchName(int id){
		
		Branch b= BranchService.getMap().get(id);
		return b.getLocateName();
	}
	

	
}
