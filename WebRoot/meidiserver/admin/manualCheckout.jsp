<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,order.*,orderproduct.*,branchtype.*,branch.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	
	long startTime = System.currentTimeMillis();
	//接受两边的id
	String[] dbSide = request.getParameterValues("dbside");
	String[] uploadSide = request.getParameterValues("uploadside");
	String selectOrderName = request.getParameter("uploadorder");
	

	//显示内容的开关
	boolean showContent = false;
	String startButton = request.getParameter("startbutton");
	
	if(startButton == null){
		if(dbSide != null && dbSide.length >0){
			MatchOrderManager.checkDBOrderList(dbSide,selectOrderName);
		}
		if(uploadSide != null && uploadSide.length > 0){
			MatchOrderManager.checkUploadOrderList(uploadSide); 
		}
	}
	
	//初始化要对比的orders
	MatchOrder mo = new MatchOrder();
	List<AfterMatchOrder> afterMatchOrders = new ArrayList<AfterMatchOrder>();
	List <Order> unCheckedDBOrders = new ArrayList<Order>();
	List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
	

	
	//左侧select里面的内容
	List<BranchType> listb = BranchTypeManager.getLocate();
	Map<String,List<Branch>> map = BranchManager.getLocateMapBranch(); 
	String mapjosn = StringUtill.GetJson(map);
	
	//右侧select里面的内容
	List<UploadOrder> uploadOrders = UploadManager.getUnCheckedUploadOrders();
	List<String> orderNames = UploadManager.getAllUploadOrderNames(uploadOrders);
	
	
	//接受查询条件的提交
	String selectBranchType = request.getParameter("branchtype");
	String selectBranch = request.getParameter("branch");
	//String selectOrderName = request.getParameter("uploadorder");
	
	
	//截至时间
	String selectDate = request.getParameter("deadline");
	String deadline = "";
	if(StringUtill.isNull(selectDate)){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		deadline = sdf.format(new Date());
	}else{
		deadline = selectDate;
	}
	
	
	
	//查询条件提交后，左侧侧显示内容
	unCheckedDBOrders = MatchOrderManager.getUnCheckedDBOrders(selectBranchType, selectBranch, deadline);
	
	
	//查询条件提交后，右侧显示内容
	unCheckedUploadOrders = MatchOrderManager.getUnCheckedUploadOrders(selectOrderName);
	
	
	if(startButton != null && startButton.equals("正在对比")){
		showContent = true;
		session.setAttribute("selectBranchType", selectBranchType);
		session.setAttribute("selectBranch", selectBranch);
		session.setAttribute("selectOrderName", selectOrderName);
		session.setAttribute("deadline", deadline);
		
		if(!mo.startMatch(unCheckedDBOrders, unCheckedUploadOrders)){
			return;
		}
		//去自动匹配好的Order
		afterMatchOrders = mo.getMatchedOrders();
	}
	
	
	//如果是搜索进来的
	String search = request.getParameter("search");
	if(!StringUtill.isNull(search) && search.equals("true") && "".equals(startButton)){
		UploadOrder searchOrder = (UploadOrder)request.getSession().getAttribute("searchUploadOrder");
		

		unCheckedDBOrders = MatchOrderManager.getUnCheckedDBOrders((String)request.getSession().getAttribute("selectBranchType"), (String)request.getSession().getAttribute("selectBranch"), (String)request.getSession().getAttribute("deadline"));
		unCheckedUploadOrders = MatchOrderManager.getUnCheckedUploadOrders((String)request.getSession().getAttribute("selectOrderName"));
		
		
		unCheckedDBOrders = MatchOrderManager.searchOrderList(unCheckedDBOrders, searchOrder);
		unCheckedUploadOrders = MatchOrderManager.searchUploadOrderList(unCheckedUploadOrders, searchOrder);
		
		showContent = true;
		if(!mo.startMatch(unCheckedDBOrders, unCheckedUploadOrders)){
			return;
		}
		//去自动匹配好的Order
		afterMatchOrders = mo.getMatchedOrders();
	}
	
	//导出用
	session.setAttribute("afterMatchOrders", afterMatchOrders);
	
	
	
	int inter = 1;
	System.out.println("自动对比页面总执行时间(毫秒) = " + (System.currentTimeMillis() - startTime));
	
	
	//下面用到的背景色
	String backgroundColor ="#B9D3EE";
	boolean showColor = false;
	
	//下面用到的是否check
	boolean isChecked = false;
	
	//是否禁用
	boolean dbsideDisabled =false;
	boolean uploadsideDisabled=false;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结款页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
	<style>
     redTag{color:red}
	</style>
	<style type="text/css"> 
