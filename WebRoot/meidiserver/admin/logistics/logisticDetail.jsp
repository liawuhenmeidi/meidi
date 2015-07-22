<%@ page language="java" import="java.util.*,utill.*,branchtype.*,category.*,net.sf.json.JSONObject,logistics.*,branch.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
        
<%            
User user = (User)session.getAttribute("user");  
String id = request.getParameter("id"); 
String method = request.getParameter("method");
List<Cars> list = CarsManager.getlist();
  
String time = TimeUtill.getdateString(); 
List<User>  listu = UserService.getLogistics(user); 
List<BranchType> listb = BranchTypeManager.getLocate();
Map<String,List<Branch>> map = BranchManager.getLocateMapBranch(); 
//Map<String,List<Branch>> map = BranchService.g
String mapjosn = StringUtill.GetJson(map);

if("delRequest".equals(method)){ 
	LogisticsMessageManager.deleteRequest(user, id);
}else if("del".equals(method)){ 
	LogisticsMessageManager.delete(user, id);
}else if("updateRequest".equals(method)){
	 
	 LogisticsMessage lm  = LogisticsMessageManager.getByid(Integer.valueOf(id)); 
	 String uid = request.getParameter("uid"); 
	 System.out.println("uid"+uid);  
	 String carid = request.getParameter("carid"); 
	 String prince = request.getParameter("prince");
	 String advancePrice = request.getParameter("advancePrice"); 
	 if(StringUtill.isNull(advancePrice)){
		 advancePrice = 0+""; 
	 }
	 String realbranch = request.getParameter("realbranch");
	 String sendtime = request.getParameter("sendtime"); 
	 String remark = request.getParameter("remark"); 
	 String startlocate = request.getParameter("startlocate");
	 String pid = request.getParameter("pid");  
	 LogisticsMessage ls = new LogisticsMessage();
	 if(!StringUtill.isNull(uid)){
		lm.setUid(Integer.valueOf(uid));   
	 } 
	 if(!StringUtill.isNull(carid)){
		 lm.setCarid(Integer.valueOf(carid));
	 } 
	 if(!StringUtill.isNull(prince)){
		 lm.setPrice(Integer.valueOf(prince));
	 } 
	 if(!StringUtill.isNull(sendtime)){
		 lm.setSendtime(sendtime);
	 } 
	 if(!StringUtill.isNull(realbranch)){
		 lm.setLocates(realbranch);
	 } 
	 if(!StringUtill.isNull(remark)){
		 lm.setRemark(remark);
	 }    
	 if(!StringUtill.isNull(advancePrice)){
		 lm.setAdvancePrice(Integer.valueOf(advancePrice));
	 } 
	 if(!StringUtill.isNull(startlocate)){
		 lm.setStartLocate(startlocate); 
	 }   
	 lm.setStatues(-2);  
	LogisticsMessageManager.updaterequest(user, id,lm);
	
}else if("update".equals(method)){  
	LogisticsMessage lm  = LogisticsMessageManager.getByid(Integer.valueOf(id));
	LogisticsMessageManager.update(user, lm); 
	id= lm.getUpid()+""; 
	
	
}  
boolean flag = false ;  
   
LogisticsMessage lm  = LogisticsMessageManager.getByid(Integer.valueOf(id));
LogisticsMessage lmup = LogisticsMessageManager.getByid(Integer.valueOf(lm.getUpid()));
int uid = lm.getUser().getId(); 
int carid = lm.getCars().getId();

boolean flags = false ; 
String col = "colspan=\"2\"";   

if(lm.getStatues() == 0 || lm.getStatues() == 1){  
	col = "colspan=\"2\""; 
	flags = true ; 
}

%>  
<!DOCTYPE HTML>
<html> 
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />   
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" type="text/css" href="../../css/songhuo.css"/>   
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "<%=id%>";  
var uid = "<%=uid%>"; 
var carid = "<%=carid%>";
var time = '<%=time%>'; 
time=new Date(time.replace("-", "/").replace("-", "/"));  
var locates = new Array();
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
 
	
	$("#branch").change(function(){
		var b = $("#branch option:selected").text();
		 
		$("#addbranch").val(b); 
	}); 
     
	$("#uid").change(function(){
		var b = $("#uid option:selected").val();
		 //alert("#u"+b);
		$("#u"+b).css("display","block");

	});

	

});

function addLogistic(){   
	window.location.href="logistics.jsp?pid="+id+"&uid="+uid+"&carid="+carid; 
} 
 
