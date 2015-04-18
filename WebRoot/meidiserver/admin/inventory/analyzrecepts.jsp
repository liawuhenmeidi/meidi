<%@ page language="java" import="java.util.*,utill.*,inventory.*,branch.*,category.*,branchtype.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
String type = request.getParameter("type");
if(StringUtill.isNull(type)){ 
	type = "unconfirmed"; 
}  
List<Inventory> invetorylist = InventoryManager.getCategoryAnalyze(user,type); 

Map<Integer,Branch> branchmap = BranchService.getMap();
%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>调货处理</title>

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript">
 
 function detail(id){
	 window.location.href="anlyzreceiptsAdd.jsp?id="+id;
 }
 
	
	
</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
 
<div class="main">
 
 <!--       -->    
     
     <div class="">
   <div class="weizhi_head">现在位置：调货处理
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     


   </div>    
     
 
   <div class="table-list">
<div class="btn">
<table width="100%"  cellspacing="1" id="table">
	<thead>
		<tr>
			
			<th align="left">入库单位</th>
			<th align="left">状态</th> 
			<th align="left">日期</th>
			
			<th align="left">打印</th>
		</tr>
	</thead>
      <% 
       for(int i=0;i<invetorylist.size();i++){ 
    	   Inventory invetory = invetorylist.get(i);
    	   %>
    	   <tr id="<%=i%>" class="asc"  ondblclick="detail('<%=invetory.getId()%>')"  onclick="updateClass(this)"  >
			
			
			<td align="left">
			<%
			   if(branchmap != null){
				   Branch branch = branchmap.get(invetory.getInbranchid());
			       if(branch != null){
			    	   %> 
			    	   
			    	   <%=branch.getLocateName() %>
			    	   <%
			       }
			   }
			   
			 %>
			</td>
			<td align="left">
			<% if(invetory.getInstatues() == 1){
				%>
				已确认
				<%
			}else if(invetory.getInstatues() == 0){
			%>
			    未确认
			<% }
			%>
			</td>
			<td align="left"><%=invetory.getIntime() %></td>
			
			<td>
			 <% 
			 if(invetory.getOutstatues() == 0){
				%> 
				未打印
				<% 
			 }else {
			 %>
			已打印
			 <%
			 }
			 %>
			</td>
		</tr>

    	   <%
       }
      %>

</table>





</div>
<div id="pages"></div>
</div>  

     </div>

</div>



</body>
</html>
