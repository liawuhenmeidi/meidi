<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,order.*,orderproduct.*,branchtype.*,branch.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	long startTime = System.currentTimeMillis();
	//接受id
	String[] auto = request.getParameterValues("auto");
	String[] manual = request.getParameterValues("manual");
	
	//显示内容的开关
	boolean showContent = false;
	String startButton = request.getParameter("startbutton");
	
	
	//接受提交的单据，并check
	if(startButton == null){
		if(auto != null && auto.length > 0 ){
			MatchOrderManager.checkOrder(auto);
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
	String selectOrderName = request.getParameter("uploadorder");
	
	//查询条件提交后，左侧侧显示内容
	if(selectBranchType != null && !selectBranchType.equals("") ){
		//第一级选择的是否是all
		if(selectBranchType.equals("all")){
			unCheckedDBOrders = MatchOrderManager.getUnCheckedDBOrders();
		}else{
			
			if(selectBranch != null && !selectBranch.equals("")){
				//第二级选择的是否是all
				if(selectBranch.equals("all")){
					unCheckedDBOrders = OrderManager.getCheckedDBOrdersbyBranchType(selectBranchType);
				}else{
					unCheckedDBOrders = OrderManager.getCheckedDBOrdersbyBranch(selectBranch);
				}
			}
			
		}
	}
	
	
	//查询条件提交后，右侧显示内容
	if(selectOrderName != null && !selectOrderName.equals("")){
		if(selectOrderName.equals("all")){
			unCheckedUploadOrders = uploadOrders;
		}else{
			unCheckedUploadOrders = UploadManager.getOrdersByName(selectOrderName);
		}
	}
	
	
	if(startButton != null && startButton.equals("正在对比")){
		showContent = true;
		if(!mo.startMatch(unCheckedDBOrders, unCheckedUploadOrders)){
			return;
		}
		//去自动匹配好的Order
		afterMatchOrders = mo.getMatchedOrders();
		//从数据库中取到需要匹配的Order
		unCheckedDBOrders = mo.getUnMatchedDBOrders();
		//从上传列表取得需要匹配的Order
		unCheckedUploadOrders = mo.getUnMatchedUploadOrders();
		
	}
	
	int inter = 1;
	
	session.setAttribute("afterMatchOrders", afterMatchOrders);
	session.setAttribute("unCheckedDBOrders", unCheckedDBOrders);
	session.setAttribute("unCheckedUploadOrders", unCheckedUploadOrders);	
	System.out.println("自动对比页面总执行时间(毫秒) = " + (System.currentTimeMillis() - startTime));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自动结款页</title>
  
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
<script type="text/javascript">

function checkdata(){
	
}

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
    
    
<form name="baseform" id="baseform" method="post">
<table width="100%" height="100%" align="center" border=0>
       <tr>
       <table  width="100%"  border=1>
       <tr>
			<td colspan="6" align="center">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<h3>本地记录的订单</h3>
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

			<input type="submit" id="startbutton" name="startbutton" value="对比" onmousedown="$('#baseform').attr('action','');$('#startbutton').val('正在对比');$('#startbuttonhidden').val('正在对比')"/>
			<br/>
			
			
			<%
			if(showContent){
			%>
			<input type="button" value="导出" onclick="$('#baseform').attr('action','../MatchOrderExport');$('#baseform').submit()"/>
			<br/>
			<input type="submit" value="提交"/>
			<%
			}
			%>
			</td>
			
			
			<td colspan="6" align="center">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<h3>上传的订单</h3>
			<select name="uploadorder">
				<option value="all" selected="selected">全部</option>
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
			<td align="center"></td> 
			<td align="center">序号</td> 
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
			if(afterMatchOrders.get(i).getCompareLevel() >= 5.0){
		%>
		<tr>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>checkbox"><input name="auto"  checked="checked" type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbshop"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbposno"><a href="./dingdanDetailmini.jsp?id=<%=afterMatchOrders.get(i).getDBOrder().getId() %>" target="_BLANK"><%= afterMatchOrders.get(i).getDBSidePosNo() %></a></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbsaletime"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbtype"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbcount"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center" id=""></td>
			<td align="center" id=""><%=inter++ %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			}
		}
		%>
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			if(afterMatchOrders.get(i).getCompareLevel() >= 4.0 && afterMatchOrders.get(i).getCompareLevel()< 5.0){
		%>
		<tr>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>checkbox"><input name="auto"  type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbshop"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbposno"><a href="./dingdanDetailmini.jsp?id=<%=afterMatchOrders.get(i).getDBOrder().getId() %>" target="_BLANK"><%= afterMatchOrders.get(i).getDBSidePosNo() %></a></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbsaletime"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbtype"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbcount"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center" id=""></td>
			<td align="center" id=""><%=inter++ %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			}
		}
		%>
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			if(afterMatchOrders.get(i).getCompareLevel() >= 3.0 && afterMatchOrders.get(i).getCompareLevel() < 4.0){
		%>
		<tr>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>checkbox"><input name="auto"  type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbshop"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbposno"><a href="./dingdanDetailmini.jsp?id=<%=afterMatchOrders.get(i).getDBOrder().getId() %>" target="_BLANK"><%= afterMatchOrders.get(i).getDBSidePosNo() %></a></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbsaletime"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbtype"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbcount"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center" id=""></td>
			<td align="center" id=""><%=inter++ %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			}
		}
		%>
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			if(afterMatchOrders.get(i).getCompareLevel() >= 2.0 && afterMatchOrders.get(i).getCompareLevel() <3.0){
		%>
		<tr>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>checkbox"><input name="auto"  type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbshop"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbposno"><a href="./dingdanDetailmini.jsp?id=<%=afterMatchOrders.get(i).getDBOrder().getId() %>" target="_BLANK"><%= afterMatchOrders.get(i).getDBSidePosNo() %></a></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbsaletime"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbtype"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbcount"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center" id=""></td>
			<td align="center" id=""><%=inter++ %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			}
		}
		%>
		
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			if(afterMatchOrders.get(i).getCompareLevel() >= 1.0 && afterMatchOrders.get(i).getCompareLevel()< 2.0){
		%>
		<tr>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>checkbox"><input name="auto"  type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbshop"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbposno"><a href="./dingdanDetailmini.jsp?id=<%=afterMatchOrders.get(i).getDBOrder().getId() %>" target="_BLANK"><%= afterMatchOrders.get(i).getDBSidePosNo() %></a></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbsaletime"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbtype"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbcount"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center" id=""></td>
			<td align="center" id=""><%=inter++ %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			}
		}
		%>
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			if(afterMatchOrders.get(i).getCompareLevel() >= 0.0 && afterMatchOrders.get(i).getCompareLevel()< 1.0){
		%>
		<tr>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>checkbox"><input name="auto"  type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbshop"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbposno"><a href="./dingdanDetailmini.jsp?id=<%=afterMatchOrders.get(i).getDBOrder().getId() %>" target="_BLANK"><%= afterMatchOrders.get(i).getDBSidePosNo() %></a></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbsaletime"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbtype"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center" id="<%=afterMatchOrders.get(i).getDBOrder().getId() %>dbcount"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center" id=""></td>
			<td align="center" id=""><%=inter++ %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center" id=""><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			}
		}
		%>
		
		
		<%
			for(int i = 0 ;;){
				if(unCheckedDBOrders != null && unCheckedDBOrders.size() > 0 && i< unCheckedDBOrders.size()){					
		%>	
					<tr>  	
					<td align="center"  id="<%=unCheckedDBOrders.get(i).getId() %>checkbox"><input name="manual" disabled="disabled" type="checkbox" value=""  /></td> 
					<td align="center" id="<%=unCheckedDBOrders.get(i).getId() %>dbshop"><%= unCheckedDBOrders.get(i).getbranchName(unCheckedDBOrders.get(i).getBranch()) %></td>
					<td align="center" id="<%=unCheckedDBOrders.get(i).getId() %>dbposno"><a href="./dingdanDetailmini.jsp?id=<%=unCheckedDBOrders.get(i).getId() %>" target="_BLANK"><%= unCheckedDBOrders.get(i).getPos() %></a></td>
					<td align="center" id="<%=unCheckedDBOrders.get(i).getId() %>dbsaletime"><%= unCheckedDBOrders.get(i).getSaleTime() %></td>
					<td align="center" style="overflow:hidden" id="<%=unCheckedDBOrders.get(i).getId() %>dbtype"><%= unCheckedDBOrders.get(i).getSendType() %></td> 
					<td align="center" id="<%=unCheckedDBOrders.get(i).getId() %>dbcount"><%= unCheckedDBOrders.get(i).getSendCount() %></td> 
		<%
				}else{
		%>
					<tr>
					<td align="center"><input name="manual" disabled="disabled" type="checkbox" value=""  /></td> 
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"></td> 
		<%
				}
			
				if(unCheckedUploadOrders != null && unCheckedUploadOrders.size() > 0 && i< unCheckedUploadOrders.size()){
		%>
							<td align="center"></td>
							<td align="center" id=""><%=inter++ %></td> 
							<td align="center"><%= unCheckedUploadOrders.get(i).getShop() %></td>
							<td align="center"><%= unCheckedUploadOrders.get(i).getPosNo() %></td>
							<td align="center"><%= unCheckedUploadOrders.get(i).getSaleTime() %></td>
							<td align="center"><%= unCheckedUploadOrders.get(i).getType() %></td> 
							<td align="center"><%= unCheckedUploadOrders.get(i).getNum() %></td> 
							</tr>
		<%
				}else{
		%>					
							<td align="center"></td>
							<td align="center"></td>
							<td align="center"></td>
							<td align="center"></td>
							<td align="center"></td>
							<td align="center"></td> 
							<td align="center"></td>
							</tr>
		<%
				}
				i ++ ;
				if(i >= unCheckedDBOrders.size() && i >=unCheckedUploadOrders.size()){
					break;
				}
			}
		%>
		
		
</table> 



</div>
   </td>
   </tr>
 </table>
 </form>
</body>
</html>
