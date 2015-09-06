<%@ page language="java" import="java.util.*,wilson.upload.*,utill.*,java.util.ArrayList,wilson.matchOrder.*,user.*,product.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8"); 
	User user = (User)session.getAttribute("user");
	TokenGen.getInstance().saveToken(request);
	String token = (String)session.getAttribute("token"); 
	  
	String fileName = request.getParameter("fileName");
	String categoryID = request.getParameter("categoryID");
	String confirm = request.getParameter("confirm");
	String filePath = ExcelUpload.getProductFilePath();   
	//UploadOrderManager uom = new UploadOrderManager();
      
	boolean showContent =false;
	       
	boolean confirmResult = false;  
	List<Product> list = null ;    
	if(confirm != null && confirm != ""){
		if(confirm.equals("confirm")){   
			confirmResult = UploadManager.saveproductFileToDB(filePath,fileName,categoryID);
		}  
		response.sendRedirect("../product.jsp?categoryID="+categoryID); 
		return;   
	}else{  
		if(fileName != null && fileName != "" && !fileName.equals("")){			
			list = new XLSReader().readProductXML(filePath,fileName,categoryID); 
			showContent = true;  
		}	  
	} 
	    
	//System.out.println(StringUtill.GetJson(list));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传产品查看</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
</head>
   
<body style="scoll:no">
 
<!--   头部开始   --> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
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


<br/>  

<form id="baseform" method="post" action="">
<input type="hidden" name="token" value="<%=token%>"/> 

    <table  id="table" width="100%">
    <tr class="dsc">
    <td>产品型号</td>
    <td>产品编码</td>
    
    <td>体积</td>
    <td>售价</td>
     <td>销售类别</td>
    </tr>
    <% for(int i=0;i<list.size();i++){
    	Product p = list.get(i);
    	%>  
    	<tr class="asc">
    	 <td><%=p.getType() %></td>
    	  <td><%=p.getEncoded() %></td>
    	   <td><%=p.getSize() %></td>
    	    <td><%=p.getStockprice() %></td>
    	    <td><%=p.getSaletypeName() %></td>
    	</tr> 
    	<% 
    } %>  
		<tr class="asc">
		 <td width="100%" align="center" colspan = 4 >
       <input type="hidden" name="fileName" value="<%=fileName %>"/>
		<input type="hidden" name="confirm" value="confirm" id="submitswitcher"/>
		<%if(showContent){ %>  
		<input type="button" id="commitbutton" value="提交" onclick="submited()"></input>
		<%} %>
		</td>
		</tr>
</table> 

 </form>
 
 
</body>
</html>
