<%@ page language="java"  import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");   
User user = (User)session.getAttribute("user");   
String uid = request.getParameter("uid"); 
List<LogisticsMessage>	list = null;
if(StringUtill.isNull(uid)){  
	list = LogisticsMessageManager.getlist("2,3"); 
}else {  
	list = LogisticsMessageManager.getlist(Integer.valueOf(uid),"2,3");
}     

List<User>  listu = UserService.getLogistics(user);  
//System.out.println("CarsService.getmap()"+CarsService.getmap());

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结款</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
var uid = "<%=uid%>";
$(function () { 
	 $("#uid").val(uid);
});



function detail(id){
	window.location.href="logisticDetail.jsp?id="+id;
}
  
var flag = false;  
function chager(){
	$("input[type='checkbox'][id='check_box']").each(function(){          
   		if($(this).attr("checked")){
   				var str = this.value; 
   				 
   				if(str != null  &&  str != ""){
	   				  // attract[i] = str; 
		   	          //  i++;
		   	          flag = true;
	   				}	
   		} 
   	});  
	  
	if(flag ){
		if(uid != ""){
			$("#myformLG").submit();
		}else {
			alert("请选择司机");
		}
		
	}
}

function totalInit(){
	var total = 0 ;  
	$("input[type='checkbox'][id='check_box']").each(function(){          
   		if($(this).attr("checked")){ 
   				var str = this.value;  
   				var prince = $("#p"+str).text();
   				total += prince*1;
   				
   		} 
   	});  
	 
	$("#total").text(total);
	
}
</script>
</head>

<body>
<!--   头部开始   --> 

 <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
 
     
 <!--       -->    
     
     <div class="">
   <div class="weizhi_head">
    <form id="mypost">
       <table>
   <tr>
   <td> 现在位置：结款页</td>
   <td>司机</td>
   <td> <select id="uid" name="uid"> 
	 <option></option>
	   <%   
	    if(null != listu){
	    	for(int i=0;i<listu.size();i++){
	    		User u = listu.get(i);
	    		 
	    		%>
	    		<option value="<%=u.getId()%>" id="<%=u.getId()%>"><%=u.getUsername() %></option>
	    		
	    		<% 
	    	}
	    }
	    
	   %>
	 
	 </select>
	 </td>
   <td><input type="submit"  value="确定"/></td>
   
   
   </tr>
   
   </table>
   
	 
   </form>
   </div> 
 
   <div class="table-list">   
   <form action="logisticschargePrint.jsp" id="myformLG"> 
   <input type="hidden" value="4" name="statues"/>
<table width="100%" cellspacing="1" id="table"> 
<tr class="dsc">
<td width="5%" class="s_list_m" align="center"><input
						type="checkbox" value="" id="allselect"
						onclick="seletall(allselect);totalInit()"></input>
					</td> 
					<td>单号</td>
<td>司机</td> 
<td>
	车牌号
</td>
<td>送货地址</td> 
<td>运费</td>
<td>垫付运费</td>
<td>送货时间</td>
<td>提交时间</td>
<td>关联送货号</td>
</tr>
	 
	 <%
	 if(null != list){
		 int count = 0;
		 int advancecount = 0 ;
		 
		 for(int i=0;i<list.size();i++){
			 LogisticsMessage ca = list.get(i);
			 String cl = "class=\"asc\"";
			 if(ca.getPid() != 0){
				 cl = "class=\"bsc\"";
			 }  
			 if(ca.getOperation() != 0){
  				 cl = "class=\"rsc\"";
  			 } 
			 count += ca.getPrice();
			 advancecount += ca.getAdvancePrice();   
			 
			 %>    
			 <tr <%=cl %> ondblclick="detail('<%=ca.getId()%>')"> 
			 <td align="center">
			 <%
			   if(ca.getStatues() == 3){
				   %>
				   
				  <input type="checkbox"
						value="<%= ca.getId()%>" name="lid" id="check_box" onclick="totalInit()"></input> 
				   <%
			   }else { 
				   %>
				   司机手机端同意
				   <%
			   }
			 %>
			       
				   </td> 
			  <td>
			  <%=ca.getId() %>
			  </td>
			 <td >  
			<%=ca.getUser().getUsername() %>
	 </td>
	<td> 
<%=ca.getCars().getNum() %>  
	</td>   
	  <td>   
	    <%=ca.getLocates()%>
	  </td> 
	  <td> 
	  <label id="p<%=ca.getId()%>"><%=ca.getPrice() %></label> 
	  
	  </td> 
	  <td> 
	  <%=ca.getAdvancePrice() %>
	  </td>
	  <td>
	  <%=ca.getSendtime() %>
	  </td>
	  <td>
	  <%=ca.getSubmittime() %>
	  </td>
	  <td>
	  <%=ca.getPid() %>
	  </td>
	  
	</tr> 
			 
			 <% 
		 }
		 
		  
		 %> 
		 <tr class="asc">
		  <td >合计</td>
		 <td colspan="4"></td>
		 <td><label id="total"> <%=count %></label></td>
		  <td><label id="total"> <%=advancecount %></label></td>
		 <td colspan="3"></td>
		 </tr>
		<tr class="asc"> 
		<td colspan="8">
		 
		</td>
		<td colspan="2"> 
		<input type="button" onclick="chager()" value="结款打印" />
		 
		</td>
		 
		</tr> 
		 
		 
		 <%
	 }
	 %>
	 
	
	
</table>

   </form>  
     </div>


</div>



</body>
</html>
