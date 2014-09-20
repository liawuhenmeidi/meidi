<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
  
<%  

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int id = user.getId();  
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
int opstatues = OrderPrintln.release;   

String serchProperty = ""; 
String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");
String sort = request.getParameter("sort"); 
if(StringUtill.isNull(pageNum)){
	pageNum = "1"; 
}
if(StringUtill.isNull(numb)){
	numb = "100";
} 
if(StringUtill.isNull(sort)){ 
	sort = "id desc"; 
}

int count = 0;
int Page = Integer.valueOf(pageNum);

System.out.println("Page"+Page);

int num = Integer.valueOf(numb);
if(Page <=0){ 
	Page =1 ; 
} 
List<Order> list = null ;
//String sear = (String)session.getAttribute("sear");
//if(StringUtill.isNull(sear)){ 
//	sear = ""; 
//}
String sear = "";
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
	
	//session.setAttribute("sear", sear); 
	
}
    
//list = OrderManager.getOrderlist(user,Group.sencondDealsend,str,sort);      
list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.orderDispatching,num,Page,sort,sear);  
session.setAttribute("exportList", list); 
count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.orderDispatching,num,Page,sort,sear);  
    
  
HashMap<Integer,User> usermap = UserManager.getMap(); 
//获取送货员      
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
List<User> listS = UserManager.getUsers(user,Group.send);
    
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
//Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.release);

//Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,1);


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
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
    width:100px;
    line-height:15px;
}

#table{  
    width:2100px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:2100px;
    height:30px;
    top:0;
    left:0;
}
#wrap{

    position:relative;
    padding-top:30px;
    overflow:auto;
    height:400px;
}

</style>
</head>

<body>

<script type="text/javascript" src="../../js/common.js"></script>
<!--   头部开始   -->
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "";
var pages = "" ;
var num = "";
var oid ="<%=id%>";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>";

$(function () {
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
	$("select[id='numb'] option[value='"+num+"']").attr("selected","selected");
	
	 $("#page").blur(function(){
		 pages = $("#page").val();
		 window.location.href="dingdanpeidan2.jsp?pages="+pages+"&numb="+num;
	 });
	 
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdanpeidan2.jsp?pages="+pages+"&numb="+num;
	 });  
	 
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="dingdanpeidan2.jsp?page="+pages+"&numb="+num+"&sort="+sort;
	 }); 
});

