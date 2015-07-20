<%@ page language="java"  import="java.util.*,category.*,utill.*,branchtype.*,com.zhilibao.model.*,com.zhilibao.service.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% 
	request.setCharacterEncoding("utf-8");   
User user = (User)session.getAttribute("user");
TokenGen.getInstance().saveToken(request);  
  
String time = TimeUtill.getdateString();  
String token = (String)session.getAttribute("token"); 
String method = request.getParameter("method");
String pid = request.getParameter("pid");  
String uid = request.getParameter("uid"); 
String carid = request.getParameter("carid");
if(StringUtill.isNull(pid)){  
	pid = "0";  
}

Map<String,List<Branch>> map = BranchManager.getLocateMapBranch(); 
//Map<String,List<Branch>> map = BranchService.g
String mapjosn = StringUtill.GetJson(map);
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物流送货增加</title> 
    
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/calendar.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
var jsonmap = '<%=mapjosn%>';
var time = '<%=time%>';   
var pid = '<%=pid%>';  
var uid = '<%=uid%>';  
var carid = '<%=carid%>';  
time=new Date(time.replace("-", "/").replace("-", "/"));  
var locates = new Array(); 
$(function () {
	$("#uid").val(uid);
	$("#carid").val(carid);
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

	
	$("#branch").change(function(){
		var b = $("#branch option:selected").text();
		 
		$("#addbranch").val(b); 
	});


});



function mysubmit(){ 
	  
	var uid = $("#uid").val();
	var carid = $("#carid").val();
    var prince = $("#prince").val();
   
    var sendtime = $("#sendtime").val();   
    var send=new Date(sendtime.replace("-", "/").replace("-", "/"));
    var startlocate = $("#startlocate").val();
    //alert(send);
	if(null == uid || "" == uid){
		alert("司机不能为空"); 
		return false; 
	}
	 
	if(null == carid || "" == carid){
		alert("车牌号不能为空");
		return false;
	}   
	if(pid != 0){
		if(null == startlocate || "" == startlocate){
			alert("起始地址不能为空");
			return false ;
		}
	} 
	if(locates.length < 1 ){
		alert("送货地址不能为空"); 
		return false;
	} 
	     
	if(null ==prince || "" == prince){
		alert("运费不能为空");
		return false; 
	}else {
		if(isNaN(prince)){
			alert("运费必须是数字");
			return false; 
		}
	}
	
	if(null == sendtime || "" == sendtime){
		alert("送货时间不能为空");
		return  false;
	}else { 
		if(send <time){
			alert("送货时间过期");
			return  false ;
		}
	}
	
	$("#myform").submit();
}

function addlocate(){
	var locate = $("#addbranch").val();
	var num =$.inArray(locate,locates);
	//alert(num); 
	if(num == -1){ 
		locates.push(locate);
		initLocate();
	}
	 
	 $("#addbranch").val(""); 
}
  
function initLocate(){
	$("#addlocate td").remove(); 
	var html = '';   
	for(var i=0;i<locates.length;i++){
		lo = locates[i]; 
		html +='<td bgcolor="white" onclick="delLocate(\''+lo+'\')">['+lo+']</td>';
		 
	}  
    $("#locates").val(locates.toString());
	$("#addlocate").append(html);
}
function delLocate(lo){
	
	locates.splice($.inArray(lo,locates),1);
	initLocate(); 
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
      
 <form id="myform" action="save.do"  method="post">     
   <input type="hidden" name="method" value="add"/> 
   <input  type="hidden" name="token" value="<%=token%>" />
   <input  type="hidden" name="locates" id="locates" />
   <input  type="hidden" name="pid" value="<%=pid%>" /> 
   <input  type="hidden" name="submittime" value="<%=TimeUtill.gettime()%>" />
<table width="100%" cellspacing="1" id="table">  
	<tr class="asc"> 
	<td>司机<span style=" color:#F00;">*</span></td>
	 <td >  
	
	 <select id="uid" name="uid" >
	 <option></option> 
	  <c:forEach var="user"  items="${users}" >
     <option value="${user.id }">${user.username }</option>
      </c:forEach>  
	 </select>
	 </td>
	 
	</tr>
	<tr class="asc"> 
    <td>车牌号<span style=" color:#F00;">*</span></td>   
	<td><select id="carid" name="carid">
	 <option></option>
	 
	 <c:forEach var="car"  items="${cars}" >
     <option value="${car.id }">${car.num }</option>
      </c:forEach> 
	 
	 </select></td>  
	 
	</tr>
	<tr class="asc">
	<td>起始地址</td>
	<td>   
     <input type="text" name="startlocate" id="startlocate" placeholder="起始地址"/>
	</td>
	</tr>
	
	<tr class="asc">
	<td>送货地址</td>
	<td>
      <table>
      <tr id="addlocate">
         
      </tr>
      </table>
	</td> 
	</tr>
	
	 <tr class="asc"> 
    <td width="25%">选择地址<span style=" color:#F00;">*</span></td>
    <td width="65%">
     <table width="100%">  
	    <tr class="asc">
	    <td> <select class = "quyu" name="branchtype" id="branchtype" >
          <option >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
		
		 <c:forEach var="BranchType"  items="${BranchTypes}" >
           <option value="${BranchType.id }">${BranchType.name }</option>
      </c:forEach>  
 
	</select>  
    <select id="branch" name="branch">
          <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option> 
       </select></td>
	    </tr> 
	    <tr class="asc">  
	    <td>
	    <input type="text" name="addbranch" id="addbranch" value="" placeholder="增加地址"/>
	    <input type="button" value="增加" onclick="addlocate()"/>
	    
	    </td>
	    
	    </tr>
	 </table>
	 
    
  
  
    </td>
  </tr>
	<tr class="asc">
	<td>运费</td>
	<td>
	<input type="text" name="prince" id="prince" placeholder="运费"/>(元)
	</td>
	</tr>
	<tr class="asc">
	<td>垫付金额</td> 
	<td>
	<input type="text" name="advancePrice" id="advancePrice" placeholder="垫付金额"/>(元)
	</td>
	</tr>
	<tr class="asc"> 
	<td>送货时间</td> 
	<td> 
	<input type="text" name="sendtime" id="sendtime" maxlength="10"
						onclick="new Calendar().show(this);" placeholder="必填" />
	</td>
	</tr>
	<tr class="asc"> 
	<td>备注</td>     
	<td> 
	<textarea name="remark" id="remark" placeholder="备注"></textarea>
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
