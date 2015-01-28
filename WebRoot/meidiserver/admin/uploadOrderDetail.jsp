<%@page import="order.OrderManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="utill.StringUtill"%>
<%@ page language="java" import="java.util.*,user.*,wilson.upload.*,utill.*,orderproduct.*,product.*,order.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String submit = request.getParameter("submit");



UploadOrder uo = new UploadOrder();
Order o = null;
List<OrderProduct> type_trans = new ArrayList<OrderProduct>();

SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");


List<String> listt = ProductService.getTypeNameList();
String plist = StringUtill.GetJson(listt);


try{
	String idSTR = request.getParameter("id");
	if(StringUtill.isNull(idSTR)){
		return;
	}
	int id = Integer.parseInt(idSTR);
	
	
	String oidSTR = request.getParameter("oid");
	int oid = 0;
	if(!StringUtill.isNull(oidSTR)){
		oid = Integer.parseInt(oidSTR);
	}
	
	
	List<OrderProduct> type_transList = new ArrayList<OrderProduct>();
	type_transList = (List<OrderProduct>)request.getSession().getAttribute("type_transList");
	type_trans = OrderProductService.getOrderProductFromList(type_transList, id);
	
	if(!StringUtill.isNull(submit)&&submit.equals("true")){
		String shop = request.getParameter("shop");
		String pos = request.getParameter("pos");
		String saletime = request.getParameter("saletime");
		String type = request.getParameter("saletype");
		String num = request.getParameter("salenum");
		String saleprice = request.getParameter("saleprice");
		
		String rowsforsendSTR = request.getParameter("rowsforsend");
		int rowsforsend = Integer.parseInt(rowsforsendSTR);
		if(type_trans.size() > 0){
			type_transList.removeAll(type_trans);
		}
		for(int i = 0 ; i < rowsforsend ; i ++){
			String sendType = request.getParameter("sendType" + i);
			String sendNum = request.getParameter("sendNum" + i );
			String sendPrice = request.getParameter("sendPrice" + i);
			OrderProduct op = new OrderProduct();
			op.setId(id);
			op.setTypeName(sendType);
			op.setCount(Integer.parseInt(sendNum));
			op.setPrice(Double.parseDouble(sendPrice));
			if(type_transList == null){
				type_transList = new ArrayList<OrderProduct>();
			}
			type_transList.add(op);
		}
		session.setAttribute("type_transList", type_transList);
		
		if(StringUtill.isNull(saletime)){
			return;
		}
		uo.setId(id);
		uo.setShop(shop);
		uo.setPosNo(pos);
		uo.setSaleTime(sdf1.format(sdf2.parse(saletime)));
		uo.setType(type);
		uo.setNum(Integer.parseInt(num));
		uo.setSalePrice(Double.parseDouble(saleprice));
		
		if(UploadManager.saveUploadOrder(uo)){
			out.print("<script>alert('操作成功!!');window.close()</script>"); 
			return;
		}else{
			out.print("<script>alert('操作失败！填写参数格式错误');window.close()</script>"); 
			return;
		}
	}
	
	
	
	
	uo = UploadManager.getUploadOrderById(id);

	if(oid != 0){
		o = OrderManager.getOrderID(user, oid);
	}
	
	
	if(!StringUtill.isNull(uo.getSaleTime())){
		uo.setSaleTime(sdf2.format(sdf1.parse(uo.getSaleTime().replace("-", "").replace("/", "").trim())));
	}
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
<link rel="stylesheet" href="../css/jquery-ui.css"/>
<script src="../js/jquery-ui.js"></script>


<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 
<script type="text/javascript">

var types = <%=plist%>;

$(function () {
	//初始化
	autoCompleteInit();
}); 

function autoCompleteInit(){
	var rows = calcRows();
	for(var i = 0 ; i < rows ; i ++){
		$('#' + 'sendType' + i ).autocomplete({
		 source: types
	    });
	}
}

function initPrice(){
	var rows = calcRows();
	var content = $('#saleprice').val() + ",";
	for(var i = 0 ; i < rows ; i ++){
		content += $('#' + 'sendType' + i ).val() + ":";
		content += $('#' + 'sendNum' + i ).val() + ",";
	}
	
	$.ajax({ 
        type:"post", 
         url:"AjaxHandler.jsp",
         //data:"method=list_pic&page="+pageCount,
         data:"action=getprice&content=" + content,
         dataType: "",  
         success: function (data) {
        	 //refresh
        	for(var i = 0 ; i < rows ; i ++){
        		 $('#' + 'sendPrice' + i ).val(data.split(",")[i]);
        	}
           },  
          error: function (XMLHttpRequest, textStatus, errorThrown) { 
        	  return false ;
            } 
           }); 
}

function checkFullFill(){
	var rows = calcRows();
	var result  = false;
	for(var i = 0 ; i < rows ; i ++){
		if($('#' + 'sendNum' + i ).val() == "")
			return result;
		
		if($('#' + 'sendType' + i ).val() == "")
			return result;
	}
	
	
	for(var i = 0 ; i < rows ; i ++){
		if(isNaN(Number($('#' + 'sendNum' + i ).val()))){
			alert('数量或者单价请填写数字!');
			return result;
		}
		if(isNaN(Number($('#' + 'sendPrice' + i ).val()))){
			alert('数量或者单价请填写数字!');
			return result;
		}
	}
	
	result = true;
	return result;
}

function onBlurInit(){
	var rows = calcRows();
	for(var i = 0 ; i < rows ; i ++){
		$('#' + 'sendType' + i ).blur( function() {
			if($('#' + 'sendNum' + i ).val() != ""){
				if(checkFullFill()){
					initPrice();
				}
			}
	    });
		$('#' + 'sendNum' + i ).blur( function() {
			if($('#' + 'sendType' + i ).val() != ""){
				if(checkFullFill()){
					initPrice();
				}
			}
	    });
	}
}

function checkedd(){
	// window.opener.document.getElementById("refresh").value ="refresh";
	//parent.location.reload(); 
	//window.returnValue='refresh'; 
	//window.close();
	//window.oper.reload();
	 //window.opener.location.reload();
	var rows = calcRows();
	var send = "";
	
	if(isNaN(Number( $('#salenum').val() )) || isNaN(Number($('#saleprice').val()))){
		alert('数量或者单价请填写数字!');
		return false;
	}
	
	for(var i = 0 ; i < rows ; i ++){
		if(isNaN(Number($('#' + 'sendNum' + i ).val())) ||$('#' + 'sendNum' + i ).val() == ""){
			alert('数量或者单价请填写数字!');
			return false;
		}
		if(isNaN(Number($('#' + 'sendPrice' + i ).val())) || $('#' + 'sendPrice' + i ).val() == ""){
			alert('数量或者单价请填写数字!');
			return false;
		}
		send += $('#' + 'sendType' + i ).val() + ":";
		send += $('#' + 'sendNum' + i ).val() + "台:";
		send += $('#' + 'sendPrice' + i ).val()+"元";
		send += ",";
	}
	$('#<%=uo.getId()%>uploadshop', window.opener.document).text($('#shop').val());
	$('#<%=uo.getId()%>uploadposno', window.opener.document).html("<a href='#' onClick=\"javascript:window.open('./uploadOrderDetail.jsp?id=" + <%=uo.getId()%> + "&oid=" + <%=o==null?"0":o.getId()%> + "', 'newwindow', 'scrollbars=auto,resizable=no, location=no, status=no')\"  >" + $('#pos').val() + "</a>");
	$('#<%=uo.getId()%>uploadsaletime', window.opener.document).text($('#saletime').val().replace(/-/g,""));
	$('#<%=uo.getId()%>uploadtype', window.opener.document).text($('#saletype').val());
	$('#<%=uo.getId()%>uploadcount', window.opener.document).text($('#salenum').val());
	
	$('#<%=uo.getId()%>uploadtype_trans', window.opener.document).text(send);
	$('#<%=uo.getId()%>uploadtype_trans', window.opener.document).attr('bak',send);
}

function calcRows(){
	var others = 7;
	var RowsForSend  = $('#table tr').length - others;
	$('#rowsforsend').val(RowsForSend);
	return RowsForSend;
}

function addSend(){
	var rows = calcRows();
	var newLine = "<tr class='asc'><td align='center' >送货型号/数量/单价<input type='button' id='delSendButton' style='background-color:red;font-size:5px;'  value='删除' onclick='delSend($(this))'/></td><td align='center' >送货型号<input type='text'  name='sendType" + rows +"' id='sendType" + rows +"' value=''/><br/>数量<input type='text'  name='sendNum" + rows +"' id='sendNum" + rows +"' value=''/><br/>单价<input type='text'  name='sendPrice" + rows +"' id='sendPrice" + rows +"' value='' readonly='readonly'/></td></tr>";
	$('#addSendButton').parent().parent().before(newLine);
	autoCompleteInit();
	onBlurInit();
}

function delSend(obj){
	obj.parent().parent().remove();
	if(checkFullFill()){
		initPrice();
	}
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
<input type="hidden" name="id" value="<%=uo.getId() %>"/>
<input type="hidden" id="rowsforsend" name="rowsforsend" value="0"/>
 
<table  cellspacing="1"  id="table" style="margin:auto; width:80%;" > 
		  
		<tr class="asc">	 
			<td align="center" >门店名</td>
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
			<td align="center" >销售日期</td>
			<td align="center" >
			<input class="date2" name="saletime" type="text" id="saletime" onclick="new Calendar().show(this);" placeholder="<%=uo.getSaleTime().replace("/", "-") %>" value="<%=uo.getSaleTime().replace("/", "-") %>"/>
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >票面型号</td>
			<td align="center" >
	        <input type="text"  name="saletype" id="saletype" value="<%=uo.getType() %>" readonly="readonly" />
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >票面数量</td>
			<td align="center" >
	        <input type="text"  name="salenum" id="salenum" value="<%=uo.getNum() %>"  readonly="readonly"/>
			</td>
		</tr>
		<tr class="asc">	 
			<td align="center" >价格</td>
			<td align="center" >
	        <input type="text"  name="saleprice" id="saleprice" value="<%=String.valueOf(uo.getSalePrice()) %>" readonly="readonly" />
			</td>
		</tr>
		
		<%
		int i = 0 ;
		if(o != null && o.isDiangma()){
			String output = OrderProductService.getSendTypeAndCountAndPrice(o, uo, false);
			//MXG15-22:1:123.2,SS15T:2:155.0
			for(i = 0 ; i < output.split(",").length ; i ++){
				String tmp_type = output.split(",")[i].split(":")[0];
				String tmp_num = output.split(",")[i].split(":")[1];
				String tmp_price = output.split(",")[i].split(":")[2];
			
		%>
		<tr class="asc">	 
			<td align="center" >
			送货型号/数量/单价
			</td>
			<td align="center" >
	        	送货型号<input type="text"  name="sendType<%=i %>" id="sendType<%=i %>" value="<%=tmp_type%>" readonly="readonly"/><br/>
	       		数量<input type="text"  name="sendNum<%=i %>" id="sendNum<%=i %>" value="<%=tmp_num%>" readonly="readonly"/><br/>
	      		单价<input type="text"  name="sendPrice<%=i %>" id="sendPrice<%=i %>" value="<%=tmp_price%>" readonly="readonly"/>
			</td>
		</tr>
		<%
			}
		}
		%>
		
		<%
		for(int j = 0 ; j < type_trans.size(); j ++){	
		%>

		<tr class="asc">	 
			<td align="center" >
			送货型号/数量/单价<input type="button" id="delSendButton" style="background-color:red;font-size:5px;"  value="删除" onclick="delSend($(this))"/>
			</td>
			<td align="center" >
	        	送货型号<input type="text"  name="sendType<%=i %>" id="sendType<%=i %>" value="<%=type_trans.get(j).getTypeName()%>"/><br/>
	       		数量<input type="text"  name="sendNum<%=i %>" id="sendNum<%=i %>" value="<%=type_trans.get(j).getCount()%>"/><br/>
	      		单价<input type="text"  name="sendPrice<%=i %>" id="sendPrice<%=i %>" value="<%=type_trans.get(j).getPrice()%>" readonly="readonly"/>
			</td>
		</tr>
		<%
			i++;
		} 
		%>
		<tr class="asc">
			<td width="100%" class="center" colspan="2">
				<input type="submit"  style="background-color:red;font-size:25px;"  value="确认修改" />
				<input type="button" id="addSendButton" style="background-color:red;font-size:25px;"  value="添加送货型号" onclick="addSend()"/>
			</td>
		</tr>
	
</table> 

</div>

</body>
</html>
