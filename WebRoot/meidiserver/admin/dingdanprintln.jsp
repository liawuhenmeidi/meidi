<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="searchdynamic.jsp"%>
 
<%   
request.setCharacterEncoding("utf-8");

  
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.serach,num,Page,sort,sear); 
session.setAttribute("exportList", list); 
count =   OrderManager.getOrderlistcount(user,Group.dealSend,Order.serach,num,Page,sort,sear);   
//获取二次配单元（工队） 

List<User> listS = UserManager.getUsers(user,Group.sencondDealsend);   

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
//System.out.println("%%%%%"+gMap);     
//修改申请      
Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);

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
<script type="text/javascript" src="../js/common.js"></script>
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
    line-height:15px;
}

#table{  
    
     width:4400px;
     table-layout:fixed ;
} 
 
#th{
    background-color:white;
    position:absolute;
    width:4400px;
    height:30px; 
    top:0;
    left:0;
   
}
  
  
#wrap{  
    clear:both; 
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:400px; 
} 
  
</style>

</head>


<!--   头部开始   -->

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

function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}

function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.name; 
	   				
	   				if(str != null  &&  str != ""){
		   				   attract[i] = str; 
			   	            i++;
		   				}	
	   		}
	   	}); 


		$.ajax({ 
	        type: "post", 
	         url: "server.jsp", 
	         data:"method=deleteOrder&id="+attract.toString(),
	         dataType: "",    
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("操作失败") ;
	        		 return ;   
	        	 }else if (data > 0){
	        		  alert("共删除"+data+"条数据");  
	        		  window.location.href="dingdanprintln.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("删除失败"); 
	            } 
	           });
	}
}

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
function seletall(all){
	if($(all).attr("checked")){
		$("input[type='checkbox']").each(function(){
			$(this).attr("checked",true);

	     });
	}else if(!$(all).attr("checked")){
		$("input[type='checkbox']").each(function(){
			$(this).attr("checked",false);
	     });
	};
} 
function orderPrint(id,statues,type,deliveryStatues){ 
	 if(statues ==0){
		 alert("不能打印未打印订单，请去打印页打印");
	 }else{    
		 window.location.href="print.jsp?id="+id+"&type="+type+"&deliveryStatues="+deliveryStatues; 
	 } 
} 
</script>

<body>
 
<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">

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
<% if(UserManager.checkPermissions(user, Group.Manger)){
	
 %>
  
 <div class="btn">
 <input type="submit" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  
</div>
 
<%
}
%>

</div>

 
<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>
</div>

