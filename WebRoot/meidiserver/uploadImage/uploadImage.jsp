<%@ page language="java" contentType="text/html; charset=utf-8" import=" java.util.*"
	pageEncoding="utf-8"%>
 
<%
int id = Integer.parseInt(request.getParameter("id"));
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>index.html</title>

    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
 
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
 
  </head>

  <body>
   <form action="../servlet/FileUpload" method="post" enctype="multipart/form-data" name="form1">
   <input type="hidden" name="id" value="<%=id %>">
  <input type="file" name="file">
  <input type="submit" name="Submit" value="upload">
</form> 

  </body>
</html>
