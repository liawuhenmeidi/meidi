<%@ page language="java" import="java.util.*,inventory.*,branch.*,category.*,branchtype.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");
  
List<Inventory> invetorylist = InventoryManager.getCategory(user,"confirmed");  
Map<Integer,Branch> branchmap = BranchManager.getIdMap();   
%> 
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript">
function detail(id){
	 window.location.href="receiptsAdd.jsp?id="+id;
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
   <div class="weizhi_head">现在位置：单据管理
   
   <a href="receipts.jsp"><font style="color:blue;font-size:20px;" >返回</font></a>       
   </div>     
       
        <div class="main_r_tianjia">
   <ul>    
   <% 
   if(UserManager.checkPermissions(user, Group.inventory)){
   %>                                                                                                    
     <li><a href="receiptsAdd.jsp?">新增单据</a></li>
     <li><a href="receiptsold.jsp?">查看已确认单据</a></li> 
    <%
   }
    %>
     </ul>
     
   </div>  
   <div class="table-list">
<div class="btn">
<table width="100%"  cellspacing="1" id="table">
	<thead>
		<tr>
			<th align="center">序号</th>
			<th align="center">日期</th>
			<th align="center">出库单位</th>
			<th align="center">入库单位</th>
			<th align="center">备注</th>
			<th align="center">状态</th> 
			
		</tr>
	</thead>
      <% 
       for(int i=0;i<invetorylist.size();i++){ 
    	   Inventory invetory = invetorylist.get(i);
    	   %>
    	   <tr id="<%=i%>" class="asc"  ondblclick="detail('<%=invetory.getId()%>')" onclick="updateClass(this)">
			<td align="center"><%=invetory.getId() %></td>
			<td align="center"><%=invetory.getIntime() %></td>
			<td align="center">
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
			<td align="center">
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
			<td align="center">
			<%=invetory.getRemark() %>
			</td> 
			<td align="center">
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

</div>



</body>
</html>
