<%@ page language="java"  import="java.util.*,ordersgoods.*,branchtype.*,product.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%    
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");     
String branchtype = request.getParameter("branchtype"); 
List<BranchType> listgt = BranchTypeManager.getLocate(); 
Map<String,OrderGoodsAll> map  = OrderGoodsAllManager.getsendmap(user,OrderMessage.all); 
 // System.out.println(StringUtill.GetJson(map));      
%>             
<!DOCTYPE html>   
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />
 
<title>订单审核</title>
 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
 

$(function () {  
	//$("#"+type).css("color","red");
}); 
 
function detail(src){
	window.location.href=src;
}
  
function search(statues){
	window.location.href="maintain.jsp?statues="+statues;
}

function check(){
	var flag = false;
	
	$("input[type='checkbox'][id='check_box']").each(function(){          
   		if($(this).attr("checked")){
   				var str = this.value; 
   				
   				if(str != null  &&  str != ""){
	   				  // attract[i] = str; 
		   	          //  i++;
		   	          flag = true;
	   				}	
   		} 
   	}); 
	  
	return flag;
}

</script>  
</head>

<body> 
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>   
 </div>      
 
 <div class="weizhi_head"> 
			现在位置：卖场入库</div>
  
  <form action="ordergoodssendhistory.jsp" method="post">
		<table>
			<tr> 
				<td>销售系统： <select id="branchtype" name="branchtype">
						<option></option> 
						<%
							if (null != listgt) {
								for (int i = 0; i < listgt.size(); i++) {
									BranchType bt = listgt.get(i);
									if (bt.getTypestatues() == 1) {
						%>
						<option value="<%=bt.getId()%>"><%=bt.getName()%></option>
						<%
							}
								}
							}
						%>
				</select>
				</td>
				<td><input type="submit" id="submit" value="查询" />
				</td>

			</tr>
		</table>
	</form>
	
<!--  头 单种类  -->     
<table width="100%" border="0" cellspacing="1"  id="Ntable"> 
  <tr class="dsc">  
  <!-- 
  <td width="10%" class="s_list_m"  align="center"><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></td>    
     -->
      <td   class="s_list_m" align="center">单号</td> 
    <td   class="s_list_m" align="center">门店</td>
    <td   class="s_list_m" align="center">导购</td>
    <td   class="s_list_m" align="center">订单时间</td>
    <td   class="s_list_m" align="center">发货时间</td>
  </tr>   
   <%        
   if(null != map){     
	   Set<Map.Entry<String,OrderGoodsAll>> mapent = map.entrySet();
		Iterator<Map.Entry<String,OrderGoodsAll>> itmap = mapent.iterator();
		int i = 0 ;  
		while(itmap.hasNext()){ 
			Map.Entry<String,OrderGoodsAll> en =  itmap.next();
			OrderGoodsAll o =en.getValue(); 
			String sendtime = o.getList().get(0).getBillingtime();
			String key = en.getKey();   
			if (StringUtill.isNull(branchtype)
					|| o.getOm().getBranch().getPid() == Integer 
							.valueOf(branchtype)) { 
  %>                         
<tr class="asc" ondblclick="detail('ordergoodsdetail.jsp?id=<%=key%>&type=<%=OrderMessage.all%>&statues=<%=OrderMessage.all%>')">      
	  <!--    
	 <td align="center"><input type="checkbox"  value="<%=o.getOm().getId() %>"  name="omid" id="check_box"></input></td>
	-->  
	 <td align="center"><%=o.getOm().getId() %></td> 
	 <td align="center"><%=o.getOm().getBranchname()%></td>   
     <td align="center"><%=o.getOm().getUser().getUsername()%></td> 
     <td align="center"><%= o.getOm().getSubmittime()%></td>  
     <td align="center"><%= sendtime%></td> 
      
  </tr>  
  
     <%}}
    }%>
    
</table>


</body>
</html>