<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>

<link rel="stylesheet" type="text/css" rev="../stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>

</head>
 
<body>

<% 

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 

Map<String,List<Group>> map = GroupService.getPidMap();



String ptype = request.getParameter("ptype"); 
String id = request.getParameter("id");
String action = request.getParameter("action");
Group groups = null;
String name = "";
String detail = "";

if(!StringUtill.isNull(id)){
	HashMap<Integer,Group> maps =GroupManager.getGroupMap();
	groups = maps.get(Integer.valueOf(id));
	name = groups.getName(); 
	detail = groups.getDetail().trim();
	
}
if("add".equals(action)){
	String ids = request.getParameter("id");
	String named = request.getParameter("name");
    String detaild = request.getParameter("detail");
   
    Group group = new Group();
    group.setId(Integer.valueOf(ids));
    group.setDetail(detaild);
    group.setName(named);  
    group.setPtype(groups.getPtype());
     
    GroupManager.updateName(user, group); 
    response.sendRedirect("juese.jsp?ptype="+ptype);
    return ;
}

%>
<script type="text/javascript">
var name = '<%=name %>';
var detail = '<%=detail%>';

$(function(){ 
	$("#name").val(name);
	$("#detail").val(detail);
	$("#"+pid).attr("checked","checked");
});
 function check(){
	 var name = $("#name").val();
	 var detail = $("#detail").val();

	 if(name == "" || name == null || name == "null"){
		 alert("角色名称不能为空");
		 return false;
	 }
	 
	 if(detail == "" || detail == null || detail == "null"){
		 alert("详细信息不能为空");
		 return false;
	 }
	  
	 return true ;
 }
 
</script>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
<!--   头部结束   -->

<div class="main">
 
      
 <!--       -->    
     <div class="">
 <div class="weizhi_head">角色编号<%=id %></div> 
 <form action="jueseUpdate.jsp"  method = "post"  onsubmit="return check()">
 <input type="hidden" name="action" value="add"/> 
 <input type="hidden" name="id" value="<%=id%>"/>
 <input type="hidden" name="ptype" value="<%=ptype%>"/> 
     <div class="juese_head">角色名称：
            <input type="text"  id="name" name="name" />    
        </div>
       <div class="juese_head">  详细信息:
       <textarea name="detail" id="detail" rows="4" cols="20" > 
       </textarea>  
       </div> 
       <div class="juese_head"></div>
       <br/>
       
      
   <ul>
     <li><input type="submit" value="提  交" /></li>
     </ul>
 </form>  
   </div>
     </div>
  

</body>
</html>