table {table-layout:fixed;} 
tr strong,tr td {white-space:normal} 
</style>
</head>

<body>
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript">

var jsonmap = '<%=mapjosn%>';   
 
$(function () {
    var opt = { }; 
    opt.date = {preset : 'date'};	  
		  $("#branchtype").change(function(){
			  $("#branch").html(""); 
			  var num = ($("#branchtype").children('option:selected').val());
			  var jsons =  $.parseJSON(jsonmap);
			
	          var options = '<option value="all">全部</option>'; 
			  if(num != "all"){
				  var json = jsons[num];
				  //alert(json);

		          for(var i=0; i<json.length; i++) 
		        	 {
		        	 options +=  "<option value='"+json[i].id+"'>"+json[i].locateName+"</option>";
		        	 }
			  }else{
				  options = '<option value="all" selected="selected">全部</option>'; 
			  }
			  
	        	 $("#branch").html(options);    	  
		  }); 
		  
		  
		  
 });
 
<%
if(selectBranchType != null && !selectBranchType.equals("") && !selectBranchType.equals("all")){
%>
$(function (){
	$("#branch").html(""); 
	
	  var num = <%=selectBranchType%>;
	  var jsons =  $.parseJSON(jsonmap);
	  var json = jsons[num];
	  var options = '<option value="all">全部</option>';
	  if(<%=selectBranch != null && selectBranch.equals("all")%>){
		  options = '<option value="all" selected="selected">全部</option>';
	  }
    for(var i=0; i<json.length; i++) 
  	 {
    	if(json[i].id != "<%=selectBranch%>"){
    		options +=  "<option value='"+json[i].id+"'>"+json[i].locateName+"</option>";
    	}else{
    		options +=  "<option value='"+json[i].id+"' selected='selected'>"+json[i].locateName+"</option>";
    	}
  	 
  	 }
  	 $("#branch").html(options);    
});
<%
}
%>
</script>

  
<jsp:include flush="true" page="head.jsp">
<jsp:param name="dmsn" value="" />
</jsp:include>
    

<table width="100%">
	<tr>
		<td width="15%">
		本页显示为 
		<select onchange="location.href='CheckedOrders.jsp'">
			<option value="1" selected="selected">对比未结款单据</option>
			<option value="2" >对比已结款单据</option>
		</select>
		</td>
		<td><h3><a href="#" onClick="javascript:window.open('./searchOrder.jsp?unchecked=true&branchtype=<%=selectBranchType%>&branch=<%=selectBranch %>&uploadorder=<%=selectOrderName %>', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')" >搜索</a></h3></td>
	</tr>
</table>

