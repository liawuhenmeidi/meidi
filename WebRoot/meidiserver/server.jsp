<%@ page language="java" import="java.util.*,branchtype.*,user.*,utill.*,locate.*,branch.*,order.*,orderPrint.*,category.*,group.*,grouptype.*;" pageEncoding="utf-8"%>
<%

request.setCharacterEncoding("utf-8");

User user = (User)session.getAttribute("user");
// peidan  
String method = request.getParameter("method");
   
if("dingdaned".equals(method)){   
	String id = request.getParameter("id"); 
	String oid = request.getParameter("oid");
	String statues = request.getParameter("statues");   
	OrderPrintlnManager.updateOrderStatues(user,Integer.valueOf(id),Integer.valueOf(oid),user.getId(),Integer.valueOf(statues)); 
}else if("huiyuan_add".equals(method)){    
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
