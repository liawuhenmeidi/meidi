<%@ page language="java" import="java.util.*,product.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%    
request.setCharacterEncoding("utf-8");
String username = request.getParameter("username");
String password = request.getParameter("password");
User user = UserManager.check(username, password);

String type = request.getParameter("type"); 
if(StringUtill.isNull(type)){
	type = Order.serach+"";
}

String sort =  "id desc"; 
boolean flag = false ; 
String saledateStart = request.getParameter("saledateStart");
String saledateEnd = request.getParameter("saledateEnd");
String sailId = request.getParameter("sailId");
String pos = request.getParameter("pos");
//String username = request.getParameter("username");
String phone1 = request.getParameter("phone1");
String printlnid = request.getParameter("printlnid"); 
String sear = "";    
// pos == "" || pos == null || pos == "null"
if(saledateStart != null && saledateStart != "" && saledateStart != "null"){
	sear += " and saledate BETWEEN '" + saledateStart + "'  and  ";
    flag = true ;
}    
  
if(saledateEnd != null && saledateEnd != "" && saledateEnd != "null"){
	sear += " ' " + saledateEnd + "'";
}else if(flag){
	sear += "now()";
}
  
if(sailId != null && sailId  != "" && sailId  != "null"){
	sear += " and sailId like '%"+sailId+"%'";
}
if(pos != null && pos != "" && pos != "null"){
	sear += " and pos like '%"+pos+"%'";
}  
if(username != null && username != "" && username != "null"){
	sear += " and username like '%"+username+"%'";
}     
if(phone1 != null && phone1 != "" && phone1 != "null"){
	sear += " and phone1 like '%"+phone1+"%'";
};
if(!StringUtill.isNull(printlnid)){
	sear += "and printlnid like '%" + printlnid +"%'";
}

%>