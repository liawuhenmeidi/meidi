<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
 
   
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安装网点结款页</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../css/alert.css" />
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

#table{  
    width:1100px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:1100px;
    height:30px;
    top:0;
    left:0;
}
#wrap{ 
   
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:400px;
}

</style>
</head>
<body> 
<%@ include file="searchdynamic.jsp"%> 
<!--   头部开始   -->
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/jquery.alerts.js"></script>



<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="page.jsp">
    
	<jsp:param name="type" value="<%=Order.over %>"/>
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div> 

<script type="text/javascript">
var id = "";
var type = "<%=Group.dealSend%>";
sort = "submittime , phone1 ,locateDetail  asc" ;
$(function () { 
	 fixation(); 
	 initOrder(type,statues,num,page,sort,sear);
}); 
  

 
function winconfirm(){
	var question = confirm("你确认要执行此操作吗？");	
	if (question != "0"){
		var attract = new Array();
		var i = 0;
		
		$("input[type='checkbox'][id='check_box']").each(function(){          
	   		if($(this).attr("checked")){
	   				var str = this.value;
	   				if(str != null && str != ""){
		   				   attract[i] = str;
			   	            i++;
		   				}
	   		}
	   	});
		
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",   
	         data:"method=orderover&id="+attract.toString(),
	         dataType: "", 
	         success: function (data) {
	        	 if(data == -1){
	        		 alert("执行失败") ;
	        		 return ;
	        	 }else if (data > 0){
	        		  alert("执行成功");
	        		  window.location.href="dingdanover.jsp";
	        	 };	  
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("执行失败"); 
	            } 
	           });
		
		
	}
}

function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}

function amortization(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:1000px;dialogTop:0px;dialogLeft:center;scroll:no');
} 
  
 function save(){
	 var branch = new Array();
	 var mess = new Array();
	 var attract = new Array();
	 //if(-1 != num){
	//	 alert("请选择行数为所有");
	//	 return false ;
	// }
	 $("input[name='dealsendid']").each(function(){
			var dealsend = $(this).val();
			if($.inArray(dealsend,branch) == -1){
				branch.push(dealsend);
			}
        
	     });
	 if(branch.length >1){
		 alert("您不能保存，请选择具体安装网点");
		 return ;
	 }
	 
	 $("input[name='saleresut']").each(function(){
			var id = $(this).val();
			var price = $("#"+id+"left").val();
			mess.push(id+"-"+price+"-"+0);
	     }); 
	// alert(mess);
	 //jPrompt('请输入keleyi.com或者其他:', 'keleyi.com(预填值)', 'Prompt对话框', function(r) {
		 //   if( r ) alert('You entered ' + r);
	//	});
	 var str = window.prompt("请输入文件名称","") ;
	 
	 if(str == null || str == ""){
		 alert("文件名称不能为空");
		 return false;
	 }

		
	 $("input[name ='orderid']").each(function(){          
			var str = this.value;
			if(str != null && str != ""){
				attract.push(str);
				}
	   	});
	  
	 $.ajax({  
	        type: "post", 
	         url: "server.jsp",   
	         data:"method=dealsendcharge&id="+attract.toString()+"&branchid="+branch.toString()+"&name="+str+"&message="+mess,
	         dataType: "", 
	         success: function (data) {
	        	 initOrder(type,statues,num,page,sort,sear);
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("执行失败"); 
	            } 
	           });
	 
  }

 function addcount(){
	 var totalcount = 0 ;
	 $("input[name='saleresut']").each(function(){
			var id = $(this).val();
			var price = $("#"+id+"left").val();
			if(!isNaN(price)){
				 totalcount += price*1;
		      } 
			
	     });
	 $("#addcount").html(totalcount);
 }
 
 
</script>

<div class="btn">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" name="dosubmit" value="确认" onclick="winconfirm()"></input> 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" value="设置标准" onclick="amortization('salesmoney.jsp')" ></input>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="button" value="保存" onclick="save()" ></input>  
</div>

</div > 
<div style=" height:130px;">
</div>
<br/> 
<%@ include file="searchOrderAll.jsp"%>
 
  
<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th" >  
			<td align="center" width=""><input type="checkbox" name="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>
			<td align="center">单号</td>
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
		</tr>

</table> 

     </div>

</body>
</html>
