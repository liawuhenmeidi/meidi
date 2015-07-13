<%@ page language="java" import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
        
<%           
User user = (User)session.getAttribute("user"); 
String id = request.getParameter("id");
String method = request.getParameter("method"); 
boolean flag = false ; 
if("savelocate".equals(method)){ 
	String locate = request.getParameter("locate"); 
	LogisticsMessageManager.updateLocate(user, locate, id);
}else if("savestatues".equals(method)){
	LogisticsMessageManager.updatestatues(user, id);
	response.sendRedirect("logistic.jsp");
}   

LogisticsMessage lm  = LogisticsMessageManager.getByid(Integer.valueOf(id));

int statues = lm.getStatues();

          
%>  
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=yes"/> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />   
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<link rel="stylesheet" type="text/css" href="../../css/songhuo.css"/>  
<style type="text/css">
		body{
			margin:0; 
			height:100%;
			width:100%;
			position:absolute;
		}
		#mapContainer{
			position: absolute;
			top:0;
			left: 0;
			right:0;
			bottom:0;
		}
		#tip{
			height:90px;
			background-color:#fff;
			padding-left:10px;
			padding-right:10px; 
			position:absolute; 
			font-size:12px;
			right:10px;
			bottom:20px;
			border-radius:3px;
			border:1px solid #ccc;
			line-height: 20px;
		}
		
		#tip>div:first-child{
			height:40px;
		}
		
		#tip input[type='button']{
			margin:10px;
			background-color: #0D9BF2;
			height:30px;
			text-align:center;
			line-height:30px;
			color:#fff;
			font-size:12px;
			border-radius:3px;
			outline: none;
			border:0;
		}
		#info{margin-top:5px;}
	</style>
</head> 
<body> 
   
<div id="mapContainer"  style="display:none"></div>
<div class="main">  
<div class="s_main_tit"><span class="qiangdan"><a href="logistic.jsp">返回</a></span>
<% if(statues == 0) {%>
<span class="qiangdan"><a href="javascript:savestatues()">送货已完成</a></span>
<% }%>
</div>
     <div>               
     <form method = "post"  >  
      <table style="width:100% "> 
      <tr class="asc">
<td align="center">  
	车牌号  
</td>   
<td align="center"> 
<%=lm.getCars().getNum() %>  
	</td>   
</tr>
<tr class="asc"> 
<td align="center"> 起始地址</td> 
<td align="center">     
	 <%=lm.getStartLocate() %> 
	  </td> 
</tr>
<tr class="asc"> 
<td align="center"> 送货地址</td> 
<td align="center">    
	 <%=lm.getLocates() %> 
	  </td>
</tr><tr class="asc">
<td align="center"> 价格</td>
<td align="center">  
	  <%=lm.getPrice() %>
	  </td>  
</tr><tr class="asc">
<td align="center"> 送货时间</td>
 <td align="center"> 
	  <%=lm.getSendtime() %>
	  </td> 
 </tr>    
 <tr class="asc">
 <td align="center">行车记录</td>
 <td align="center"> 
 <table>
	 
 <%  
 String locateM = lm.getLocateMessage();
 if(!StringUtill.isNull(locateM)){
	 String[] locateMs = locateM.split(",");
	 for(int i=1;i<locateMs.length;i++){
		 String locate = locateMs[i];  
		 String[] locates = locate.split("::");
		 String time = locates[0]; 
		 String l = locates[1];
		 
		 %>
		 <tr><td><%= time%></td><td><%=l %></td></tr>
		 
		 <%
	 }
	 
	 
 }
 
 
 %>
 </table>
 </td>  
 </tr>
 <% if(statues == 0){ %>
    	<tr class="asc">  
	<td colspan="2" align="center"> 
	<input type="button" value="中转站记录地点" onclick="showLocationInfo()"/>
	</td>   
	</tr>
	
<% }%>			 
   
     
      </table>
      
 </form>
     </div>

</div>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=9d0cf651d73e4019e3968c9e915fe4a7"></script>

<script type="text/javascript">
    var address ;
 function search(statues){
		window.location.href="logistic.jsp?statues="+statues;
	}

 	var toolBar, locationInfo;
	//初始化地图对象，加载地图
	map = new AMap.Map("mapContainer", {
		resizeEnable: false
	}); 
	//地图中添加地图操作ToolBar插件
	map.plugin(["AMap.ToolBar"],function(){		
		toolBar = new AMap.ToolBar(); //设置地位标记为自定义标记
		map.addControl(toolBar);		 
		AMap.event.addListener(toolBar,'location',function callback(e){ 	   
			locationInfo = e.lnglat;     			 
		}); 
	});
		     
	toolBar.doLocation();  
	init();   
 	  
	function init(){ 
		   var MGeocoder;
		    //加载地理编码插件 
		    AMap.service(["AMap.Geocoder"], function() {        
		        MGeocoder = new AMap.Geocoder({ 
		            radius: 1000,
		            extensions: "all"
		        }); 
		        //逆地理编码 
		        MGeocoder.getAddress(locationInfo, function(status, result){
		        	if(status === 'complete' && result.info === 'OK'){
		        		geocoder_CallBack(result);
		        	} 
		        });
		    }); 
		       
		     
			function geocoder_CallBack(data) { 
				address = data.regeocode.formattedAddress;
				window.location.href="logisticDetail.jsp?method=savelocate&locate="+address+"&id=<%=id%>";
 
				//alert(address); 
			} 
	}  
	 
	function showLocationInfo() 
	{   
		//alert(1); 
		//var locationX = locationInfo.lng; //定位坐标经度值
		//var locationY = locationInfo.lat; //定位坐标纬度值
		//document.getElementById('info').innerHTML = "定位点坐标：("+locationX+","+locationY+")";
		//alert("定位点坐标：("+locationX+","+locationY+")");
		init();   
	} 
	    
	function savestatues(){
		window.location.href="logisticDetail.jsp?method=savestatues&id=<%=id%>";
	}
 </script>   

</body>
</html>
