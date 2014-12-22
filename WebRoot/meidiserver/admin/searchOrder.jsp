<%@page import="java.text.SimpleDateFormat"%>
<%@page import="utill.StringUtill"%>
<%@ page language="java" import="java.util.*,user.*,wilson.upload.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String submit = request.getParameter("submit");

String unChecked =request.getParameter("unchecked");
boolean fromUnCheckedPage = true;
try{
	fromUnCheckedPage = Boolean.parseBoolean(unChecked);
}catch(Exception e){
	fromUnCheckedPage = true;
}

//接受查询条件的提交
String selectBranchType = request.getParameter("branchtype");
String selectBranch = request.getParameter("branch");
String selectOrderName = request.getParameter("uploadorder");


UploadOrder uo = new UploadOrder();

SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");



try{
	
	if(!StringUtill.isNull(submit)&&submit.equals("true")){
		String shop = request.getParameter("shop");
		String pos = request.getParameter("pos");
		String saletime = request.getParameter("saletime");
		String type = request.getParameter("saletype");
		String num = request.getParameter("salenum");
		if(!StringUtill.isNull(saletime)){
			uo.setSaleTime(sdf1.format(sdf2.parse(saletime)));
		}
		if(!StringUtill.isNull(num)){
			uo.setNum(Integer.parseInt(num));
		}
		uo.setShop(shop);
		uo.setPosNo(pos);
		uo.setType(type);
		
		
		session.setAttribute("searchUploadOrder", uo);
		out.print("<script>window.close()</script>"); 
		return;
		
	}
	
}catch( Exception e){
	out.print("<script>alert('操作失败！填写参数格式错误');window.close()</script>"); 
	return;
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传订单修改页</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript" src="../js/common.js"></script>

  
<link rel="stylesheet" href="../css/jquery-ui.css"/>
<script src="../js/jquery-ui.js"></script>
<!--  
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
-->
<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript">

function checkedd(){
	// window.opener.document.getElementById("refresh").value ="refresh";
	//parent.location.reload(); 
	//window.returnValue='refresh'; 
	//window.close();
	//window.oper.reload();
	 //window.opener.location.reload();
	
	var temp = window.opener.document.createElement("form");
	temp.action = "";      
    temp.method = "post";
    temp.style.display = "none";
       
        var opt1 = document.createElement("input");      
        opt1.name = 'search';      
        opt1.value = 'true';            
        temp.appendChild(opt1);  
        
        var opt2 = document.createElement("input");  
        opt2.name = 'branchtype';      
        opt2.value = '<%=selectBranchType %>';            
        temp.appendChild(opt2);  
        
        var opt3 = document.createElement("input");  
        opt3.name = 'branch';      
        opt3.value = '<%=selectBranch %>';            
        temp.appendChild(opt3);  
        
        var opt4 = document.createElement("input");  
        opt4.name = 'uploadorder';      
        opt4.value = '<%=selectOrderName %>';            
        temp.appendChild(opt4);  
        
        var opt5 = document.createElement("input");  
        opt5.name = 'checkBoxStatus';      
        opt5.value = window.opener.getCheckBox();            
        temp.appendChild(opt5); 
          
    window.opener.document.body.appendChild(temp);      
    temp.submit();
}

</script>
<div style="position:fixed;width:100%;height:100px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

</div >



<div style=" height:100px;">
</div>


 
<br/>  
 
<div id="wrap" style="text-align:center;">  
<form  action=""  method ="post"  name="baseForm" id="baseForm" onsubmit="return checkedd()">

<input type="hidden" name="submit" id="submit" value="true"/>
 
<table  cellspacing="1"  id="table" style="margin:auto; width:80%;"> 
		  
		<tr class="asc">	 
			<td align="center" >门店名</td>
			<td align="center" >
	        <input type="text"  name="shop" id="shop" value=""  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >pos(厂送)单号</td>
			<td align="center" >
	        <input type="text"  name="pos" id="pos" value=""  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >销售日期</td>
			<td align="center" >
			<input class="date2" name="saletime" type="text" id="saletime" onclick="new Calendar().show(this);" placeholder="<%=uo.getSaleTime().replace("/", "-") %>" value="<%=uo.getSaleTime().replace("/", "-") %>"/>
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >票面型号</td>
			<td align="center" >
	        <input type="text"  name="saletype" id="saletype" value=""  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >票面数量</td>
			<td align="center" >
	        <input type="text"  name="salenum" id="salenum" value=""  />
			</td>
		</tr>
		<tr class="asc">
			<td width="100%" class="center" colspan="2"><input type="submit"  style="background-color:red;font-size:25px;"  value="搜索" /></td>
		</tr>
	
</table> 

</div>

</body>
</html>
