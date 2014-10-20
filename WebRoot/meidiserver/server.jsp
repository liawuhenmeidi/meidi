<%@ page language="java" import="java.util.*,branchtype.*,user.*,utill.*,locate.*,branch.*,order.*,orderPrint.*,category.*,group.*,grouptype.*;" pageEncoding="utf-8"%>
<%

request.setCharacterEncoding("utf-8");

User user = (User)session.getAttribute("user");
// peidan 
String method = request.getParameter("method");
   
System.out.println("*************method"+method);

if("peidan".equals(method)){
	String uid = request.getParameter("uid");
	String id = request.getParameter("id"); 
	int statues = OrderManager.updateStatues(method,Integer.valueOf(uid), id);  
	//int i = OrderManager.updatePeidan(Integer.valueOf(uid), Integer.valueOf(id));
	response.getWriter().write(""+statues); 
	response.getWriter().flush();  
	response.getWriter().close(); 
}else if("dingdaned".equals(method)){  
	String id = request.getParameter("id"); 
	String oid = request.getParameter("oid");
	String statues = request.getParameter("statues");   
	OrderPrintlnManager.updateOrderStatues(user,Integer.valueOf(id),Integer.valueOf(oid),user.getId(),Integer.valueOf(statues)); 
}else if("huiyuan_add".equals(method)){ 
	System.out.println("&&&&&");   
	String categoryName = request.getParameter("huiyuanName");
	boolean b = UserManager.getName(categoryName);
	response.getWriter().write(""+b);
	response.getWriter().flush(); 
	response.getWriter().close();   
}else if("quit".equals(method)){  
	session.invalidate();
	response.sendRedirect("login.jsp");
	return ;  
}

%>
