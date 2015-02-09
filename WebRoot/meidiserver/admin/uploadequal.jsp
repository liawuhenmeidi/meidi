<%@ page language="java" import="java.util.*,wilson.upload.*,utill.*,java.util.ArrayList,wilson.matchOrder.*,user.*,change.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	TokenGen.getInstance().saveToken(request);
	String token = (String)session.getAttribute("token"); 
	  
	String fileName = request.getParameter("fileName");
	String confirm = request.getParameter("confirm");
	String filePath = ExcelUpload.getChangeFilePath();  
	//UploadOrderManager uom = new UploadOrderManager();
    
	boolean showContent =false;
	   
	boolean confirmResult = false;  
	UploadChangeAll b = null ; 
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
	  
	//System.out.println(StringUtill.GetJson(b));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>苏宁消单明细上传页面</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style media=print type="text/css">   
.noprint{visibility:hidden}   
</style> 
</head>
   
<body style="scoll:no">
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
var flag = true ; 
function submited(){ 
	 if(flag){  
		    $('#commitbutton').val('正在提交');
		    $('#commitbutton').css("display","none"); 
		    $('#commitbutton').attr("disable","disable");
			$('#baseform').submit(); 
			$('#submitswitcher').val('confirmed');
	 }else {  
		 return ;
	 }
}

</script>
<%
	if(fileName != null && fileName != "" && !fileName.equals("")){	
		String temp = "";
		if(null != b.getFilename()){
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
 
<div style=" height:20px;">
</div>
 
<br/>  

<form id="baseform" method="post" action="">
<input type="hidden" name="token" value="<%=token%>"/> 
<table width="100%" align="center" border=0>
       <tr style="height:30px" align="center">
       <td width="100%" align="center">
       <input type="hidden" name="fileName" value="<%=fileName %>"/>
		<input type="hidden" name="confirm" value="confirm" id="submitswitcher"/>
		<h3><%=b.getFilename() %></h3>
		<%if(showContent){ %> 
		<input type="button" id="commitbutton" value="提交" onclick="submited()"></input>
		<%} %>
		</td>
		</tr> 
 
   <tr>
   <td align="center" width="100%">
    <div style="overflow-y:auto; width:100%;height:450px">
    <table  cellspacing="1" border="2px"  width="80%">
		<tr class="bsc" >    
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td style="width:20;" align="center">门店</td>
			<td  style="width:80;" align="center"  >型号化</td>
		</tr> 
		<tr>
		  <td valign="top">
		   <table width="100%">
		    <%Set<String> set = b.getBranch();
		    Iterator<String> it =  set.iterator();
		      while(it.hasNext()){
		    	  String str = it.next();
		    	  %>
		    	  <tr class="bsc">
		    	  <td align="center">
		    	   <%=str %>
		    	  
		    	  </td>
		    	  
		    	  </tr>
		    	  
		    	  <%
		      }
		    
		    
		    %>
		  </table>
		  
		  </td>
		  <td valign="top">
		   <table width="100%">
		    <%Set<String> sett = b.getTypes() ;
		    Iterator<String> itt =  sett.iterator();
		      while(itt.hasNext()){ 
		    	  String str = itt.next();
		    	  %>
		    	  <tr class="bsc">
		    	  <td align="center">
		    	   <%=str %>
		    	  
		    	  </td>
		    	  
		    	  </tr>
		    	  
		    	  <%
		      }
		    %>
		  </table>
		  </td>
		</tr>
</table> 
</div>
   </td>
   </tr>
 </table>
 </form>
 
 
</body>
</html>
