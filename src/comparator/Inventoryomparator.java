package comparator;

import java.util.Comparator;
 
import httpClient.download.SNInventory;
  
public class Inventoryomparator implements Comparator{
		   
	    public int compare(Object o1, Object o2) {  
	    	
	    	System.out.println("********");
	        if(null!=o1&&null!=o2)   
	        {      
	        	SNInventory menu1=(SNInventory)o1;  
	        	SNInventory menu2=(SNInventory)o2;  
	        	   
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
	    
	    /*public int compare(Object o1, Object o2) { 
	    	int num = 1 ;
	        if(null!=o1&&null!=o2)   
	        {     
	        	SNInventory menu1=(SNInventory)o1;  
	        	SNInventory menu2=(SNInventory)o2;   
	        	// System.out.println(menu1.getDynamic());  
	        	// System.out.println(menu2.getDynamic()); 
	           if(menu1.getDynamic() > menu2.getDynamic()){
	        	   return 0 ; 
	           }else if(menu1.getDynamic() < menu2.getDynamic()){
	        	   return 1 ;  
	           }else if(menu1.getDynamic() == menu2.getDynamic()){
	        	   if(menu1.getDynamic() >= menu2.getDynamic()){
	        		   return 0 ; 
	        	   }else { 
	        		   return 1 ;
	        	   }
	           }  
	           
	            
	        }   
	        return num;  
	    }  */

}
