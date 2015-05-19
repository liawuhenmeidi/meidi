<%@ page language="java" import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 

if(!UserManager.checkPermissions(user, Group.ManagerUser,"w")){
	return ; 
}
List<BranchType> listb = BranchTypeManager.getLocate();
Map<String,List<Branch>> map = BranchManager.getLocateMapBranch(); 
String mapjosn = StringUtill.GetJson(map);

String uid = request.getParameter("uid");

User u = UserService.getMapId().get(Integer.valueOf(uid));
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>电话修改</title>
 
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
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
  
 function gettime(){
	  
	 var newphone = $("#newphone").val();
	 var branchid = $("#branch").val();
	 
	 if((newphone == "" || newphone == null) && (branchid == null || branchid == "")){
		 alert("请您添加修改内容");
		 return false ;
	 }
	  
     if(branchid == "" ||  branchid == null || ( newphone != null && newphone != "" )){
    	var filter = /^1[3|4|5|7|8][0-9]\d{8}$/;  
 	    var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
 	    
 		 if(!filter.test(newphone) && !isPhone.test(newphone)){
 			 alert("请填写正确的手机号码或电话");     
 			 return false;  
 		 }
     }
     
	
		
		 
	 window.opener.document.getElementById("fresh").value ="fresh";
	 window.opener.document.getElementById("phone").value =newphone;
	 window.opener.document.getElementById("branchid").value =branchid;
	 //window.returnValue='refresh'; 
	// timer=window.setInterval("IfWindowClosed()",500);
      window.close(); 
 }

</script>
</head>
<body>
<!--   头部开始   -->
<!--   头部结束   -->  
  <table cellpadding="1" cellspacing="0" style="margin:auto">
   <tr> 
      <td>原电话</td>
      <td><input name="control_date" type="text" id="oldphone" value="<%=u.getPhone() %>" size="10"
                        maxlength="11"   /> </td>
   </tr> 
  <tr>  
      <td>新电话</td> 
      <td> <input name="control_date2" type="text" id="newphone" size="10"
                        maxlength="11" /></td> 
  </tr>
   <% if(UserManager.checkPermissions(user, Group.ManagerUser)) { %>
  <tr> 
      <td>原门店</td> 
      <td><input name="control_date" type="text" id="oldphone" value="<%=u.getBranchName() %>" size="10"
                        maxlength="11"   /> </td>
   </tr> 
  <tr>  
      <td>新门店</td> 
      <td> <select class = "quyu" name="branchtype" id="branchtype" >
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
	 <br/> 
    <select id="branch" name="branch">
          <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option> 
       </select>
       </td>
  </tr>
  <% }%>
  <tr> 
     <td></td>
     <td><input type="button"  onclick="gettime()"  style="background-color:red;font-size:20px;"  value="确认" /></td>
  </tr>
 
 </table>
</body>
</html>
