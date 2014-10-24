<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%    
request.setCharacterEncoding("utf-8");

String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");  
String sort = request.getParameter("sort");  
String searched = request.getParameter("searched");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
</head>

<body style="scoll:no">

<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
<!--   头部开始   --> 


<script type="text/javascript"> 
var search = new Array();
var searchstr = "";  
function add(){
	  var name = ($("#serch").children('option:selected').attr("value"));
	  var value = ($("#serch").children('option:selected').text());  
	  var flag = $.inArray(name,search);
	  if(flag == -1 && name != "" ){ 
		  search.push(name); 
		 // alert(name == "");  
		    if("statues4" == name || "statues1" == name || "statues2" == name || "statues3" == name || "statuesdingma" == name || "statues" == name || "deliverytype" == name){
		    	$("#search").append(value+":是<input type=\"radio\"  name=\""+name+"\"  value=\"1\" />否<input type=\"radio\"  name=\""+name+"\"  value=\"0\" /><input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>");  
		    }else if("deliveryStatues" == name){
		    	var str = "";
		    	str +=  "<select name=\""+name+"\">"+
		    	           "<option value=\"1\">已送货</option>"+
		    	           "<option value=\"2\">已安装</option>"+
		    	           "<option value=\"0\">未送货</option>"+
		    	           "<option value=\"-1\">已退货</option>"+
		    	         "</select>" + 
		    	         " <input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>";
		    	$("#search").append(str);
		    }else if("oderStatus" == name){
		    	var str = "";
		    	str +=  "<select name=\""+name+"\">"+
		    	           "<option value=\"0\">需派送</option>"+
		    	           "<option value=\"8\">已自提</option>"+
		    	           "<option value=\"9\">只安装(门店提货)</option>"+
		    	           "<option value=\"10\">只安装(顾客已提)</option>"+
		    	           "<option value=\"20\">换货单</option>"+
		    	         "</select>" + 
		    	         " <input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>";
		    	$("#search").append(str);
		    }else if("saledate" == name || "andate" == name || "dealsendTime" == name ){ 
		    	$("#search").append(value+":开始时间<input type=\"text\"  id=\""+name+"start\"  name=\""+name+"start\"  placeholder=\"yyyy-mm-dd\"></input> 结束时间<input type=\"text\"  id=\""+name+"end\"  name=\""+name+"end\" placeholder=\"yyyy-mm-dd\"></input><input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>");  
		        var start = name+"start";
		        var end = name+"end";
		        var opt = { };   
		        opt.date = {preset : "date"};
		    	$("#"+start).scroller("destroy").scroller($.extend(opt["date"], 
		    	{ theme: "android-ics light", mode: "scroller", display: "modal",lang: "zh" ,startYear:"1980",endYear:"2020"}));	
				var opt2 = { };   
			    opt2.date = {preset : "date"}; 
				$("#"+end).scroller("destroy").scroller($.extend(opt2["date"], 
				{ theme: "android-ics light", mode: "scroller", display: "modal",lang: "zh" ,startYear:"1980",endYear:"2020"}));
		    }else {  
		    	$("#search").append(value+"<input type=\"text\"  name=\""+name+"\"></input><input type=\"hidden\"  name=\"search\"  value=\""+name+"\"></input>");  
		    }
	  }else{    
		  alert("您已选择"+value+"搜索");    
	  }
	}
</script>
 
<form action="">  
 <input type="hidden" name="searched" value="searched"/>
 <input type="hidden" name="page" value="<%=pageNum%>"/>
 <input type="hidden" name="numb" value="<%=numb%>"/>
 <input type="hidden" name="sort" value="<%=sort%>"/>
<div id="search">      
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <select id="serch" name="serch"> 
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
    <option value="printlnid">单号</option>    
    <option value="orderbranch">门店</option>  
    <option value="pos">pos(厂送)单号</option> 
    <option value="sailId">OMS订单号</option> 
    <option value="checked">验证码(联保单)</option> 
    <option value="username">顾客姓名</option>  
    <option value="phone1">顾客电话</option>
    <option value="oderStatus">上报状态</option>  
    <option value="deliveryStatues">送货状态</option> 
    <option value="dealSendid">安装单位</option>
    <option value="sendId">送货人员</option> 
    <option value="installid">安装人员</option> 
    <option value="locates">送货地区</option> 
    <option value="saledate">开票日期</option> 
    <option value="andate">安装日期</option> 
    <option value="dealsendTime">文员配单日期</option>  
    <option value="saleID">销售员</option> 
    <option value="sendtype">送货型号</option>    
    <option value="saletype">票面型号</option> 
    <option value="statues4">是否给安装网点结款</option>
    <option value="statues1">厂送票是否已回</option> 
    <option value="statues2">厂送票是否已消</option> 
    <option value="statues3">厂送票是否结款</option> 
    <option value="statuesdingma">是否已调账</option>  
    <option value="deliverytype">先送货后安装</option>
    <option value="categoryname">送货名称</option>   
   
    <option value="giftName">赠品</option> 
    <option value="statues">赠品是否已自提</option>
</select>   

</div> 
<div > 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="button" name="" value="增加搜索条件" style="height:20px" onclick="add()"/>
 <input type="submit"  value="搜索" style="width:80px;height:20px"/>
</div>
 
</form>


</body>
</html>
