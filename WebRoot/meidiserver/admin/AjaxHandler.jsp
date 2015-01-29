<%@page import="wilson.catergory.CatergoryMaping"%>
<%@page import="wilson.catergory.CatergoryManager"%>
<%@ page language="java" import="java.util.*,utill.*,orderproduct.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");


//action
String action = request.getParameter("action");

if(!StringUtill.isNull(action)) {
	
	if(action.equals("getprice")){
		
		String content = request.getParameter("content");
		response.getWriter().write(OrderProductService.getPrice(content));
		
	}else if(action.equals("clearsession")){
		
		request.getSession().setAttribute("type_transList", null);
		request.getSession().setAttribute("sendType_changed_List", null);
		
	}else if(action.equals("sendtypeswitch")){
		
		String tmp_bool = request.getParameter("value");
		//System.out.println(tmp_bool);
		
		request.getSession().setAttribute("sendtypeswitch",tmp_bool);
		
	}
}
%>
