<%@page import="utill.StringUtill"%>
<%@page import="wilson.salaryCalc.SalaryCalcManager"%>
<%@page import="wilson.salaryCalc.SalaryResult"%>
<%@ page language="java" import="java.util.*,wilson.upload.*,group.*,utill.*,orderproduct.*,order.*,saledealsend.*,user.*,wilson.matchOrder.*,user.*,wilson.salaryCalc.*,java.text.SimpleDateFormat;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8"); 
	User user = (User)session.getAttribute("user");  
	List<User> listS = null ;  
	if(UserManager.checkPermissions(user, Group.dealSend)){
		listS =  UserManager.getUsers(user,Group.sencondDealsend);
	}else {
		listS = UserManager.getUsers(user,Group.send); 
	}
	
	Map<String ,Order> map = null;
	String type = request.getParameter("type");
	List<Saledealsend> listsa = null;
	Saledealsend sa = null;
	boolean sera = false ;
	boolean bsa = false ;
	boolean unquery = false ;
	String[] mess = null; 
	List<SaledealsendMessage> list = null;
	String  isdisabel = ""; 
	String name = "";
	String dealsendID = "";
	if(StringUtill.isNull(type)){
		listsa = SaledealsendManager.getSaledealsendUnquery(user,BasicUtill.dealsend);
		unquery = true ; 
	}else if("search".equals(type)){
	    String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate"); 
		String dealsendid = request.getParameter("dealsendid");
	    listsa = SaledealsendManager.getList(user,startDate,endDate,dealsendid,BasicUtill.dealsend);
	    sera = true ; 
   }else if("check".equals(type)) {
	   String id = request.getParameter("said");
	  // System.out.println(id);
	   sa = SaledealsendManager.getSaledealsend(id);
	   bsa = true ;
	  // sera = true ; 
	   name = sa.getName();
	   dealsendID = sa.getDealsendid()+"";
	   String message = sa.getMessage();
	  // mess = message.split(",");
	   list = SaladealsendMessageManager.getlistByid(sa);
	   map = OrderManager.getOrdermapByIds(user,sa.getOrderids());
	   if(sa.getGivestatues() == 1){ 
		   isdisabel = " readonly "; 
	   }
	   
   }
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提成导出页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<style type="text/css">
body {
	font-family: "Trebuchet MS", "Helvetica", "Arial",  "Verdana", "sans-serif";
	font-size: 62.5%;
	overflow: auto;
}
</style>

</head>

<body style="overflow:auto">
  
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/calendar.js"></script> 

<script type="text/javascript">
var name = "<%=name%>";
var dealsendID = "<%=dealsendID%>";

function save(statues,sta){
	var price = $("#addcount").html();
	var message = "";
	if(statues == 0 ){
		 message = "打印之后不能修改，您确定要打印吗？";
	}else {
		message = "您确定要保存吗？";
	} 
	if(statues == 0 && sta == 0 ){ 
		message = "安装网点尚未确认,打印之后不能修改，您确定要打印吗？";
	}
	//alert(price);
	//alert(name);
	//alert(dealsendID);
	//return false ;
	var question = confirm(message);	
	if (question != "0"){
		var mess = new Array();
		 var id = $("#said").val();
		 //alert(id);
		 //return false ;
		 var savetype = $("#savetype").val();
		 if(2 == statues){
			 savetype = "updateleft";
		 }
		 $("input[name='orderidresult']").each(function(){
				var id = $(this).val();
				var leftprice = $("#"+id+"left").val();
				var rightprice = $("#"+id+"right").val();
				
				mess.push(id+"-"+leftprice+"-"+rightprice);
		     }); 
		 //jPrompt('请输入keleyi.com或者其他:', 'keleyi.com(预填值)', 'Prompt对话框', function(r) {
			 //   if( r ) alert('You entered ' + r);
		//	});
		   
		 $.ajax({ 
		        type: "post", 
		         url: "server.jsp",   
		         data:"method=dealsendcharge&message="+mess+"&said="+id+"&savetype="+savetype,
		         dataType: "", 
		         success: function (data) { 
		        	 if(statues == 0 ){
		        		 window.location.href="printChargeDealsend.jsp?price="+price+"&name="+name+"&dealsendID="+dealsendID+"&type=<%=BasicUtill.dealsend%>";
		        	 }else{
		        		 window.location.reload();
		        	 }
		        	
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		        	 alert("执行失败"); 
		            } 
		           });
	}
 }
 
 
 function checkedd(){
	 var said = $("#said").val();
	 if(said == null || said == ""){
		 return false ;
	 }
 }
 
 function addcount(){
	 var totalcount = 0 ;
	 $("input[name='orderidresult']").each(function(){
			var id = $(this).val();
			var price = $("#"+id+"left").val();
			if(!isNaN(price)){
				 totalcount += price*1;
		      } 
			
	     });
	 $("#addcount").html(totalcount);
 }
 
 function unconfire(said){
	 $("#said").val(said);
	 $("#unquery").submit();
 }
