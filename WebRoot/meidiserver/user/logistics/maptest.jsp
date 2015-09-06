<%@ page language="java"  import="java.util.*,utill.*,category.*,logistics.*,branch.*,group.*,user.*"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<% 
request.setCharacterEncoding("utf-8");   
User user = (User)session.getAttribute("user");

%>   
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>当前位置精确定位</title>
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
	<div id="mapContainer" ></div>
	<div id="tip">  
		<div>
			 
			<input type="button" value="显示位置信息" onClick="javascript:showLocationInfo()"/>  
		</div>  
		<div id="info"></div>         
        <div style="color: #C0C0C0">不支持IE9以下版本</div>   
	</div> 
	  
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=9d0cf651d73e4019e3968c9e915fe4a7"></script>
	<script type="text/javascript">
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
		//获取定位位置信息 
		function showLocationInfo()   
		{   
			//var locationX = locationInfo.lng; //定位坐标经度值
			//var locationY = locationInfo.lat; //定位坐标纬度值
			//document.getElementById('info').innerHTML = "定位点坐标：("+locationX+","+locationY+")";
			//alert("定位点坐标：("+locationX+","+locationY+")");
			init();
		}
		 
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
					var  address = data.regeocode.formattedAddress;
					alert(address); 
				} 
		}
		
	</script>
</body>
</html>	

