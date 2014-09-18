<%@ page language="java" import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
int count = 0 ;   
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");  
String sort = request.getParameter("sort");  
int opstatues = OrderPrintln.releasedispatch;   
//String sear = (String)session.getAttribute("sear");
//if(StringUtill.isNull(sear)){ 
//	sear = ""; 
//}  
String sear = "";
if(!StringUtill.isNull(sort)){
	session.setAttribute("sort", sort);
}else {
	sort = "id desc";   
}  
 
if(!StringUtill.isNull(numb)){
	session.setAttribute("numb", numb);
}else {
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



List<Order> listnew = new ArrayList<Order>(); 
List<Order> list = OrderManager.getOrderlist(user,Group.dealSend,Order.orderDispatching,num,Page,sort,sear);
//if(list != null){
//Iterator<Order> it = list.iterator();
 
//while(it.hasNext()){
	//Order neworder = it.next();
	//if(TimeUtill.getLongtime(neworder.getOdate())){
	    // listnew.add(neworder);
		// it.remove();
	///}  
//}
//if(list != null){
//System.out.println(list.size());
//for(int i=0;i<list.size();i++){
	//Order neworder = list.get(i);
	//listnew.add(neworder);
//}
//}
//}

session.setAttribute("exportList", list); 
count =   OrderManager.getOrderlistcount(user,Group.dealSend,Order.orderDispatching,num,Page,sort,sear);    
   
HashMap<Integer,User> usermap = UserManager.getMap();
        
//获取二次配单元（工队）
List<User> listS = UserManager.getUsers(user ,Group.sencondDealsend);   
  
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
 
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);


Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);


//修改申请
//Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.modify);
// 退货申请
//Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.returns); 
  
//Map<Integer,OrderPrintln> opMap2 = OrderPrintlnManager.getOrderStatues(user,OrderPrintln.release);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  
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

td { 
    width:100px;
    line-height:30px;
}
 
#table{  
    BACKGROUND-IMAGE: url('../image/f.JPG');
    width:2400px;
    table-layout:fixed ;
}

#th{ 
    background-color:white;
    position:absolute; 
    width:2400px; 
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

<body style="scoll:no">
 
<!--   头部开始   --> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
var id = "";
var pages = "<%=Page%>";   
var num = "<%=num%>"; 
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
		 window.location.href="dingdan.jsp?pages="+pages+"numb="+num;
	 });

	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href="dingdan.jsp?page="+pages+"&numb="+num;
	 }); 
	  
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val());
		// alert(num);  
		 window.location.href="dingdan.jsp?page="+pages+"&numb="+num+"&sort="+sort;
	 }); 
}); 
     
