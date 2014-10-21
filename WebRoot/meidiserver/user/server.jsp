<%@ page language="java" import="java.util.*,user.*,order.*,orderPrint.*;" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head> 
 
<%
request.setCharacterEncoding("utf-8");
boolean flag = true ;
User user = (User)session.getAttribute("user");
String method = request.getParameter("method");
System.out.println("*************method"+method);
if("songhuo".equals(method)){
	String statues = request.getParameter("statues");
	String id = request.getParameter("id"); 
	String type = request.getParameter("type");
	if(type.equals(""+Order.returns)){
		method = "tuihuo"; 
	} 
	int statuesflag = OrderManager.updateStatues(user,method,Integer.valueOf(statues), id);  
	response.getWriter().write(""+statuesflag);
	response.getWriter().flush(); 
	response.getWriter().close(); 



}else if("dingdaned".equals(method)){  
	String id = request.getParameter("id"); 
	String oid = request.getParameter("oid");  
	OrderPrintlnManager.updateOrderStatues(user,Integer.valueOf(id),Integer.valueOf(oid),user.getId(),OrderPrintln.comited);
}else if("shifang".equals(method)){   
	String id = request.getParameter("oid");
	String pGroupId = request.getParameter("pGroupId");
	String opstatues = request.getParameter("opstatues");
	System.out.println("*************"+id+"****"+opstatues);  
	//OrderManager.updateSendstad(user, id);  
	OrderPrintln  or = new OrderPrintln(); 
	or.setOrderid(Integer.valueOf(id));

	if(Integer.valueOf(opstatues) == OrderPrintln.releasedispatch){
		or.setMessage("文员申请退货");  
	}else if(Integer.valueOf(opstatues) == OrderPrintln.releasemodfy){
		or.setMessage("文员申请释放");   
	}else { //OrderPrintln.releasemodfy
		or.setMessage("释放");   
	} 
	or.setStatues(OrderPrintln.comit);
	or.setType(Integer.valueOf(opstatues));     
	or.setpGroupId(Integer.valueOf(pGroupId));  
	OrderPrintlnManager.save(or);  
}else if("peidan".equals(method)){     
	String uid = request.getParameter("uid");
	String id = request.getParameter("id"); 
	String type = request.getParameter("type"); 
	//System.out.println(uid+"****"+id+"****"+type);       
	int i = OrderManager.updatePeisong(user,Integer.valueOf(uid), Integer.valueOf(id),Integer.valueOf(type));
	response.getWriter().write(""+i);
	response.getWriter().flush(); 
	response.getWriter().close();    
}else if("printlnStatues".equals(method)){  
	String oid = request.getParameter("oid"); 
	//OrderPrintln  or = new OrderPrintln(); 
	//or.setOrderid(Integer.valueOf(oid)); 
	//or.setType(OrderPrintln.unmodify);
	//OrderPrintlnManager.save(or);   
	 
	Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));  
	response.getWriter().write(""+order.getPrintSatues());
	response.getWriter().flush();  
	response.getWriter().close();
}else if("pos".equals(method) || "phone1".equals(method) || "sailId".equals(method) || "checked".equals(method)){   
	String name = request.getParameter("name"); 
	String branch = request.getParameter("branch"); 
	boolean b = OrderManager.getName(method,name,branch);     
	response.getWriter().write(""+b);
	response.getWriter().flush(); 
	response.getWriter().close();
}else if("tuihuo".equals(method)){   
	String oid = request.getParameter("oid"); 
	OrderManager.delete(Integer.valueOf(oid));
	response.sendRedirect("serch_list.jsp");  
}else if("tuihuoed".equals(method)){   
	String oid = request.getParameter("oid"); 
	OrderManager.deleteed(Integer.valueOf(oid));  
	response.sendRedirect("serch_list.jsp");  
}else if("quit".equals(method)){   
	session.invalidate(); 
	response.sendRedirect("dengluN.jsp");
	return ;
}
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
