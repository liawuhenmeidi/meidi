<%@ page language="java" import="java.util.*,utill.*,config.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%       
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
boolean searchflag = false ; 
String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");  
String sort = request.getParameter("sort");   
String searched = request.getParameter("searched");
String sear = (String)session.getAttribute("sear"); 
  
if(StringUtill.isNull(sear)){
	sear = "";   
}

if(StringUtill.isNull(sort)){
	sort = "submittime desc"; 
	searchflag = true ;
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
 
//if("searched".equals(searched)){
//	sear = HttpRequestUtill.getSearch(request);
//	session.setAttribute("sear", sear); 
//}

// 用户id
int id = user.getId();   

//  上级管理组ID
int pgroup = -1 ; 
//int pgroup = GroupService.getidMap().get(user.getUsertype()).getPid();   
  
// 所有用户
HashMap<Integer,User> usermap = UserService.getMapId();
 
Map<String,User> usermaps = UserService.getuserIdStr();

String usermapstr = StringUtill.GetJson(usermaps);

// 安装网点
List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend); //UserService.getsencondDealsend(user);

List<User> listSend = UserManager.getUsers(user,Group.send); //UserService.getsend(user);
//提示信息 
//System.out.println(listSend);
Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);

int count = 0 ; 

int opstatues = OrderPrintln.releasedispatch;  

boolean flag = false ;
Config con = ConfigManager.getinstance().map.get(Config.addbarName);
if(null != con){
  if(con.getStatues() == Config.isok){
	   flag = true ;
	  }
  }

%>