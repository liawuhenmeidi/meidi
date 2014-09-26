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
	
	//session.setAttribute("sear", sear); 
	
}
 
//list = OrderManager.getOrderlistl(user,Group.sencondDealsend,"",sort); 
     
list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.charge,num,Page,sort,sear);  
session.setAttribute("exportList", list); 
count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.charge,num,Page,sort,sear);  
     
HashMap<Integer,User> usermap = UserManager.getMap(); 
//获取送货员    
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
List<User> listS = UserManager.getUsers(user,Group.send);
    
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,3);

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
    width:2000px;
    table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:2000px;
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
		 window.location.href="dingdan_charge.jsp?pages="+pages+"&numb="+num;
	 });
	 
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdan_charge.jsp?pages="+pages+"&numb="+num;
	 });  
	 
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="dingdan_charge.jsp?page="+pages+"&numb="+num+"&sort="+sort;
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
 
function changes(str1){
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="dingdanpeidan2.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function change(str1,str2){
	var uid = $("#"+str1).val();
	$.ajax({ 
        type: "post",    
         url: "../../user/server.jsp",
         data:"method=peidan&id="+str2+"&uid="+uid,
         dataType: "", 
         success: function (data) {
           alert("设置成功");  
           window.location.href="dingdanpeidan2.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });

}
 
function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.name;
	   				if(str != null && str != ""){
		   				   attract[i] = str;
			   	            i++;
		   				}
	   		}
	   	}); 
		
		$.ajax({ 
	        type: "post",  
	         url: "../server.jsp", 
	         data:"method=statuesinstall&id="+attract.toString(),
	         dataType: "",   
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("操作失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("操作成功");  
	        		  window.location.href="dingdan_charge.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("服务器忙，请稍后重试"); 
	            }  
	           });
	}
}

function orderPrint(id,statues){
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=print2&id="+id+"&statues="+statues,
         dataType: "",  
         success: function (data) {  
           window.location.href="printPaigong.jsp?id="+id;
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

function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

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
	<jsp:param name="type" value="<%=Order.pcharge%>"/> 
</jsp:include>
 
<jsp:include page="../search.jsp"/>
 
<div class="btn">
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input>  
</div>

</div>
<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>

</div>
<div style=" height:170px;">
</div>

 
<br/>  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">  
			
			<td align="center"><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">销售员</td>
			<td align="center">顾客信息</td>
			<td align="center">送货名称</td>
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
          
            <td align="center">安装日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			
			<td align="center">备注</td>
			<td align="center">送货人员</td>
			<td align="center">送货时间</td>
			<td align="center">送货是否已结款</td>
			<td align="center">安装人员</td>
			<td align="center">安装时间</td>
			
			<td align="center">先送货后安装</td>
		    <td align="center">是否已回访</td>
		     
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
		<td align="center" width="20"><input type="checkbox" value="" id="check_box" name = "<%=o.getId() %>"></input></td>
		<td align="center"><%=o.getPrintlnid() == null?"":o.getPrintlnid()%></td>
		<td align="center"><%=o.getBranch()%></td>
		<td align="center">
		    
                		  
		<%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		
		</td> 
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
		    	 if(null !=op ){  
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
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		<td align="center">
		<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td>
		
		
		
        <td align="center">   
		    <%=o.getRemark() %>
		</td>
 
		<td align="center"> 
		<% if(o.getSendId() != 0){
			if(usermap.get(Integer.valueOf(o.getSendId())) != null){
		 %>
		 <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
		 <%
		  }
		}
		 %>
		
		</td>
	<td align="center"> 
		<%=o.getSendtime()
		 %> 
		
		</td>
		 <td align="center"> 
		<%=o.getStatuesPaigong() == 1 ?"是":"否"
		 %> 
		 </td>
		<td align="center"> 
		<% if(o.getInstallid() != 0){   
		      
			%> 
		 <%=usermap.get(o.getInstallid()).getUsername() %>
		 <%
		  }else if(o.getInstallid() == 0 && o.getDeliveryStatues() == 2){
			  if(usermap.get(o.getSendId()) != null){
			  %> 
			  
			 <%=usermap.get(o.getSendId()).getUsername() %>
			 
			 <% 
		  }  
		  }
		 %>
		
		</td>
		<td align="center"> 
		
		<%=o.getInstalltime()==null?"空":o.getInstalltime()
		 %> 
		 </td>
		
		 <td align="center"> 
		<%=o.getDeliverytype() == 2 ?"是":"否"
		 %> 
		 </td>
		<td align="center">    
		    <%=o.getStatuescallback()==0?"否":"是" %> 
		</td>
    </tr>
    <%}
    
    }%>
</tbody>
</table>


     </div>


</body>
</html>
