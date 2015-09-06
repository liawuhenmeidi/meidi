<%@page import="java.net.URLEncoder"%>

<%@ page language="java" import="java.util.*,wilson.upload.*,change.*,group.*,net.sf.json.JSONObject,uploadtotalgroup.*,utill.*,wilson.matchOrder.*,uploadtotal.*,user.*,wilson.salaryCalc.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%  
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	String statues = request.getParameter("statues");
	String[] ids = request.getParameterValues("ids"); 
	if(null != ids){
		ChangeManager.delete(ids);
	}
	
	Map<String,List<Change>> map =  ChangeManager.getmapListC(statues); 

	 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传管理页面</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style media=print type="text/css">    
.noprint{visibility:hidden}   
</style>
<script type="text/javascript">

function amortization(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
} 

function detail(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function println(){
	$("#wrapsearch").css("display","block");
	// window.print();
} 

function changeprintln(){
	 
    $('input[name="oderStatus"]').not("input:checked").each(function(){
    	//alert($(".noprinln"+$(this).val()));
    	// $(".noprinln"+$(this).val()).attr("class","noprint");
    	$(".noprinln"+$(this).val()).css("display","none");
        
    }); 
	$("#wrapsearch").css("display","none");
	 window.print();
}


function winconfirm(){
	$("#post").submit();
}
 
function dosearch(){
	var statues = $("#typestatues").val();
	$("#statues").val(statues);
	$("#post").submit();
}
</script>
</head> 
     
<body style="scoll:no">
  <div style="position:fixed;width:100%;height:80px;">
   
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
   
<div class="btn">
  <table> 
   <tr>  
   <%  if(UserManager.checkPermissions(user, Group.change,"w")){%>
    <td> <input type="button" class="button" name="dosubmit" value="确认删除" onclick="winconfirm()"></input>   </td>
  <%} %>
   <td> <select id="typestatues"> 
     <option value="">全部</option>
     <option value="0" >门店</option>
     <option value="1">型号</option>
       </select></td>
   <td> <input type="button" class="button" name="dosubmit" value="查询" onclick="dosearch()"></input></td>
   </tr>
  
  </table>

 
  
 
 
</div>
</div> 
<div style=" height:120px;">
</div>
     
 <div >
 <form action="" id="post">
  <input type="hidden" name="statues" id="statues"/>
<div style="height:450px"> 
<table  cellspacing="1"   id="table" width=80% >  
		<tr class="asc">    
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"/></td>
			<td  align="center" >编号</td>
			<td  align="center">转换结果</td>
			<td   align="center"  >待转化</td>
		</tr> 
		<% 
			Set<Map.Entry<String, List<Change>>> setmap = map.entrySet();
			
			Iterator<Map.Entry<String, List<Change>>> itmap = setmap.iterator();
			int count = 0 ;
			while(itmap.hasNext()){
				Map.Entry<String, List<Change>> mape = itmap.next();
				String name = mape.getKey();
				List<Change> list = mape.getValue();
		    	String   rowspan = "";
			 for(int i=0;i<list.size();i++){
				 count ++ ;
			  	if(list.size() > 1){
					 if(i == 0 ){ 
						rowspan = "rowspan="+(list.size()) ;
						%>
					   <tr class="asc">   
			              <td align="center" ><input type="checkbox" value="<%=list.get(i).getId() %>" name="ids" id="check_box" /></td>
			              <td align="center"  ><%=count %></td>  
			              <td align="center"  <%=rowspan %>><%=name %></td>  
				          <td align="center" ><%=list.get(i).getBechange() %></td>
				       </tr>	
						<%
						
					}else {  
						rowspan = "";
					%>	
					 <tr class="asc">  
					     <td align="center" ><input type="checkbox" value="<%=list.get(i).getId() %>"  name="ids"  id="check_box" /></td>
					     <td align="center"  ><%=count %></td> 
			   
				          <td align="center" ><%=list.get(i).getBechange()%></td>
				       </tr>
						<%
					}
				}else {
					%>
					<tr class="asc"> 
					   <td align="center"><input type="checkbox" value="<%=list.get(i).getId() %>" name="ids" id="check_box" /></td>
					<td align="center"  ><%=count%></td> 
			      
			       <td align="center"  <%=rowspan %>><%=name %></td>   
				   <td align="center" ><%=list.get(i).getBechange() %></td>
				</tr>
					
					<%
				}
				%>
				
				
				<%
			    } %>
				<%
			}
		%>
		
		
		
</table> 
</div>
 </form>
</div>

</body>
</html>
