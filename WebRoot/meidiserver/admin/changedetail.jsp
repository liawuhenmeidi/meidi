<%@page import="java.net.URLEncoder"%>

<%@ page language="java" import="java.util.*,wilson.upload.*,change.*,net.sf.json.JSONObject,uploadtotalgroup.*,utill.*,wilson.matchOrder.*,uploadtotal.*,user.*,wilson.salaryCalc.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<% 
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	Map<String,List<String>> map =  ChangeManager.getmapList(); 
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传管理页面</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style media=print type="text/css">   
.noprint{visibility:hidden}   
</style>
<script type="text/javascript">

function amortization(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
} 

function detail(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:800px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function println(){
	$("#wrapsearch").css("display","block");
	// window.print();
} 

function changeprintln(){
	 
    $('input[name="oderStatus"]').not("input:checked").each(function(){
    	//alert($(".noprinln"+$(this).val()));
    	// $(".noprinln"+$(this).val()).attr("class","noprint");
    	$(".noprinln"+$(this).val()).css("display","none");
        
    }); 
	$("#wrapsearch").css("display","none");
	 window.print();
}

function checkedd(){
	 
}
</script>
</head> 
    
<body>
  
<table  cellspacing="1" border="2px"  id="table" width="80%">
		<tr class="asc">    
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td style="width:20;" align="center">转换结果</td>
			<td  style="width:80;" align="center"  >待转化</td>
		</tr> 
		<%
			Set<Map.Entry<String, List<String>>> setmap = map.entrySet();
			
			Iterator<Map.Entry<String, List<String>>> itmap = setmap.iterator();
			while(itmap.hasNext()){
				Map.Entry<String, List<String>> mape = itmap.next();
				String name = mape.getKey();
				List<String> list = mape.getValue();
				%> 
				<tr class="asc">   
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
		%>
		
		
		
</table> 



</body>
</html>
