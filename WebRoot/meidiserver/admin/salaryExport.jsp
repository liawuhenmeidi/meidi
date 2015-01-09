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
				HiddenCatergoryMapingManager.delCatergoryMaping(name);
			}else{
				//lists = SalaryCalcManager.getSalaryResultByDate((Date)request.getSession().getAttribute("startDate"), (Date)request.getSession().getAttribute("endDate"));
			}
		}else{
			return;
		}
		response.sendRedirect("salaryCalc.jsp");
	}
	
	//隐藏文件
	String hide = request.getParameter("hide_button");
	if(!StringUtill.isNull(hide) && hide.equals("true")){
		String hideName = request.getParameter("name");
		HiddenFileManager.transferFile(hideName);
		response.sendRedirect("salaryExport.jsp");
		return;
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
			//showResult = SalaryCalcManager.getCalcedSalaryResultByName(name);
			showResult = SalaryCalcManager.initSalaryModel(showResult);
			
		}else{
			showResult = SalaryCalcManager.initSalaryModel(SalaryCalcManager.getSalaryResultByDate((Date)request.getSession().getAttribute("startDate"), (Date)request.getSession().getAttribute("endDate")));
			
		}
		if(showResult.size() > 0 ){
			showResult =  SalaryCalcManager.sortSalaryResult(showResult,true);
		}
		//导出用
		session.setAttribute("exportSalaryName", name);
	}
	
	//下面用到的背景色(总计用到)
	String backgroundColor ="#B9D3EE";
	//下面用到的字体色(负数提成或者未提成时候用到)
	String fontColor = "#EE3B3B";
	
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
<script type="text/javascript">
var salary_fileTotal = 0.0;
var salary_sum = 0.0;

var saleprice_sum=0.0;
var saleprice_fileTotal = 0.0;

var salenum_sum=0;
var salenum_fileTotal=0;

var catergoryTotalDetail = {};

function reloadTable(){
	var cols = $('#basetable tr')[0].cells.length;
	 $('#basetable tr').each(function () {  
		 //提成
		 var id = this.cells[cols-1].id;
		 var item = $('#'+ id);
		 if(item.attr('value') == 'total'){
			 item.text(salary_sum);
			 salary_fileTotal += salary_sum;
			 salary_sum = 0;
		 }else{
			 if(checkNumber(item.attr('value'))){
				 salary_sum += item.attr('value')*1.0;
			 }
		 }
		 //个数
		 var id3 = this.cells[cols-3].id;
		 var item3 = $('#'+ id3);
		 if(item3.attr('value') == 'total'){
			 item3.text(salenum_sum);
			 salenum_fileTotal += salenum_sum;
			 salenum_sum = 0;
		 }else{
			 if(checkNumber(item3.attr('value'))){
				 salenum_sum += item3.attr('value')*1.0;
			 }
		 }
		//售价
		 var id2 = this.cells[cols-2].id;
		 var item2 = $('#'+ id2);
		 if(item2.attr('value') == 'total'){
			 saleprice_sum = Math.round(saleprice_sum*100)/100; 
			 item2.text(saleprice_sum);
			 saleprice_fileTotal += saleprice_sum;
			 saleprice_sum = 0;
		 }else{
			 if(checkNumber(item2.attr('value'))){
				 saleprice_sum += item2.attr('value')*1.0*item3.attr('value');
			 }
		 }
		 
		 //品类
		 var id4 = this.cells[cols-6].id;
		 var item4 = $('#' + id4);
		 	//非总计
		 if(id4 != '0catergory'){
			 var new_category = {};
			 
			 if(catergoryTotalDetail[item4.text()] == undefined){
				 //如果是新的catergory
				 new_category["catergory"] = item4.text();

				 if(checkNumber(item2.attr('value'))){
					 new_category["price"] = item2.attr('value')*1.0*item3.attr('value');
				 }
				 if(checkNumber(item3.attr('value'))){
					 new_category["num"] = item3.attr('value')*1.0;
				 }
				 if(checkNumber(item.attr('value'))){
					 new_category["salary"] = item.attr('value')*1.0;
				 }
				 
				 catergoryTotalDetail[item4.text()] = new_category;
				 
			 }else{
				 new_category = catergoryTotalDetail[item4.text()];
				 
				 if(checkNumber(item2.attr('value'))){
					 new_category["price"] += item2.attr('value')*1.0*item3.attr('value');
				 }
				 if(checkNumber(item3.attr('value'))){
					 new_category["num"] += item3.attr('value')*1.0;
				 }
				 if(checkNumber(item.attr('value'))){
					 new_category["salary"] += item.attr('value')*1.0;
				 }
				 catergoryTotalDetail[item4.text()] = new_category;
				 
			 }
			 
		 }else if($('#' + this.cells[cols-9].id).text().indexOf('-品类总计')>=0){
			 var tmp_catergory = catergoryTotalDetail[item4.text()];
			 
			 item.text(tmp_catergory["salary"]);
			 item2.text(tmp_catergory["price"]);
			 item3.text(tmp_catergory["num"]);
		 }
		 
		 
     });
	 
	 
	 
	 $('#filetotalsalary').text(salary_fileTotal);
	 salary_fileTotal = 0;
	 
	 saleprice_fileTotal = Math.round(saleprice_fileTotal*100)/100; 
	 $('#filetotalsaleprice').text(saleprice_fileTotal);
	 saleprice_fileTotal = 0;
	 
	 $('#filetotalnum').text(salenum_fileTotal);
	 salenum_fileTotal = 0;
}

