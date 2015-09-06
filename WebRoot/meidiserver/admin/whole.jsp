<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%   
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

HashMap<Integer,User> usermap = UserManager.getMap();
 
String pageNum = request.getParameter("page");
//String pageNum = (String)session.getAttribute("page");
//String numb = (String)session.getAttribute("numb");
String numb = request.getParameter("numb"); 
String sort = request.getParameter("sort");

System.out.println("sortss"+sort);
 
if(!StringUtill.isNull(sort)){
	session.setAttribute("sort", sort);
} 
if(!StringUtill.isNull(numb)){
	session.setAttribute("numb", numb);
}

//String sear = request.getParameter("sear");

String sear = (String)session.getAttribute("sear");
if(StringUtill.isNull(sear)){ 
	sear = ""; 
} 

if(StringUtill.isNull(pageNum)){
	pageNum = "1";  
}
if(StringUtill.isNull(numb)){
	numb = "100";
}

if(StringUtill.isNull(sort)){
	sort = "id";  
}


int count = 0;
int Page = Integer.valueOf(pageNum);
//System.out.println("Page"+Page+"sort"+sort);

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
			if("saledate".equals(str) || "andate".equals(str)){
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
			}else if("categoryname".equals(str) || "sendtype".equals(str) || "saletype".equals(str)){
				String strr = request.getParameter(str); 
				if(strr != "" && strr != null){   
					sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr +"%')"; 
				}  // giftName
			}else if("giftName".equals(str) || "statues".equals(str)){ 
				String strr = request.getParameter(str);  
				if(strr != "" && strr != null){    
					sear += " and id in (select orderid  from mdordergift where " + str + " like '%" + strr +"%')"; 
				}  // giftName
			}else if("dealSendid".equals(str) || "saleID".equals(str) || "sendId".equals(str)){
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
  
//System.out.println("sear"+sear); 

 
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.serach,num,Page,sort,sear); 
count =   OrderManager.getOrderlistcount(user,Group.dealSend,Order.serach,num,Page,sort,sear); 
System.out.println("count"+count);   
 
//获取二次配单元（工队）

List<User> listS = UserManager.getUsers(user,Group.sencondDealsend);   

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
//System.out.println("%%%%%"+gMap);    
//修改申请      
Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,0);
// 退货申请
Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,1);

Map<Integer,OrderPrintln> opMap2 = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.release);
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
  
<title>文员派工页</title> 
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
 <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />


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

td{ 
    width:110px;
    line-height:30px;
}

#table{  
    width:3300px;
     table-layout:fixed ;
} 
 
#th{
    background-color:#888888;
    position:absolute;
    width:3300px;
    height:30px; 
    top:0;
    left:0;
   
}
td{
 align:"center" 
}   
  
#wrap{   
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:350px; 
}
  
</style>

</head>


<!--   头部开始   -->
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">

$(function () {
	
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		 window.location.href="dingdanprintln.jsp?page="+pages+"&numb="+num; 
	 }); 
	  
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		 window.location.href="dingdanprintln.jsp?sort="+sort;   
	 }); 
	 
	$("#wrap").bind("scroll", function(){ 
		if(pre_scrollTop != ($("#wrap").scrollTop() || document.body.scrollTop)){
	        //滚动了竖直滚动条
	        pre_scrollTop=($("#wrap").scrollTop() || document.body.scrollTop);
	       
	        if(obj_th){
	            obj_th.style.top=($("#wrap").scrollTop() || document.body.scrollTop)+"px";
	        }
	    }
	    else if(pre_scrollLeft != (document.documentElement.scrollLeft || document.body.scrollLeft)){
	        //滚动了水平滚动条
	        pre_scrollLeft=(document.documentElement.scrollLeft || document.body.scrollLeft);
	    }
		}); 	 
});

 
function funcc(str,str2){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdan&id="+str2,
         dataType: "", 
         success: function (data) {
           //window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            }
           });
}

function changepeidan(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=peidan&id="+str2+"&uid="+uid,
         dataType: "",  
         success: function (data) {
          if(data = 0) {
        	 alert("订单已打印，不能配单");  
          }	 
           alert("设置成功"); 
           window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}
  
function add(){
	 
  var name = ($("#serch").children('option:selected').attr("value"));
  var value = ($("#serch").children('option:selected').text());  
  var flag = $.inArray(name,search);
  if(flag == -1 && name != "" ){ 
	  search.push(name); 
	 // alert(name == "");  
	    if("statues4" == name || "statues1" == name || "statues2" == name || "statues3" == name || "deliveryStatues" == name || "statuesdingma" == name || "statues" == name){ 
	    	$("#search").append(value+":是<input type=\"radio\"  name=\""+name+"\"  value=\"1\" />否<input type=\"radio\"  name=\""+name+"\"  value=\"0\" /><input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>");  
	    }else if("saledate" == name || "andate" == name ){ 
	    	$("#search").append(value+":开始时间<input type=\"text\"  id=\""+name+"start\"  name=\""+name+"start\"></input> 结束时间<input type=\"text\"  id=\""+name+"end\"  name=\""+name+"end\"></input><input type=\"hidden\"  name=\"search\" value=\""+name+"\"></input>");  
	        var start = "#"+name+"start";
	        var end = "#"+name+"end";
	        var opt = { };   
	        opt.date = {preset : 'date'};
	    	$(start).val('').scroller('destroy').scroller($.extend(opt['date'], 
	    	{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));	
			var opt2 = { };   
		    opt2.date = {preset : 'date'}; 
			$(end).val('').scroller('destroy').scroller($.extend(opt2['date'], 
			{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
	    }else {  
	    	$("#search").append(value+"<input type=\"text\"  name=\""+name+"\"></input><input type=\"hidden\"  name=\"search\"  value=\""+name+"\"></input>");  
	    }
  }else{    
	  alert("您已选择"+value+"搜索");    
  }
}

function change(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=songhuo&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功"); 
           window.location.href="dingdan.jsp";
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}

function changes(str1){
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="dingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function orderPrint(id,statues,type){ 
	 if(statues ==0){
		 alert("不能打印未打印订单，请去打印页打印");
	 }else{    
		 window.location.href="print.jsp?id="+id+"&type="+type; 
	 } 
} 
</script>

<body>
 
<div style="position:fixed;width:100%;height:200px;" >

 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
    
<jsp:include flush="true" page="page.jsp">
    <jsp:param name="href" value="dingdanprintln.jsp" />
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" /> 
	<jsp:param name="count" value="<%=count %>" /> 
	<jsp:param name="type" value="<%=Order.serach%>"/>  
</jsp:include>     
 

<jsp:include page="search.jsp"/>

</div>

<div style=" height:160px;">
</div>
<div>
 <jsp:include flush="true" page="dingdan.jsp">
   
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" /> 
	<jsp:param name="count" value="<%=count %>" /> 
	<jsp:param name="type" value="<%=Order.serach%>"/>  
</jsp:include>   
 
</div>
</body>
</html>
