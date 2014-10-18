<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,order.*,orderproduct.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	String[] auto = request.getParameterValues("auto");
	String[] manual = request.getParameterValues("manual");
	
	if(auto != null && auto.length > 0 ){
		MatchOrderManager.checkOrder(auto);
		//MatchOrderManager.checkOrder(Integer.parseInt(dbSide), Integer.parseInt(uploadSide));
	}
	
	MatchOrder mo = new MatchOrder();
	List<AfterMatchOrder> afterMatchOrders = new ArrayList<AfterMatchOrder>();
	List <Order> unCheckedDBOrders = new ArrayList<Order>();
	List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();
	
	boolean showContent = false;
	String startButton = request.getParameter("start");
	
	List<UploadOrder> uploadOrders = UploadManager.getUnCheckedUploadOrders();
	List<String> orderNames = UploadManager.getAllUploadOrderNames(uploadOrders);
	
	if(startButton != null && startButton.equals("对比")){
		showContent = true;
		if(!mo.startMatch()){
			return;
		}
		//去自动匹配好的Order
		afterMatchOrders = mo.getMatchedOrders();
		//从数据库中取到需要匹配的Order
		unCheckedDBOrders = mo.getUnMatchedDBOrders();
		//从上传列表取得需要匹配的Order
		unCheckedUploadOrders = mo.getUnMatchedUploadOrders();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动结款页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
	<style>
     redTag{color:red}
	</style>
</head>

<body>
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">


</script>

  
<jsp:include flush="true" page="head.jsp">
<jsp:param name="dmsn" value="" />
</jsp:include>

<form action="" method="post">
<table  cellspacing="1" border="2px">
		

		<tr>
			<td colspan="6" align="center"><h3>本地记录的订单</h3></td>
			<td align="center"><h3><input type="submit" name="start" value="对比" /></h3></td> 
			<td colspan="6" align="center">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<h3>上传的订单</h3>
			<select name="uploadorder">
				<option value="all" selected="selected">全部</option>
				<%
				for(int i = 0 ; i < orderNames.size() ; i ++){
				%>
				<option value="<%=orderNames.get(i) %>" ><%=orderNames.get(i) %></option>
				<%
				}
				%>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="submit" value="显示"/>
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
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
	
		</tr> 
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			if(afterMatchOrders.get(i).getCompareLevel() >= 3.0){
		%>
		<tr>
			<td align="center"><input name="auto"  checked="checked" type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getDBSidePosNo() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center"></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
		</tr>
		
		<%
			}
		}
		%>
		
		<%
		for(int i = 0 ; i < afterMatchOrders.size();i++	){
			if(afterMatchOrders.get(i).getCompareLevel() < 3.0){
		%>
		<tr>
			<td align="center"><input name="auto"  type="checkbox" value="<%=afterMatchOrders.get(i).getDBOrder().getId() %>,<%=afterMatchOrders.get(i).getUploadOrder().getId() %>"/></td>		
			<td align="center"><%= afterMatchOrders.get(i).getDBSideShop() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getDBSidePosNo() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getDBSideSaleTime() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getDBSideType() %></td> 
			<td align="center"><%= afterMatchOrders.get(i).getDBSideCount() %></td> 
			<td align="center"></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideShop() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSidePosNo() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideSaleTime() %></td>
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideType() %></td> 
			<td align="center"><%= afterMatchOrders.get(i).getUploadSideCount() %></td> 
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
					
					<td align="center"><input name="manual" disabled="disabled" type="checkbox" value=""  /></td> 
					<td align="center"><%= unCheckedDBOrders.get(i).getBranch() %></td>
					<td align="center"><%= unCheckedDBOrders.get(i).getPos() %></td>
					<td align="center"><%= unCheckedDBOrders.get(i).getSaleTime() %></td>
					<td align="center"><%= unCheckedDBOrders.get(i).getSendType() %></td> 
					<td align="center"><%= unCheckedDBOrders.get(i).getSendCount() %></td> 
		<%
				}else{
		%>
					<tr>
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
							</tr>
		<%
				}
				i ++ ;
				if(i >= unCheckedDBOrders.size() && i >=unCheckedUploadOrders.size()){
					break;
				}
			}
		%>
		<%
		if(showContent){
		%>
		
		<tr>
			<td colspan="12" align="center"><input type="submit" value="提交"/></td>
		</tr>
		<%
		}
		%>
		
</table> 
</form>

</body>
</html>
