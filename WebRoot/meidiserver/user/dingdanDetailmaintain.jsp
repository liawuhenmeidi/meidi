<%@ page language="java"  import="java.util.*,aftersale.*,category.*,group.*,user.*,utill.*,product.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%  
String id = request.getParameter("id");
String statues = request.getParameter("statues");
User user = (User)session.getAttribute("user");
  
AfterSale af = null ;  
String strorder= null;    
String listap = null;  
List<AfterSaleProduct> listasp = null ;

if(!StringUtill.isNull(id)){   
	af = AfterSaleManager.getAfterSaleID(user,id);
	strorder = StringUtill.GetJson(af); 
	listasp  = AfterSaleProductManager.getmaintain(af.getId(),statues);
	listap = StringUtill.GetJson(listasp); 
} 
  
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单详细页</title>
 
<meta name="viewport" content="initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes"/> 


<link rel="stylesheet" href="../css/songhuo.css">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<script type="text/javascript">

var id = "<%=id%>";

function change(statues,oid,type){ 
	var statue = $("#"+statues).val(); 
	if(statues == -1){
		return false;
	}  
	  
	$.ajax({     
        type: "post",      
         url: "../AfterSaleServlet", 
         data:"method="+type+"&afid="+oid+"&statues="+statue,
         dataType: "",    
         success: function (date) { 
        	//alert(date); 
        	 if(date == 1){
        		 alert("设置成功"); 
    	         window.location.href="maintain.jsp";
        		 return ; 
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
<div class="s_main">
   <jsp:include flush="true" page="../head.jsp">
     <jsp:param name="dmsn" value="" />
  </jsp:include> 
  </div>
  <div> 
<span class="qiangdan"><a href="welcom.jsp">返回</a></span>
</div>

<!--  头 单种类  -->

<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">

  <tr>
    <td width="25%" class="s_list_m">单号</td>
    <td class="s_list_m"><%=af.getPrintid() == null?"":af.getPrintid()%></td>
    
  </tr> 
  
  <tr >
    <td width="25%" class="s_list_m">产品型号</td>
    <td class="s_list_m"><%= af.getcName()%></td>
   
  </tr>
 
 <tr  >
    <td width="25%" class="s_list_m">产品类别</td>
    <td class="s_list_m"><%=	af.gettName()%></td>
   
  </tr>
   
  <%
  String time = "";  
  int stas = -1 ;
  for(int g = 0 ;g<listasp.size();g++){
	  AfterSaleProduct op = listasp.get(g);
	   time = op.getThistime();
	   stas = op.getStatues(); 
    	 %>
    	  
    	 <tr style="background:orange">
         <td width="55%" class="s_list_m">保养类别</td>
      
         <td class="s_list_m">
 
    		  <%= op.getCname()%> 
         </td>
         </tr>
    	  <tr style="background:orange">
     <td width="55%" class="s_list_m">保养型号</td>
     <td class="s_list_m">
		    
		    	<%=  op.getTname()%>
      </td>
      </tr>
    	 
    	
    <%   
   }
  %>
   
	
  <tr>
    <td class="s_list_m">顾客姓名</td>
    <td class="s_list_m"><%=af.getUname() %></td>
  </tr>
 
    
  
  <tr >
    <td class="s_list_m">电话</td> 
    
    <td><a href="tel:<%=af.getPhone() %>"></a></td>
    <!--  一键拨号  -->
  </tr>
   
    <tr >
    <td class="s_list_m">预约日期</td>
    <td><%=time %></td>
    
  </tr>
  
  <tr>
    <td class="s_list_m">送货地点</td>
    <td class="s_list_m"><%=af.getLocation() %></td>
  </tr>
  
   <tr>
    <td class="s_list_m"> 状态</td>
    <td class="s_list_m">
    <%
   
    if(stas == 0 ){
 
    %>
     <select class = "category" name="category"  id="songh<%=af.getId() %>" >
     
      <option value="" >&nbsp;&nbsp;&nbsp;&nbsp;</option> 
      <option value="1" >已处理 </option>  
      <option value="2" >无法处理驳回 </option> 
      </select>   
     <input type="button" onclick="change('songh<%=af.getId() %>','<%=af.getId() %>','maintain')"  value="确定"/>

   
   <%
    }
		%>
     
    </td>
 
   <tr >
    <td class="s_list_m">备注</td>
    <td align="left" class="s_list_m"><%=af.getDetail() %></td>
    
  </tr>  
     
</table>

  <br>
  <br> 
 
<!--  zong end  -->
</div>

</body>
</html>