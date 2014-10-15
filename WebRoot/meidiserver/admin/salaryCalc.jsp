<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,wilson.salaryCalc.*,order.*,orderproduct.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	
	
	String paraOrderName = request.getParameter("orders");
	String paraSalaryModelName = request.getParameter("models");
	String paraSave = request.getParameter("save");
	
	List<UploadOrder> showOrders = new ArrayList<UploadOrder>();
	List<UploadSalaryModel> showSalaryModels = new ArrayList<UploadSalaryModel>();
	
	if(paraOrderName != null && !paraOrderName.equals("")){
		showOrders = UploadManager.getCheckedOrdersByName(paraOrderName);
	}
	
	if(paraSalaryModelName != null && !paraSalaryModelName.equals("")){
		showSalaryModels = UploadManager.getSalaryModelsByName(paraSalaryModelName);
	}

	//取要显示的order和salarymodel实例的集合
	List<UploadOrder> uploadOrders = UploadManager.getCheckedUploadOrders();
	List<UploadSalaryModel> salaryModels = UploadManager.getAllSalaryModel();
	
	//取出其中的名称
	List<String> orderNames = UploadManager.getAllUploadOrderNames(uploadOrders);
	List<String> salaryModelsNames = UploadManager.getAllSalaryModelNames(salaryModels);
	
	//下面用到的
	String tempString = "";
	
	
	List<SalaryResult> salaryResult = SalaryCalcManager.calcSalary(showOrders, showSalaryModels);
	List<UploadOrder> unCalcUploadOrders = SalaryCalcManager.getUnCalcUploadOrders();
	
	
	
	if(paraSave != null && !paraSave.equals("")){
		if(showOrders!=null&&showOrders.size()>0 && showSalaryModels!=null&&showSalaryModels.size()>0){
			SalaryCalcManager.saveSalaryResult(salaryResult);
			response.sendRedirect("/meidi/meidiserver/admin/salaryCalc.jsp");
			return;
		}
	}
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

<table>
<tr>
<td align="left" valign="top">
	<table  cellspacing="1" border="2px" >
			<tr>
				<form method="post" action="">
				<td colspan="6" align="center">
				<h3>需要提成的订单</h3>
				&nbsp;&nbsp;
				<select name="orders"/>
				<%
				for(int i = 0 ; i < orderNames.size() ; i ++){
					tempString = orderNames.get(i);
					
				%>
				<option <%	if(tempString.equals(paraOrderName)){%>selected="selected"<% }%>  value="<%=tempString %>"><%=tempString %></option>
				}
				<%
				}
				%>
				&nbsp;&nbsp;
				<input type="submit" value="显示"/>
				</td>
				
			</tr>
			<tr>  
	
				<td align="center">销售门店</td>
				<td align="center">pos(厂送)单号</td>
				<td align="center">销售日期</td>
				<td align="center">票面型号</td> 
				<td align="center">票面数量</td> 
				<td align="center">供价</td>
				
			</tr>
			<%
			for(int i = 0 ; i < showOrders.size() ; i ++){
			%>
			<tr>  
	
				<td align="center"><%=showOrders.get(i).getShop() %></td>
				<td align="center"><%=showOrders.get(i).getPosNo()%></td>
				<td align="center"><%=showOrders.get(i).getSaleTime() %></td>
				<td align="center"><%=showOrders.get(i).getType() %></td> 
				<td align="center"><%=showOrders.get(i).getNum() %></td> 
				<td align="center"><%=showOrders.get(i).getSalePrice() %></td>
				
			</tr>
			<%
			}
			%>
	</table>
</td>
<td width="100px"></td>
<td valign="top">
	<table cellspacing="1" border="2px">
			<tr>
				
				<td colspan="6" align="center">
				<h3>提成标准</h3>
				&nbsp;&nbsp;
				<select name="models"/>
				<%
				for(int i = 0 ; i < salaryModelsNames.size() ; i ++){
					tempString = salaryModelsNames.get(i);
				%>
				<option <%	if(tempString.equals(paraSalaryModelName)){%>selected="selected"<% }%> value="<%=tempString %>"><%=tempString %></option>
				<%
				}
				%>
				&nbsp;&nbsp;
				<input type="submit" value="显示"/>
				</td>
				</form>
			</tr>
			<tr>
				<td align="center">门店</td>
				<td align="center">类别</td>
				<td align="center">型号</td>
				<td align="center">提成标准</td>
				<td align="center">生效日期</td>
				<td align="center">截至日期</td> 
		
			</tr> 
			<%
			for(int i = 0 ; i < showSalaryModels.size() ; i ++){
			%>
			<tr>
				<td align="center"><%=showSalaryModels.get(i).getShop() %></td>
				<td align="center"><%=showSalaryModels.get(i).getCatergory() %></td>
				<td align="center"><%=showSalaryModels.get(i).getType() %></td>
				<td align="center"><%=showSalaryModels.get(i).getContent() %></td>
				<td align="center"><%=showSalaryModels.get(i).getStartTime() %></td>
				<td align="center"><%=showSalaryModels.get(i).getEndTime() %></td> 
		
			</tr> 
			<%
			}
			%>
	</table>
</td> 
</tr>


<tr>
<td colspan="3" align="center">
	<table border="2px" align="center">
		<tr>
			<td align="center" colspan="9"><h3>提成结果</h3></td>
		</tr>
		<tr>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">类别</td> 
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
			<td align="center">供价</td>
			<td align="center">提成标准</td>
			<td align="center">提成</td>
		</tr>
		<%for(int i = 0 ; i < salaryResult.size() ; i ++){
		%>
		<tr>
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getShop() %></td>
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getPosNo() %></td>
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getSaleTime() %></td>
			<td align="center"><%=salaryResult.get(i).getSalaryModel().getCatergory() %></td> 
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getType() %></td> 
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getNum() %></td> 
			<td align="center"><%=salaryResult.get(i).getUploadOrder().getSalePrice() %></td>
			<td align="center"><%=salaryResult.get(i).getSalaryModel().getContent() %></td>
			<td align="center"><%=salaryResult.get(i).getSalary() %></td>
		</tr>	
		<%
		}
		%>
		
		<tr>
			<td align="center" colspan="9"><h3>无对应提成标准的订单</h3></td>
		</tr>
		
		<tr>
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
			<td align="center">供价</td>
		</tr>
		<%
		for(int i = 0 ; i < unCalcUploadOrders.size() ; i ++){
			
		%>
		<tr>
			<td align="center"><%=unCalcUploadOrders.get(i).getShop() %></td>
			<td align="center"><%=unCalcUploadOrders.get(i).getPosNo() %></td>
			<td align="center"><%=unCalcUploadOrders.get(i).getSaleTime() %></td>
			<td align="center"><%=unCalcUploadOrders.get(i).getType() %></td> 
			<td align="center"><%=unCalcUploadOrders.get(i).getNum() %></td> 
			<td align="center"><%=unCalcUploadOrders.get(i).getSalePrice() %></td>
		</tr>
		<%
		}
		%>
		
		<%if(salaryResult.size() > 0 )
		{ 
		%>
		<tr>
			<td align="center" colspan="9">
			<form method="post" action="">
			<input type="hidden"  name="save" value="true" />
			<input type="hidden"  name="orders" value="<%=paraOrderName%>"/>
			<input type="hidden"  name="models" value="<%=paraSalaryModelName%>"/>
			<input type="submit" value="提交保存"/>
			</form>
			</td>	
		</tr>
		<%
		}
		%>
	</table>
</td>
</tr>


</table>

</body>
</html>
