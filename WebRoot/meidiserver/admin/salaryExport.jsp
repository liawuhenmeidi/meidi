<%@page import="utill.StringUtill"%>
<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@page import="wilson.salaryCalc.SalaryResult"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,wilson.salaryCalc.*,java.text.SimpleDateFormat,wilson.catergory.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	//重置文件
	String reset = request.getParameter("reset");
	if(!StringUtill.isNull(reset) && reset.equals("true")){
		String name = (String)request.getSession().getAttribute("exportSalaryName");
		
		if(!StringUtill.isNull(name)){
			if(!name.equals("all")){
				SalaryCalcManager.resetSalaryResultByName(name);
			}else{
				//lists = SalaryCalcManager.getSalaryResultByDate((Date)request.getSession().getAttribute("startDate"), (Date)request.getSession().getAttribute("endDate"));
			}
		}else{
			return;
		}
		response.sendRedirect("salaryCalc.jsp");
	}
	
	
	
	String startDateSTR = request.getParameter("startDate");
	String endDateSTR = request.getParameter("endDate");
	
	 
	//取出所有salaryResult
	List<SalaryResult> salaryResult = SalaryCalcManager.getSalaryResult();
	//取出对应的uploadOrder
	List<UploadOrder> showOrders = SalaryCalcManager.getUploadOrderFromSalaryResult(salaryResult);
	//取出对应的名字
	List<String> orderNames = UploadManager.getAllUploadOrderNames(showOrders);
	
	
	
	List<SalaryResult> salaryResultBytime = new ArrayList<SalaryResult>(); 
	List<UploadOrder> showOrdersBytime = new ArrayList<UploadOrder>();
	List<String> orderNamesBytime = new ArrayList<String>();
	
	if(startDateSTR != null && !startDateSTR.equals("") && endDateSTR!= null && !endDateSTR.equals("")){
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Date endDate = new Date();
		startDate = df1.parse(startDateSTR);
	    endDate = df1.parse(endDateSTR);
	    salaryResultBytime = SalaryCalcManager.getSalaryResultByDate(startDate, endDate);
	    showOrdersBytime = SalaryCalcManager.getUploadOrderFromSalaryResult(salaryResultBytime);
	    orderNamesBytime = UploadManager.getAllUploadOrderNames(showOrdersBytime);
	    
		//导出用
		session.setAttribute("startDate", startDate);
		session.setAttribute("endDate", endDate);
	}
	
	//下面用到的temp变量
	String tempName = "";
	
	boolean byTime = false;
	if(orderNamesBytime.size() > 0 ){
		byTime = true;
	}
	
	//展示用
	List<SalaryResult> showResult = new ArrayList<SalaryResult>();
	
	String name = request.getParameter("name");
	if(!StringUtill.isNull(name)){
		if(!name.equals("all")){
			showResult = SalaryCalcManager.getSalaryResultByName(name);
			showResult = SalaryCalcManager.initSalaryModel(showResult);
			
		}else{
			showResult = SalaryCalcManager.initSalaryModel(SalaryCalcManager.getSalaryResultByDate((Date)request.getSession().getAttribute("startDate"), (Date)request.getSession().getAttribute("endDate")));
			
		}
		if(showResult.size() > 0 ){
			showResult =  SalaryCalcManager.sortSalaryResult(showResult, showResult.get(0).getUploadOrder().getFileName());
		}
		//导出用
		session.setAttribute("exportSalaryName", name);
		
	}

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提成导出页</title>
  
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
<div style="position:fixed;width:100%;height:20px;"></div>
<div style="position:fixed;width:80%;height:20px;"></div>
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>     
<p>按时间导出</p>
<form action="" method="post">
<input type="hidden" name="type" value="bydate"/>
开始: <input class="date2" name="startDate" type="text" id="datepicker1" onclick="new Calendar().show(this);" placeholder="必填"/>----------结束: <input class="date2" name="endDate" type="text" id="datepicker2" onclick="new Calendar().show(this);" placeholder="必填"/>
<input type="submit" value="搜索"/>
</form>

<%
if(byTime){
%>

<form action="" method="post">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名称:
<input type="hidden" name="type" value="byname"/>
<select name="name">
	<option value="all" selected="selected">全部</option>
	<%
	for(int i = 0 ; i < orderNamesBytime.size() ; i ++){
		tempName = orderNamesBytime.get(i);
	%>
	<option value="<%=tempName %>" ><%=tempName %></option>
	<%
	} 
	%>
</select>
<input type="submit" value="查看"/>
</form>
<%
}
%>

<hr style="border : 1px dashed blue;" />

<p>按文件名导出</p>

<form action="" method="post">
<input type="hidden" name="type" value="byname"/>
<select name="name">
	<option value="" selected="selected"></option>
	<%
	for(int i = 0 ; i < orderNames.size() ; i ++){
		tempName = orderNames.get(i);
	%>
	<option value="<%=tempName %>" ><%=tempName %></option>
	<%
	} 
	%>
</select>
<input type="submit" value="查看"/>
</form>
<hr style="border : 1px dashed blue;" />
<%
if(showResult.size() > 0 ){
%>

<a href="../SalaryExportServlet"><button name="exportButton" style="background-color:red;font-size:50px;" >导出</button></a>
<a href="salaryExport.jsp?reset=true"><button name="resetButton" style="background-color:red;font-size:50px;" onclick="return confirm('是否确认?')" >重新计算提成</button></a>
	
<hr style="border : 1px dashed blue;" />
	<table border="1px" align="left" >
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
			<td>单价</td>
			<td>提成</td>
		</tr>
		<%for(int i = 0 ; i <  showResult.size() ; i ++){ %>
		<tr>
			<td><%= i+1 %></td>
			<td id="<%=showResult.get(i).getId() %>filename"><%=showResult.get(i).getUploadOrder().getName() %></td>
			<td id="<%=showResult.get(i).getId() %>shop"><%=showResult.get(i).getUploadOrder().getShop() %></td>
			<td id="<%=showResult.get(i).getId() %>pos"><%=showResult.get(i).getUploadOrder().getPosNo() %></td>
			<td id="<%=showResult.get(i).getId() %>saletime"><%=showResult.get(i).getUploadOrder().getSaleTime() %></td>
			<td id="<%=showResult.get(i).getId() %>catergory"><%=showResult.get(i).getSalaryModel() == null?"":showResult.get(i).getSalaryModel().getCatergory() %></td>
			<td id="<%=showResult.get(i).getId() %>salemanname"><%=showResult.get(i).getUploadOrder().getSaleManName() %></td>
			<td id="<%=showResult.get(i).getId() %>saletype"><%=showResult.get(i).getUploadOrder().getType()  %></td>
			<td id="<%=showResult.get(i).getId() %>num"><%=showResult.get(i).getUploadOrder().getNum() %></td>
			<td id="<%=showResult.get(i).getId() %>saleprice"><%=showResult.get(i).getUploadOrder().getSalePrice() %></td>
			<td id="<%=showResult.get(i).getId() %>salary"><a href="#" onClick="javascript:window.open('./salaryResultDetail.jsp?id=<%=showResult.get(i).getId()%>', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')" ><%=showResult.get(i).getSalary()==null?"":showResult.get(i).getSalary() %></a></td>
		</tr>
		<%} %>
	</table>
	
<%
}
%> 
</body>
</html>