function changepeidan(str1,oid){
	question = confirm("您确定要配单并打印吗？");
	if (question != "0"){
		var uid = $("#"+str1).val();
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",
	         data:"method=peidan&id="+oid+"&uid="+uid,
	         dataType: "", 
	         success: function (data) { 
	            
	            window.location.href="print.jsp?id="+oid; 
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
	}else {
		return ;
	}
	
	
}

function changes(str1,str2,str3,str4,str5,str6,type){
  
	if( 2 == str3 ){       
		if(0 != str4){
			if(str6 != 2 ){    
			question = confirm("商品已送货，您不能直接同意，是否联系安装公司释放");
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
			          alert("释放申请已提交成功"); 
			          window.location.href="dingdan.jsp";
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			          alert("释放申请失败");
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
	           window.location.href="dingdan.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
	
}  

function searchlocate(id){  
	  window.location.href="../adminmap.jsp?id="+id;
}
</script>
<div style="position:fixed;width:100%;height:200px;">
<div style="position:fixed;width:70%;height:200px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      
<jsp:include flush="true" page="page.jsp">
	<jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
	<jsp:param name="type" value="<%=Order.orderDispatching %>"/> 
</jsp:include> 


<jsp:include page="search.jsp"/>


</div >
 
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
			<td align="center">销售员</td>
			<td align="center">pos(厂送)单号</td>
			<td align="center">OMS订单号</td>
			
			<td align="center">验证码(联保单)</td>
			<td align="center">顾客信息</td>
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
			<td align="center">备注</td>
			 
			<td align="center">配单</td>
			<td align="center">查看位置</td>
			<td align="center">安装单位释放</td> 
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
		 
		<td align="center" <%=o.getPosremark()==1?tdcol:"" %>><%=o.getPos() %></td>
		<td align="center" <%=o.getSailidrecked()==1?tdcol:"" %>><%=o.getSailId() %></td>
		<td align="center" <%=o.getReckedremark()==1?tdcol:"" %>><%=o.getCheck() %></td>
		<td align="center"><%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		%>
		
		</td>  
		     <% 
		    // String pcategory = "";
		     String scategory = "";
		    // String ptype = "";
		     String stype = "";
		     //String pcountt = "";
		     String scountt = "";
		     List<OrderProduct> lists = OrPMap.get(o.getId());
		     if(lists != null ){
			     for(int g = 0 ;g<lists.size();g++){
			    	 OrderProduct op = lists.get(g);
			    	 if(op.getStatues() == 1 ){
			    		// pcategory =  categorymap.get(Integer.valueOf(op.getCategoryId())).getName()+"</p>";
				         //pcountt += op.getCount() +"</p>";
				         //ptype += op.getSaleType()==null ||op.getSaleType() == "null" ? "":op.getSaleType() +"</p>";
			    	 }else {
			    		 scategory = categorymap.get(Integer.valueOf(op.getCategoryId())).getName()+"</p>";
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
		     String gstatues = "";
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
		<%
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == o.getDeliveryStatues()){
		%>
		 未发货
		<%
          }else if(1 == o.getDeliveryStatues()){

		%>
		已送货
		<%
          }else if(2 == o.getDeliveryStatues()){
		%>
	      已安装
		<%
          }else if(3 == o.getDeliveryStatues()){
		%>
		
		 已退货
		<%
          }
		%>
		</td>
		
        <td align="center"> 
		    <%=o.getRemark() %>
		</td>
 
 
 
		<td align="center">
		 <%   
		    int flag = -1 ;
		    int flag2 = -1 ;
		    if(opmap.get(OrderPrintln.modify) != null){
				OrderPrintln op = opmap.get(OrderPrintln.modify).get(o.getId());
				
				 if(op != null){  
					 flag = op.getStatues() ;
					 }
				 }
		    
		    if(opmap.get(OrderPrintln.returns) != null){
				OrderPrintln op = opmap.get(OrderPrintln.returns).get(o.getId());
				
				 if(op != null){  
					 flag2 = op.getStatues() ;
					 }
				 }
					  
		  
		   if(o.getDealsendId() == 0){
			   if(flag != 2 && flag != 0 && flag2 != 2 && flag2 != 0){
			   %>
				<select class = "category" name="category"  id="songh<%=o.getId() %>" >
				 <option value="0"></option>
				<%     if(listS != null ){
					     
			             
		               for(int j=0;j< listS.size();j++){
		            	   User u = listS.get(j);
		            	   String str = "";  
		            	   if(u.getId() == o.getDealsendId()){ 
		            		   str = "selected=selected" ;
		            	   }     
		            	   %> 
		            	    <option value=<%=u.getId() %>  <%= str%>> <%=u.getUsername() %></option>
		            	   <% 
		                    }
				     }
			            %>
		         </select> 
		      
		         <input type="button" onclick="changepeidan('songh<%=o.getId()%>','<%=o.getId()%>')"  value="确定"/> 
		         
			<% 	
		     }
		   }else {
			   %>
			    
			<%=usermap.get(o.getDealsendId()).getUsername() %>
			   
			   
			   <% 
		   }
		 %>
		</td> 
		
		<td align="center"> 
		    <a href="javascript:void(0);"  onclick="searchlocate('<%=o.getId() %>')">[查看位置]</a> 
		</td>
		
		<td align="center"> 
		<% 
		int shifangstatues = -1 ;
		if(opmap.get(OrderPrintln.release) != null){
		OrderPrintln orp = opmap.get(OrderPrintln.release).get(o.getId()); 
		
		 if(orp != null){
			 shifangstatues = orp.getStatues();
			 if(orp.getStatues() == 2){ 
			     	 
		    	 %> 
		    	  <%=orp.getMessage() %>
		    	   <p>释放申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(orp.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>释放申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
			 
			 
			 
			 %>
		  
		 <%=orp.getMessage() %> 
		    	      
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','0','','','<%=OrderPrintln.release %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','0','','','<%=OrderPrintln.release %>')"  value="不同意"/>
	
		
		<%
		   }   
		 }
		}
		%>
		 
		<%  
		if(opmap.get(OrderPrintln.salereleasesonghuo) != null){
		OrderPrintln orp = opmap.get(OrderPrintln.salereleasesonghuo).get(o.getId()); 
		
		 if(orp != null){
			 shifangstatues = orp.getStatues();
			 if(orp.getStatues() == 2){ 
			     	
		    	 %> 
		    	   <%=orp.getMessage() %>
		    	   <p>释放申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(orp.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>释放申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {

			 %>
		  
		 <%=orp.getMessage() %> 
		    	      
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','0','','','<%=OrderPrintln.salereleasesonghuo%>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','0','','','<%=OrderPrintln.salereleasesonghuo %>')"  value="不同意"/>
	
		
		<%
		   }   
		 }
		}
		%>
		
		<%  
		
		if(opmap.get(OrderPrintln.salereleaseanzhuang) != null){
		OrderPrintln orp = opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId()); 
		
		 if(orp != null){
			 shifangstatues = orp.getStatues();
			 System.out.println(shifangstatues);
			 if(orp.getStatues() == 2){ 
				 
		    	 %> 
		    	   <%=orp.getMessage() %>
		    	   <p>释放申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(orp.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>释放申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
			 
			 
			 
			 %>
		  
		 <%=orp.getMessage() %> 
		    	      
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','0','','','<%=OrderPrintln.salereleaseanzhuang %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','0','','','<%=OrderPrintln.salereleaseanzhuang %>')"  value="不同意"/>
	
		
		<%
		   }   
		 }
		}
		%>
		</td> 
		<td align="center"> 
		<%
		 if(opmap.get(OrderPrintln.modify) != null){
		OrderPrintln op = opmap.get(OrderPrintln.modify).get(o.getId());
		
		 if(op != null){  
			
			 if(op.getStatues() == 2){ 
			     	
		    	 %> 
		    	
		    	   <p>导购修改中</p>  
		    	   
		    	 <%
		    	 
		    	  }else if(op.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>修改申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
				    		  int statues = -1 ; 		  
							    if(opmap.get(OrderPrintln.releasemodfy) != null){
									OrderPrintln oppp = opmap.get(OrderPrintln.releasemodfy).get(o.getId());
									if(oppp != null ){
										statues = oppp.getStatues() ;
									} 
							    }
			 %>
		  
		 <%=op.getMessage() %>  
		 <% if(statues == 0 || shifangstatues == 0){
			 
		  %> 
		   安装公司处理中 
		 <%}else if(statues == 2 || shifangstatues == 2){  
		 %>
		 <input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','','<%=OrderPrintln.releasemodfy %>')"  value="同意"/>
	     
		<%}else if(statues == -1){ %> 
		<input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','','<%=OrderPrintln.releasemodfy %>')"  value="同意"/>
		<input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','<%=o.getDealsendId() %>','<%=statues %>','','<%=OrderPrintln.releasemodfy %>')"  value="不同意"/>
		<% }%>
		 
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
				    		  
				    int statues = -1 ;		  
				    if(opmap.get(OrderPrintln.releasedispatch) != null){
						OrderPrintln oppp = opmap.get(OrderPrintln.releasedispatch).get(o.getId());
						if(oppp != null){ 
							statues = oppp.getStatues() ;
						}

				    }
			 %>
		 <%=op1.getMessage() %>
		  <%if(statues == 0 || shifangstatues == 0){
		   %>    
		   安装公司处理中 
		  <% } else if(statues == 2 || shifangstatues == 2){%>
		  
		  <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="同意退货"/>
		   
		  <%}else { %>	         
		 <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','<%=o.getDealsendId() %>','<%=statues %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="不同意"/> 
		<% }%>
		</td>
		<%
		 }
		 }
		}
		%>

    </tr>

    <% 
    }
    }%>
</table> 
     </div>

</body>
</html>