function checkNumber(val){  
    //var reg = /^-*[0-9]+.[0-9]*$/;  
    if(val ==""){
    	return false;
    }
   // if(reg.test(val)){  
  //      return true;  
  //  }else{
  //  	return false;
  //  }
   if(!isNaN(val)){
	   return true;
   }else{
	   return false;
   }
}
</script>
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
<input id="hide_button" name="hide_button" type="hidden" value="false"/> 
<button type="submit">查看</button>
<button type="submit" onclick="if(confirm('是否确认?隐藏后，文件将不可修改')){$('#hide_button').val('true');}else{return false;}">隐藏</button>
</form>
<hr style="border : 1px dashed blue;" />
<a href="#" onClick="javascript:window.open('./hiddenSalaryResult.jsp', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')" ><button type="button">查看已隐藏文件</button></a>

<hr style="border : 1px dashed blue;" />
<%
if(showResult.size() > 0 ){
%>

<a href="../SalaryExportServlet"><button name="exportButton" style="background-color:red;font-size:50px;" >导出</button></a>
<a href="salaryExport.jsp?reset=true"><button name="resetButton" style="background-color:red;font-size:50px;" onclick="return confirm('是否确认?')" >重新计算提成</button></a>
<a href="printSalaryResult.jsp"><button name="exportButton" style="background-color:red;font-size:50px;" >打印</button></a>

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
			<td>单价</td>
			<td>合计提成</td>
		</tr>
		<%
		boolean total = false;
		boolean filetotal = false;
		boolean editable = true;
		boolean ifFontColor = false;
		
		for(int i = 0 ; i <  showResult.size() ; i ++){
			if(showResult.get(i).getStatus() == SalaryResult.STATUS_TOTAL){
				total = true;
			}else{
				total = false;
			}
			if(!showResult.get(i).isFinished()){
				editable = false;
			}else{
				editable = true;
			}
			
			if(!editable || showResult.get(i).getSalary() < 0){
				ifFontColor = true;
			}else{
				ifFontColor = false;
			}
			
			if(i == showResult.size() -1 ){
				filetotal = true;
			}

		%>
		<tr bgcolor='<%=total?backgroundColor:"" %>'>
			<td><%= i+1 %></td>
			<td id="<%=showResult.get(i).getId() %>filename"><%=total?"总计":showResult.get(i).getUploadOrder().getName() %></td>
			<td id="<%=showResult.get(i).getId() %>shop"><%=showResult.get(i).getUploadOrder().getShop() %></td>
			<td id="<%=showResult.get(i).getId() %>pos"><%=showResult.get(i).getUploadOrder().getPosNo() %></td>
			<td id="<%=showResult.get(i).getId() %>saletime"><%=showResult.get(i).getUploadOrder().getSaleTime() %></td>
			<td id="<%=showResult.get(i).getId() %>catergory"><%=showResult.get(i).getSalaryModel() == null?"":showResult.get(i).getSalaryModel().getCatergory() %></td>
			<td id="<%=showResult.get(i).getId() %>salemanname"><%=showResult.get(i).getUploadOrder().getSaleManName() %></td>
			<td id="<%=showResult.get(i).getId() %>saletype"><%=showResult.get(i).getUploadOrder().getType()  %></td>
			<%if(!total){%>
			<td id="<%=showResult.get(i).getId() %>num" value="<%=showResult.get(i).getUploadOrder().getNum() %>"><%=showResult.get(i).getUploadOrder().getNum() %></td>
			<td id="<%=showResult.get(i).getId() %>saleprice" value="<%=showResult.get(i).getUploadOrder().getSalePrice() %>"><%=showResult.get(i).getUploadOrder().getSalePrice() %></td>
			<td id="<%=showResult.get(i).getId() %>salary" value="<%=showResult.get(i).getPrintSalary() %>" <%if(ifFontColor){ %> style="background:<%=fontColor %>" <%} %>><a  <%if(editable){ %>href="#" onClick="javascript:window.open('./salaryResultDetail.jsp?id=<%=showResult.get(i).getId()%>', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')" <%} %> ><%=showResult.get(i).getPrintSalary() %></a></td>
			<%
			}else{
			%>
			<td id="<%=filetotal?"filetotal":StringUtill.shortUUID() %>num" value="total"><%=showResult.get(i).getUploadOrder().getNum() %></td>
			<td id="<%=filetotal?"filetotal":StringUtill.shortUUID() %>saleprice" value="total"><%=showResult.get(i).getUploadOrder().getSalePrice() %></td>
			<td id="<%=filetotal?"filetotal":StringUtill.shortUUID() %>salary" value="total" <%if(ifFontColor){ %> style="background:<%=fontColor %>" <%} %>><%=showResult.get(i).getPrintSalary() %></td>
			<%
			}
			%>
		</tr>
		<%} %>
	</table>
	
<%
}
%> 
</body>
</html>
