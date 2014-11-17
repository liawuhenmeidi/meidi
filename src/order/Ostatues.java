package order;

import java.util.Map;

import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderPrint.OrderPrintlnService;
import user.User;

public class Ostatues { 
	 public int totalshifang = -1 ; 
     public OrderPrintln orp = null ;  
     public OrderPrintln op1 = null;
     public OrderPrintln huanhuoObject = null;
     public OrderPrintln salereleaseo = null;
     public OrderPrintln salereleasereturno = null;
     public OrderPrintln releaseo = null;
     public OrderPrintln salereleaseanzhuango = null;
      
     public int returns = -1;
     public int huanhuo = -1;
     public int releasedispatch = -1;
     public int salereleasesonghuo = -1;
     public int release = -1;
     public int salereleaseanzhuang= -1;
     public int salerelease = -1;
	 public Ostatues(Order o){ 
	    Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnService.getmap(new User());
		op1 = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.returns, o.getId()) ;
	    huanhuoObject = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.huanhuo, o.getId()) ;
	    salereleaseo =OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.salerelease, o.getId()) ;
	    
	    salereleaseanzhuango = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.salereleaseanzhuang, o.getId()) ; 
	    salereleasereturno = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.salereleasereturn, o.getId()) ;
	    
	     
	    returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, o.getId());
	    release = OrderPrintlnManager.getstatues(opmap, OrderPrintln.release, o.getId());
	    huanhuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.huanhuo, o.getId());
	    releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId());
	    salereleasesonghuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleasesonghuo, o.getId());
	    salereleaseanzhuang	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleaseanzhuang, o.getId());
	     salerelease = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salerelease, o.getId()) ;
	   
	    
	    
	    
	    
	    if(release != -1){     
	    	totalshifang = release ;
	    	orp = opmap.get(OrderPrintln.release).get(o.getId()); 
	    	
	    } 
	    if(salereleasesonghuo != -1){
	    	totalshifang = salereleasesonghuo ;
	    	orp = opmap.get(OrderPrintln.salereleasesonghuo).get(o.getId()); 
	    } 
	    if(salereleaseanzhuang != -1){
	    	totalshifang = salereleaseanzhuang ;
	    	orp = opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId()); 
	    }	 
	 }
	    
    public static int getstatues(Map<Integer,Map<Integer,OrderPrintln>> opmap,int type ,int orderid){
		   int statues = -1 ;  
		   if(opmap.get(type) != null){
				OrderPrintln op = opmap.get(type).get(orderid);
				 if(op != null){  
					 statues = op.getStatues() ;
					 } 
				 }  
		   
		   return statues ;
	   }
}