<div style=" height:170px;">
</div>
 
 
<br/>  
<div id="wrap">
<table  cellspacing="1" id="table" >
		<tr id="th">  
			<!-- <td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>  -->
			<% if(UserManager.checkPermissions(user, Group.Manger)){ %>
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<%} %>
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">销售员</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">OMS订单号</td>
			
			<td align="center">验证码(联保单)</td>
			<td align="center">顾客信息</td>
			<td align="center">票面名称</td>
			<td align="center">票面型号</td>
			
			<td align="center">票面数量</td>
			<td align="center">送货名称</td>
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
            <td align="center">开票日期</td>
            <td align="center">安装日期</td>
            <td align="center">文员配单日期</td> 
            <td align="center">送货地区</td>
            
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			<td align="center">送货人员</td>
			 <td align="center">送货时间</td>
			 
			 <td align="center">安装人员</td>
			 <td align="center">安装时间</td>
			<td align="center">安装单位</td>
			<td align="center">送货是否已结款</td>
			<td align="center">厂送票是否已回</td>
			
			<td align="center">厂送票是否已消</td>
			<td align="center">厂送票是否结款</td>
			<td align="center">是否已调账</td>
			<td align="center">是否已退货</td> 
			<td align="center">备注</td> 
			
			<td align="center">打印</td> 
			<td align="center">调账打印</td> 
			<td align="center">安装单位驳回</td> 
			<td align="center">导购修改申请</td> 
			<td align="center">导购退货申请</td>   
		</tr>
	

 <% 
   if(null != list){
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	
    	String col = "";
    	if(i%2 == 0){ 
    		col = "style='background-color:yellow'";
    	}
  %>
   
  <tr id="<%=o.getId()+"ss" %>"  class="asc"  onclick="updateClass(this)"> 
		<!--  <td align="center"><input type="checkbox" value="1" name="userid[]"/></td> -->
		<% if(UserManager.checkPermissions(user, Group.Manger)){ %>
		<td align="center" width="20"><input type="checkbox" value="" id="check_box" name = "<%=o.getId() %>"></input></td>
		<%} %> 
		<td align="center"><a href="javascript:void(0)" onclick="adddetail('dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
		<td align="center"><%=o.getBranch()%></td> 
		
		
		<td align="center"> 		  
		<%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		</td> 
		<% 
		String tdcol = " bgcolor=\"red\"" ;
		
		  %>   
		<td align="center" <%=o.getPosremark()==1?tdcol:"" %>><%=o.getPos() %></td>
		<td align="center" <%=o.getSailidrecked()==1?tdcol:"" %>><%=o.getSailId() %></td>
		<td align="center" <%=o.getReckedremark()==1?tdcol:"" %>><%=o.getCheck() %></td>
		<%if(o.getPhoneRemark()!=1){ 
			tdcol = "";
		} %>
			<td align="center"><%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		%>
		
		</td> 
	    <% 
		     String pcategory = "";
		     String scategory = "";
		     String ptype = "";
		     String stype = "";
		     String pcountt = "";
		     String scountt = "";
		     List<OrderProduct> lists = OrPMap.get(o.getId());
		     if(lists != null ){
			     for(int g = 0 ;g<lists.size();g++){
			    	 OrderProduct op = lists.get(g);
			    	 if(op.getStatues() == 1 ){
			    		 pcategory +=  categorymap.get(Integer.valueOf(op.getCategoryId())).getName()+"</p>";
				         pcountt += op.getCount() +"</p>";
				         ptype += op.getSaleType()==null ||op.getSaleType() == "null" ? "":op.getSaleType() +"</p>";
			    	 }else {
			    		 scategory += categorymap.get(Integer.valueOf(op.getCategoryId())).getName()+"</p>";
				         scountt += op.getCount() +"</p>";
				         stype += op.getSendType()==null ||op.getSendType() == "null" ? "":op.getSendType() +"</p>"; 
			    	 }  
			     }
		     }
		     %> 
		   
		  <td align="center"><%= o.getCategory(1,"</p>")%></td>    
		  <td align="center" ><%=o.getSendType(1,"</p>")%></td>    
		  <td align="center" ><%= o.getSendCount(1,"</p>")%></td>    
		  <td align="center"><%= o.getCategory(0,"</p>")%></td>  
		  <td align="center" ><%=o.getSendType(0,"</p>")%></td>  
		  <td align="center" ><%= o.getSendCount(0,"</p>")%></td>   
		<td align="center" ><%= o.getGifttype("</p>")%></td>  
		<td align="center" ><%= o.getGifcount("</p>")%></td>  
		<td align="center" ><%= o.getGifStatues("</p>")%></td>
		
		<td align="center"><%=o.getSaleTime() %></td>
		<td align="center"><%=o.getOdate() %></td>
		<td align="center"><%=o.getDealSendTime() %></td>
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		<td align="center">
		<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td> 
		<td align="center">
		<%
		//打印状态     0  未打印   1 打印
		if(0 == o.getPrintSatues()){
		%>
		 未打印
		<%
         }else if(1 == o.getPrintSatues()){
		%>
		已打印
		<%
         }
		%>
		
		
		</td>
		
		
		<td align="center" style="white-space:nowrap;">  
		  <% if(o.getSendId() != 0){
			  if(usermap.get(Integer.valueOf(o.getSendId())) != null){
		 %>
		 <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
		 <%
		  }}
		 %>
		 
		</td>
        <td align="center" > 
		  <%=o.getSendtime()
		 %>
		 
		</td>
        <td align="center" style="white-space:nowrap;"> 
		  <% if(o.getInstallid() != 0){
			  if(usermap.get(Integer.valueOf(o.getInstallid())) != null){
		 %>
		 <%=usermap.get(Integer.valueOf(o.getInstallid())).getUsername() %>
		 <%
		  }}
		 %>
		 
		</td>
		  <td align="center"> 
		  <%=o.getInstalltime() 
		 %>
		 
		</td>
		<td align="center">
		
		 <% if(o.getDealsendId() != 0){  
		 %>
		 <%=usermap.get(Integer.valueOf(o.getDealsendId())).getUsername()+"<p/>"+usermap.get(Integer.valueOf(o.getDealsendId())).getPhone() %>
		 <%
		  }
		 %>
		 
		</td>
		<td align="center"> 
		    <%=o.getStatues4()==0?"否":"是" %> 
		</td>
		<td align="center">  
		    <%=o.getStatues1()==0?"否":"是" %> 
		</td>
		<td align="center">  
		    <%=o.getStatues2()==0?"否":"是" %> 
		</td>
		<td align="center">   
		    <%=o.getStatues3()==0?"否":"是" %> 
		</td>
		
		  <td align="center">   
		    <%=o.getStatuesDingma()==1?"是":"否" %> 
		</td> 
		<td align="center">   
		    <%=o.getDeliveryStatues()==3?"是":"否" %> 
		</td> 
		
		 <td align="center"> 
		    <%=o.getRemark() %>
		</td>
		<td align="center"> 
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',<%=o.getPrintSatues() %>,'','<%=o.getDeliveryStatues() %>')">[打印]</a>
		</td>
		<%
		  if(o.getStatuesDingma()==1){
			  %>  
		  <td align="center">  
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',1,'<%= Order.dingma%>','')">[打印]</a>
		</td>
		 
		<% 
		  }else {
		%>
		  <td align="center"> 
		    
		</td>
		<%
		  }
		%> 
		<td align="center">  
		<% 
		if(opmap.get(OrderPrintln.release) != null){ 
		OrderPrintln orp = opmap.get(OrderPrintln.release).get(o.getId()); 
		
		 if(orp != null){
			 if(orp.getStatues() == 2){ 
			     	
		    	 %> 
		    	
		    	   <p>驳回申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(orp.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>驳回申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
			 
			 
			 
			 %>
		  
		 <%=orp.getMessage() %>
	 
		</td>
		<%
		   }   
		 }
		}
		%>
		<td align="center"> 
		<%
		if(opmap.get(OrderPrintln.modify) != null){
		OrderPrintln op = opmap.get(OrderPrintln.modify).get(o.getId());
		
		 if(op != null){
			
			 if(op.getStatues() == 2){ 
			     	
		    	 %> 
		    	
		    	   <p>修改申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(op.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>修改申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
			 %>
		  
		 <%=op.getMessage() %>   
		</td>
		<%
		 }
		 }
		}
		%> 
		<td align="center"> 
		<%
		if(opmap.get(OrderPrintln.returns) != null){ 
		OrderPrintln op1 = opmap.get(OrderPrintln.returns).get(o.getId());
		
		if(op1 != null){
			 if(op1.getStatues() == 2){ 
			     	
		    	 %> 
		    	
		    	   <p>退货申请已同意</p>
		    	   
		    	 <%  
		    	 
		    	  }else if(op1.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>退货申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else{
			 %>
		 <%=op1.getMessage() %>
		</td>
		<%
		 }
		 }
		}
		%>
			
       
    </tr>
    <%}
    
    }%>
 
</table>
     </div>
     
    
</body>
</html>
