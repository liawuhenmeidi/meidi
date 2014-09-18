<%@ page language="java"  import="java.util.*,utill.*,order.*;"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%
request.setCharacterEncoding("utf-8");
String message = "";  
int count = 0 ;

String pageNum = request.getParameter("page");
int type = Integer.valueOf(request.getParameter("type"));
String href = request.getParameter("href");
//String sear = (String)session.getAttribute("sear");
String numb = (String)session.getAttribute("numb"); 
String sort = (String)session.getAttribute("sort");
  
if(request.getParameter("count") != null && !"".equals(request.getParameter("count"))){
	count = Integer.valueOf(request.getParameter("count"));
} 
   

if(StringUtill.isNull(pageNum)){
	pageNum = "1";  
}
if(StringUtill.isNull(numb)){
	numb = "100";
}

if(StringUtill.isNull(sort)){
	sort = "id";  
}

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

var id = "";
var pages = "<%=Page%>";   
var num = "<%=num%>";
var search = new Array();
var href = "<%=href %>";
var sort = "<%=sort%>" ;

$(function () {
	 $("select[id='numb'] option[value='"+num+"']").attr("selected","selected");
	 $("select[id='sort'] option[value='"+sort+"']").attr("selected","selected");

 
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
     	 <option value="100">100</option> 
     	 <option value="200">200</option>
     	 <option value="500">500</option>
     	 <option value="1000">1000</option>
     	</select>     
  &nbsp; &nbsp; &nbsp; &nbsp; 
   
     <a href="<%=href %>?page=1&numb=<%=num %>">首页</a>    
     <a href="<%=href %>?page=<%=Page-1%>">上一页</a>
     <a href="<%=href %>?page=<%=Page+1%>">下一页</a>
     <a href="<%=href %>?page=<%=count/num+1%>">尾页</a>   
       &nbsp; &nbsp; &nbsp; &nbsp; 
   
      第    
     <input type="text" size="5" name="username" value="<%=Page%>"id ="page"/>    
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


