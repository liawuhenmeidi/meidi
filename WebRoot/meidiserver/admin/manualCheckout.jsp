<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,order.*,orderproduct.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	String dbSide = request.getParameter("dbside");
	String uploadSide = request.getParameter("uploadside");
	
	if(dbSide != null && !dbSide.equals("") && uploadSide != null && !uploadSide.equals("")){
		
		System.out.println();
		MatchOrderManager.checkOrder(Integer.parseInt(dbSide), Integer.parseInt(uploadSide));
		
	}else if(dbSide != null && !dbSide.equals("")){
		MatchOrderManager.checkDBOrder(Integer.parseInt(dbSide));
	}else if(uploadSide != null && !uploadSide.equals("")){
		MatchOrderManager.checkUploadOrder(Integer.parseInt(uploadSide));
	}
	
	//从数据库中取到需要匹配的Order
	List <Order> unCheckedDBOrders = MatchOrderManager.getUnCheckedDBOrders();
	//从上传列表取得需要匹配的Order
	List <UploadOrder> unCheckedUploadOrders = MatchOrderManager.getUnCheckedUploadOrders();
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动结款页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

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


<table  cellspacing="1" border="2px">
		<form action="" method="post">
		<tr>
			<td colspan="6" align="center"><h3>本地记录的订单</h3></td>
			<td align="center"></td> 
			<td colspan="6" align="center"><h3>苏宁返回的订单</h3></td>
		</tr>

		<tr>  

			<td align="center">选中</td>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
			<td align="center"></td> 
			<td align="center">选中</td>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
	
		</tr> 
		
		<%
			for(int i = 0 ;;){
				if(unCheckedDBOrders != null && unCheckedDBOrders.size() > 0 && i< unCheckedDBOrders.size()){
		%>	
					<tr>  
					<td align="center"><input name="dbside" type="radio" value="<%=unCheckedDBOrders.get(i).getId() %>"/></td>
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
							<td align="center"><input name="uploadside" type="radio" value="<%=unCheckedUploadOrders.get(i).getId() %>"/></td>
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
		<tr>
			<td colspan="12" align="center"><input type="submit" value="提交"/></td>
		</tr>
		</form>
</table> 


</body>
</html>
