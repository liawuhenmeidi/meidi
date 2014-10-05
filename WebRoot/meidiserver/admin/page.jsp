<%@ page language="java"  import="java.util.*,utill.*,order.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
int type = Integer.valueOf(request.getParameter("type"));
String message = "";
String href = request.getParameter("href");
int count = 0 ;

if(request.getParameter("count") != null && !"".equals(request.getParameter("count"))){
	count = Integer.valueOf(request.getParameter("count"));
} 

String pageNum = request.getParameter("page");
String numb = request.getParameter("numb");  
String sort = request.getParameter("sort");  
String sear = request.getParameter("sear");

//System.out.println("page"+sear);

int Page = Integer.valueOf(pageNum);
int num = Integer.valueOf(numb);
 


if(Order.orderDispatching == type){
	message = "文员派工页";
}else if(Order.charge == type){
	message = "确认厂送票已结款";
}else if(Order.come == type){
	message = "确认厂送票已回";
}else if(Order.go == type){
	message = "确认厂送票已消";
}else if(Order.dingma == type){ 
	message = "调账确认页";
}else if(Order.over == type){
	message = "安装单位结款页";
}else if(Order.orderPrint == type){
	message = "文员打印页";
}else if(Order.serach == type){
	message = "查看订单页";  
}else if( Order.porderDispatching== type){ 
	message = "工长派工"; 
}else if( Order.pinstall== type){ 
	message = "安装派工"; 
}else if( Order.pinstallprintln== type){ 
	message = "安装打印"; 
}else if(Order.pserach == type){
	message = "工长查询"; 
}else if(Order.porderPrint == type){
	message = "工长打印";  
}else if(Order.callback == type){
	message = "客户回访";  
}else if(Order.pcharge == type){
	message = "安装结款";   
}else if(Order.pchargepaisong == type){
	message = "送货结款"; 
}else if(Order.deliveryStatuesTuihuo == type){
	message = "退货订单页"; 
}else if(Order.chargeall == type){
	message = "送货安装结款"; 
}





String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/meidiserver/";

%>
<script type="text/javascript">

var num = "<%=num%>";
var sort = "<%=sort%>" ;
var href = "<%=href%>";
var page = "<%=pageNum%>";
var sear = "<%=sear%>";

$(function () { 
	
	$("select[id='numb'] option[value='"+num+"']").attr("selected","selected");
	$("select[id='sort'] option[value='"+sort+"']").attr("selected","selected");
	
	$("#page").blur(function(){
		 page = $("#page").val();
		
		 window.location.href=href+"?page="+page+"&numb="+num+"&sort="+sort+"&sear="+sear;
	 });

	 $("#numb").change(function(){
		 num = ($("#numb").children('option:selected').val());
		// alert(num);
		 window.location.href=href+"?page="+page+"&numb="+num+"&sort="+sort+"&sear="+sear;
	 }); 
	   
	 $("#sort").change(function(){
		 sort = ($("#sort").children('option:selected').val()); 
		 window.location.href=href+"?page="+page+"&numb="+num+"&sort="+sort+"&sear="+sear;
	 }); 
}); 



</script>

  <div class="weizhi_head">现在位置：<%=message %> 
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <a href="<%=basePath %>Print"><font style="color:red;font-size:20px;" >导出数据</font> </a>
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <font style="color:red;font-size:20px;" >导入数据</font>
  </div>        
   
   <div class="btn">
    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;行数
     	<select class = "category" name="category"  id="numb">
     	 <option value="10">10</option> 
     	 <option value="20">20</option>
     	 <option value="50">50</option>
     	 <option value="100">100</option>
     	 <option value="-1">所有</option>
     	</select>     
  &nbsp; &nbsp; &nbsp; &nbsp;  
    
     <a href="<%=href %>?page=1&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">首页</a>     
     <a href="<%=href %>?page=<%=Page-1%>&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">上一页</a>
     <a href="<%=href %>?page=<%=Page+1%>&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">下一页</a>
     <a href="<%=href %>?page=<%=count/num+1%>&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">尾页</a>   
       &nbsp; &nbsp; &nbsp; &nbsp; 
   
      第    
     <input type="text" size="5" name="username" value="<%=Page%>"  id="page"/>    
        页
        
    共
    <%=count %> 
    
   条记录  
&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
     按 
       <select class ="" name=""  id="sort" >  
       <option value="andate asc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
     	 <option value="andate asc">安装日期</option> 
     	 <option value="saledate asc">开票日期 </option>  
     	</select>
        排序 <br />      
   <br/>         
</div>


