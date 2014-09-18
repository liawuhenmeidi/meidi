<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%    
request.setCharacterEncoding("utf-8");
int count = 0 ;  
 
String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");  
String sort = request.getParameter("sort");

String sear = (String)session.getAttribute("sear");

if(StringUtill.isNull(sear)){ 
	sear = ""; 
}

if(!StringUtill.isNull(sort)){
	session.setAttribute("sort", sort);
}else {
	sort = "id"; 
} 

if(!StringUtill.isNull(numb)){
	session.setAttribute("numb", numb);
}else{
	numb = "100";
}


if(StringUtill.isNull(pageNum)){
	pageNum = "1"; 
} 

int Page = Integer.valueOf(pageNum);

int num = Integer.valueOf(numb);

if(Page <=0){
	Page =1 ;
}

String searched = request.getParameter("searched");

if("searched".equals(searched)){
	
	String[] search = request.getParameterValues("search");
	if(search != null){ 
		for(int i = 0 ;i<search.length;i++){
			String str = search[i];
			
			boolean fflag = false ;  
			if("saledate".equals(str) || "andate".equals(str) || "dealsendTime".equals(str)){
				String start = request.getParameter(str+"start");
				String end = request.getParameter(str+"end");
				boolean flag = false ; 
				if(start != null && start != "" && start != "null"){
					sear += " and " + str + "  BETWEEN '" + start + "'  and  ";
				    flag = true ;
				}   
				if(end != null && end != "" && end != "null"){
					sear += " '" + end + "'";
				}else if(flag){ 
					sear += "now()";
				}      
			}else if("sendtype".equals(str) || "saletype".equals(str)){
				String strr = request.getParameter(str); 
				if(strr != "" && strr != null){   
					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
					sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
				}  // giftName
			}else if("categoryname".equals(str)){
				String strr = request.getParameter(str); 
				if(strr != "" && strr != null){    
					//sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')";
					sear += " and id in ( select orderid  from mdorderproduct where categoryID in (select id  from mdcategory where " + str + " like '%" + strr +"%'))";
				}  // giftName
			}else if("giftName".equals(str) || "statues".equals(str)){ 
				String strr = request.getParameter(str);  
				if(strr != "" && strr != null){    
					sear += " and id in (select orderid  from mdordergift where " + str + " like '%" + strr +"%')"; 
				}  // giftName
			}else if("dealSendid".equals(str) || "saleID".equals(str) || "sendId".equals(str) || "installid".equals(str)){
				String strr = request.getParameter(str);
				if(strr != "" && strr != null){  
				  sear += " and " + str + " in (select id from mduser  where username like '%" + strr +"%')"; 
				}
			}else {     
				String strr = request.getParameter(str);
				if(strr != "" && strr != null){
				  sear += " and " + str + " like '%" + strr +"%'"; 
				}  
			}
		} 	
	}else { 
		sear = "";
	} 
	
	session.setAttribute("sear", sear); 
}    

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
var id = "";
var pages = "<%=Page%>";     
var num = "<%=num%>";
 
$(function () { 

}); 
  
function add(){
	  var name = ($("#serch").children('option:selected').attr("value"));
	  var value = ($("#serch").children('option:selected').text());  
	  var flag = $.inArray(name,search);
	  if(flag == -1 && name != "" ){ 
		  search.push(name); 
		 // alert(name == "");  
		    if("statues4" == name || "statues1" == name || "statues2" == name || "statues3" == name || "deliveryStatues" == name || "statuesdingma" == name || "statues" == name || "deliverytype" == name){
		    	if("deliverytype" == name){ 
		    		$("#search").append(value+":是<input type=\"radio\"  name=\""+name+"\"  value=\"1\" />否<input type=\"radio\"  name=\""+name+"\"  value=\"2\" /><input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>");  
		    	}else {
		    		$("#search").append(value+":是<input type=\"radio\"  name=\""+name+"\"  value=\"1\" />否<input type=\"radio\"  name=\""+name+"\"  value=\"0\" /><input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>");  
		    	}
		    	
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
<input type="hidden" name="numb" value="<%=numb %>"/>
<input type="hidden" name="sort" value="<%=sort %>"/>
 
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
    <option value="deliveryStatues">是否已送货</option> 
    <option value="dealSendid">安装单位</option>
    <option value="sendId">送货人员</option> 
    <option value="installid">安装人员</option> 
    <option value="locates">送货地区</option> 
    <option value="saledate">开票日期</option> 
    <option value="andate">安装日期</option> 
    <option value="dealsendTime">文员配单日期</option>  
    <option value="saleID">销售员</option>  
    <option value="statues4">送货是否已结款</option> 
    <option value="statues1">厂送票是否已回</option> 
    <option value="statues2">厂送票是否已消</option> 
    <option value="statues3">厂送票是否结款</option> 
    <option value="statuesdingma">是否已调账</option>  
    <option value="deliverytype">先送货后安装</option>
    <option value="categoryname">送货名称</option>   
    <option value="sendtype">送货型号</option>    
    <option value="saletype">票面型号</option>
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
