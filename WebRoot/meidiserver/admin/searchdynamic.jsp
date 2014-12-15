<%@ page language="java" import="java.util.*,java.net.*,product.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<% 

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
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
List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend); //UserService.getsencondDealsend(user);

List<User> listSend = UserManager.getUsers(user,Group.send); //UserService.getsend(user);
//提示信息
Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);

int count = 0 ; 

int opstatues = OrderPrintln.releasedispatch;  


%> 

