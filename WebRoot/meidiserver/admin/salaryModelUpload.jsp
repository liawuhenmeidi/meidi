<%@ page language="java" import="java.util.*,utill.ArrayListUtil,wilson.upload.*,wilson.matchOrder.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	
	String fileName = request.getParameter("fileName");
	String confirm = request.getParameter("confirm");
	String filePath = ExcelUpload.getSalaryFilePath();
	List <UploadSalaryModel> salaryModelList = new ArrayList<UploadSalaryModel>();

	
	boolean showContent =false;
	int maxLength = 0 ;
	
	boolean confirmResult = false;
	
	if(confirm != null && confirm != ""){
		if(confirm.equals("confirm")){
			confirmResult = UploadManager.saveSalaryFileToDB(filePath,fileName);
		}
		response.sendRedirect("./excelUpload.jsp");
		return;
		
	}else{
		if(fileName != null && fileName != "" && !fileName.equals("")){			
			salaryModelList = new XLSReader().readSalaryModelXLS(filePath,fileName);
			maxLength = ArrayListUtil.getMaxLengthInUploadSalaryModelList(salaryModelList);
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
		if(salaryModelList!=null&&salaryModelList.size()>0){
			temp = "上传成功";
		}else{
			temp = "上传失败";
			showContent =false;
%>
			<script type="text/javascript">
				alert('<%=temp%>');
			
			</script>
<%

		}
	}

%>


<div style="position:fixed;width:100%;height:80px;">
<div style="position:fixed;width:80%;height:80px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
      
      
</div >

</div>

<div style=" height:120px;">
</div>
 
<br/>  
<form id="baseform" method="post" action="" >

<table width="100%" align="center" border=0>
       <tr style="height:30px" align="center">
       <td width="100%" align="center">		
       <input type="hidden" name="fileName" value="<%=fileName %>"/>
		<input type="hidden" name="confirm" value="confirm" id="submitswitcher"/>
       <h3><%=salaryModelList.get(0).getName() %>	</h3>
       <%
		if(salaryModelList.size() == 1 && salaryModelList.get(0).getId() == -1){
			return;
		}
		%>
       <%if(showContent){ %>
		<input type="button" id="commitbutton" value="提交" onmousedown="$('#commitbutton').val('正在提交');$('#baseform').submit();$('#submitswitcher').val('confirmed')"></input>
		<%} %>
		</td>
		</tr> 
 
   <tr>
   <td align="center" width="100%">
    <div style="overflow-y:auto; width:100%;height:450px">
<table  cellspacing="1" border="2px"  id="table">
		
		
		
		
		
		<tr>  
			<td align="center">序号</td>
			<td align="center">型号</td>
			<%
			for(int j = 0 ; j < maxLength ; j ++){
			%>
			<td align="center">零售价</td>
			<td align="center">提成</td> 
			<%
			}
			%>
		</tr> 
		
		
		<%
		if(showContent){
			for(int i = 0 ; i< salaryModelList.size();i++){
		%>
		
		<tr>  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center"><%= i+1 %></td>
			<td align="center"><%= salaryModelList.get(i).getType() %></td>
			<%
				String tempContent = salaryModelList.get(i).getPrintContent();
				tempContent = tempContent.replace("{", "").replace("}", "").replace("\"", "");
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
		}
		%>
		
		
		
</table> 
</div>
   </td>
   </tr>
 </table>
 </form>
</body>
</html>