</script>
<div style="position:fixed;width:100%;height:20px;"></div>
<div style="position:fixed;width:80%;height:20px;"></div>
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>     

<hr style="border : 1px dashed blue;" />

<form action="" method="post">
<input type="hidden" name="type" value="search"/>

                             开始: <input class="date2" name="startDate" type="text" id="datepicker1" onclick="new Calendar().show(this);" placeholder="必填"/>
----------结束: <input class="date2" name="endDate" type="text" id="datepicker2" onclick="new Calendar().show(this);" placeholder="必填"/>
<%if(UserManager.checkPermissions(user, Group.dealSend)) {%>
<select name="dealsendid">
<option></option>
 <% for(int i=0; i<listS.size();i++){
	 
	 User u = listS.get(i); 
 %>
  <option value="<%=u.getId() %>" ><%=u.getUsername() %></option>
 
 <%  }%>
 
</select>
<% }else {
	 
%>
<input type="hidden" name="dealsendid" value="<%=user.getId()%>"/><%=user.getUsername() %>
<% }%>
<input type="submit" value="搜索"/>
</form>
<hr style="border : 1px dashed blue;" />
<%
if(sera){
%>
 
<form action="" method="post" onsubmit="return checkedd()">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名称:
<input type="hidden" name="type" value="check"/>
<select name="said" id="said">
	<option ></option>
	<%
	for(int i = 0 ; i < listsa.size() ; i ++){ 
		Saledealsend sain  = listsa.get(i);
	%>
	<option value="<%=sain.getId() %>" ><%=sain.getSubmittime()+sain.getName() %></option>
	<%
	} 
	%>
</select>
<input type="submit" value="查看"/>
</form>
<%
}
%>
<hr style="border : 1px dashed blue;" /><br/>
<%
if(unquery){
%>
<form action="" method="post" id="unquery"> 
<input type="hidden" name="type" value="check"/>
<input type="hidden" name="said" id="said" value=""/>

<table border="1px" align="left" width="100%">
       <tr>
		  <td align="center">名称</td>
		  <td align="center">安装网点</td>
		  <td align="center">提交时间</td>
		  <td align="center">安装网点是否确认</td>
		  <td align="center">文员是否确认</td>
		</tr>

 <%
   if(null != listsa){
	for(int i = 0 ; i < listsa.size() ; i ++){ 
		Saledealsend sain  = listsa.get(i);
		
	%>
	<tr class="asc" ondblclick="unconfire('<%=sain.getId()%>')" onclick="updateClass(this)">
	 <td align="center">
	 <%=sain.getName() %>
	 </td>
	<td align="center">
	 <%=UserService.getMapId().get(Integer.valueOf(sain.getDealsendid())).getUsername()%>
	 </td>
	 <td align="center">
	 <%=sain.getSubmittime() %>
	 </td>
	 <td align="center">
	 <%=sain.getReceivestatues()==0?"否":"是" %>
	 </td>
	 <td align="center"> 
	 <%=sain.getGivestatues()==0?"否":"是" %>
	 </td>
	</tr>
	<%
	} 
   }
}
	%>


</table>
</form>