function deleteRequestLogistic(){
	var  question = confirm("您确定要提交删除请求吗？");  
		if (question != "0"){   
			window.location.href="logisticDetail.jsp?method=delRequest&id="+id;
		} 
}  
 
 function deleteLogistic(){ 
	 var  question = confirm("您确定要删除吗？");  
		if (question != "0"){    
			window.location.href="logisticDetail.jsp?method=del&id="+id;
		}  
 } 
 
 function updateRequestLogistic(){
	 var  question = confirm("您确定要提交修改请求吗？");  
		if (question != "0"){ 
			$("#myform").submit(); 
			//window.location.href="logisticDetail.jsp?method=updateRequest&id="+id;
		} 
 }
 
 
function updateLogistic(){ 
	var  question = confirm("您确定要修改吗？");
	if (question != "0"){   
		window.location.href="logisticDetail.jsp?method=update&id="+id;
	}
}
function initLocate(){
	$("#addlocate td").remove(); 
	var html = '';   
	for(var i=0;i<locates.length;i++){
		lo = locates[i]; 
		html +='<td bgcolor="white" onclick="delLocate(\''+lo+'\')">['+lo+']</td>';
		 
	}  
    $("#realbranch").val(locates.toString());
	$("#addlocate").append(html);
}

function delLocate(lo){
	
	locates.splice($.inArray(lo,locates),1);
	initLocate(); 
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
</script>
</head> 
<body>  
 
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
 
   <div class="">
   <div class="weizhi_head">现在位置：查看明细
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>
    
   </div>       
      <form id="myform"> 
      <input type="hidden" name="method" value="updateRequest"/>
       <input type="hidden" name="id" value="<%=id%>"/>
          <input  type="hidden" name="realbranch" id="realbranch" />
      
      <table  id="table" width="80%" cellspacing="1" >
      <tr class="dsc">
<td align="center" <%=col%>> 单据信息</td>
 <td align="center">  
	修改内容
	</td>
 </tr>      
    
      <tr class="asc">
<td align="center" >    
	司机
</td>   
<td align="center"> 
<%=lm.getUser().getUsername() %>
	</td> 
	                     
	<td class="ras" align="center">   
	<%if(lm.getStatues() == 0 && lm.getOperation() == 0 ){ %>
	 <select id="uid" name="uid" >
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
	 <%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)){
	 
		 
		  %>  
		  <%= lmup.getUser().getUsername()%>
		  
		  <%
	 } %>
	 </td>
	   
</tr>  

      <tr class="asc">
<td align="center" >   
	车牌号  
</td>   
<td align="center"> 
<%=lm.getCars().getNum() %>  
	</td>
	<td align="center">
  	<%if(lm.getStatues() == 0  && lm.getOperation() == 0 ){ %>
	<select id="carid" name="carid">
	 <option></option>
	   <% 
	    if(null != list){
	    	for(int i=0;i<list.size();i++){
	    		Cars ca = list.get(i);
	    		  
	    		%>
	    		<option value="<%= ca.getId()%>" id="u<%=ca.getUid() %>" style="display: none"><%=ca.getNum() %></option>
	    		
	    		<%
	    	}
	    }
	   
	   %>
	 
	 </select>
	  <%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)){
		 
		
			  %>  
			  <%= lmup.getCars().getNum()%>
		  
		  <%
	 } %>
	 
	 
	 
	 </td> 
	    
</tr><tr class="asc"> 
<td align="center"> 送货地址</td> 
<td align="center">    
	  <%=lm.getLocates()%>
	  </td> 
	  <td align="center">
	  <%if(lm.getStatues() == 0  && lm.getOperation() == 0 ){ %>
      <table>
      <tr id="addlocate">
         
      </tr>
      </table>
      
       <%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)) {
    	   
    			  %>
    			  <%=lmup.getLocates()%>
		  <%
	 } %>
	 
	</td> 
</tr>
<tr class="asc">  
<td align="center"> 增加送货地址</td> 
<td align="center">     
	  </td>  
 <td width="65%"> 
  <%if(lm.getStatues() == 0  && lm.getOperation() == 0 ){ %>
     <table width="100%">  
	    <tr class="asc">
	    <td align="center"> <select class = "quyu" name="branchtype" id="branchtype" >
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
       </select></td>
	    </tr> 
	    <tr class="asc"> 
	    <td align="center">
	    <input type="text" name="addbranch" id="addbranch" value="" placeholder="增加地址"/>
	    <input type="button" value="增加" onclick="addlocate()"/>
	    
	    </td>
	    
	    </tr>
	 </table>
	 
    <%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)){
    	%>
    	<%
    } %>
  
  
    </td>
	
	
