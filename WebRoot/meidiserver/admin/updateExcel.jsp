<%@ page language="java" import="java.util.*,wilson.upload.*,wilson.matchOrder.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	
	String fileName = request.getParameter("fileName");
	String confirm = request.getParameter("confirm");
	String filePath = new SuningDataUpload().getUploadPath();
	List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
	//UploadOrderManager uom = new UploadOrderManager();

	boolean showContent =false;
	
	boolean confirmResult = false;
	
	if(confirm != null && confirm != "" && confirm.equals("confirm")){
		confirmResult = UploadManager.saveSuningFileToDB(filePath,fileName);
		response.sendRedirect("/meidi/meidiserver/admin/updateExcel.jsp");
		return;
	}else{
		if(fileName != null && fileName != "" && !fileName.equals("")){			
			UploadOrders = new XLSReader().readSuningXLS(filePath,fileName);
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

<%
	if(fileName != null && fileName != "" && !fileName.equals("")){	
		String temp = "";
		if(UploadOrders!=null&&UploadOrders.size()>0){
			temp = "上传成功";
		}else{
			temp = "上传失败";
%>
			<script type="text/javascript">
				alert('<%=temp%>');
			
			</script>
<%

		}
	}

%>

<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
      
    <div > 
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="../Model"><font style="color:red;font-size:20px;" >下载模板</font> </a>

  <form action="/meidi/meidiserver/SuningDataUpload" method="post" enctype ="multipart/form-data" runat="server"> 
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
			
			<td align="center">销售门店</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">销售日期</td>
			<td align="center">交货日期</td> 
			<td align="center">票面型号</td> 
			<td align="center">票面数量</td> 
			<td align="center">供价</td> 
			<td align="center">扣点</td> 
		
		</tr> 
		<%
		if(showContent){
			for(int i = 0 ; i< UploadOrders.size();i++){
		%>
		
		<tr>  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			
			<td align="center"><%= UploadOrders.get(i).getShop() %></td>
			<td align="center"><%= UploadOrders.get(i).getPosNo() %></td>
			<td align="center"><%= UploadOrders.get(i).getSaleTime() %></td>
			<td align="center"><%= UploadOrders.get(i).getDealTime() %></td> 
			<td align="center"><%= UploadOrders.get(i).getType() %></td> 
			<td align="center"><%= UploadOrders.get(i).getNum() %></td> 
			<td align="center"><%= UploadOrders.get(i).getSalePrice() %></td> 
			<td align="center"><%= UploadOrders.get(i).getBackPoint() %></td> 
		
		</tr> 
		<%
			}
		%>
		<form method="post" action="">
		<input type="hidden" name="fileName" value="<%=fileName %>"/>
		<input type="hidden" name="confirm" value="confirm"/>
		<tr>
			<td colspan="8" align="center" ><input name="submit" type="submit" value="确认"/></td>
			<td colspan="1">  </td>
		</tr>
		</form>
		<%
		}
		%>
</table> 
     </div>

</body>
</html>