<%
if(bsa){
	if(list.size() > 0 ){
%>
	<form>
	<% 
	  String message = "";
	 if(UserManager.checkPermissions(user, Group.dealSend)){
		 if(sa.getGivestatues() == 0){
         %> 
          <input type="button" class="button" value="打印并保存" onclick="save('0','<%=sa.getReceivestatues() %>')" ></input>
          <input type="button" class="button" value="确认修改" onclick="save('2','<%=sa.getReceivestatues() %>')" ></input>
          <input type="hidden" name="savetype" id="savetype" value="left"/>
         <%    
           }
		 %>
		 <%
     }else {
    	 if(sa.getGivestatues() == 0){
     %>
     <input type="button" class="button" value="保存" onclick="save('-1','')" ></input>
     <input type="hidden" name="savetype" id="savetype" value="right"/>
    <%  } 
         }
    %>
    <input type="button" class="button" value="导出" onclick="javascript:window.location.href='ChargeExportServlet?said=<%=sa.getId() %>'"></input> 
    
	<input type="hidden" name="type" value="save"/> 
	<input type="hidden" name="said" id="said" value="<%=sa.getId() %>" value="save"/> 
	<br/>
	<table border="1px" align="left" >
		<tr>
		    <td align="center">序号</td>
			<td align="center">单号</td>
			<td align="center">门店</td>
			<td align="center">安装网点</td>
			<td align="center">顾客信息</td>

			<td align="center">送货名称</td>
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
            
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
			<td align="center">备注</td>
			<td align="center">结款金额</td>
			<td align="center">网点建议</td>
		</tr>
		<%  
		List<OrderProduct> listopid = new ArrayList<OrderProduct>();
		int total = 0 ;
		int x = 0 ;
		for(int i = 0 ; i < list.size() ; i ++){
			SaledealsendMessage sas = list.get(i);
			int price1 = sas.getDealsendprice();
			String price2 = sas.getDealsendMessage();
			String orderid = sas.getOrderids();
			// String str = mess[i];
			// String[] strr = str.split("-");
			// String price1 = strr[1];
			// String price2 = strr[2];
			 total += price1;
			 //String orderid = strr[0];
			 String[] ordersid = orderid.split("_");
			 int count = ordersid.length ;
			 //System.out.println(count);
			 //if(ordersid.length > 1){
				// for(int j=0;j<ordersid.length;j++){
					// System.out.println(ordersid[j]);
					 //System.out.println(StringUtill.GetJson(map));
					// Order o = map.get(ordersid[j]);
					// System.out.println(o);
					// List<OrderProduct> listooo = o.getOrderproduct();
					
					//for(int m=0;m<listooo.size();m++){
					//	count ++;
					//}
				// } 
			// }
			 
			 for(int j=0;j<ordersid.length;j++){
				 Order o = map.get(ordersid[j]);
				 x++;
				// List<OrderProduct> listooo = o.getOrderproduct();
				   // logger.info(x);
				    //logger.info(listo); 
				//for(int m=0;m<ordersid.size();m++){
					//Order op = ordersid.get(m);
					//listopid.add(op);
					//if(op.getStatues() == 0){
						String tdcol = " bgcolor=\"red\"" ; 
						if(o.getPhoneRemark()!=1){
							tdcol = ""; 
						}
						
			    		%>
						<tr id="<%=o.getId()%>"  class="asc"  onclick="updateClass(this)">
						<!--  <td align="center" width="20"><input type="checkbox"  name="orderid" value="<%=o.getId()%> "></input></td> -->
						<td align="center" ><%=x%></td>   
						<td align="center"><a href="javascript:void(0)" onclick="adddetail('dingdanDetail.jsp?id=<%=o.getId()%>')" ><%=(o.getPrintlnid() == null?"":o.getPrintlnid())%></a></td>
						<td align="center" ><%=o.getbranchName(o.getBranch())%></td>  
						<td align="center"><input type="hidden" name="dealsendid"  value="<%=o.getDealsendId()%>"/><%=o.getdealsendName()%></td>
						<%
						if(o.getPhoneRemark()!=1){    
							tdcol = ""; 
						} 
						%>
						<td align="center"><%=o.getUsername()  %><font color="<%= tdcol%>"><%=o.getPhone1()%></font></td>  
						<td align="center"><%=o.getCategory(0, "</p>")%></td> 
						<td align="center" ><%=o.getSendType(0, "</p>")%></td>  
						<td align="center" ><%= o.getSendCount(0, "</p>")%></td> 
						<td align="center"><%=o.getLocate()%></td>
						<td align="center"><%=o.getLocateDetail() %></td>
						<td align="center"><%=OrderManager.getDeliveryStatues(o)%></td>
						<td align="center"><%=o.getRemark() %></td> 
						<%  
							if(j == 0 && count > 1 || count <= 1 ){
								 String color = "";
								 String iscolor = "";
								 if(count >1){
									 color = "style=\"background-color:red\";";    
								 }
								 if(!price2.equals("0")){
									 iscolor = "style=\"background-color:orange\";";
								 }
								 if(UserManager.checkPermissions(user, Group.dealSend)){
							 %>
							<td align="center"  <%=color%> rowspan="<%=count %> ">
			                     <input type="hidden" name="orderidresult" id="orderidresult" value="<%=orderid%>"/>
			                     <input type="text" id="<%=orderid%>left" value="<%=price1%>" <%=isdisabel %>  onBlur="addcount()"  style="border:0; width:80px"/>
			                 </td>  
			                 <td align="center"  <%=iscolor%> rowspan="<%=count %> ">
			                    <input type="hidden" id="<%=orderid%>right" value="<%=price2%>" />
			                       <%=price2 %>
			                 </td>
			                            
			            <% 
			                 }else { 
			                	 %>
			                	 
			                	 <td align="center"  <%=color%> rowspan="<%=count %> ">
			                          <input type="hidden" name="orderidresult" id="orderidresult" value="<%=orderid%>"/>
			                            <input type="hidden" id="<%=orderid%>left" value="<%=price1%>" />
			                            <%=price1%>
			                     </td>  
			                     <td align="center"  <%=iscolor%> rowspan="<%=count %> ">
			                            <input type="text" id="<%=orderid%>right" value="<%=price2 %>" <%=isdisabel %> />
			                     </td>
			                	 <%
			                 } 
						}
					//}
				
			// }
			%>
		</tr> 
						
						<%
			    }
               } %>
  	 <tr class="asc">
     <td align="center" colspan=10 ></td>
     <td align="center" >合计金额</td> 
     <td align="center" colspan=2><span style="color:red;font-size:20px;" id="addcount"><%= total %></span></td> 
    </tr>
	</table>
	</form>
	
<%
	}
}
%> 


</body>
</html>
