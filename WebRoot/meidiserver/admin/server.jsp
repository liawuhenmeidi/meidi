<%@ page language="java" import="java.util.*,orderproduct.*,product.*,message.*,inventory.*,branchtype.*,user.*,utill.*,locate.*,branch.*,order.*,orderPrint.*,category.*,group.*,grouptype.*;" pageEncoding="utf-8"%>
<%

request.setCharacterEncoding("utf-8");

User user = (User)session.getAttribute("user");
// peidan
String method = request.getParameter("method");
 
if("peidan".equals(method)){ 
	String uid = request.getParameter("uid"); 
	String id = request.getParameter("id");   
	int statues = OrderManager.updateStatues(user,method,Integer.valueOf(uid), id);  
	//int i = OrderManager.updatePeidan(Integer.valueOf(uid), Integer.valueOf(id));
	response.getWriter().write(""+statues); 
	response.getWriter().flush();  
	response.getWriter().close();  // deleteOrder
	
}else if("deleteOrder".equals(method)){ 
	String id = request.getParameter("id");
	String[] list = id.split(",");
	int count = 0 ; 
	for(int i=0;i<list.length;i++){ 
		boolean flag = OrderManager.delete(user,Integer.valueOf(list[i]));
	    if(flag){
	    	count ++ ;
	    }
	} 
	response.getWriter().write(""+count);  
	response.getWriter().flush();   
	response.getWriter().close();    
}else if("dingdaned".equals(method)){  
	String id = request.getParameter("id"); 
	String oid = request.getParameter("oid");
	String statues = request.getParameter("statues");
	String uid = request.getParameter("uid");
	if(StringUtill.isNull(uid)){ 
		uid = "-1";    
	}     
	System.out.println("uid"+uid); 
	boolean flag= OrderPrintlnManager.updateOrderStatues(user,Integer.valueOf(id),Integer.valueOf(oid),Integer.valueOf(uid),Integer.valueOf(statues)); 
	response.getWriter().write(""+flag); 
	response.getWriter().flush(); 
	response.getWriter().close(); 
}else if("dealshifang".equals(method)){   
	String statues = request.getParameter("statues"); 
	String oid = request.getParameter("oid"); 
	String uid = request.getParameter("uid"); 
	System.out.println(oid+"**"+uid+"**"+statues);
	
	OrderManager.updateShifang(user,Integer.valueOf(oid),Integer.valueOf(uid),Integer.valueOf(statues));    
	//boolean flag= OrderPrintlnManager.updateOrderStatues(user,Integer.valueOf(id),Integer.valueOf(oid),Integer.valueOf(uid),Integer.valueOf(statues)); 
	//response.getWriter().write(""+flag); 
	
	//response.getWriter().flush(); 
	//response.getWriter().close(); 
}else if("category_add".equals(method)){     
	String categoryName = request.getParameter("categoryName");
	boolean b = CategoryManager.getName(categoryName);
	response.getWriter().write(""+b);
	response.getWriter().flush();
	response.getWriter().close();
	//updatePhone
}else if("updatePhone".equals(method)){     
	String uid = request.getParameter("uid");
	String phone = request.getParameter("phone");
	boolean b = UserManager.updatePhone(uid, phone);
	response.getWriter().write(""+b);
	response.getWriter().flush();
	response.getWriter().close();
	//updatePhone
}else if("locate".equals(method)){ 
	String locateName = request.getParameter("id");
	LocateManager.save(locateName);
}else if("jusetype".equals(method)){ 
	String jueseName = request.getParameter("id");
	GrouptypeManager.save(jueseName); 
}else if("branch".equals(method)){ // branchis 
	String locateName = request.getParameter("id");
	String pid = request.getParameter("pid");
	//BranchManager.save(locateName,pid);        
}else if("branchis".equals(method)){ // branchis 
	String locateName = request.getParameter("id");
	String pid = request.getParameter("pid");
	boolean flag = BranchManager.isname(locateName);  
	response.getWriter().write(""+flag);
	response.getWriter().flush(); 
	response.getWriter().close();  
}else if("branchtype".equals(method)){ 
	String locateName = request.getParameter("id");
	System.out.println(locateName);  
	BranchTypeManager.save(locateName);  
	//branchtypeupdate
}else if("branchtypeupdate".equals(method)){  
	String bid = request.getParameter("bid");
	String c = request.getParameter("id");
	BranchTypeManager.update(c, bid) ; 
	//branchinventory
}else if("branchinventory".equals(method)){  
	String bid = request.getParameter("bid");
	String statues = request.getParameter("statues"); 
	BranchManager.update(bid,statues) ;   
	 //branchtypeinventory
}else if("branchtypeinventory".equals(method)){  
	String bid = request.getParameter("bid"); 
	String statues = request.getParameter("statues"); 
	BranchTypeManager.update(Integer.valueOf(statues), bid);    
	 //branchtypeinventory
}else if("grouptypeupdate".equals(method)){ 
	String bid = request.getParameter("bid");
	String c = request.getParameter("id");
	GrouptypeManager.update(c, bid) ;    
	//branchtypeupdate
}else if("jihuo".equals(method)){ 
	String id = request.getParameter("id");
	String statues = request.getParameter("statues");
	UserManager.setStatues(Integer.valueOf(id), Integer.valueOf(statues));
}else if("duanhuo".equals(method)){ 
	String id = request.getParameter("id"); 
	String statues = request.getParameter("statues");
	CategoryManager.setStatues(Integer.valueOf(id), Integer.valueOf(statues));
}else if("huiyuan_add".equals(method)){ 
	System.out.println("&&&&&"); 
	String categoryName = request.getParameter("huiyuanName");
	boolean b = UserManager.getName(categoryName);
	response.getWriter().write(""+b);
	response.getWriter().flush(); 
	response.getWriter().close();   
}else if("orderDingma".equals(method) || "orderGo".equals(method) || "orderCome".equals(method) || "orderCharge".equals(method)  || "print2".equals(method) || "print3".equals(method)   || "print4".equals(method)  || "print".equals(method) || "orderover".equals(method) || "statuescallback".equals(method) || "statuespaigong".equals(method) || "statuesinstall".equals(method) || "statuesinstalled".equals(method) || "printdingma".equals(method) || "statuescallback".equals(method) || "wenyuancallback".equals(method)){  
	String id = request.getParameter("id");   
	int statues = OrderManager.updateStatues(user,method,Order.query, id);  
	response.getWriter().write(""+statues); 
	response.getWriter().flush();   
	response.getWriter().close();  //orderDingma
}else if("huiyuan_zhuguan".equals(method)){ 
	System.out.println(12);	 	 
	String type = request.getParameter("type"); 
	List<String> list = UserManager.getUsersregist(Integer.valueOf(type)); 
	String str = StringUtill.GetJson(list);	
	response.getWriter().write(str); 
	response.getWriter().flush();   
	response.getWriter().close();
}else if("juese_add".equals(method)){ 
	String categoryName = request.getParameter("jueseName");
	boolean b = GroupManager.getName(categoryName);
	response.getWriter().write(""+b);
	response.getWriter().flush(); 
	response.getWriter().close();
}else if("headremind".equals(method)){
	HashMap<String,Integer> map = new HashMap<String,Integer>(); 
	List<User> ulist = UserManager.getUserszhuce(user);
	int ucount = 0 ;     
	if(ulist != null){  
		ucount = UserManager.getUserszhuce(user).size(); 
	} 
	int mcount = OrderManager.getOrderlistcount(user,Group.dealSend,Order.motify ,0,0,"id",""); 
	int rcount = OrderManager.getOrderlistcount(user,Group.dealSend,Order.release,0,0,"id","");
	int ncount = OrderManager.getOrderlistcount(user,Group.dealSend,Order.neworder,0,0,"id",""); 
	int recount = OrderManager.getOrderlistcount(user,Group.dealSend,Order.returns,0,0,"id",""); 
	int hcount = OrderManager.getOrderlistcount(user,Group.dealSend,Order.huanhuo,0,0,"id",""); 
	int inventory = InventoryManager.getCategory(user,"unconfirmed").size(); 
	
	map.put("ucount", ucount);
	map.put("mcount", mcount);
	map.put("rcount", rcount);
	map.put("ncount",ncount);    
	map.put("recount",recount); 
	map.put("hcount",hcount); 
	map.put("inventory",inventory);
	String strmap = StringUtill.GetJson(map);
	response.getWriter().write(strmap);  
	response.getWriter().flush(); 
	response.getWriter().close();
}else if("disatchpHeadremind".equals(method)){  
	HashMap<String,Integer> map = new HashMap<String,Integer>(); 
	int dcount = OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.dispatch,0,0,"id","");  
	int icount = OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.installonly,0,0,"id","");   
	//int ncount = OrderManager.getOrderlistcount(user,Group.dealSend,Order.neworder,0,0,"id",""); 
	List<User> ulist = UserManager.getUserszhuce(user);
	int hcount = 0 ;     
	if(ulist != null){   
		hcount = UserManager.getUserszhuce(user).size(); 
	} 
	int inventory = InventoryManager.getCategory(user,"unconfirmed").size(); 
	
	int rcount = OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.release,0,0,"id",""); 
	map.put("dcount", dcount);
	map.put("icount", icount);  
	map.put("hcount",hcount);    
	map.put("rcount",rcount); 
	map.put("inventory",inventory);
	
	String strmap = StringUtill.GetJson(map);
	response.getWriter().write(strmap);  
	response.getWriter().flush(); 
	response.getWriter().close(); //inventorydetail
}else if("inventory".equals(method)){ 
	 
	String branch = request.getParameter("branch");
	String ctype = request.getParameter("ctype"); 
	String time = request.getParameter("time");
	
	List<InventoryBranch> list = InventoryBranchManager.getCategory(branch, ctype); 
	System.out.println(time+"time");
	if(!StringUtill.isNull(time)){ 
		session.setAttribute("inventorytime", time);
	}else {   
		session.removeAttribute("inventorytime");	
	}
	String str = StringUtill.GetJson(list); 
	response.getWriter().write(str);   
	response.getWriter().flush(); 
	response.getWriter().close(); //inventory
}else if("inventoryall".equals(method)){ 
	String str = "";  
	String branch = request.getParameter("branch"); 
	String category = request.getParameter("category"); 
	String product = request.getParameter("product");
	//System.out.println(product);

	List<InventoryBranch> list = null ;
    if(StringUtill.isNull(product)){
    	if(!StringUtill.isNull(branch)){
    		if(!NumbleUtill.isNumeric(branch)){
    			Branch b = BranchManager.getLocatebyname(branch);
    			branch = b.getId()+"";
    		} 
    	}
    	
    	list = InventoryBranchManager.getCategoryid(branch, category);  
    }else {
    	if(!NumbleUtill.isNumeric(product)){
			Product p = ProductService.gettypemap().get(product);
			product = p.getId()+"";
		} 
    	
    	
    	
    	if(!StringUtill.isNull(branch)){
    		System.out.println(branch);
    		if(NumbleUtill.isNumeric(branch)){
    			Branch b = BranchManager.getLocatebyid(branch);
    			branch = b.getLocateName();
    		}  
    		 
    		list = InventoryBranchManager.getCategory(branch, product);  
    	}else {
    		list = InventoryBranchManager.getCategory(branch, product);
    	}
    }
	
	if(StringUtill.isNull(category)){
	    Map<Integer,InventoryAll> map  = new HashMap<Integer,InventoryAll>();  
		 
	    for(int i=0;i<list.size();i++){ 
	    	InventoryBranch inb = list.get(i);
	    	int categoryid = inb.getInventoryid();
	    	InventoryAll listp= map.get(categoryid);  
	    	if(listp == null){
	    		listp = new InventoryAll();
	    		Category c = CategoryManager.getCategory(categoryid+"");
	    		listp.setCategoryid(c.getId());  
	    		listp.setCateoryName(c.getName());  
	    		listp.setTypeid(inb.getTypeid());
	    		listp.setPapercount(inb.getPapercount());   
	    		listp.setRealcount(inb.getRealcount()); 
	    		listp.setIsquery(inb.isquery()); 
	    		//System.out.println(inb.getPapercount()+"***"+inb.getRealcount());
	    		map.put(categoryid, listp); 
	    	}else {  
	    		//System.out.println(inb.getPapercount()+"***"+inb.getRealcount());
	    		listp.setPapercount(listp.getPapercount()+inb.getPapercount());
	    		listp.setRealcount(listp.getRealcount()+inb.getRealcount());
	    		listp.setIsquery(listp.getIsquery()&&inb.isquery());
	    	}
	    }
		 
		Collection<InventoryAll> c = map.values();
		str = StringUtill.GetJson(c);
	}else if(!StringUtill.isNull(category)){
        Map<String,InventoryAll> map  = new HashMap<String,InventoryAll>();   
		
	    for(int i=0;i<list.size();i++){ 
	    	InventoryBranch inb = list.get(i);
	    	int categoryid = inb.getInventoryid();
	    	String type = inb.getType();
	    	InventoryAll listp= map.get(type);  
	    	if(listp == null){
	    		listp = new InventoryAll();  
	    		Category c = CategoryManager.getCategory(categoryid+"");
	    		listp.setCategoryid(c.getId());
	    	    listp.setType(type); 
	    	    listp.setTypeid(inb.getTypeid());
	    		listp.setCateoryName(c.getName());  
	    		listp.setPapercount(inb.getPapercount()); 
	    		listp.setRealcount(inb.getRealcount());
	    		listp.setIsquery(inb.isquery());
	    		if(!StringUtill.isNull(branch)){
		    		listp.setTime(inb.getQuerymonth());
		    	}
	    		
	    		map.put(type, listp);
	    	}else { 
	    		listp.setPapercount(listp.getPapercount()+inb.getPapercount());
	    		listp.setRealcount(listp.getRealcount()+inb.getRealcount());
	    		listp.setIsquery(listp.getIsquery()&&inb.isquery());
	    	}
	    }
	    Collection<InventoryAll> c = map.values();
		str = StringUtill.GetJson(c);
	}     
	//System.out.println(str+""); 
	response.getWriter().write(str);   
	response.getWriter().flush(); 
	response.getWriter().close(); //inventory
}else if("inventorydis".equals(method)){ 
	String str = "";  
	String branch = request.getParameter("branch"); 
	String category = request.getParameter("category"); 
    String type = request.getParameter("type");
    if(StringUtill.isNull(type)){ 
		List<InventoryBranch> list = InventoryBranchManager.getCategoryid(branch, category); 
	    Map<String,InventoryAll> map  = new HashMap<String,InventoryAll>();   
			
		    for(int i=0;i<list.size();i++){ 
		    	InventoryBranch inb = list.get(i);
		    	int categoryid = inb.getInventoryid();
		    	int branchid = inb.getBranchid();
		    	InventoryAll listp= map.get(branchid+"");    
		    	if(listp == null){ 
		    		listp = new InventoryAll();   
		    		Category c = CategoryManager.getCategory(categoryid+"");
		    		listp.setCategoryid(c.getId());
		    		listp.setCateoryName(c.getName());
		    		listp.setBranchid(branchid); 
		    		listp.setTypeid(inb.getTypeid());
		    		listp.setPapercount(inb.getPapercount()); 
		    		listp.setRealcount(inb.getRealcount()); 
		    		listp.setIsquery(inb.isquery());
		    		map.put(branchid+"", listp);
		    	}else {       
		    		listp.setPapercount(listp.getPapercount()+inb.getPapercount());
		    		listp.setRealcount(listp.getRealcount()+inb.getRealcount());
		    		listp.setIsquery(listp.getIsquery()&&inb.isquery());
		    	}
		  
		    Collection<InventoryAll> c = map.values();
			str = StringUtill.GetJson(c);
		}   
     }else { 
    	List<InventoryBranch> list = InventoryBranchManager.getCategory(branch, type); 
 	    Map<String,InventoryAll> map  = new HashMap<String,InventoryAll>();   
 			 
 		    for(int i=0;i<list.size();i++){ 
 		    	InventoryBranch inb = list.get(i); 
 		    	int categoryid = inb.getInventoryid();
 		    	int branchid = inb.getBranchid();
 		    	InventoryAll listp= map.get(branchid+"");     
 		    	if(listp == null){ 
 		    		listp = new InventoryAll();   
 		    		Category c = CategoryManager.getCategory(categoryid+"");
 		    		listp.setCategoryid(c.getId()); 
 		    		listp.setCateoryName(c.getName());
 		    		listp.setType(inb.getType()); 
 		    		listp.setTypeid(inb.getTypeid());
 		    		listp.setBranchid(branchid);    
 		    		listp.setPapercount(inb.getPapercount()); 
 		    		listp.setRealcount(inb.getRealcount()); 
 		    		listp.setIsquery(inb.isquery());
 			    	listp.setTime(inb.getQuerymonth());
 		    		map.put(branchid+"", listp);  
 		    	}else {        
 		    		listp.setPapercount(listp.getPapercount()+inb.getPapercount());
 		    		listp.setRealcount(listp.getRealcount()+inb.getRealcount());
 		    		listp.setIsquery(listp.getIsquery()&&inb.isquery());
 		    	}
 		  
 		    Collection<InventoryAll> c = map.values();
 			str = StringUtill.GetJson(c);
 		}    
     }	    
	System.out.println(str+""); 
	response.getWriter().write(str);   
	response.getWriter().flush(); 
	response.getWriter().close(); //inventory
}else if("inventorydetail".equals(method)){  

	String ctype = request.getParameter("ctype"); 
	String branchid = request.getParameter("branchid");
	String time = request.getParameter("time");
	if(StringUtill.isNull(time)){ 
		time = "";
	}
	List<InventoryBranchMessage > list = InventoryBranchMessageManager.getCategory(ctype,branchid,time);  
	  
	String str = StringUtill.GetJson(list);
	response.getWriter().write(str);   
	response.getWriter().flush(); 
	response.getWriter().close(); //inventory
}else if("getinventory".equals(method)){ 
	String types = request.getParameter("types"); 
	String uid = request.getParameter("uid");  
	Map<String,String> map = new HashMap<String,String>(); 
	if(!StringUtill.isNull(types) && !StringUtill.isNull(uid)){
		User u = UserManager.getUesrByID(uid); 
		List<InventoryBranch> list = InventoryBranchManager.getCategory(u.getBranch(), "");  
		String[] type = types.split("</p>");
		for(int i = 0 ; i<type.length;i++){
			if(!StringUtill.isNull(type[i])){ 
				map.put(type[i], "账面库存："+0+"实际库存:"+0);
				for(int j=0;j<list.size();j++){
					InventoryBranch inb = list.get(j); 

//System.out.println(type[i]+"***"+inb.getType());
					if(inb.getType().equals(type[i])){ 
						map.put(type[i], "账面库存："+inb.getPapercount()+"   实际库存:"+inb.getRealcount());
					} 
				}
			}
		} 
	}
	String str = StringUtill.GetJson(map);
	response.getWriter().write(str);   
	response.getWriter().flush(); 
	response.getWriter().close(); //inventory
	
}else if("quit".equals(method)){  
	session.invalidate();
	response.sendRedirect("login.jsp");
	return ;   
}else if("updateorder".equals(method)){
	int statues = -1 ;
	int pstatues = 0 ;
	String oid = request.getParameter("oid"); 
	String phone1 = request.getParameter("phone1");
	String andate = request.getParameter("andate");
	String diqu = request.getParameter("diqu");
	String locations = request.getParameter("locations");
	String message = request.getParameter("message");
	String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
	if(UserManager.checkPermissions(user, Group.dealSend)){
		String POS = request.getParameter("pos");
		String sailId = request.getParameter("sailid"); 
		String check = request.getParameter("check");
		String saledate = request.getParameter("saledate");
		String saleType = request.getParameter("dingmatype"); 
		String categoryId = request.getParameter("dingmaordercategory");
		String count = request.getParameter("dingmaproductNum");
		statues = OrderManager.updateMessage(phone1,andate,locations,POS,sailId,check,oid,remark,saledate,diqu) ; 
	    if(!StringUtill.isNull(saleType) && !StringUtill.isNull(categoryId)){
	    	Order order = new Order();
	    	order.setId(Integer.valueOf(oid));
	    	String type = order.getSendType(1, "");

		    	if(StringUtill.isNull(type)){
		    		
		    		OrderProduct o = new OrderProduct();
		    		 if(!NumbleUtill.isNumeric(saleType)){
						   type = ProductService.gettypemap().get(saleType).getId()+"";
					   }
		    
		    		    o.setCategoryId(Integer.valueOf(categoryId));
						o.setCount(Integer.valueOf(count));
						o.setSaleType(type);	
						o.setStatues(1); 
						
						List<OrderProduct>  listop = new ArrayList<OrderProduct>();
						listop.add(o);
						order.setOrderproduct(listop);
						
						List<String> sqls = OrderProductManager.save(Integer.valueOf(oid), order);
						DBUtill.sava(sqls);
		    	}else {
		    		
		    		pstatues = OrderProductManager.updateOrderStatues(user,categoryId,saleType,count,oid);
		    	}
	    }else {
    		String sql = OrderProductManager.delete(Integer.valueOf(oid),1);
    		DBUtill.sava(sql); 
    	} 
	    OrderProductManager.resetOrPMap(); 
	}else {    
		statues = OrderManager.updateMessage(phone1,andate,locations,oid,remark);  
	} 
	  
	if(!StringUtill.isNull(message)){
	    Message  messa = new Message();  
		messa.setOid(Integer.valueOf(oid)); 
		messa.setMessage(user.getUsername()+":"+message+"\n");     
		MessageManager.save(user,messa);        
	}    
	//response.sendRedirect("dingdanDetail.jsp?id="+oid);
	//response.sendRedirect(""); 
	 
	if(statues == -1 ||  pstatues == -1){  
		response.sendRedirect("../jieguo.jsp?type=update");
		//System.out.println(123);  
		session.setAttribute("message", "修改失败");
		 
	}else {
		response.sendRedirect("../jieguo.jsp?type=updated");
		//System.out.println(123);   
		session.setAttribute("message", "修改成功");
	}
	return ;   
}else if("pandian".equals(method)){
	//data:"method=pandian&branchid="+branchid+"&type="+type,
	String branchid = request.getParameter("branchid"); 
    String  type = request.getParameter("type"); 
	InventoryBranchManager.update(user, branchid, type);
}

%>
