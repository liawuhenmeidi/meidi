<%@ page language="java" import="java.util.*,inventory.*,branch.*,category.*,branchtype.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");
  
List<Inventory> invetorylist = InventoryManager.getCategory(user,"unconfirmed");  
Map<Integer,Branch> branchmap = BranchManager.getNameMap();   
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript">
 
 function detail(id){
	 window.location.href="receiptsAdd.jsp?id="+id;
 }

</script>
</head>

<body>

<div class="s_main">

    
 <!--       -->   
   <div class="weizhi_head">现在位置：单据管理
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
   <a href="../welcom.jsp"><font style="color:blue;font-size:20px;" >返回</font></a>       
   </div>    
   
  <div class="main_r_tianjia">
   <ul>     
   <% 
   if(UserManager.checkPermissions(user, Group.inventory)){
   %>                                                                                                    
     <li><a href="receiptsAdd.jsp?">新增单据</a></li>
    <%
   } 
    %>
     <li><a href="receiptsold.jsp">查看已确认单据</a></li> 
     </ul>
     
   </div>  
   <div class="table-list">
   
   
   
   
   
<div class="btn">
<table width="100%"  cellspacing="1" id="table">
	<thead>
		<tr>
			<th align="left">序号</th>
			<th align="left">日期</th>
			<th align="left">出库单位</th>
			<th align="left">入库单位</th>
			<th align="left">状态</th> 
		</tr>
	</thead>
      <% 
       for(int i=0;i<invetorylist.size();i++){ 
    	   Inventory invetory = invetorylist.get(i);
    	   %>
    	   <tr id="<%=i%>" class="asc"  onclick="detail('<%=invetory.getId()%>')">
			<td align="left"><%=invetory.getId() %></td>
			<td align="left"><%=invetory.getIntime() %></td>
			<td align="left">
			 <% 
			   if(branchmap != null){
				   Branch branch = branchmap.get(invetory.getOutbranchid());
			       if(branch != null){
			    	   %>
			    	      
			    	   <%=branch.getLocateName() %>
			    	   <%
			       }
			   }
			 
			 %>
			
			</td>
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
			<% if(invetory.getInstatues() == 1 && invetory.getOutstatues() == 1){
				%>
				双方已确认
				<%
			}else if(invetory.getInstatues() == 1 && invetory.getOutstatues() == 0){
			%>
			    出库方待确认
			<% }else if(invetory.getInstatues() == 0 && invetory.getOutstatues() == 1){
			%>
			 入库方待确认
			<%
			}else { 
			%>
			双方待确认
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



</body>
</html>
