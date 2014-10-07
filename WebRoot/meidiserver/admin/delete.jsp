<%@ page language="java" import="java.util.*,grouptype.*,branchtype.*,locate.*,category.*,branch.*,group.*,user.*,product.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>删除</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="style/css/bass.css" />
</head>
<body>  
<!--   头部开始   -->
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String method = request.getParameter("method");

if("category".equals(method)){
	String str = request.getParameter("id");
	System.out.println(str);
	CategoryManager.delete(str);
}else if("product".equals(method)){
	String str = request.getParameter("id");
	//System.out.println(str);
	ProductManager.delete(str);
}else if("juese".equals(method)){ //juesetype
	String str = request.getParameter("id");
	int i = GroupManager.delete(str);    
	response.getWriter().write(""+i); 
	response.getWriter().flush();
	response.getWriter().close();   
}else if("juesetype".equals(method)){ //juesetype
	String str = request.getParameter("id"); 
	int i = GrouptypeManager.delete(user,str);       
	response.getWriter().write(""+i); 
	response.getWriter().flush();
	response.getWriter().close();
}else if("branch".equals(method)){
	String str = request.getParameter("id");
	//System.out.println(str);
	BranchManager.delete(str);
}else if("branchtype".equals(method)){
	String str = request.getParameter("id");
	//System.out.println(str); 
	boolean flag = BranchTypeManager.delete(str);
	response.getWriter().write(""+flag);  
	response.getWriter().flush();
	response.getWriter().close();
}else if("locate".equals(method)){
	String str = request.getParameter("id");
	System.out.println(str);
	LocateManager.delete(str);
}
 %>
</body>
</html>
