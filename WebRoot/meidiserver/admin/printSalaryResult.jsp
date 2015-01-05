<%@page import="utill.StringUtill"%>
<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@page import="wilson.salaryCalc.SalaryResult"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,wilson.salaryCalc.*,java.text.SimpleDateFormat,wilson.catergory.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	List<SalaryResult> showResult = new ArrayList<SalaryResult>();
	
	String name = (String)request.getSession().getAttribute("exportSalaryName");
	if(!name.equals("all")){
		showResult = SalaryCalcManager.getSalaryResultByName(name);
	}else{
		
	}
	if(showResult.size() > 0){
		showResult = SalaryCalcManager.sortSalaryResult(showResult);
	}
	
	for(int i = 0 ; i < showResult.size() ; i ++){
		if(showResult.get(i).getStatus() != SalaryResult.STATUS_TOTAL){
			showResult.remove(i);
			i --;
		}
	}
	
	session.setAttribute("exportType", "input");
	session.setAttribute("exportList", showResult);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提成导出页(打印)</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style type="text/css">
body {
	font-family: "Trebuchet MS", "Helvetica", "Arial",  "Verdana", "sans-serif";
	font-size: 62.5%;
	overflow: auto;
}
</style>

</style>
</head>

<body style="overflow:auto">
 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript">
window.onload = function() { 
	 window.print();
}; 

</script>
<div style="position:fixed;width:100%;height:20px;"></div>
<div style="position:fixed;width:80%;height:20px;"></div>
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>     

<%
if(showResult.size() > 0 ){
%>

<a href="../SalaryExportServlet"><button name="exportButton" style="background-color:red;font-size:20px;" >导出</button></a>

<hr style="border : 1px dashed blue;" />
	<table border="1px" align="left" id="basetable" name="basetable">
		<tr>
			<td>序号</td>
			<td>文件名称</td>
			<td>门店</td>
			<td>POS单号</td>
			<td>销售日期</td>
			<td>品类</td>
			<td>导购员姓名</td>
			<td>销售型号</td>
			<td>数量</td>
			<td>金额</td>
			<td>提成</td>
		</tr>
		<%
		boolean filetotal = false;
		int b = 1;
		for(int i = 0 ; i <  showResult.size() ; i ++){
			
			if(i == showResult.size() -1 ){
				filetotal = true;
			}
		%>
		<tr>
			<td><%= b++ %></td>
			<td id="<%=showResult.get(i).getId() %>filename">总计</td>
			<td id="<%=showResult.get(i).getId() %>shop"><%=showResult.get(i).getUploadOrder().getShop() %></td>
			<td id="<%=showResult.get(i).getId() %>pos"><%=showResult.get(i).getUploadOrder().getPosNo() %></td>
			<td id="<%=showResult.get(i).getId() %>saletime"><%=showResult.get(i).getUploadOrder().getSaleTime() %></td>
			<td id="<%=showResult.get(i).getId() %>catergory"><%=showResult.get(i).getSalaryModel() == null?"":showResult.get(i).getSalaryModel().getCatergory() %></td>
			<td id="<%=showResult.get(i).getId() %>salemanname"><%=showResult.get(i).getUploadOrder().getSaleManName() %></td>
			<td id="<%=showResult.get(i).getId() %>saletype"><%=showResult.get(i).getUploadOrder().getType()  %></td>
			<td id="<%=filetotal?"filetotal":StringUtill.shortUUID() %>num" value="total"><%=showResult.get(i).getUploadOrder().getNum() %></td>
			<td id="<%=filetotal?"filetotal":StringUtill.shortUUID() %>saleprice" value="total"><%=showResult.get(i).getUploadOrder().getSalePrice() %></td>
			<td id="<%=filetotal?"filetotal":StringUtill.shortUUID() %>salary" value="total"><%=showResult.get(i).getSalary()==null?"0":showResult.get(i).getSalary() %></td>
			
		</tr>
		<%} %>
	</table>
	
<%
}
%> 
</body>
</html>
