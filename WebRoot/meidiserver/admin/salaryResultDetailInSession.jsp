<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@page import="wilson.salaryCalc.SalaryResult"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="utill.StringUtill"%>
<%@ page language="java" import="java.util.*,user.*,wilson.upload.*,utill.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String submit = request.getParameter("submit");

ArrayList<SalaryResult> modifiedSalaryResult = (ArrayList<SalaryResult>)request.getSession().getAttribute("modifiedSalaryResult");

SalaryResult sr = new SalaryResult();
UploadOrder uo = sr.getUploadOrder();

SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");



try{
	String idSTR = request.getParameter("id");
	String uoidSTR = request.getParameter("uoid");
	
	if(StringUtill.isNull(idSTR)){
		return;
	}
	int id = Integer.parseInt(idSTR);
	
	
	if(!StringUtill.isNull(submit)&&submit.equals("true")){
		if(StringUtill.isNull(uoidSTR)){
			return;
		}
		int uoid = Integer.parseInt(uoidSTR);
		
		String filename = request.getParameter("filename");
		String shop = request.getParameter("shop");
		String pos = request.getParameter("pos");
		String salemanname =request.getParameter("salemanname");
		String saletime = request.getParameter("saletime");
		//String catergory = request.getParameter("catergory");
		String type = request.getParameter("saletype");
		String num = request.getParameter("salenum");
		String saleprice = request.getParameter("saleprice");
		String salary = request.getParameter("salary");
		if(StringUtill.isNull(saletime)){
			return;
		}
		
		sr.setId(id);
		sr.setUploadOrderId(uoid);
		uo.setId(uoid);
		uo.setName(filename);
		uo.setShop(shop);
		uo.setPosNo(pos);
		uo.setSaleManName(salemanname);
		uo.setSaleTime(sdf1.format(sdf2.parse(saletime)));
		uo.setType(type);
		uo.setNum(Integer.parseInt(num));
		uo.setSalePrice(Double.parseDouble(saleprice));
		sr.setSalary(Double.parseDouble(salary));
		sr.setUploadOrder(uo);
		
		salaryResult.set(id, sr);
		session.setAttribute("calcResult", salaryResult);
		
		//if(SalaryCalcManager.saveSalaryResult(sr)){
			out.print("<script>alert('操作成功!!');window.close()</script>"); 
			return;
		//}else{
		//	out.print("<script>alert('操作失败！填写参数格式错误');window.close()</script>"); 
		//	return;
		//}
	}
	
	
	
	sr = UploadManager.getUploadOrderById(id);
	uo = sr.getUploadOrder();
	
	uo.setSaleTime(sdf2.format(sdf1.parse(uo.getSaleTime().replace("-", "").replace("/", "").trim())));
}catch( Exception e){
	e.printStackTrace();
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

  
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

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

	$('#<%=sr.getId()%>shop', window.opener.document).text($('#shop').val());
	$('#<%=sr.getId()%>pos', window.opener.document).text($('#pos').val());
	$('#<%=sr.getId()%>salemanname', window.opener.document).text($('#salemanname').val());
	$('#<%=sr.getId()%>saletime', window.opener.document).text($('#saletime').val().replace(/-/g,""));

	$('#<%=sr.getId()%>saletype', window.opener.document).text($('#saletype').val());
	$('#<%=sr.getId()%>num', window.opener.document).text($('#salenum').val());
	$('#<%=sr.getId()%>saleprice', window.opener.document).text($('#saleprice').val());
	$('#<%=sr.getId()%>salary', window.opener.document).html("<a href='#' onClick=\"javascript:window.open('./salaryResultDetailInSession.jsp?id=" + <%=sr.getId()%> + "', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')\"  >" + $('#salary').val() + "</a>");
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
<input type="hidden" name="id" value="<%=sr.getId() %>"/>
<input type="hidden" name="uoid" value="<%=uo.getId() %>"/>
 
<table  cellspacing="1"  id="table" style="margin:auto; width:80%;"> 
		  
		<tr class="asc">	 
			<td align="center" >文件名</td>
			<td align="center" >
	        <input type="text"  name="filename" id="filename" readonly="readonly" value="<%=uo.getName() %>"  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >门店</td>
			<td align="center" >
	        <input type="text"  name="shop" id="shop" value="<%=uo.getShop() %>"  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >pos(厂送)单号</td>
			<td align="center" >
	        <input type="text"  name="pos" id="pos" value="<%=uo.getPosNo() %>"  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >导购员姓名</td>
			<td align="center" >
	        <input type="text"  name="salemanname" id="salemanname" value="<%=uo.getSaleManName() %>"  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >销售日期</td>
			<td align="center" >
			<input class="date2" name="saletime" type="text" id="saletime" onclick="new Calendar().show(this);" placeholder="<%=uo.getSaleTime().replace("/", "-") %>" value="<%=uo.getSaleTime().replace("/", "-") %>"/>
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >品类</td>
			<td align="center" >
			<input name="catergory" type="text" id="catergory"  readonly="readonly" disabled="disabled"  value="<%=sr.getSalaryModel().getCatergory() %>"/>
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >票面型号</td>
			<td align="center" >
	        <input type="text"  name="saletype" id="saletype" value="<%=uo.getType() %>"  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >票面数量</td>
			<td align="center" >
	        <input type="text"  name="salenum" id="salenum" value="<%=uo.getNum() %>"  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >单价</td>
			<td align="center" >
	        <input type="text"  name="saleprice" id="saleprice" value="<%=uo.getSalePrice() %>"  />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >提成</td>
			<td align="center" >
	        <input type="text"  name="salary" id="salary" value="<%=sr.getSalary()==null?"":sr.getSalary() %>"  />
			</td>
		</tr>
		<tr class="asc">
			<td width="100%" class="center" colspan="2"><input type="submit"  style="background-color:red;font-size:25px;"  value="确认修改" /></td>
		</tr>
	
</table> 

</div>

</body>
</html>
