<%@ page language="java" import="java.util.*,category.*,utill.*,branch.*,branchtype.*,group.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String id = request.getParameter("id");
 
BranchType branch = BranchTypeManager.getLocate(Integer.valueOf(id)); 

List<Branch> list =BranchManager.getLocate(id) ;
boolean flag = UserManager.checkPermissions(user, Group.branch,"w"); 
HashMap<String,List<User>> map = UserManager.getMapBranch(); 
// System.out.println("list.size()"+list.size());
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
  
<script type="text/javascript">
var pid = "<%=id%>";
function winconfirm(){
	question = confirm("你确认要删除吗？");
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		$("input[type='checkbox']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.name;
	   	            attract[i] = str;
	   	            i++;	
	   		}
	   	});
		$.ajax({  
	        type: "post",  
	         url: "delete.jsp",
	         //data:"method=list_pic&page="+pageCount,
	         data:"method=branch&id="+attract.toString(),
	         dataType: "",  
	         success: function (data) { 
	          alert("删除成功");
	          window.location.href="branch1.jsp?id="+pid;
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("删除失败");
	            } 
	           });
	 }
}

function update(id,statues,type){

		if(statues != 1){
			 window.location.href="branch1add.jsp?pid=<%=id%>&id="+id+"&type="+type;
		}
	
}

function changes(bid,statues){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=branchinventory&bid="+bid+"&statues="+statues,
         dataType: "", 
         success: function (data) {
           window.location.href="branch1.jsp?id="+pid;
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function seletall(all){
	if($(all).attr("checked")){
		$("input[type='checkbox']").each(function(){
			$(this).attr("checked",true);
	     }); 
	}else if(!$(all).attr("checked")){
		$("input[type='checkbox']").each(function(){
			$(this).attr("checked",false);
	     });
	}
}
</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
   
 <!--       -->    
 
   <div class="weizhi_head">现在位置：<%=branch.getName()%>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="branch.jsp"><font style="color:blue;font-size:20px;" >返回</font></a>       
   </div>     
     <div class="main_r_tianjia">
      <%
      if(flag){
	      if(Integer.valueOf(id) != 2 && branch.getIsSystem() != 1){  
	       %>
	   <ul>                                                                                                     
	     <li><a href="branch1add.jsp?pid=<%=id%>">添加门店</a></li>
	     </ul>
	    <%} 
      }%>
   </div>
     
     
     
     
   <div class="table-list">
<table width="100%" cellspacing="1" id="table">
	<thead>
		<tr>
			<th align="left" width="20">
			 <input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></th>
			<th align="left" width="40">门店号</th>
			<th align="left">门店</th>
			<th align="left">卖场别名</th>
			<th align="left">门店编码</th>
			<th align="left">库位名称</th>   
			<th align="left">导购人数</th>
			<th align="left">门店报装单所需信息</th>
			<!-- <th align="left">是否做为总库</th> -->
		</tr>
	</thead>
<tbody>
<% 
if(list != null){
  for(int i =0 ;i<list.size();i++){
	  Branch category = list.get(i) ;
	  //category.getId()
	
	  int count =map.get(category.getId()+"")==null?0:map.get(category.getId()+"").size();
%>       
    <tr id="<%=i%>" class="asc"  onclick="updateClass(this)" ondblclick="update('<%= category.getId()%>','<%=category.getId()%>','<%=branch.getIsSystem() %>')" >  
		<td align="left">
		<% if(category.getPid() != 2){ %>
		<input type="checkbox" value="1" name="<%=category.getId() %>"></input>
		<% }%></td> 
		<td align="left"><%=i+1 %></td>    
 		<td align="left"><%=category.getLocateName() %></td> 
 			<td align="left"><%=category.getNameSN() %></td> 
 		<td align="left"><%=category.getEncoded()%></td>  
 			<td align="left"><%=category.getReservoir()%></td>  
 		<td align="left"><%=count%></td>
		<td align="left">  
		        <%  
		           String message = category.getMessage(); 
		          if (StringUtill.isNull(message)){
		        	  message = "无"; 
		          }else{
		        	  message = message.replaceAll("pos","pos(厂送)单号").replace("sailId", "OMS订单号").replace("checked", "验证码(联保单)");
		        	 
		          }
		        
		        %> 
		   
		  
               <%=message%>    
         </td>
         <!--  
         <td align="left">  
            <% if(category.getStatues() ==0){
             %>
                                           否<input type="button" onclick="changes('<%=category.getId()%>','1')"  value="开启"/> 
                                           
               &nbsp; &nbsp;&nbsp;&nbsp;                              
            <%
            }else {  
            %>
              
                                             是<input type="button" onclick="changes('<%=category.getId()%>','0')"  value="关闭"/> 
            <% }%>
             
         </td>  
          --> 
    </tr>  
    <% } 
    }%>
</tbody>
</table>

<div class="btn">

<!--  <input type="button" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  -->

</div>
<div class="btn">  
  <!--  
      门店名称： <input type="text"  id="locate" name="locate" />  
  <input type="button" onclick="changes()"  value="增加"/> </br> 
   -->   
   <%if(flag){
    if(branch.getIsSystem() == 0 ){%>   
  <input type="submit" class="button" name="dosubmit" value="删除"  onclick="winconfirm()"></input>
   <%} 
   }%>
</div>
</div>



</body>
</html>