<form name="baseform" id="baseform" method="post">
<input type="hidden" name="search" value="false"/>
<table width="100%" height="100%" align="center" border=0>
       <tr>
       <table  width="100%"  border=1>
       <tr>
			<td colspan="7" align="center">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<h3>本地记录的订单(截至时间<input class="date2" type="text" name="deadline" id ="serviceDate2" onclick="new Calendar().show(this);"  readonly="readonly" style="width:20% " value="<%=deadline %>"></input>)</h3>
			<select name="branchtype" id="branchtype" >
          	<option value="all">全部</option> 
          	<%
			 for(int i=0;i<listb.size();i++){
				 BranchType lo = listb.get(i); 
				 if(lo.getId() != 2){ 
			%>	    
			<option value="<%=lo.getId()%>" <%if(String.valueOf(lo.getId()).equals(selectBranchType)){ %>selected="selected" <%} %>><%=lo.getName()%></option>
			<%
				 }
			 }
			%>
			</select>
			
			<select id="branch" name="branch">
          	<option value="all">全部</option> 
      		</select>
			</td>
			
			
			
			<td align="center">

			<input type="submit" id="startbutton" name="startbutton" value="对比" onmousedown="$('#baseform').attr('action','');$('#startbutton').val('正在对比')"/>
			<br/>
			
			
			<%
			if(showContent){
			%>
			<input type="button" value="导出" onclick="$('#baseform').attr('action','../MatchOrderExport');$('#baseform').submit()"/>
			<br/>
			<input type="submit" id="submitbutton" value="提交"/>
			<%
			}
			%>
			</td>
			
			
			<td colspan="6" align="center">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<h3>上传的订单</h3>
			<select name="uploadorder" onchange="$('#submitbutton').attr('disabled','disabled');" >
				<%
				for(int i = 0 ; i < orderNames.size() ; i ++){
				%>
				<option value="<%=orderNames.get(i) %>" <%if(orderNames.get(i).equals(selectOrderName)){ %>selected="selected" <%} %>><%=orderNames.get(i) %></option>
				<%
				}
				%>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			
			
		</tr>
		
		<tr>  

			<td align="center">选中</td>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
			<td align="center">送货状态</td> 
			<td align="center">序号</td> 
			<td align="center">选中</td>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
	
		</tr> 
       </table>
       </tr>
   <tr>
   <td align="center" width="100%">
    <div style="overflow-y:auto; width:100%;height:500px">

<table  align="center" width="100%" cellspacing="1" border="2px" >
		
		
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			
			showColor = false;
			isChecked = false;
			dbsideDisabled =false;
			uploadsideDisabled=false;
			if(afterMatchOrders.get(i).getUploadSideOrderId() == -2){
				showColor = true;
			}	
			if(afterMatchOrders.get(i).getCompareLevel() == 5){
				isChecked = true;
			}
			
			if(afterMatchOrders.get(i).getUploadSideOrderId() < 0){
				uploadsideDisabled = true;
			}
			if(afterMatchOrders.get(i).getDBSideOrderId() < 0){
				dbsideDisabled = true;
			}
			
		%>
		<tr>
			<td align="center"><input <%if(isChecked) {%>checked="checked"<% }%> <%if(dbsideDisabled) {%>disabled="disabled"<% }%> name="dbside"  type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>"  /></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbshop"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbposno"><a href="#" onClick="javascript:window.open('./dingdanDetailmini.jsp?id=<%=afterMatchOrders.get(i).getDBOrder().getId() %>', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')"><%= afterMatchOrders.get(i).getDBSidePosNo() %></a></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbsaletime"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbtype"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbcount"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbstatus"><%= OrderManager.getDeliveryStatues(afterMatchOrders.get(i).getDBOrder()) %></td> 
			<td align="center" id=""><%=inter++ %></td> 
			<td align="center"><input <%if(isChecked) {%>checked="checked"<% }%> <%if(uploadsideDisabled) {%>disabled="disabled"<% }%> name="uploadside"  type="checkbox" value="<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getUploadOrder().getId() %>uploadshop"><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getUploadOrder().getId() %>uploadposno"><a href="#" onClick="javascript:window.open('./uploadOrderDetail.jsp?id=<%=afterMatchOrders.get(i).getUploadOrder().getId() %>', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')"><%= afterMatchOrders.get(i).getUploadSidePosNo() %></a></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getUploadOrder().getId() %>uploadsaletime"><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getUploadOrder().getId() %>uploadtype"><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center" bgcolor="<%=showColor?backgroundColor:"" %>" id="<%=afterMatchOrders.get(i).getUploadOrder().getId() %>uploadcount"><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			
		}
		%>
		
</table> 
</form>


</div>
   </td>
   </tr>
 </table>
</body>
</html>
