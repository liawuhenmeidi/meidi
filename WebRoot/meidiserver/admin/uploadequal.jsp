<%@ page language="java" import="java.util.*,wilson.upload.*,utill.*,wilson.matchOrder.*,user.*,change.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	
	String fileName = request.getParameter("fileName");
	String confirm = request.getParameter("confirm");
	String filePath = ExcelUpload.getChangeFilePath(); 
	//UploadOrderManager uom = new UploadOrderManager();

	boolean showContent =false;
	
	boolean confirmResult = false; 
	BranchTypeChange b = null ;
	if(confirm != null && confirm != ""){
		if(confirm.equals("confirm")){ 
			confirmResult = UploadManager.savechangeFileToDB(filePath,fileName);
		}
		response.sendRedirect("./uploadManage.jsp"); 
		return;
	}else{
		if(fileName != null && fileName != "" && !fileName.equals("")){			
			b = new XLSReader().readchangeXML(filePath,fileName); 
			showContent = true;
		}	
	}
	  
	System.out.println(StringUtill.GetJson(b));
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
		if(null != b.getName()){
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

<form id="baseform" method="post" action="">
<table width="100%" align="center" border=0>
       <tr style="height:30px" align="center">
       <td width="100%" align="center">
       <input type="hidden" name="fileName" value="<%=fileName %>"/>
		<input type="hidden" name="confirm" value="confirm" id="submitswitcher"/>
		<h3><%=b.getName() %></h3>
		<%if(showContent){ %>
		<input type="button" id="commitbutton" value="提交" onmousedown="$('#commitbutton').val('正在提交');$('#baseform').submit();$('#submitswitcher').val('confirmed')"></input>
		<%} %>
		</td>
		</tr> 
 
   <tr>
   <td align="center" width="100%">
    <div style="overflow-y:auto; width:100%;height:450px">
<table  cellspacing="1" border="2px"  id="table" width="80%">
		<tr>    
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td style="width:20;" align="center">转换结果</td>
			<td  style="width:80;" align="center"  >待转化</td>
		</tr> 
		<%
		if(showContent){
			Map<String, List<String>> map = b.getMaplist();
			Set<Map.Entry<String, List<String>>> setmap = map.entrySet();
			
			Iterator<Map.Entry<String, List<String>>> itmap = setmap.iterator();
			while(itmap.hasNext()){
				Map.Entry<String, List<String>> mape = itmap.next();
				String name = mape.getKey();
				List<String> list = mape.getValue();
				%> 
				<tr>   
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center"  ><%=name %></td>
			<td align="center"   >   
			<table width="100%">
			
			<% for(int i=0;i<list.size();i++){
				
				%>
				<tr>
				<td align="center"><%=list.get(i) %></td>
				</tr>
				
				<%
			} %>
			
			</table>
			</td>
		
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