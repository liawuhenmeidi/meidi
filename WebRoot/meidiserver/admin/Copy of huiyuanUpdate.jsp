<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<% 

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String action = request.getParameter("action");

String id = request.getParameter("id");

User users = new User();
if(!StringUtill.isNull(id)){
	users = UserManager.getUesrByID(id);
	System.out.println("id"+id);
}
List<Group> list = GroupManager.getGroup();

System.out.println("****"+action);
  
if("add".equals(action)){
	String name = request.getParameter("username");
	String positions = request.getParameter("position");
	String branch = request.getParameter("branch");
    String juese = request.getParameter("juese");
    System.out.println("juese"+juese);
    String date = request.getParameter("date");
    String password = request.getParameter("password");
    User u = new User();
    u.setId(Integer.valueOf(id));  
    System.out.println("******"+Integer.valueOf(id));
    u.setBranch(branch); 
    u.setEntryTime(date);
    u.setPositions(positions);
    u.setUsertype(Integer.valueOf(juese));
    u.setUsername(name); 
    u.setUserpassword(password);
    System.out.println("u.getUsertype()"+u.getUsertype());
    boolean flag = UserManager.update(u); 
    if(flag){
    	
    	
    	
    }
    response.sendRedirect("huiyuan.jsp");
}
   
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会员修改</title>
 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script src="js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
  <script src="js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>

  <link href="css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
  <script src="js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>

  <script src="js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
  <link href="css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
$(function () {
    var opt = { };
    opt.date = {preset : 'date'};
		$('#serviceDate').val('').scroller('destroy').scroller($.extend(opt['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020',minDate: new Date()}));
		
 });
 function check(){
	 var name = $("#name").val();
	 var detail = $("#position").val();

	 if(name == "" || name == null || name == "null"){
		 alert("会员名不能为空");
		 return false;
	 }
	 
	 if(detail == "" || detail == null || detail == "null"){
		 alert("所属门店不能为空");
		 return false;
	 }
	 
	 $('input[name="permission"]:checked').each(function(){ 
		    alert($(this).val()); 
		    
	 });
	 
	 return true ;
 }
 
</script>
</head>

<body>


<!--   头部开始   -->
<div class="head">
  <div class="head_logo"><img src="../style/image/logo.png" /></div>
</div>

<!--   头部结束   -->

<div class="main">
 
     
 <!--       -->    
       
     <div class="">
 <div class="weizhi_head">现在位置：添加会员</div>
 <form action="huiyuanUpdate.jsp"  method = "post"  onsubmit="return check()">
 <input type="hidden" name="action" value="add"/>
  
 <input type="hidden" name="id" value="<%=id%>"/>
 <input type="hidden" name="password" value="<%=users.getUserpassword()%>"/>
     
       <div class="juese_head"> 职工名称:
        <input type="text"  id="name" value = "<%=users.getUsername() %>" name="username" />    
        </div>
       <div class="juese_head"> 所属门店:
       <input type="text"  id="branch" name="branch"  value = "<%=users.getBranch() %>"/>    
       </div> 
       <div class="juese_head"> 电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话:
       <input type="text"  id="branch" value="<%=users.getPhone() %>" name="phone" />    
       </div> 
        <div class="juese_head"> 所属职位:
       <select id = "juese" name = "juese">
       <% 
        for(int i=0;i<list.size();i++){
        	String str = "";
        	Group g = list.get(i);
        	if(g.getId() == users.getUsertype()){
        		str = "selected = \"selected\"";
        	}
        	%>
        	 <option value="<%= g.getId() %>"  <%=str %>> <%=g.getName() %></option>
        	 <% 
        	 
        }

       %>
       
       </select>
       
       </div>
       <div class="juese_head"> 入职时间:
       <input class="date" type="text" name="date" placeholder="选填"  id="serviceDate"  readonly="readonly"  value = "<%=users.getEntryTime() %>"></input>   
       </div> 
         <div class="juese_head"></div>
   
        <div class="juese_head"></div>

        <div class="main_r_tianjia" />
 
        <ul>
        <li><input type="submit" value="提  交" /></li>
         </ul>
        </form>  
   </div>
     </div>

</div>

</body>
</html>
