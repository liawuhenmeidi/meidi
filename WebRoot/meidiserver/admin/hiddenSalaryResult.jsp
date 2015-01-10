<%@page import="wilson.salaryCalc.ExcelShowModel"%>
<%@page import="wilson.salaryCalc.SalaryResult"%>
<%@page import="wilson.salaryCalc.HiddenFileManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="utill.StringUtill"%>
<%@ page language="java" import="java.util.*,user.*,wilson.upload.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

List<String> names = HiddenFileManager.getFileNames();
String namelist = StringUtill.GetJson(names);

ExcelShowModel showList = new ExcelShowModel();

String actiontype = request.getParameter("actiontype");
String name = "";
String inputType = "";
if(StringUtill.isNull(actiontype)){
	
}else{
	//取提交上来的文件名
	inputType = request.getParameter("inputType");
	if(StringUtill.isNull(inputType)){
		return;
	}else{
		if(inputType.equals("nameselect")){
			name = request.getParameter("nameselect");
		}else if(inputType.equals("nameinput")){
			name =request.getParameter("nameinput");
		}else{
			return;
		}
	}
	
	//判断执行的操作
	if(actiontype.equals("del")){
		HiddenFileManager.delFile(name);
		response.sendRedirect("hiddenSalaryResult.jsp");
	}else if (actiontype.equals("view")){
		showList = HiddenFileManager.getFileContent(name);
	}else if (actiontype.equals("export")){
		response.sendRedirect("../../" + HiddenFileManager.getDownloadPath(name));
	}
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>隐藏文件管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<link rel="stylesheet" href="../css/jquery-ui.css"/>
<script src="../js/jquery-ui.js"></script>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>


<!-- 
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
 -->

  


<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">
var namelist = <%=namelist %>;

var namepara = '<%=name %>';
var inputType = '<%=inputType %>';



$(function () {  
	$("#nameinput").autocomplete(namelist,{
		width: 320, 
		max: 4, 
		highlight: false, 
		multiple: true, 
		multipleSeparator: "", 
		scroll: true, 
		scrollHeight: 300 
	});  
	
	if(inputType == 'nameselect'){
		$('#nameselect').val(namepara);
	}else if(inputType == 'nameinput'){
		$('#nameinput').val(namepara);
	}
	

	$('#nameselect').change(function(){
		$('#nameinput').val('');
		$('#inputType').val('nameselect');
	});
	
	$('#nameinput').change(function(){
		$('#nameselect').val('');
		$('#inputType').val('nameinput');
	});
	
	$('#exportbutton').click(function(){
		$('#actiontype').val('export');
	});
	$('#delbutton').click(function(){
		var confirmDel = confirm("确认删除？删除后不可找回");
		if(confirmDel){
			$('#actiontype').val('del');
			return true;
		}else{
			return false;
		}
		
	});
	$('#viewbutton').click(function(){
		$('#actiontype').val('view');
	});
});

</script>
<div style="position:fixed;width:100%;height:100px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

</div >



<div style=" height:100px;">
</div>


 
<br/>  
 
<div id="wrap" style="text-align:center;">  
<form  action=""  method ="post"  name="baseForm" id="baseForm" onsubmit="">
<input id="actiontype" type="hidden" name="actiontype" value="view"/>
<input id="inputType" type="hidden" name="inputType" value="nameselect"/>
<table  cellspacing="1"  id="table" style="margin:auto; width:80%;"> 
		  
		<tr class="asc">	 
			<td align="center" >文件名</td>
			<td align="center" >
	        <select id='nameselect' name="nameselect" >
	        	<option value="" selected="selected"></option>
	        	<%for(int i = 0 ; i < names.size() ; i ++){ %>
	        	<option value="<%=names.get(i)%>"><%=names.get(i)%></option>
	        	<%} %>
	        </select>
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center">文件名</td>
			<td align="center" >
				<input id="nameinput" name="nameinput" value=""/>
			</td>
		</tr>
		
</table>
<hr style="border : 1px dashed blue;" /> 
<button style="background-color:red;font-size:35px;" name="exportbutton" id="exportbutton" value="false">导出</button>
<button style="background-color:red;font-size:35px;" name="delbutton" id="delbutton" value="false">永久删除</button>
<button style="background-color:red;font-size:35px;" name="viewbutton" id=""viewbutton"" value="false">查看</button>
<%if(showList.getRows() > 0 ){ %>
<table align="center" border="1px">
	<%
	for(int i = 0 ; i < showList.getRows() ; i ++){
	%>
	<tr align="center">
		<%for(int j = 0 ; j < showList.getColc() ; j ++){ %>
		<td align="center"><%=showList.getContent()[i][j]%></td>
		<% }%>
	</tr>
	<%
	}
	%>
</table>
<%} %>

</div>

</body>
</html>
