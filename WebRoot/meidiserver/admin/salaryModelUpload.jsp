<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	
	String fileName = request.getParameter("fileName");
	String confirm = request.getParameter("confirm");
	String filePath = new SalaryModelUpload().getUploadPath();
	List <UploadSalaryModel> salaryModelList = new ArrayList<UploadSalaryModel>();

	
	boolean showContent =false;
	
	boolean confirmResult = false;
	
	if(confirm != null && confirm != "" && confirm.equals("confirm")){
		confirmResult = UploadManager.saveSalaryFileToDB(filePath,fileName);
		response.sendRedirect("/meidi/meidiserver/admin/salaryModelUpload.jsp");
		return;
	}else{
		if(fileName != null && fileName != "" && !fileName.equals("")){			
			salaryModelList = new XLSReader().readSalaryModelXLS(filePath,fileName);
			showContent = true;
		}	
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>苏宁消单明细上传页面</title>
  
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



<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
      
    <div > 
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../../data/model/tichengmuban.xls"><font style="color:red;font-size:20px;" >下载模板</font> </a>

  <form action="../SalaryModelUpload" method="post" enctype ="multipart/form-data" runat="server"> 
      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
      <font style="color:red;font-size:20px;" >导入数据 : </font>
      <input id="File1" runat="server" name="UpLoadFile" type="file" /> 
      <input type="submit" name="Button1" value="提交文件" id="Button1" />
  </form>
  

  </div>     
      
</div >

</div>

<div style=" height:120px;">
</div>
 
<br/>  
<div >
<table  cellspacing="1" border="2px"  id="table">
		<tr>  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			
			<td align="center">类别</td>
			<td align="center">型号</td>
			<td align="center">零售价</td>
			<td align="center">提成</td> 
		
		</tr> 
		
		<%
		if(salaryModelList.size() == 1 && salaryModelList.get(0).getId() == -1){
			
		%>
		<tr>
			<td colspan="5" style="color:red"><h3><%=salaryModelList.get(0).getContent() %></h3></td>
		</tr>
		<%
		return;
		}
		%>
		
		<%
		if(showContent){
			for(int i = 0 ; i< salaryModelList.size();i++){
		%>
		
		<tr>  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			
			<td align="center"><%= salaryModelList.get(i).getCatergory() %></td>
			<td align="center"><%= salaryModelList.get(i).getType() %></td>
			<%
				String tempContent = salaryModelList.get(i).getContent();
				tempContent = tempContent.replace("{", "").replace("}", "");
				//System.out.println(tempContent);
				for(int j = 0 ; j < tempContent.split(",").length ; j ++){
			%>
			<td align="center"><%= tempContent.split(",")[j].split(":")[0] %></td>
			<td align="center"><%= tempContent.split(",")[j].split(":")[1] %></td> 
			<%
				}
			%>
		
		</tr> 
		<%
			}
		%>
		<form method="post" action="">
		<input type="hidden" name="fileName" value="<%=fileName %>"/>
		<input type="hidden" name="confirm" value="confirm"/>
		<tr>
			<td colspan="4" align="center" ><input name="submit" type="submit" value="确认"/></td>

		</tr>
		</form>
		<%
		}
		%>
</table> 
     </div>

</body>
</html>
