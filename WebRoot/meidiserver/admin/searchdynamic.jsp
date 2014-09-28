<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      
  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int id = user.getId(); 

int count = 0 ;   
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");  
String sort = request.getParameter("sort");  
int opstatues = OrderPrintln.releasedispatch;   
 
String sear = "";
if(!StringUtill.isNull(sort)){
	session.setAttribute("sort", sort);
}else {
	sort = "id desc";   
}  
  
if(!StringUtill.isNull(numb)){
	session.setAttribute("numb", numb);
}else {
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

String searched = request.getParameter("searched");
  
if("searched".equals(searched)){
	sear = HttpRequestUtill.getSearch(request);
}  

%>