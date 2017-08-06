<%@ page language="java" import="java.util.*,category.*,utill.*,branch.*,branchtype.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String pid = request.getParameter("pid");
String id = request.getParameter("id"); 
String type = request.getParameter("type");
 
HashMap<String,List<User>> map = UserService.getMapBranchid();
System.out.println(StringUtill.GetJson(map.get(id)));
Branch branchold = null; 
String branchoidname = "";
String message = null;
String branchids = null;
String[] permission = null ;
String topYardMust = "0"; 
Set<String> perm = null ;
String[] branchid = null;  
if(!StringUtill.isNull(id)){ 
	branchold= BranchManager.getLocatebyid(id);
	branchoidname = branchold.getLocateName();
	message = branchold.getMessage();
	topYardMust = branchold.getTopYardMust()+"";  
	branchids = branchold.getBranchids();
	if(!StringUtill.isNull(message)){
		//perm = StringUtill.GetSetByObject(message);
		System.out.println(perm);
		//permission = message.split("_");
	}
}  
 

 
//String json = StringUtill.GetJson(message);

String action = request.getParameter("action");
if("add".equals(action)){
	String branchname = request.getParameter("locate"); 
	//System.out.println(branchname);
	permission = request.getParameterValues("permission");
	branchid = request.getParameterValues("branchid");
	topYardMust = request.getParameter("topYardMust");
    String messagenew = "[";
    String branchidsnew = "";
    if(permission != null ){     
		for(int i = 0;i<permission.length;i++){
			if(!StringUtill.isNull(permission[i])){ 
				 String minnum = request.getParameter("min"+permission[i]);
				 String maxnum = request.getParameter("max"+permission[i]);
				 messagenew += "{\""+permission[i]+"\":{\"min\":\""+minnum+"\",\"max\":\""+maxnum+"\"}},";  
				//messagenew += permission[i]+"_";   
			}       
		}      
		   
		messagenew = messagenew.substring(0, messagenew.length() -1);
		messagenew +="]"; 
    }
    if(branchid != null ){   
  		for(int i = 0;i<branchid.length;i++){
  			if(!StringUtill.isNull(branchid[i])){
  				branchidsnew += branchid[i]+"_";
  			} 
  		}  
      } 
	Branch branch = new Branch(); 
	if(!StringUtill.isNull(id)){
		branch.setId(Integer.valueOf(id)); 
	}
	  
	branch.setLocateName(branchname);
	branch.setPid(Integer.valueOf(pid));
	branch.setMessage(messagenew); 
	branch.setBranchids(branchidsnew);  
	branch.setTopYardMust(Integer.valueOf(topYardMust));  
	// System.out.println(messagenew);                       
	BranchManager.save(branch);  
	response.sendRedirect("branch1.jsp?id="+pid); 
} 

BranchType branch = BranchTypeManager.getLocate(Integer.valueOf(pid)); 

%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
  
<script type="text/javascript">
var pid = "<%=pid%>"; 
var message = '<%=message%>';     
//var products = '[{"pos":{"minpos":"12","maxpos":"12"}},{"checked":{"minchecked":"12","maxchecked":"12"}}]';
var topYardMust = '<%=topYardMust%>' ;    
  
var jsons = "<%=branchids%>";  
var branchid = new Array();    
$(document).ready(function(){ 
	/*
	$.each(products,function(key,val){
		alert(key);
		alert(val);
	}); 
	*/ 
	//alert(products.length); 
	try{
		var products = $.parseJSON(message);
		 for(var i=0;i<products.length;i++){
			 for(var key in products[i]){   
			       //alert(key+':'+products[i][key]);  
			       $("#"+key).attr("checked","checked");
			       
			       $("#min"+key).val(products[i][key]["min"]);
			       $("#max"+key).val(products[i][key]["max"]);
			   }   
			  
		 } 
	}catch(e){   
		var pro = message.split("_"); 
		//alert(pro.length);
		for(var i=0;i<pro.length;i++){    
		       //alert(key+':'+products[i][key]);
		       //alert("#"+pro[i]); 
		       $("#"+pro[i]).attr("checked","checked");
		       
		       
		   } 
	}
	
	branchid = jsons.split("_"); 
	for(var i=0;i<branchid.length;i++){
		 $("#"+branchid[i]).attr("checked","checked");
	 }
	
	
  $("#locate").focus(function(){
      $("#locate").css("background-color","#FFFFCC");
  }); 
   
  $("#locate").blur(function(){
	   changes();
   });  
//alert(topYardMust);
  $("#topYardMust"+topYardMust).attr("checked","checked");
  
   });    

