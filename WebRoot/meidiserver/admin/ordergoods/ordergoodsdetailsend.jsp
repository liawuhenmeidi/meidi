<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>
    
<%         

OrderGoodsAll oa = null;  
List<OrderGoods> list = null; 
String id = request.getParameter("id"); 
String statues = request.getParameter("statues");
String type = request.getParameter("type");
String branchname = ""; 
String remark = ""; 
String message = "修改实收数量"; 

 
if(!StringUtill.isNull(id)){ 
	if((OrderMessage.billing+"").equals(type)){   
		oa = OrderGoodsAllManager.getOrderGoodsAllBySendid(user,id,statues); 
	}else { 
		oa = OrderGoodsAllManager.getOrderGoodsAllByid(user,id,Integer.valueOf(statues),type);
	} 
	
	branchname = oa.getOm().getBranchname();
	remark = oa.getOm().getRemark(); 
	list = oa.getList(); 
} 
 
String json = StringUtill.GetJson(list); 
// System.out.println(json); 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title> 
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css"/> 
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
 
<script type="text/javascript">
 
  var jsons = <%=json%>;
 //alert(listall);
   
 var row = 10;   
 var rows = new Array();
 var count = 1 ; 
  
 $(function () { 

	 var num = jsons.length;
	 row = num ;  
	 addrowinti();
 });  
    
 function addrowinti(){
	//  alert(rows); 
	 for(var i=0;i<row;i++){  
		 if($.inArray(i,rows) == -1){ 
			 addrow(i); 
		 }  
		    
	} 
	 
	 addcount();
 }   
      
 function changecolor(obj){
	 if($(obj).attr("checked")){
		 $("#"+$(obj).val()).attr("class","dsc");
	 }else {
		 $("#"+$(obj).val()).attr("class","asc");
	 }
	 ;
 }
 
 function addcount(){ 
	 var totalcount = 0 ;
	  
	 for(var i=0;i<rows.length;i++){
		// alert(1);  
		 var countrow = $("#orderproductNum"+rows[i]).text();
		// alert(countrow);
		 if(!isNaN(countrow)){  
			 totalcount += countrow*1; 
		 }else {   
			 alert("请输入数字");  
			 $("#orderproductNum"+rows[i]).val("");
			 return ; 
		 }
	 } 
	 if(rows.length == 10){
		 row = 15;
		 addrowinti();
	 }
	 $("#addcount").html(totalcount);
 }
  
 function addrow(row){ 
	  rows.push(row);
	  var json = jsons[row]; 
	  $("#uuid").text(json.uuid);
	   
	  var cl = 'class="asc"';
	  
	  var str = '<tr '+cl+' id="'+json.id+'">' +    
	    
	     ' <td align=center   >'+(row*1+1*1)*1+'</td> '+  
	     ' <td  align=center >'+json.tname+'</td> ' +     
	     ' <td  align=center >'+json.statuesName+'</td> ' +    
	     '<td align=center >'+json.realnum+'</td> '+ 
	     ' <td align=center   ><input type="checkbox" name="ogid"  value="'+json.id+'" id="check_box" onclick="changecolor(this)"></td> '
	     ;  
	         
	     if(json.statues == 6 || json.statues == 7 || json.statues == 8 || json.statues == 9  || json.statues == 10){ 
	    	//alert(json.statues);  
	    	 str += '<td align=center >'+ 
	    	 '</td> '+  
	    		 '<td align=center  bgcolor="red">'+   
			    // '<input type="hidden" name="id" value='+json.id+'>'+
			     '<input type="text" name="returnrealsendnum'+json.id+'" id="returnrealsendnum'+json.id+'" value='+json.realnum+'>'+
			     '</td> ';  
	     }else if(json.statues == 4){   
	    	 str += '<td align=center >'+ 
	    		 '<input type="text" name="realsendnum'+json.id+'" id="realsendnum'+json.id+'" value='+json.realnum+'>'+
		     '</td> ' +  
		     '<td align=center  bgcolor="red" >'+     
			    // '<input type="hidden" name="id" value='+json.id+'>'+
			     '<input type="text" name="returnrealsendnum'+json.id+'" id="returnrealsendnum'+json.id+'" value='+json.realnum+'>'+
			     '</td> ' ;  
	     }else {  
	    	 str += '<td align=center >'+
	    	 '<input type="text" name="realsendnum'+json.id+'" id="realsendnum'+json.id+'" value='+json.realnum+'>'+
		     '</td> '+
		     '<td align=center >'+ 
	    	 '</td> ';
	     }
	    // '<input type="hidden" name="id" value='+json.id+'>'+
	     
		      
	     str += '</tr>';  
	              
	$("#Ntable").append(str); 
	
 }  
             
  
 function check(){ 
	 var attract = new Array(); 
	 var j = 0 ;
	 $("input[type='checkbox'][name='ogid']").each(function() {
			if ($(this).attr("checked")) {
				var str = this.value;
  
				if (str != null && str != "") {
					 attract[j] = str; 
					  j++; 
				}
			} 
		});
	   
	 if(attract.length <1){
		 return false;
	 }
	  
	  
	 for(var i=0;i<row;i++){
		 var json = jsons[i];  
		 var realnum = json.realnum;
		 var realsendnum = $("#realsendnum"+json.id).val();
		 if(realsendnum == "" || realsendnum == null){
			 realsendnum = 0 ; 
		 } 
		 
		if(isNaN(realsendnum)){
			alert("实际发货数必须是数字");
			return false;
		}else { 
			if(realsendnum <0 ){
				 alert("实际发货数不能小于0");
				 return false ; 
			 }else if(realnum < realsendnum){
				 alert("实际发货数不能大于订单数");
				 return false ;
			 }
		}
  
	 }
 } 
 
 
 
