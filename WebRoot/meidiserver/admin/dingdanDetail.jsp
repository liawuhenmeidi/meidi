<%@ page language="java" import="java.util.*,message.*,locate.*,utill.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String id = request.getParameter("id");

List<String> listallp = ProductManager.getProductlist();
String listallpp = StringUtill.GetJson(listallp); 

Order o = OrderManager.getOrderID(user,Integer.valueOf(id));

List<Locate> listl = LocateManager.getLocate();

HashMap<Integer,User> usermap = UserManager.getMap();

List<OrderProduct> list = o.getOrderproduct();         
//获取二次配单元（工队）
List<User> listS = UserManager.getUsers(user ,Group.sencondDealsend);   
  
List<Category> listc = CategoryManager.getCategory(user,Category.sale); 

Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
HashMap<String,ArrayList<String>> listt = ProductManager.getProductName(); 

String plist = StringUtill.GetJson(listt);
Message message = MessageManager.getMessagebyoid(id); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript" src="../js/common.js"></script>

   
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<style type="text/css">
.fixedHead {  
position:fixed;
}  
</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">
var id = "<%=id%>";
var jsons = <%=plist%> ; 
var jsonallp =  <%=listallpp%>; 

$(function () {
	initproductSerch("#dingmaordercategory","#dingmatype");
	
}); 

function initproductSerch(str,str2){ 
    cid = $(str).val();
	$(str2).autocomplete({
		 source: jsons[cid]
	    }); 
	$(str).change(function(){
		$(str2).val("");
		cid = $(str).val();  
		$(str2).autocomplete({
			 source: jsons[cid]
		    });
		}) ;
   } 