</tr>


<tr class="asc">
<td align="center"> 运费</td>
<td align="center">  
	  <%=lm.getPrice() %>
	  </td>  
	  <td align="center"> 
	  <%if(flags && lm.getOperation() == 0 ){ %>
	<input type="text" name="prince" id="prince" placeholder="运费"/>(元)
	 <%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)){
		 %>
		  <%=lmup.getPrice()
		  
		  
		  %>
		  
		  <%
	 } %>
	</td>
</tr>
<tr class="asc">
<td align="center">垫付金额</td>
<td align="center">  
	  <%=lm.getAdvancePrice() %>
	  </td>
	  <td align="center">
	  <%if(flags && lm.getOperation() == 0 ){ %>
	<input type="text" name="advancePrice" id="advancePrice" placeholder="垫付金额"/>(元)
	<%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)){
		%>
		<%=lmup.getAdvancePrice() %>
		 
		<%
	} %>
	</td>  
</tr><tr class="asc">
<td align="center"> 送货时间</td>
 <td align="center"> 
	  <%=lm.getSendtime() %>
	  </td>
	  <td align="center"> 
	  <%if(lm.getStatues() == 0 && lm.getOperation() == 0 ){ %>
	<input type="text" name="sendtime" id="sendtime" maxlength="10"
						onclick="new Calendar().show(this);" value="<%=time %>" placeholder="必填" />
	 <%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)){  
		  %> 
		  <%=lmup.getSendtime()
		  
		  
		  %>
		  
		  <%
	 } %>
	 </td> 
 </tr>    
 <tr class="asc">
 <td align="center">行车记录</td>
 <td align="center" <%=col %>> 
 <table>
	 
 <%  
 String locateM = lm.getLocateMessage();
 if(!StringUtill.isNull(locateM)){
	 String[] locateMs = locateM.split(",");
	 for(int i=1;i<locateMs.length;i++){
		 String locate = locateMs[i];  
		 String[] locates = locate.split("::");
		 String ti = locates[0]; 
		 String l = locates[1];
		 
		 %>
		 <tr><td align="center"><%= ti%></td><td align="center"><%=l %></td></tr>
		 
		 <%
	 }
 }
 %>
 </table>
 </td>  
 </tr>
 <tr class="asc">
<td align="center"> 关联送货号</td>
<td align="center" <%=col %>>  
	  <%=lm.getPid()%>
	  </td>  
</tr>
 
 
     
     <tr class="asc">
<td align="center"> 备注</td>
<td align="center">  
	  <%=lm.getRemark()%>
	  </td> 
	  <td align="center">
	  <%if(flags && lm.getOperation() == 0 ){ %>  
	<textarea name="remark" id="remark" placeholder="备注"></textarea>
	 <%}else if(flags && (lm.getOperation() == 4 ||lm.getOperation() == 5)){ 
	  
		  %>
		  <%=lmup.getRemark()
		   
		  
		  %>
		  
		  <%
	 } %>
	</td>  
	   
</tr>

 <tr class="asc">
  
 <td align="center" >
 <% if(lm.getStatues() == 0) {%>
 <input type="button" value="补充配工" onclick="addLogistic()" />
 <%} %> 
 </td> 
<td align="center">  
    <% if(lm.getOperation() == 0 && lm.getStatues() == 0){
	 %> 
 <input type="button" value="删除申请" onclick="deleteRequestLogistic()" />  
	    
	 <%
 } else if(lm.getOperation() == 1){
	 %>  
	  等待司机同意删除
	  <%
 } else if(lm.getOperation() == 2){
	%> 
	  <input type="button" value="删除" onclick="deleteLogistic()" />
	<%
 }%> 
	  </td>   
<td align="center">  
	 <% if(lm.getOperation() == 0 && (lm.getStatues() == 0 || lm.getStatues() == 1 || lm.getStatues() == 2)){
	 %> 
 <input type="button" value="修改申请" onclick="updateRequestLogistic()" />
	     
	 <%
 } else if(lm.getOperation() == 4){
	 %> 
	  等待司机同意修改
	  <%
 } else if(lm.getOperation() == 5){
	%>  
	  <input type="button" value="修改" onclick="updateLogistic()" />
	<%
 }else {
	  
 }%>
	  </td> 
</tr> 

    
 
      </table>
      </form>
     </div>


</body>
</html>
