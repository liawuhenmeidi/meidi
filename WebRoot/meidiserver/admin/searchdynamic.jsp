<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");  
String sort = request.getParameter("sort");   
String searched = request.getParameter("searched");
String sear = (String)session.getAttribute("sear"); 
 

if(StringUtill.isNull(sear)){
	sear = "";   
}

if(StringUtill.isNull(sort)){
	sort = "id desc";   
}

if(StringUtill.isNull(numb)){
	numb = "100";
} 

if(StringUtill.isNull(pageNum)){
	pageNum = "1"; 
}

int Page = Integer.valueOf(pageNum);

int num = Integer.valueOf(numb);

if(Page <=0){
	Page =1 ;
}

if("searched".equals(searched)){
	sear = HttpRequestUtill.getSearch(request);
	session.setAttribute("sear", sear);
}
 

int id = user.getId(); 
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
int opstatues = OrderPrintln.releasedispatch; 
int count = 0 ;   
  

%>