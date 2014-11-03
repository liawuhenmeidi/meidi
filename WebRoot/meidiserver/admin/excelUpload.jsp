<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	String fileName = request.getParameter("fileName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Excel上传页</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style type="text/css">
.fixedHead { 
position:fixed;
}  
.tabled tr td{ 
width:50px
}  
*{
    margin:0;
    padding:0;
}

td { 
    width:100px;
    line-height:30px;
}
 
#table{  
    BACKGROUND-IMAGE: url('../image/f.JPG');
   
    table-layout:fixed ;
}

#th{ 
    background-color:white;
    position:absolute;
   
    height:30px;
    top:0;
    left:0;
}
#wrap{
    clear:both;
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:450px;
}

</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>

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

<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
      
    <div > 
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../DownloadServlet?name=suningmuban&type=model"><font style="color:red;font-size:20px;" >系统比对模板</font> </a><br />
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../DownloadServlet?name=tichengmuban&type=model"><font style="color:red;font-size:20px;" >提成标准模板</font> </a><br />
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../DownloadServlet?name=xiaoshoudanmuban&type=model"><font style="color:red;font-size:20px;" >销售单模板</font> </a>
  

  <form action="../ExcelUpload" method="post" enctype ="multipart/form-data" runat="server"> 
      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
      <font style="color:red;font-size:20px;" >导入数据 : </font>
      <input id="File1" runat="server" name="UpLoadFile" type="file" /> 
      类型：<select name="uploadType">
      <option value="1">系统比对单上传</option>   
        <option value="2">提成标准上传</option>   
        <option value="3">销售单上传</option>   
      </select>
      <input type="submit" name="Button1" value="提交文件" id="Button1" />
  </form>
  

  </div>     
      
</div >

</div>

<div style=" height:120px;">
</div>
 
<br/>  

</body>
</html>
