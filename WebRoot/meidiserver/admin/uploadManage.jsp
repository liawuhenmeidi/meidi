<%@page import="java.net.URLEncoder"%>

<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*,wilson.salaryCalc.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	String fileName = request.getParameter("fileName");
	
	String type = request.getParameter("type"); 
	String name =request.getParameter("name");
	String button = request.getParameter("button");

	//类型不能为空 
	if(type != null && !type.equals("")){
		//接受到的名称不能为空
		if(name != null && !name.equals("")){
			
			//判断行为
			if(button != null && button.equals("删除")){
				 
				if(type.equals("uploadorder")){
					UploadManager.deleteUploadOrderByName(name);
				}else if(type.equals("salarymodel")){
					UploadManager.deleteSalaryModelByName(name);
				}else if(type.equals("changemodel")){ 
					UploadManager.deleteChangeModelByName(name);
				} 
				 
			}else if(button != null && button.equals("导出")){
				
				if(type.equals("uploadorder")){

					String url = "../UploadExport?type=uploadorder&name=" + URLEncoder.encode(URLEncoder.encode(name,"utf-8"));
					response.sendRedirect(url);
					return;
				}else if(type.equals("salarymodel")){
					String url = "../UploadExport?type=salarymodel&name=" + URLEncoder.encode(URLEncoder.encode(name,"utf-8"));
					response.sendRedirect(url);
					return;
				}
				
			}
				
		}
			
	}
	
	//销售单,苏宁单
	List<String> orderNames = UploadManager.getOrderNamesByStatus(UploadOrder.CALCED);
	
	//提成标准
	List<String> salaryModelNames = UploadManager.getAllSalaryModelNames();
	// 转化单 
	List<String> changeModelNames = UploadManager.getAllChangeModelNames();
	//下面用到的临时变量
	String tempName = "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传管理页面</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<link rel="stylesheet" href="../css/jquery-ui.css"></link>
<script src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/jquery-ui.js"></script>
  
<style type="text/css">
body {
	font-family: "Trebuchet MS", "Helvetica", "Arial",  "Verdana", "sans-serif";
	font-size: 62.5%;
}
</style>

</head>

<body>
 

 <%
	if(fileName != null && fileName != "" && !fileName.equals("")){	
		String temp = "上传失败";
%>
			<script type="text/javascript">
				alert('<%=temp%>');
			
			</script>
<%

	}
%>
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include> 
  
  
      <div >  
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../DownloadServlet?name=suningmuban&type=model"><font style="color:red;font-size:20px;" >系统比对模板</font> </a><br />
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../DownloadServlet?name=tichengmuban&type=model"><font style="color:red;font-size:20px;" >提成标准模板</font> </a><br />
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../DownloadServlet?name=xiaoshoudanmuban&type=model"><font style="color:red;font-size:20px;">销售单模板</font> </a><br />
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../DownloadServlet?name=changemuban&type=model"><font style="color:red;font-size:20px;">转化单模板</font> </a>
   

  <form action="../ExcelUpload" method="post" enctype ="multipart/form-data" runat="server"> 
      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
      <font style="color:red;font-size:20px;" >导入数据 : </font>
      <input id="File1" runat="server" name="UpLoadFile" type="file" /> 
      
      
      类型：<select name="uploadType">
      <option value="1">系统比对单上传</option>   
        <option value="2">提成标准上传</option>   
        <option value="3">销售单上传</option>   
        <option value="4">转化单上传</option>   
      </select>
      <input type="submit" name="Button1" value="提交文件" id="Button1" />
  </form>
  

  </div> 
  
  <br/><br/><br/><br/><br/>
<hr style="border : 1px dashed blue;" />

<p>系统对比/销售单 删除</p>
<form action="" method="post">
<input type="hidden" name="type" value="uploadorder"/>
<select name="name" id="uploadorderselect">
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
<input name="button" type="submit" value="删除" onclick="return confirm('是否确认删除' + $('#uploadorderselect').find('option:selected').text() + '?')"/>
<input name="button" type="submit" value="导出"/>
</form>




<p>提成模板  删除</p>
<form action="" method="post">
<input type="hidden" name="type" value="salarymodel"/>
<select name="name" id="salarymodelselect">
	<option value="" selected="selected"></option>
	<%
	for(int i = 0 ; i < salaryModelNames.size() ; i ++){
		tempName = salaryModelNames.get(i);
	%>
	<option value="<%=tempName %>" ><%=tempName %></option>
	<%
	} 
	%>
</select>
<input name="button" type="submit" value="删除" onclick="return confirm('是否确认删除' + $('#salarymodelselect').find('option:selected').text() + '?')"/>
<input name="button" type="submit" value="导出"/>

</form>

 
 <p>上传转化单删除</p>
<form action="" method="post">
<input type="hidden" name="type" value="changemodel"/>
<select name="name" id="changemodelselect">
	<option value="" selected="selected"></option>
	<%
	for(int i = 0 ; i < changeModelNames.size() ; i ++){
		tempName = changeModelNames.get(i);
	%>
	<option value="<%=tempName  %>" ><%=tempName %></option>
	<%
	} 
	%>
</select>
<input name="button" type="submit" value="删除" onclick="return confirm('是否确认删除' + $('#changemodelselect').find('option:selected').text() + '?')"/>
<input name="button" type="submit" value="导出"/>

</form>

</body>
</html>
