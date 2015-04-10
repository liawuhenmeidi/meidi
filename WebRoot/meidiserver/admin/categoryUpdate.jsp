<%@ page language="java" import="java.util.*,category.*,group.*,branchtype.*,user.*,utill.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
String action = request.getParameter("action");
  
List<BranchType> list =  BranchTypeManager. getLocatelist(BranchType.sale);

if("add".equals(action)){
	String categoryName = request.getParameter("name");
	String time = request.getParameter("time");
	String id = request.getParameter("id");
	String[] sales = request.getParameterValues("sales");
	
	if(!StringUtill.isNull(categoryName)){
		System.out.println("category");
		Category c = new Category();
		c.setId(Integer.valueOf(id));
		c.setName(categoryName );
		c.setTime(time); 
		c.setSales(StringUtill.getStr(sales, "_")); 
		boolean  me = CategoryManager.update(c); 
		if(me){
			response.sendRedirect("category.jsp");
			return ;
		}
	}
}
String sales = "";
String id = request.getParameter("id"); 
Category category = CategoryManager.getCategory(id);
sales = category.getSales();
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>产品管理</title>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
  <script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
  <link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
  <script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
  <script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
  <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
  
<script type="text/javascript">
var sales = "<%=sales%>";
$(function () {
	init();
	
	
    var opt = { };		  
		     $("#name").focus(function(){
			    $("#name").css("background-color","#FFFFCC");
			  });
			  $("#name").blur(function(){
			    $("#name").css("background-color","#D6D6FF");
			    var categoryName = $("#name").val();
			    $.ajax({ 
			        type:"post", 
			         url:"server.jsp",
			         //data:"method=list_pic&page="+pageCount,
			         data:"method=category_add&categoryName="+categoryName,
			         dataType: "", 
			         success: function (data) {
			        	 if("true" == data){
			        		 alert("已存在相同的类别名称");
			        		 $("#name").focus();
					          return ;
			        	 }
			           }, 
			          error: function (XMLHttpRequest, textStatus, errorThrown) { 
			        	  //alert(123);
			            } 
			           });
			  
			  });
 });
  
 function init(){
	 if("" != sales && null != sales){
		 sales = sales.split("_");
		 for(var i=0;i<sales.length;i++){
			 $("input[value="+sales[i]+"]").attr("checked",'checked');
		 }
	 } 
 }
function checkedd(){
	 var name = $("#name").val();
	 var time = $("#time").val();
	 
	 if(name == "" || name == null || name == "null"){
		 alert("类别名称不能为空");
		 return false;
	 }
	 
     if(time == "" || time == null || time == "null"){
		 alert("预约安装截止日期不能为空");
		 return false;
	 }

	 

	 return true ;
 }
 
</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

<div class="main"> 

    <div class="weizhi_head">现在位置：添加产品类别</div>     
     
     
     <form action="categoryUpdate.jsp"  method = "post"  onsubmit="return checkedd()">
      <input type="hidden" name="action" value="add"/>
      <input type="hidden" name="id" value="<%=category.getId()%>"/>
      <table id="table" width="80%">
      <tr class="asc"> 
         <td align="center">类&nbsp;&nbsp;别&nbsp;&nbsp;名&nbsp;&nbsp;称<span style="color:red">*</span></td>
         <td align="center"> <input type="text"  id="name" value="<%=category.getName() %>" name="name" /> <br /> </td>
      </tr>
      <tr class="asc"> 
         <td align="center"> 类别安装截止日期<span style="color:red">*</span>:</td>
         <td align="center"><input type="text"  id="time" value="<%=category.getTime() %>" name="time" />天 <br /></td>
      </tr>
      <tr class="asc"> 
         <td align="center">产品类别</td>
         <td align="center"> 
          <% if(category.getPtype() == 0 ) { 
        %> 
                                 销售产品 <input type="radio" name="ptype"  value=0  checked="checked"/> 
                                   维修配件    <input type="radio" name="ptype" value = 1 />
          <%	  
          } else { 
          %>        
                                                 销售产品 <input type="radio" name="ptype"  value=0  />
                                                维修配件    <input type="radio" name="ptype" value = 1 checked="checked"/>
          <% }%>
                                        
                                        
               </td>
      </tr>
      <tr class="asc">
       <td align="center">销售卖场</td>
       <td>
         <table>
          <tr>
          <% if(null != list){
        	  for(int i=0;i<list.size();i++){
        		  BranchType bt = list.get(i);
        		  
        		 %> 
        		 <td> 
            <input type="checkbox" name="sales" value="<%=bt.getId()%>"/><%=bt.getName()%>
           </td>
        		 <%
        	  }
          } %>
           
          
          </tr>
         
         </table>
       
       
       </td>
       
      </tr>
      </table>
      

                  
         
                 
      
               
      <input type="submit" value="提  交" />

 
 </form>

</div>  
</body>
</html>