function func(str){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
}
function funcc(str,str2){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
	$.ajax({  
        type: "post", 
         url: "../server.jsp", 
         data:"method=dingdan&id="+str2,
         dataType: "", 
         success: function (data) {
            // window.location.href="senddingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            }
           });
}
 
function changes(oid,id,statues,flag,returnstatues,type){
	if(statues == 2){ 
		if(flag == 2){
			alert("请联系送货员驳回");
			return ;      
		} 
		
		if(flag == 1){
			if(returnstatues == 0){
				alert("请配置退货员拉回商品再同意");
				return ;
			}
		}
		
		
	} 
		$.ajax({   
	        type: "post", 
	         url: "../server.jsp", 
	         data:"method=dingdaned&oid="+oid+"&id="+id+"&statues="+statues, 
	         dataType: "", 
	         success: function (data) {
	        	
	        	 if(statues == 2){ 
	        		
	        		 if(<%=OrderPrintln.releasedispatch %> != type){

	        				
	        			window.location.href="../print.jsp?id="+oid+"&type="+<%=Order.deliveryStatuesTuihuo%>;
	        				
	        		}else {
	        			window.location.href="dingdanpeidan2.jsp";
	        		}
	        	 }else {
	        		
	        		 window.location.href="dingdanpeidan2.jsp";
	        	 } 
	        	 
	     
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           }); 
	
}
 
function change(str1,oid,type,statues){
	
	var uid = $("#"+str1).val();
	if(uid == null || uid == ""){
		alert("请选择送货员");
		return ;
	}
	if(0 == statues){   
		alert("您已提交驳回申请，不能派工");
	}else { 
		question = confirm("您确定要配单并打印吗？");
		if (question != "0"){
			
			$.ajax({   
		        type: "post",     
		         url: "../../user/server.jsp",
		         data:"method=peidan&id="+oid+"&uid="+uid+"&type="+type,
		         dataType: "",  
		         success: function (data) { 
		        	 if(data == 0){
		        		 alert("导购提交修改申请，不能配工");
		        		 return ; 
		        	 }else if(data == 20){ 
		        		 alert("导购提交退货申请，不能配工");
		        		 return ; 
		        	 }else {   
		        		 window.location.href="../printPaigong.jsp?id="+oid+"&type="+type;
		        	 }   
		        	 
		           },  
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        // alert(errorThrown); 
		            } 
		           });
	}
	
	}
}

function adddetail(src){ 
	//window.location.href=src ;
	window.showModalDialog(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function winconfirm(str,str2,sendid){
	if(sendid != 0){
		alert("请联系送货员驳回");
		return ;
	}else {
		if(4 == str2){
	    	var question = confirm("您的驳回请求别拒绝，是否继续申请？");
				if(question == 0){
					return ;
				}
	    }else if(0 == str2){
	    	alert("您已提交驳回请求");
	    	return ; 
	    }

	}
  //alert(str2);
    
	
    question = confirm("你确认要驳回吗？");
	
	if (question != "0"){

		$.ajax({    
	        type:"post",  
	         url:"../../user/server.jsp",
	         //data:"method=list_pic&page="+pageCount,      
	         data:"method=shifang&oid="+str+"&pGroupId="+pgroup+"&opstatues="+opstatues,
	         dataType: "",  
	         success: function (data) {    
	          alert("驳回申请已提交成功");  
	          window.location.href="dingdanpeidan2.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("驳回申请失败");
	            } 
	           });
	 }
}

function searchlocate(id){  
	  window.location.href="../../adminmap.jsp?id="+id;
}

function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.showModalDialog(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}
</script>
<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:80%;height:200px;">

 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
    
<jsp:include flush="true" page="../page.jsp"> 
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/>   
   <jsp:param name="type" value="<%=Order.porderDispatching%>"/> 
</jsp:include> 
 
<jsp:include page="../search.jsp"/>

</div>
<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>

</div>
<div style=" height:150px;">
</div>

 
<br/>  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">顾客信息</td>
			<td align="center">送货名称</td>
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
           
            <td align="center">安装日期</td>
            <td align="center">文员配单日期</td> 
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			
			
			<td align="center">备注</td>
			<td align="center">销售员</td>
			<td align="center">送货员</td>
			<td align="center">查看位置</td>
			  <td align="center">驳回文员</td>  
			<td align="center">送货员驳回请求</td> 
			 
		    <td align="center">驳回状态</td>
		    <td align="center">文员退货请求</td> 
		    <td align="center">退货员</td>  
		</tr>
	
<tbody> 
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
		<td align="center"><a href="javascript:void(0)" onclick="adddetail('../dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
		<td align="center"><%=o.getBranch()%></td>  
		
		<% 
		String tdcol = " bgcolor=\"red\"" ;
		if(o.getPhoneRemark()!=1){
			tdcol = "";
		}
		  %>   
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
		 
		  
		  <td align="center"><%=scategory%></td> 
		  <td align="center"><%=stype%></td>  
		  <td align="center"><%=scountt%></td> 
		<% 
		     String gstatues = ""; ;
		     String gtype = "";
		     String gcountt = ""; 
		      
		     List<Gift> glists = gMap.get(o.getId());
		     
		     if(null != glists){
		
		     for(int g = 0 ;g<glists.size();g++){
		    	 
		    	 Gift op = glists.get(g);
		    	 if(null !=op){ 
		    		 gtype += op.getName()+"</p>";
			         gcountt += op.getCount()+"</p>";
			         String statues = "";
			         if(0==op.getStatues()){
			        	 statues = "需配送";
			         }else {
			        	 statues = "已自提";
			         }
			         gstatues += statues +"</p>";
		    	 }
		     }
		     }
		     %> 
		 <td align="center"><%=gtype%></td>
		 <td align="center"><%=gcountt%></td>
		 <td align="center"><%=gstatues%></td> 
		
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
		if(0 == o.getPrintSatuesP()){
		%>
		 未打印
		<%
         }else if(1 == o.getPrintSatuesP()){
		%>
		已打印
		<%
         }
		%>
		
		
		</td>
		
		
		
        <td align="center">   
		    <%=o.getRemark() %>
		</td>
        <td align="center"> 		  
		<%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		</td> 
		<td align="center">
		<%  
		OrderPrintln or = opmap.get(OrderPrintln.releasemodfy) == null?null:opmap.get(OrderPrintln.releasemodfy).get(o.getId());  
		
		int substr = -1;
		if(opmap.get(OrderPrintln.release) != null){
			OrderPrintln orin = opmap.get(OrderPrintln.release).get(o.getId());
		    if(orin != null){
		    	substr = opmap.get(OrderPrintln.release).get(o.getId()).getStatues();
		    }
		}
		   int statuesnew = Order.orderpeisong;
		   if(o.getSendId() == 0 ){
			   if(o.getDeliveryStatues() == 9){
				   statuesnew = Order.ordersong;
			   }else if(o.getDeliveryStatues() == 10){
				   statuesnew = Order.orderinsta; 
			   }
			   if(or!= null && or.getStatues() != 0 || or == null){
			    	  
		%> 
		<select class = "category" name="category"  id="songh<%=o.getId() %>" >
		 <option value=""></option>
		<%     
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	   String str1 = "";
            	   if(u.getId() == o.getSendId()){
            		   str1 = "selected=selected" ;
            		   
            	   } 
            	   %> 
            	    <option value=<%=u.getId() %>  <%= str1%>> <%=u.getUsername() %></option>
            	   <% 
            	   
                    }
	                	%>
         </select>   
           
         <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>','<%=statuesnew %>',<%=substr %>)"  value="确定"/>
		<% }
		} else {
			
		 %>
		
		<% if(o.getSendId() != 0){
			if(usermap.get(Integer.valueOf(o.getSendId())) != null){
		 %>
		 <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
		 <%
		  }
		}
		 %>
		<%
		}
		%>
		</td> 
		<td align="center"> 
		    <a href="javascript:void(0);"  onclick="searchlocate('<%=o.getId() %>')">[查看位置]</a> 
		</td>
		<!--  
		<td align="center">
		<% 
		   if(o.getDeliveryStatues() == 1){
			   
		   
		   if(o.getInstallid() == 0){
		%>
		<select class = "category" name="category"  id="songh<%=o.getId() %>" >
		 <option value="0"></option>
		<%    
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	   String str1 = "";
            	   if(u.getId() == o.getInstallid()){
            		   str1 = "selected=selected" ;
            		   
            	   } 
            	   %> 
            	    <option value=<%=u.getId() %>  <%= str1%>> <%=u.getUsername() %></option>
            	   <% 
            	   
                    }
	                	%>
         </select>  
      
         <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>','<%=Order.orderinstall%>')"  value="确定"/>
		<%} else {
			
		 %>  
		     <%=usermap.get(Integer.valueOf(o.getInstallid())).getUsername() %>
		<%
		     } 
		   } 
		%>
		</td> 
		-->
	
		<td align="center"> 
		 <%
		     // OrderPrintln or = opmap.get(OrderPrintln.releasemodfy) == null?null:opmap.get(OrderPrintln.releasemodfy).get(o.getId()); 
		      if(or!= null){
		    	  if(or.getStatues() == 0 ){ 
		    		  
		    %>
		<%=or.getMessage() %>
			<%
		        }
                }
		%>
		     
		    <input type="submit" class="button" name="dosubmit" value="驳回订单" onclick="winconfirm('<%=o.getId()%>','<%= substr %>','<%=o.getSendId()%>')"></input>
		</td>
		<td align="center">     
		    <%
		      OrderPrintln opp = opmap.get(OrderPrintln.salerelease) == null?null:opmap.get(OrderPrintln.salerelease).get(o.getId()); 
		      if(opp!= null){
		    	  if(opp.getStatues() == 0 ){
		    %>
		  
		<!-- <span style="cursor:hand" onclick="funcc('ddiv<%=o.getId() %>',<%=o.getId() %>)">有申请驳回请求</span>  
		 <div id="ddiv<%=o.getId()%>"   >
		   <table>
		    	 <tr> 
		    	     <td> --> 
		    	     <%=opp.getMessage() %>
		    	     
		    	   <!--  </td>
		    	 </tr> 
		   </table> -->    
		   <input type="button" onclick="changes('<%=o.getId()%>','<%=opp.getId() %>','<%=OrderPrintln.comited%>','','','<%=OrderPrintln.salerelease%>')"  value="同意"/> 
		    <input type="button" onclick="changes('<%=o.getId()%>','<%=opp.getId() %>','<%=OrderPrintln.uncomited%>','','','<%=OrderPrintln.salerelease%>')"  value="不同意"/>
		<!-- </div> -->
		<%
		}
     }
		%>
		</td>
		<td align="center">   
		   <%  
		    
		    if(opmap.get(OrderPrintln.release) != null){

			 OrderPrintln orp = opmap.get(OrderPrintln.release).get(o.getId()); 
			 if(orp != null){
			  int sta = orp.getStatues();
	          String sm = "";
	          if(0 == sta){
	        	  sm = "待确认";
	          }else if(2== sta){
	        	  sm = "申请已同意"; 
	          }else if(4== sta){
	        	  sm = "申请被拒绝";
	          } 
         %> 
		   <%=sm %>
		  <%
	    	
	       } 
		 }%>
		</td>
		<td align="center"> 
		<%    
		if(opmap.get(OrderPrintln.releasedispatch) != null){
			OrderPrintln oppp = opmap.get(OrderPrintln.releasedispatch).get(o.getId());
			if(oppp != null && oppp.getStatues() == 0){
				int statues = -1;    
				  
				if(o.getDeliveryStatues() == 0 || o.getDeliveryStatues() == 9 ){
					if(o.getSendId() == 0){
						statues = 0;  
					}else if(o.getSendId() != 0 ){
						statues = 2;
					}
					 
				}else if (o.getDeliveryStatues() == 1){
					statues = 1 ;  
				}else if(o.getDeliveryStatues() == 2){ 
					statues = 1 ; 
				}  
								
				%> 
				<%=oppp.getMessage() %>
		    <input type="button" onclick="changes('<%=o.getId()%>','<%=oppp.getId() %>','<%=OrderPrintln.comited%>','<%=statues %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="同意"/> 
				<%
			
			}
	    }
		%>
		</td>
		
		<td align="center">
		<%  if(o.getDeliverytype() != 0)
		   if(o.getReturnid() == 0){    
		%>
		<select class = "category" name="category"  id="return<%=o.getId() %>" >
		 <option value="0"></option>
		<%    
               for(int j=0;j< listS.size();j++){
            	   User u = listS.get(j);
            	   String str1 = "";
            	   if(u.getId() == o.getReturnid()){
            		   str1 = "selected=selected" ;
            		   
            	   }  
            	   %> 
            	    <option value=<%=u.getId() %>  <%= str1%>> <%=u.getUsername() %></option>
            	   <% 
            	   
                    }
	                	%>
         </select>   
       
         <input type="button" onclick="change('return<%=o.getId()%>','<%=o.getId()%>','<%=Order.orderreturn%>')"  value="确定"/>
		<%} else { 
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == o.getReturnstatuse()){
		%>
		 商品未回
		<%
          }else if(1 == o.getReturnstatuse()){
		%>
	      商品已回
		
		<%
          }
		%>
		 
		 <%=usermap.get(Integer.valueOf(o.getReturnid())).getUsername() %>
		<%  
		}
		%>
		</td> 
    </tr>
    <%}
    
    }%>
</tbody>
</table>


     </div>


</body>
</html>
