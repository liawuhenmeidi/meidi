<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
  
<%   
 
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int id = user.getId();  
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
int opstatues = OrderPrintln.salereleasesonghuo; 

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
	sear = HttpRequestUtill.getSearch(request);
}
//list = OrderManager.getOrderlist(user,Group.sencondDealsend,str,sort);      
list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.porderDispatching,num,Page,sort,sear);  
session.setAttribute("exportList", list); 
count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.porderDispatching,num,Page,sort,sear);  
    
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
HashMap<Integer,User> usermap = UserManager.getMap(); 
//获取送货员    
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
		 window.location.href="dingdanpeidan2anzhuang.jsp?pages="+pages+"&numb="+num;
	 });
	 
	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdanpeidan2anzhuang.jsp?pages="+pages+"&numb="+num;
	 });  
	 
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="dingdanpeidan2anzhuang.jsp?page="+pages+"&numb="+num+"&sort="+sort;
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
 
function changes(oid,id,statues){
	$.ajax({  
        type: "post", 
         url: "../server.jsp", 
         data:"method=dingdaned&oid="+oid+"&id="+id+"&statues="+statues, 
         dataType: "",  
         success: function (data) { 
           window.location.href="dingdanpeidan2anzhuang.jsp";
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function change(str1,oid,type){
	var uid = $("#"+str1).val();
	if(uid == null || uid == ""){
		alert("请选择安装员");
		return ;
	}
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


function adddetail(src){ 
	//window.location.href=src ;
	window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function wconfirm(){
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
	         data:"method=statuesinstalled&id="+attract.toString(),
	         dataType: "",   
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("操作失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("操作成功");  
	        		  window.location.href="dingdanpeidan2anzhuang.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("服务器忙，请稍后重试"); 
	            }  
	           });
	}
}

function winconfirm(str,str2){
	
  //alert(str2);
    if(4 == str2){
    	var question = confirm("您的驳回请求别拒绝，是否继续申请？");
			if(question == 0){
				return ;
			}
    }else if(0 == str2){
    	alert("您已提交驳回请求");
    	return ; 
    }

	     
    question = confirm("你确认要驳回吗？(商品入库后再驳回)");
	
	if (question != "0"){

		$.ajax({    
	        type:"post",  
	         url:"../../user/server.jsp",
	         //data:"method=list_pic&page="+pageCount,      
	         data:"method=shifang&oid="+str+"&pGroupId="+pgroup+"&opstatues="+opstatues,
	         dataType: "",  
	         success: function (data) {     
	          alert("驳回申请已提交成功");  
	          window.location.href="dingdanpeidan2anzhuang.jsp";
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          alert("驳回申请失败");
	            } 
	           });
	 }
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
   <jsp:param name="type" value="<%=Order.pinstall%>"/> 
</jsp:include> 

<jsp:include page="../search.jsp"/>
 
<div class="btn">
 <input type="submit" class="button" name="dosubmit" value="忽略确认" onclick="wconfirm()"></input>  
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
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
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
			
            <td align="center">预约日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			<td align="center">打印状态</td>
			
			
			<td align="center">备注</td>
			<td align="center">送货员</td>
			<td align="center">送货日期</td>
			<td align="center">安装员</td>
			
			    
			<td align="center">安装员驳回请求</td>  
		    <td align="center">驳回状态</td>   
		     
		</tr>
	
<tbody> 
  <% 
    
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>
    <tr id="<%=o.getId()+"ss" %>"  class="asc"  onclick="updateClass(this)"> 
		<!--  <td align="center"><input type="checkbox" value="1" name="userid[]"/></td> -->
		<td align="center" width="20"><input type="checkbox" value="" id="check_box" name = "<%=o.getId() %>"></input></td>
		<td align="center"><a href="javascript:void(0)" onclick="adddetail('../dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
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
		   <td align="center"><%= o.getCategory(0,"</p>")%></td>  
		  <td align="center" ><%=o.getSendType(0,"</p>")%></td>  
		  <td align="center" ><%= o.getSendCount(0,"</p>")%></td>   
		<td align="center" ><%= o.getGifttype("</p>")%></td>  
		<td align="center" ><%= o.getGifcount("</p>")%></td>  
		<td align="center" ><%= o.getGifStatues("</p>")%></td>
		
		
		<td align="center"><%=o.getOdate() %></td>
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

		 <%=o.getSendtime() %>
  
		</td>  
		<td align="center">
		<%  
		   if(o.getDeliveryStatues() == 1 || o.getDeliveryStatues() == 9 || o.getDeliveryStatues() == 10){
			   
		   if(o.getInstallid() == 0){ 
		%>
		<select class = "category" name="category"  id="songh<%=o.getId() %>" >
		 <option value=""></option>
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
		 <%}else {
			    if(usermap.get(o.getInstallid()) != null){
			    %>
				<%=usermap.get(o.getInstallid()).getUsername() %>
				<% 
				   } 
		           }  
				 
		   }else {
			    if(usermap.get(o.getInstallid()) != null){
		%>
		<%=usermap.get(o.getInstallid()).getUsername() %>
		<%
		   } 
		   }		    
		%>
		</td> 
		 
		<%--<td align="center">      
		    <%   
		        int tatues = 0;
		    if(opmap != null ){
		        if( null == opmap.get(OrderPrintln.salereleaseanzhuang)){
		        	tatues = -1 ;
		        }else if(null == opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId())){
		        	tatues = -1 ;
		        }else {
		        	tatues = opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId()).getStatues();
		        }
		    } 
		    %>
		     
		    <input type="submit" class="button" name="dosubmit" value="驳回订单" onclick="winconfirm('<%=o.getId()%>','<%=tatues%>')"></input>
		</td>
		--%><td align="center">
		<%      
		     if(null != opmap.get(OrderPrintln.salereleaseanzhuang)){
			 OrderPrintln orp = opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId()); 
			 if(orp != null){
         %> 
		   <%=orp.getMessage() %>
		    <input type="button" onclick="changes('<%=o.getId()%>','<%=orp.getId() %>','<%=OrderPrintln.comited%>')"  value="同意"/>
		    <input type="button" onclick="changes('<%=o.getId()%>','<%=orp.getId() %>','<%=OrderPrintln.uncomited%>')"  value="不同意"/>
		<%   
		  }
		  
		   }%> 
		</td>
		<td align="center">      
		   <%  
		    if(opmap.get(OrderPrintln.salereleasesonghuo) != null){

			 OrderPrintln orp = opmap.get(OrderPrintln.salereleasesonghuo).get(o.getId()); 
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
		   
		   
		   
		   
		   
		   
		   
		</td>
		
		
    </tr>
    <%
  
    }%>
</tbody>
</table>


     </div>


</div>
</div>


</body>
</html>
