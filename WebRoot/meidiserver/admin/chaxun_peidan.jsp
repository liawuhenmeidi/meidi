<%@ page language="java" import="java.util.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int id = user.getId();
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend);
HashMap<Integer,User> usermap = UserManager.getMap(); 
List<User> listS = UserManager.getUsers(Group.send);      // 获取送货员
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,3);

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>送货</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script> <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="../css/songhuo.css">


<script type="text/javascript">

var id = "<%=id%>";
$(document).ready(function(){
	initTime();
   });

 function change(){
	 var flag = false ;
	 var saledateStart = $("#saledateStart").val();
	 var saledateEnd = $("#saledateEnd").val();
	 var deliveryStatues  = $("#deliveryStatues").val();
	 alert(deliveryStatues);
	 // 已分配还是未分配
	 var sendId = $("#sendId").val();
	 var str = "";
	 // pos == "" || pos == null || pos == "null"
	 if(saledateStart != null && saledateStart != "" && saledateStart != "null"){
		 str += " and saledate BETWEEN '" + saledateStart + "'  and  ";
	     flag = true ;
	 } 
	 
	 if(saledateEnd != null && saledateEnd != "" && saledateEnd != "null"){
		 str += " ' " + saledateEnd + "'";
	 }else if(flag){
		 str += "now()";
	 }
	 
	 if(deliveryStatues != null && deliveryStatues != "" && deliveryStatues != "null"){
		 if(deliveryStatues == '0'){
			 str += " and sendId = '"+id+"'";
		 }else {
			 str += " and sendId != '"+id+"'";  
		 }
	 }
	  alert(str);
	 if(sendId != null && sendId != "" && sendId != "null"){
		 str += " and sendId = '"+sendId+"'";
	 }
 
	 window.location.href="peidan.jsp?serch="+str;
  }
 
function initTime(){
	   var opt = { };
	    opt.date = {preset : 'date'};
		$('#saledateStart').val('').scroller('destroy').scroller($.extend(opt['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
		var opt2 = { };
	    opt2.date = {preset : 'date'};
		$('#saledateEnd').val('').scroller('destroy').scroller($.extend(opt2['date'], 
		{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'2014',endYear:'2020'}));
}
</script>

</head>
<body>
<div class="s_main">
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
<!--  头 单种类  -->
<div class="s_main_tit"><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span><span class="qiangdan"><a href="peidan.jsp">返回</a></span></div>
<div class="s_main_tit">订单查询页面</div>

<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">
  
  <tr>
    <td width="40%">销售开始时间</td>
    <td>  <input class="date" type="text" name="saledateStart" placeholder="必填"  id="saledateStart"  readonly="readonly" style="width:90% "></input>
     </td>
      </tr>
    <tr>
     <td width="40%">销售结束时间</td>
     <td ><input class="date2" type="text" name="saledateEnd" id ="saledateEnd" placeholder="必填"  readonly="readonly" style="width:90% "></input> 
     </td>
  </tr>
    <tr>
    <td>配送状态</td>
    <td colspan="3">
     <select class = "category" name="category"  id="deliveryStatues" >
     <option value="1" >已分配 </option>
     <option value="0" >未分配 </option>
      </select>
    </td>
    </tr>
 <tr>
    <td>配送人员</td>
    <td colspan="3">
		<select class = "category" name="category"  id="sendId" >
		   <option value=""> </option>
		   <%     
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	  
          
            	   %>
            	    <option value=<%=u.getId() %>> <%=u.getUsername() %></option>
            	   <% 
            	  
                    }
	                	%>
         </select> 
      
     </td>
    </tr>
  
     <tr>
    <td>&nbsp;</td>
    <td></td>
    <td width="25%"></td>
    <td width="25%"></td>
  </tr>
  
       <tr>
    <td></td>
    <td></td>
    <td width="25%">
     <input type="button" onclick="change()"  value="查询"/></td>

    <td width="25%"></td>
  </tr>
</table>
<br/>



<br/>
</div>














<!--  zong end  -->
</div>






</body>
</html>