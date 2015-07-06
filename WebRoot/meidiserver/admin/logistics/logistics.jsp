<%@ page language="java"  import="java.util.*,category.*,utill.*,branchtype.*,logistics.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");  
User user = (User)session.getAttribute("user");
TokenGen.getInstance().saveToken(request); 

String token = (String)session.getAttribute("token"); 
String method = request.getParameter("method");
List<Cars> list = CarsManager.getlist();
 
List<User>  listu = UserService.getLogistics(user); 
   
List<BranchType> listb = BranchTypeManager.getLocate();

Map<String,List<Branch>> map = BranchManager.getLocateMapBranch(); 
//Map<String,List<Branch>> map = BranchService.g
String mapjosn = StringUtill.GetJson(map);
 
if("add".equals(method)){  
	 String uid = request.getParameter("uid");
	 String carid = request.getParameter("carid");
	 String prince = request.getParameter("prince");
	 String bid = request.getParameter("branch");
	 String sendtime = request.getParameter("sendtime");    
	 LogisticsMessage ls = new LogisticsMessage();
	 ls.setBid(Integer.valueOf(bid)); 
	 ls.setCarid(Integer.valueOf(carid));
	 ls.setPrice(Integer.valueOf(prince));
	 ls.setUid(Integer.valueOf(uid));   
	 ls.setSubmittime(TimeUtill.getdateString()) ;
	 ls.setSendtime(sendtime);
	 boolean flag = LogisticsMessageManager.saveDB(user, ls);
	 String type = "";
	  if(flag){
		  type = "updated";
	  }     
	   
	 response.sendRedirect("../jieguo.jsp?type="+type);

}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>职工管理</title>
   
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
var jsonmap = '<%=mapjosn%>';
$(function () {
$("#branchtype").change(function(){
	  $("#branch").html("");   
	  var num = ($("#branchtype").children('option:selected').val());
	  var jsons =  $.parseJSON(jsonmap);
 
	  var json = jsons[num];
	  //alert(json); 
    var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>'; 
    for(var i=0; i<json.length; i++) 
  	 {
  	 options +=  "<option value='"+json[i].id+"'>"+json[i].locateName+"</option>";
  	 }
  	 $("#branch").html(options);    	  
}); 
});



function mysubmit(){ 
	  
	var uid = $("#uid").val();
	var carid = $("#carid").val();
    var prince = $("#prince").val();
    var branch = $("#branch").val();
	if(null == uid || "" == uid){
		alert("司机不能为空"); 
		return false; 
	}
	 
	if(null == carid || "" == carid){
		alert("车牌号不能为空");
		return false;
	} 
	 
	if(null == branch || "" == branch){
		alert("送货地址不能为空");
		return false;
	}
	
	if(null ==prince || "" == prince){
		alert("价格不能为空");
		return false;
	}else {
		if(isNaN(prince)){
			alert("价格必须是数字");
			return false; 
		}
	}
	
	
	$("#myform").submit();
}


</script>
</head>

<body>
<!--   头部开始   -->

 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
 
     
 <!--       -->    
     
     <div class="">
   <div class="weizhi_head">现在位置：物流配工</div> 

   <div class="table-list"> 
   
 <form id="myform">   
 <input type="hidden" name="method" value="add"/> 
   <input  type="hidden" name="token" value="<%=token%>" />
<table width="100%" cellspacing="1" id="table"> 
	<tr class="asc"> 
	<td>司机<span style=" color:#F00;">*</span></td>
	 <td >  
	 <select id="uid" name="uid">
	 <option></option>
	   <%   
	    if(null != listu){
	    	for(int i=0;i<listu.size();i++){
	    		User u = listu.get(i);
	    		 
	    		%>
	    		<option value="<%=u.getId()%>"><%=u.getUsername() %></option>
	    		
	    		<% 
	    	}
	    }
	   
	   %>
	 
	 </select>
	 </td>
	
	</tr>
	<tr class="asc"> 
    <td>车牌号<span style=" color:#F00;">*</span></td>   
	<td><select id="carid" name="carid">
	 <option></option>
	   <% 
	    if(null != list){
	    	for(int i=0;i<list.size();i++){
	    		Cars ca = list.get(i);
	    		
	    		%>
	    		<option value="<%= ca.getId()%>"><%=ca.getNum() %></option>
	    		
	    		<%
	    	}
	    }
	   
	   %>
	 
	 </select></td> 
	 
	</tr>
	 <tr class="asc">
    <td width="25%">送货地址<span style=" color:#F00;">*</span></td>
    <td width="65%">
     <select class = "quyu" name="branchtype" id="branchtype" >
          <option >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
		<%
		 for(int i=0;i<listb.size();i++){
			 BranchType lo = listb.get(i); 
			 if(lo.getId() != 2){ 
		%>	    
		 <option value="<%=lo.getId()%>"><%=lo.getName()%></option>
		<%
			 }
		 }
		%>
	</select>
    <select id="branch" name="branch">
          <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option> 
       </select>
 
    
    </td>
  </tr>
	<tr class="asc">
	<td>价格</td>
	<td>
	<input type="text" name="prince" id="prince"/>(元)
	</td>
	</tr>
	<tr class="asc"> 
	<td>送货时间</td> 
	<td> 
	<input type="text" name="sendtime" id="sendtime" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填" />(元)
	</td>
	</tr>
	<tr class="asc">
	<td colspan="2"> 
	<input type="button" value="提交" onclick="mysubmit()"/>
	</td> 
	</tr>
	
	
	
</table>
 
   </form>  
     
     
     </div>


</div>



</body>
</html>
