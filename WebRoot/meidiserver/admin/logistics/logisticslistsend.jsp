<%@ page language="java"  import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");   
User user = (User)session.getAttribute("user"); 
String method = request.getParameter("method");

List<LogisticsMessage>	list = LogisticsMessageManager.getlist("0");
       
//System.out.println("CarsService.getmap()"+CarsService.getmap());
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>职工管理</title>
 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">

function detail(id){ 
	window.location.href="logisticDetail.jsp?id="+id;
}
 
function del(){
	var ids = new Array(); 
	$("input[type='checkbox'][id='check_box']").each(function(){          
   		if($(this).attr("checked")){
   				var str = this.value; 
   				 
   				if(str != null  &&  str != ""){
	   				  ids.push(str);
	   				}	
   		} 
   	});   
	
	if(ids.length >1){
		window.location.href="logisticslistsend.jsp?ids="+ids.toString()+"&method=del";
	}else {
		alert("请选择需要删除的单据");
	}
	
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
   <div class="weizhi_head">现在位置：待送货</div> 

   <div class="table-list">  
<table width="100%" cellspacing="1" id="table"> 
<tr class="dsc">
<!-- 
<td width="5%" class="s_list_m" align="center"><input
						type="checkbox" value="" id="allselect"
						onclick="seletall(allselect);totalInit()"></input>
					</td> 
					 -->
					<td>单号</td>
<td>司机</td> 
<td>
	车牌号
</td>
<td>送货地址</td> 
<td>运费</td>
<td>垫付运费</td>
<td>送货时间</td>
<td>提交时间</td>
<td>关联送货号</td>
</tr>
	 
	 <%
	 if(null != list){
		 for(int i=0;i<list.size();i++){
			 LogisticsMessage ca = list.get(i);
			 String cl = "class=\"asc\"";
			 if(ca.getPid() != 0){
				 cl = "class=\"bsc\"";
			 }   

  			 if(ca.getOperation() != 0){
  				 cl = "class=\"rsc\"";
  			 } 
			 %>     
			 <tr <%=cl %> ondblclick="detail('<%=ca.getId()%>')"> 
			 <!-- 
			<td align="center">
			 <% if(ca.getOperation() == 2){ %>
				 <% }%>    
				  <input type="checkbox"
						value="<%= ca.getId()%>" name="lid" id="check_box" onclick="totalInit()"></input> 
				  
			     
				   </td> 
				   -->
			  <td>
			  <%=ca.getId() %>
			  </td>
					
			 <td >  
			<%=ca.getUser().getUsername() %>
	 </td> 
	<td> 
<%=ca.getCars().getNum() %>  
	</td>   
	  <td>   
	  <%=ca.getLocates()%>
	  </td> 
	  <td> 
	  <%=ca.getPrice() %>
	  </td>
	   <td> 
	  <%=ca.getAdvancePrice() %>
	  </td>
	  <td>
	  <%=ca.getSendtime() %>
	  </td>
	  <td>
	  <%=ca.getSubmittime() %>
	  </td>
	  <td>
	  <%=ca.getPid() %>
	  </td>
	</tr> 
			 
			 <%  
		 }
	 }
	 %>
	 
	
	
</table>

     
     </div>


</div>



</body>
</html>
