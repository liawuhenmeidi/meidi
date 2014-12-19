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
if("dingdaned".equals(method)){   
	String id = request.getParameter("id"); 
	String oid = request.getParameter("oid");  
	OrderPrintlnManager.updateOrderStatues(user,Integer.valueOf(id),Integer.valueOf(oid),user.getId(),OrderPrintln.comited);
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
	OrderManager.delete(user,Integer.valueOf(oid));
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
