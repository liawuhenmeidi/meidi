<%@ page language="java" import="java.util.*,java.net.*,product.*,utill.*,category.*,gift.*,aftersale.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%   
 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

TokenGen.getInstance().saveToken(request);
 
String token = (String)session.getAttribute("token"); 
//Cookie Cookies[]=request.getCookies();

//String sear = "";

//sear = CookieUtill.isRight(Cookies, "sear");

// 用户id 
int id = user.getId();

String statues = request.getParameter("statues");
  
//  上级管理组ID
//int pgroup = GroupService.getidMap().get(user.getUsertype()).getPid();   
int pgroup = -1;
// 所有用户
HashMap<Integer,User> usermap = UserService.getMapId();
 
Map<String,User> usermaps = UserService.getuserIdStr();
  
String usermapstr = StringUtill.GetJson(usermaps);
 
// 安装网点 
//long start = System.currentTimeMillis();
List<User> listS =  UserService.getsencondDealsend(user);  //
//long start1 = System.currentTimeMillis(); 
 //System.out.println(start1-start);
//List<User>  
//listS =  UserManager.getUsers(user,Group.sencondDealsend);
// long start2 = System.currentTimeMillis();
 //System.out.println(start2-start1);
 
List<User> listSend = UserService.getsend(user);

//List<User> listSend =UserManager.getUsers(user,Group.send);;//
//提示信息
Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);

int count = 0 ; 
 
int opstatues = OrderPrintln.releasedispatch;  


%> 

