<%@ page language="java" import="java.util.*,product.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%    
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
  
String sort =  "id desc"; 
boolean flag = false ;
String saledateStart = request.getParameter("saledateStart");
String saledateEnd = request.getParameter("saledateEnd");
String sailId = request.getParameter("sailId");
String pos = request.getParameter("pos");
String username = request.getParameter("username");
String phone1 = request.getParameter("phone1");
String printlnid = request.getParameter("printlnid"); 
String str = "";   
// pos == "" || pos == null || pos == "null"
if(saledateStart != null && saledateStart != "" && saledateStart != "null"){
	 str += " and saledate BETWEEN '" + saledateStart + "'  and  ";
    flag = true ;
}   
  
if(saledateEnd != null && saledateEnd != "" && saledateEnd != "null"){
	 str += " ' " + saledateEnd + "'";
}else if(flag){
	 str += "now()";
}
  
if(sailId != null && sailId  != "" && sailId  != "null"){
	 str += " and sailId like '%"+sailId+"%'";
}
if(pos != null && pos != "" && pos != "null"){
	 str += " and pos like '%"+pos+"%'";
}  
if(username != null && username != "" && username != "null"){
	 str += " and username like '%"+username+"%'";
}     
if(phone1 != null && phone1 != "" && phone1 != "null"){
	 str += " and phone1 like '%"+phone1+"%'";
};
if(!StringUtill.isNull(printlnid)){
	str += "and printlnid like '%" + printlnid +"%'";
}

%>