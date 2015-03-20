<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%@ include file="../searchdynamic.jsp"%>       
 <%    
 if(StringUtill.isNull(statues)){ 
	  // 网点配工 
	 statues = Order.aftersaledealcharge+"";
 }  
 
 String list = StringUtill.GetJson(UserService.getjsuser(listS ));  
 String href = "adddetailonly.jsp";  
 
 if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
	 href = "adddetail.jsp";
 }
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/aftersalecommon.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<style type="text/css">
.fixedHead {  
position:fixed;
}   
 
*{
    margin:0;
    padding:0;
}  
#table{   
    width:1400px; 
    table-layout:fixed ;   
} 
 
#th{    
    background-color:white;
    position:absolute; 
    width:1400px; 
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

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
         
<jsp:include flush="true" page="page.jsp"> 
	<jsp:param name="type" value="<%=statues%>"/> 
</jsp:include> 

<%  if(UserManager.checkPermissions(user, Group.aftersaleCharge,"q")){ %>
<div class="btn">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm('<%=AfterSale.typeupdate%>')"></input> 
&nbsp;&nbsp;&nbsp;   
</div> 
 <%} %> 
</div > 
<div style="height:100px;">
</div>

<br/>  

<%@ include file="searchOrderAll.jsp"%>  
 

<script type="text/javascript">

sort= "andate asc";
var id = ""; 
var type = "<%=Group.aftersalerepare%>";
var listuser = <%=list%>;   
var href = "<%=href%>";
$(function () { 
	 fixation();
	 initOrder(type,statues,num,page,sort,sear);
});
 
 
function change(str1,afid){

	var uid = $("#"+str1).val();
	if(uid == null || uid == ""){
		alert("请选择保养人员");
		return ; 
	}
	   
	var question = confirm("您确定要配单吗？\n");
		 if (question != "0"){ 
					$.ajax({   
				        type: "post",     
				         url: "../../AfterSaleServlet",  
				         data:"method=deal&afid="+afid+"&uid="+uid,
				         dataType: "",  
				         success: function (data) { 
				        	 if(data == 1){
				        		 initOrder(type,statues,num,page,sort,sear);
				        	 }if(data == -1){
				        		 alert("派工失败");
				        		 return ; 
				        	 }
				           },  
				         error: function (XMLHttpRequest, textStatus, errorThrown) { 
				        // alert(errorThrown); 
				            } 
				           });
				        			
			           } 
	     
}

function changeStatues(id,date){
	var question = confirm("您确定吗？\n");
	 if (question != "0"){ 
				$.ajax({      
			        type: "post",     
			         url: "../../AfterSaleServlet",    
			         data:"method=updatestatuesdeal&id="+id+"&statues="+date,
			         dataType: "",  
			         success: function (data) { 
			        		 initOrder(type,statues,num,page,sort,sear);
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			        // alert(errorThrown); 
			            } 
			           });
			        			
		           } 
}


function winconfirm(typestatues){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='radio'][name='id']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.value; 
	   				 
	   				if(str != null  &&  str != ""){
		   				   attract[i] = str; 
			   	            i++;
		   				}	
	   		}
	   	});   
            

		$.ajax({    
	        type: "post",      
	         url: "../../AfterSaleServlet",  
	         data:"method=chargematian&id="+attract.toString(),
	         dataType: "",    
	         success: function (data) { 
	        		 initOrder(type,statues,num,page,sort,sear);
	        	
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           });
	        			
           } 
}
 
function detail(id,statues){  
	winPar=window.open(href+'?id='+id+'&statues='+statues, 'detail', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');
}
 
</script>

 
<div id="wrap">
 <%@ include file="../remind.jsp"%> 
<table  cellspacing="1" id="table" >
		<tr id="th">  
		    <td align="center" width=""><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>  
			<td align="center">单号</td> 
			<td align="center">顾客姓名</td>
			<td align="center">顾客电话</td>
			<td align="center" >设备类别</td> 
			
			<td align="center" >设备型号</td> 
			<td align="center" >维修保养内容</td> 
			<td align="center" >保养类别</td> 
			<td align="center" >保养型号</td> 
			<td  align="center">安装网点</td> 
			<td  align="center">维修人员</td> 
			
			<td align="center" >批号</td> 
			<td align="center" >条码</td>
			<td align="center">地址</td> 
            
            <td align="center" >备注</td>
		</tr>
		 
	     
</table> 
     </div>

</body>
</html>
