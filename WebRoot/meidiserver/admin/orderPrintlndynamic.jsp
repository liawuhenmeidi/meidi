 
 <%  
        int totalshifang = -1 ; 
        OrderPrintln orp = null ; 
        OrderPrintln op = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.modify, o.getId()) ;
        OrderPrintln op1 = OrderPrintlnManager.getOrderPrintln(opmap, OrderPrintln.returns, o.getId()) ;
         
        int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, o.getId()) ;
	    int returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, o.getId());
	    int releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId());
	    int salereleasesonghuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleasesonghuo, o.getId());
	    int release	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.release, o.getId());
	    int salereleaseanzhuang	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.salereleaseanzhuang, o.getId());
	    int releasemodfy	= OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasemodfy, o.getId());
	    
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
       %>