function changes(){
	var str1 = $("#locate").val();
	if(str1 == null || str1 == ""){
		alert("不能为空");
	}  
	$.ajax({ 
        type: "post", 
         url: "server.jsp",
         data:"method=branchis&id="+str1+"&pid="+pid,
         dataType: "", 
         success: function (data) {
        	
        	 if(data == "true" || data == true ){
        		var question = confirm("门店已存在？是否继续?");	
     			if (question == "0"){   
     				$("#locate").focus();
     				return  ;
     			}  

        	 }
        	 
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}
</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->
   
 <!--       -->     
 
   <div class="weizhi_head">现在位置：<%=branch.getName()%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="branch1.jsp?id=<%=pid %>"><font style="color:blue;font-size:20px;" >返回</font></a></div>     
        
     <div> 
      <table width=80%  cellspacing="1" id="table" >
      <%  if(null != map){
    	      List<User> list = map.get(id);
    	      if(null != list){
    	    	  for(int i=0;i<list.size();i++){
    	    		  User u = list.get(i);
    	    		  %>
    	    		  <tr class="asc">
    	    		      <td align="center"> <%=u.getUsername() %></td>
    	    		      <td align="center"> <%=u.getPhone() %></td>
    	    		  </tr>
    	    		  <%
    	    	  }
    	      }
    	   
      } %>
      
      </table>
      <br/>
      
      <% if(!"1".equals(type)){ %>
      
     <form action="branch1add.jsp"  method = "post"  onsubmit="return checkedd()">
      <input type="hidden" name="action" value="add"/>
      <input type="hidden" name="pid" value="<%=pid%>"/> 
       <input type="hidden" name="id" value="<%=id%>"/> 
     <table cellspacing="1" id="table" width=80%>
       <tr class="asc">  
       <td align="center"> 门店名称<span style="color:red">*</span></td>
       <td align="center">     <input type="text"  id="locate" value="<%=branchoidname %>" name="locate" /> <br /> </td>
       </tr>
       <tr class="asc">
       <td align="center"> 门店报装单需要信息<span style="color:red">*</span>:  </td>
       <td align="center"> 
         <table width=100%>
          <tr><td><input type="checkbox" value="pos" name = "permission" id="pos" />&nbsp;pos(厂送)单号</td>
          <td> 
           <input type="text" id="minpos" name="minpos" style="width: 50px" placeholder="最小位数"/>--<input type="text" id="maxpos" name="maxpos" style="width: 50px" placeholder="最大位数"/>
          </td> 
  
          </tr> 
         <tr><td><input type="checkbox" value="sailId" name = "permission" id="sailId" />&nbsp;OMS订单号</td>
         
           <td> <input type="text" id="minsailId" name="minsailId" style="width: 50px" placeholder="最小位数"/>--<input type="text" id="maxsailId" name="maxsailId" style="width: 50px" placeholder="最大位数"/>  </td> 
         </tr> 
         <tr><td><input type="checkbox" value="checked" name = "permission" id="checked" />&nbsp;验证码(联保单)</td>
          <td><input type="text" id="minchecked" name="minchecked" style="width: 50px" placeholder="最小位数"/>--<input type="text" id="maxchecked" name="maxchecked" style="width: 50px" placeholder="最大位数"/> </td> 
         </tr>
         </table>  
       
       </td>
       </tr>
       <tr class="asc">
       <td align="center">  
         是否必须顶码上报： 
       </td> 
       <td align="center"> 
       <table width=100%> 
       <tr><td>    必须：<input type="radio" value=1 name="topYardMust" id="topYardMust1"/></td></tr>
       <tr><td>     非必须： <input type="radio" value=0 name="topYardMust" id="topYardMust0"/></td></tr>
       </table> 
      
    
       </td>
       </tr> 
       
       <tr class="asc">
       <td align="center">  
         允许门店可查看库存：
       </td>
       <td align="center">  
         <table width=100%>
        <% List<Branch> list = BranchManager.getLocate(1);
        for(int i =0 ;i<list.size();i++){
        	Branch b = list.get(i);
        %>        
        <tr><td>  
        <input type="checkbox" value="<%=b.getId() %>" name = "branchid" id="<%=b.getId() %>" /><%=b.getLocateName() %>
        </td></tr>
       
        <%
        }
        %>
     </table>
       </td>
       </tr> 
       
       
       <tr class="asc"> 
       <td colspan="2" align="center"> 
        <% if(UserManager.checkPermissions(user, Group.branch,"w")){%>      
      <input type="submit" value="提  交" />
      <%}  %>
       </td>
       </tr>
       
       
     </table>

   
 
     
 </form>
 <%} %>
     </div>


</body>
</html>
