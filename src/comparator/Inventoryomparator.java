package comparator;

import java.util.Comparator;
 
import httpClient.download.Inventory;
  
public class Inventoryomparator implements Comparator{
		  
	    public int compare(Object o1, Object o2) {  
	        if(null!=o1&&null!=o2)   
	        {    
	        	httpClient.download.Inventory menu1=(Inventory)o1;  
	        	httpClient.download.Inventory menu2=(Inventory)o2;  
	        	 
	           if(menu1.getSaleNum() > menu2.getSaleNum()){
	        	   return 0 ; 
	           }else if(menu1.getSaleNum() < menu2.getSaleNum()){
	        	   return 1 ;  
	           }else if(menu1.getSaleNum() == menu2.getSaleNum()){
	        	   if(menu1.getNum() >= menu2.getNum()){
	        		   return 0 ;
	        	   }else { 
	        		   return 1 ;
	        	   }
	           }  
	           
	            
	        }  
	        return 1;  
	    }  
}