</script> 
</head>

<body>
 
<div class="main">  
   <div class="weizhi_head">现在位置：<%=message  %></div>
   <div class="main_r_tianjia">
   <ul>                                                                                                          
     <li><a href="javascript:history.go(-1);">返回</a></li>
     </ul>   
   </div>       
    
   
    <form action="../../user/OrderGoodsServlet"  method = "post"  onsubmit="return check()">
       <input type="hidden" name="method" value="updaterealsendnum"/>   
       <input type="hidden" name="token" value="<%=token%>"/>   
       <input type="hidden" name="id" id="id" value="<%=id%>"/>    
        <input type="hidden" name="statues" id="statues" value="<%=statues%>"/> 
        <input type="hidden" name="type" id="type" value="<%=type%>"/>  
                
      <table style="width:100% " > 
       <tr class="asc"> 
       <td  align=center>
                          门店 
        </td>   
        <td  align=center> 
        <%=branchname %> 
        </td> 
        <td  align=center>  
         单号：    <%=oa.getOm().getId()%> 
        </td> 
       <td align=center>
       日期：<%=oa.getOm().getSubmittime() %>
       </td> 
        </tr>      
       <tr class="asc">    
       <td colspan=4 align=center>  
        <table   style="width:100% "  id="Ntable" >
         <tr class="dsc">     
           
           <td align=center width="5%"   >编号</td> 
           
           <td align=center width="20%" > 产品型号</td> 
            <td align=center width="20%" > 状态</td> 
           <td align=center width="20%"> 订货数</td> 
           <td align=center width="5%" ><input 
						type="checkbox" value="" id="allselect"
						onclick="seletall(allselect)"></input></td> 
           <td align=center width="20%">实收货数量</td> 
           <td align=center width="20%" bgcolor="red">退货数量</td> 
          </tr> 
        </table> 
       </td>
        
       </tr>
  
       <tr class="asc" >
       <!--  
       <td align=center colspan=2>
       <font style="color:blue;font-size:20px;" >合计:</font>
       <font style="color:blue;font-size:20px;" >
             <span style="color:red;font-size:20px;" id="addcount" ></span>
             (台)
        </font>    
       </td> 
          -->                   
       <td align=center colspan=4>  备注：<input id="remark" name="remark" value="<%=remark%>"></input></td>
       </tr>
        
        <tr class="asc">
       <td align=center colspan=4>
          
        <input type="submit" id="submit" value="提交" />
       </td> 
       
       </tr>
       
      </table>
      </form>
     </div>


</body>
</html>