function addImage(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function changepeidan(str1,oid,deliveryStatues){
	var uid = $("#"+str1).val();
	if(deliveryStatues != 0 ){
		if(uid == null || uid == ""){
			alert("请选择安装公司");
			return ;
		}
		question = confirm("您确定要配单并打印吗？");
		if (question != "0"){
			
			$.ajax({ 
		        type: "post", 
		         url: "server.jsp",
		         data:"method=peidan&id="+oid+"&uid="+uid,
		         dataType: "", 
		         success: function (data) { 
		            if(data == 8){
		            	alert("导购修改中。稍后重试"); 
		            }else{ 
		            	 window.location.href="print.jsp?id="+oid+"&deliveryStatues="+deliveryStatues;  
		            }
		           },  
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		            } 
		           });
		}else {
			return ;
		}
	}else { 
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",
	         data:"method=peidan&id="+oid+"&uid="+uid,
	         dataType: "", 
	         success: function (data) { 
	            if(data == 8){
	            	alert("导购修改中。稍后重试"); 
	            }else{ 
	            	window.location.href="print.jsp?id="+oid+"&deliveryStatues="+deliveryStatues;  
	            }
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
		
		
		
		
		
		
	}
	
}
 
function changes(str1,str2,str3,str4,str5,str6,type){
	  
	if( 2 == str3 ){       
		if(0 != str4){
			if(str6 != 2 ){    
			question = confirm("商品已送货，您不能直接同意，是否联系安装公司驳回");
			if (question != "0"){
				
				if(str5 == 0){   
					alert("您已经提交"); 
				}else {
				$.ajax({     
			        type:"post",  
			         url:"../user/server.jsp",  
			         //data:"method=list_pic&page="+pageCount,       
			         data:"method=shifang&oid="+str2+"&pGroupId="+pgroup+"&opstatues="+type,
			         dataType: "",  
			         success: function (data) {    
			          alert("驳回申请已提交成功"); 
			          window.location.href="dingdan.jsp";
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			          alert("驳回申请失败");
			            } 
			           });
			}
			}
			return ;
			} 
		}
	
	if(<%=OrderPrintln.salereleaseanzhuang%> == type || <%=OrderPrintln.salereleasesonghuo%> == type || <%=OrderPrintln.release%> == type || <%=OrderPrintln.releasedispatch%> == type && 2 == str6){
	question = confirm("请先打印");
	
	if (question != "0"){
		var type = "<%=Order.deliveryStatuesTuihuo%>"; 
		window.location.href="print.jsp?id="+str2+"&type="+type ;
	}else {
		return ;
	} 
	}	 
	} 
		$.ajax({  
	        type: "post", 
	         url: "server.jsp",   
	         data:"method=dingdaned&id="+str1+"&oid="+str2+"&statues="+str3,  
	         dataType: "",  
	         success: function (data) {
	          // window.location.href="dingdan.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
	
}  

function searchlocate(id){
	window.open("../adminmap.jsp?id="+id, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 // window.open.href="../adminmap.jsp?id="+id;
}

function checkedd(type){
	// window.opener.document.getElementById("refresh").value ="refresh";
	//parent.location.reload(); 
	//window.returnValue='refresh'; 
	//window.close();
	//window.oper.reload();
	// alert(type);
	
		 
	if(null != $("#dingmatype").val() && "" != $("#dingmatype").val()){
		
		var ctype = $("#dingmatype").val();
		if($.inArray(ctype,jsonallp) == -1){
				 alert("票面型号不存在，请重新输入");
				 return false;
		}
		if( null == $("#dingmaproductNum").val() || "" == $("#dingmaproductNum").val()){
			alert("票面数量不能为空");
			return false ;
		}
	}
	$("#print").val(type);
	$("#form").submit();
	 
	// window.opener.location.reload();
	// if("print" == type){
	//	 window.location.href="print.jsp?id="+ id;
	// }
}

</script>
<div style="position:fixed;width:100%;height:100px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

</div >



<div style=" height:100px;">
</div>


 
<br/>  
 
<div id="wrap" style="text-align:center;">  
<form  action="server.jsp"  name = "myForm" method ="post"  id="form"   >

<input type="hidden" name="method" value="updateorder"/>
<input type="hidden" name="oid" value="<%=id%>"/> 
<input type="hidden" name="typeMethod" id="print" value=""/>  

<table  cellspacing="1"  id="table"  style="margin:auto; width:80%;"> 
       <%  
		String tdcol = " bgcolor=\"red\"" ; 
		  %>
		  
		<tr  class="asc">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td> 
			<td align="center"><%=o.getPrintlnid() == null?"":o.getPrintlnid()%></td>  
			<td align="center">门店</td>
			<td align="center"><%=o.getbranchName(o.getBranch())%></td> 
			<td align="center">销售员</td>
			<td align="center">       		  
		     <%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		    </td> 
		</tr> 
		<tr class="asc">	 
			<td align="center">pos(厂送)单号</td>
			<td align="center" <%=o.getPosremark()==1?tdcol:"" %>>
	          <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>
			    <input type="text"  name="pos" id="pos" value="<%=o.getPos() %>"  />
			   <% }else {    
			   %>  
			     <%=o.getPos() %>
			   <%
			   }
			   %>
			</td>
				<td align="center">OMS订单号</td>
			<td align="center" <%=o.getSailidrecked()==1?tdcol:"" %>>
			
			 <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>
			    <input type="text"  name="sailid" id="sailid" value="<%=o.getSailId() %>"  />
			   <% }else {    
			   %>   
			     <%=o.getSailId() %>
			   <%
			   }
			   %>
			</td>
			<td align="center">验证码(联保单)</td>
			 <td align="center" <%=o.getReckedremark()==1?tdcol:"" %>>
			 
			 <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %> 
			    <input type="text"  name="check" id="chek" value="<%=o.getCheck() %>"  />
			   <% }else {  
				  
			   %>   
			     <%=o.getCheck() %>
			   <%
			   } 
			   %>
			 
			 </td>
			</tr>

			<%if(o.getPhoneRemark()!=1){ 
			tdcol = ""; 
		} %>
		<tr class="asc">
		<td align="center">票面名称</td>
			<td align="center">
				<% if(UserManager.checkPermissions(user, Group.dealSend) ){
	        	  %> 
			      <select class="category" name="dingmaordercategory" id="dingmaordercategory" style="width:95% ">
                      <option>&nbsp;&nbsp;&nbsp;&nbsp;</option>
					  <%  
					  for(int i=0;i<listc.size();i++){
						  Category cate = listc.get(i);
						  if(o.getCategory(1, "").equals(cate.getName())){
							  %>
							    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>" selected="selected"><%=cate.getName()%></option>
							  <%  
						  }else { 
							  %>
							    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>"><%=cate.getName()%></option>
							  <%  
						  }
					  }
					  %>
  
                   </select> 
			   <% }else {      
			   %>    
			     <%= o.getCategory(1,"")%>
			   <%
			   } 
			   %>
			</td>
		
		    <td align="center">票面型号</td> 
			<td align="center">
			<% if(UserManager.checkPermissions(user, Group.dealSend) ){
	        	  %> 
	        	 <input type="text" name="dingmatype" id ="dingmatype"  value="<%=o.getSendType(1,"")%>" style="width:90% "></input>
			   <% }else {      
			   %>    
			     <%=o.getSendType(1,"")%>
			   <%
			   } 
			   %>
			
			</td> 
			<td align="center">票面数量</td>
			 
			<td align="center">
			<% if(UserManager.checkPermissions(user, Group.dealSend) ){
	        	  %> 
	        	  <input type="text" id="dingmaproductNum" name="dingmaproductNum" value="<%= o.getSendCount(1,"")%>" style="width:50%" />
			   <% }else {      
			   %>    
			     <%= o.getSendCount(1,"")%>
			   <%
			   } 
			   %>
			   
			</td> 
		
		</tr>
	 <% 
	 for(int i = 0 ; i<list.size();i++){
		 System.out.println(list.size());
		 OrderProduct op = list.get(i);
		 if(op.getStatues() == 0){
	       
	   %>
		  
		<tr  class="asc">  
			<td align="center">送货名称</p>送货型号</td>  
			<td align="center"><%=op.getCategoryName()+"</p>"+ProductService.getIDmap().get(Integer.valueOf(op.getSendType())).getType()%></td>  
			<td align="center">上报状态</td>    
			<td align="center"><%=op.getSalestatues(op.getSalestatues())%></td> 
			<td align="center">送货数量</td>
			
			<td align="center"><%= op.getCount()%></td> 
			</tr>
      <%  }
      }%>			
		<tr class="asc">
			<td align="center">赠品</td>
			<td align="center"><%= o.getGifttype("</p>")%></td>
			<td align="center">赠品状态</td>
			<td align="center"><%= o.getGifStatues("</p>")%></td> 
			<td align="center">赠品数量</td>
			<td align="center"><%= o.getGifcount("</p>")%></td>
			</tr>
		<tr  class="asc">
			<td align="center">顾客信息</td>
			<td align="center">
			
			 <% if(UserManager.checkPermissions(user, Group.dealSend) || UserManager.checkPermissions(user, Group.sencondDealsend)){
	        	  %>
	        	   
	        	  <%=o.getUsername()  +"</p>" %>  
			    <input type="text"  color="<%=tdcol%>" id="phone1"  name="phone1" value="<%= o.getPhone1() %>"  />
			   <% }else {    
			   %>   
			     <%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		               %>
			   <%
			   } 
			   %>
 
		</td>
            <td align="center">开票日期</td> 
            <td align="center">
            <% if(UserManager.checkPermissions(user, Group.dealSend) ){
	        	  %> 
			     <input class="date" type="text" name="saledate"   id = "serviceDate2" value="<%=o.getSaleTime() %>"   onclick="new Calendar().show(this);" readonly="readonly" style="width:90% "></input> 
			   <% }else {      
			   %>    
			     <%=o.getSaleTime() %>
			   <%
			   } 
			   %>
            
            </td>  
            <td align="center">预约日期</td>
            <td align="center">
             <% if(UserManager.checkPermissions(user, Group.dealSend) || UserManager.checkPermissions(user, Group.sencondDealsend) ||  UserManager.checkPermissions(user, Group.sencondDealsend)){
	        	  %> 
	        	 <!--<input class="date2" type="text" name="andate" id ="serviceDate"  value="<%=o.getOdate() %>"   style="width:90% "></input>  -->
			     <input class="date" type="text" name="andate"  id = "serviceDate" onclick="new Calendar().show(this);" value="<%=o.getOdate() %>"  readonly="readonly" style="width:90% "></input> 
			   <% }else {     
			   %>    
			     <%=o.getOdate() %>
			   <%
			   } 
			   %>
            
            </td>
           
            </tr>
		<tr  class="asc">
		    <td align="center">送货地区</td>
            <td align="center">
            <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>
	        	 <select class = "quyu" name="diqu" id="quyu">
			        <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
					<%
					 for(int i=0;i<listl.size();i++){
						 Locate lo = listl.get(i);
						 String str = "";
						 if(o.getLocate().equals(lo.getLocateName())){
							 str = "selected=\"selected\"";
						 }
					%>	
					 <option value="<%=lo.getLocateName()%>" <%=str %>><%=lo.getLocateName()%></option>
		<%
		 } 
		%>
	</select>
			   
			   <% }else {     
			   %>   
			     <%=o.getLocate()%>
			   <%
			   } 
			   %>
            </td>
            <td align="center">送货地址</td>
            <td align="center">
            
            <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>
	        	 <textarea  id="locations"  name="locations" ><%=o.getLocateDetail() %></textarea> 
			   
			   <% }else {     
			   %>   
			     <%=o.getLocateDetail() %> 
			   <%
			   } 
			   %>
            </td>
           <td align="center">送货状态</td>
          <td align="center">
		<%=OrderManager.getDeliveryStatues(o) %>
		</td>
           </tr> 
		  <tr class="asc"> 
			<td align="center">备注</td>
			  <td align="center"> 
			  <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>  
	        	<textarea  id="remark" name="remark" ><%=o.getRemark() %></textarea>   
			   <% }else {    
			   %>   
			     <%=o.getRemark() %>
			   <%
			   } 
			   %>
		       
		      </td>
             
		<td align="center">查看位置</td> 
			  <td align="center"> 
		    <a href="javascript:void(0);"  onclick="searchlocate('<%=o.getId() %>')">[查看位置]</a> 
		</td>
		<td align="center">安装网点</td> 
			  <td align="center"> <%=o.getdealsendName() %>
		</td>
			</tr> 
</table> 

<table cellspacing="1"  id="table" style="margin:auto; width:80%;">  
    <tr id="th">   
    <td align="center">留言</td>
    
       <td align="center"> 
                <% 
                  if(message != null){
                %>  
                <textarea  id="remark" name="remark" ><%=message.getMessage() %></textarea> 
                <br/>
                
                <%} %> 
           <input  type="text" name="message"   style="width:90% "></input>
  <br/>
       
       </td>
          <td></td>
    <td width="100%" class="center"><input type="button"  style="background-color:red;font-size:25px;"  value="确认修改" onclick="checkedd('query')"  /></td>
    <td width="100%" class="center"> 
    <% if(o.getPrintSatues() == 1 && UserManager.checkPermissions(user, Group.dealSend)){ %>
    <input type="button"  style="background-color:red;font-size:25px;"  value="打印" onclick="checkedd('print')"/>
    <% }%>
    </td>
   </tr> 
   </table> 

</form>
     </div>

</body>
</html>
