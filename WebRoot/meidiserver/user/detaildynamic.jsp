<%@ page language="java" import="java.util.*,gift.*,orderPrint.*,category.*,group.*,user.*,utill.*,product.*,order.*,orderproduct.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%    
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
String id = request.getParameter("id");
Order or = OrderManager.getOrderID(user,Integer.valueOf(id));
int uid = user.getId();
 
HashMap<Integer,User> usermap = UserService.getMapId();

int saleid = or.getSaleID();
int delivery = or.getDeliveryStatues();

int canupdate = 0;
if(saleid == uid || UserManager.checkPermissions(user, Group.Manger)){
	canupdate = 1 ;
} 
 
Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
 

int modify = OrderPrintlnManager.getstatues(opmap, OrderPrintln.modify, or.getId()) ;
int returns = OrderPrintlnManager.getstatues(opmap, OrderPrintln.returns, or.getId());
int huanhuo = OrderPrintlnManager.getstatues(opmap, OrderPrintln.huanhuo, or.getId());
 
boolean flag = true ; 
int opstatues = -1;   
  
if(or.getDeliveryStatues() == 0 || or.getDeliveryStatues() == 9 ){ 
	opstatues = OrderPrintln.salerelease;     
}else if (or.getDeliveryStatues() == 1 || or.getDeliveryStatues() == 10){
	if(or.getInstallid() == 0){   
		flag = false ; 
	}  
	if(uid == or.getInstallid()){  
		opstatues = OrderPrintln.salereleaseanzhuang;
	}  
}else if(or.getDeliveryStatues() == 2 ){  
	if(or.getReturnid() == user.getId()){ 
		flag = true; 
		opstatues = OrderPrintln.salereleasereturn ;
	}else {
		flag = false ; 
	}
	
} 



%>