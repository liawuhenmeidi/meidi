<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
  
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
 
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


//List<Order> list = OrderManager.getOrderlistTuihuo(user,Group.Manger,num,Page);
 
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.deliveryStatuesTuihuo,num,Page,sort,""); 
session.setAttribute("exportList", list); 
count =   OrderManager.getOrderlistcount(user,Group.dealSend,Order.deliveryStatuesTuihuo,num,Page,sort,""); 
//System.out.println("count"+count);  

HashMap<Integer,User> usermap = UserManager.getMap();
  
//获取二次配单元（工队）
List<User> listS = UserManager.getUsers(user,Group.sencondDealsend);  

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
 // System.out.println("%%%%%"+gMap);
//修改申请 
//Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,0);
// 退货申请
//Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,1);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
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
    width:2500px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:2500px; 
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

<script type="text/javascript" src="../js/common.js"></script>
<!--   头部开始   -->
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "";
var pages = "<%=Page%>";   
var num = "<%=num%>";
  

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
		 window.location.href="dingdanTuihuo.jsp?pages="+pages+"&numb="+num;
	 });
	 
	// $("#search").blur(function(){
	 	
		// var search = $("#search").val();
		// var serchProperty = $("#serchProperty").val();
		 
		// window.location.href="dingdan.jsp?search="+search+"&serchProperty="+serchProperty;
	// });
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdanTuihuo.jsp?pages="+pages+"&numb="+num;
	 }); 
	   
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="dingdanTuihuo.jsp?page="+pages+"&numb="+num+"&sort="+sort;
	 }); 
});

function serch(){
	 var search = $("#search").val();
	 var serchProperty = $("#serchProperty").val();
	 
	 window.location.href="dingdanTuihuo.jsp?pages="+pages+"&numb="+num+"search="+search+"&serchProperty="+serchProperty;

	
}
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
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=print4&id="+id+"&statues="+statues,
         dataType: "",    
         success: function (data) {           
           window.location.href="print.jsp?id="+id+"&type="+type;
           },   
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
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
 
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
  
<jsp:include flush="true" page="page.jsp">
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
    <jsp:param name="type" value="<%=Order.deliveryStatuesTuihuo %>"/> 
</jsp:include>  
<jsp:include page="search.jsp"/>
</div>

<div style=" height:150px;">
</div>

 
<br/>  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">  
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
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			
			<td align="center">送货人员</td>
			<td align="center">备注</td>
			<td align="center">打印</td>
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
		<td align="center"><a href="javascript:void(0)" onclick="adddetail('dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
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
		<td align="center"><%=o.getPos() %></td>
		<td align="center"><%=o.getSailId() %></td>
		<td align="center"><%=o.getCheck() %></td>
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
		 
		  <td align="center"><%=pcategory%></td>
		  <td align="center"><%=ptype%></td>
		  <td align="center"><%=pcountt%></td> 
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
		<td align="center"><%=o.getSaleTime() %></td>
		<td align="center"><%=o.getOdate() %></td>
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		<td align="center">
		<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td>
		<td align="center">
		
		<% 
		//打印状态     0  未打印   1 打印
		if(0 == o.getReturnwenyuan()){
		%>
		 未打印
		<%
         }else if(1 == o.getReturnwenyuan()){
		%>
		已打印 
		<%
         }
		%>
		
		
		</td>
		
		
		<td align="center" style="white-space:nowrap;">
		
		<%  if(2 == o.getDeliveryStatues()) {
			 String str = "";
			 if(null != listS){
			 for(int j=0;j< listS.size();j++){
          	   User u = listS.get(j);
          	   if(u.getId() == o.getSendId()){
          		   str = u.getUsername() ;
          	   } 
			 }
			 }
         %>
         <%=str %>
         
		<%
         }else {
        	 String str = "";
        	 for(int j=0;j< listS.size();j++){
          	   User u = listS.get(j);
          	   
          	   if(u.getId() == o.getSendId()){
          		  str = u.getUsername() ;
          	   } 
        	 }
		%>
	<%=str %>
		<%
        
         }
		%>
		 
		 
		</td>
        
        <td align="center"> 
		    <%=o.getRemark() %>
		</td>
		<td align="center"> 
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',1,'<%=Order.deliveryStatuesTuihuo%>')">[打印]</a>
		</td>
    </tr>
    <%} 
    
    }%>
</tbody>
</table>
 <!--
<div class="btn">
 <input type="submit" class="button" name="dosubmit" value="删除" onclick="return confirm('您确定要删除吗？')"></input>

</div>  
 -->

     </div>


</div>
</div>


</body>
</html>
