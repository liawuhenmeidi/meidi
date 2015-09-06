<%@ page language="java" import="java.util.*,company.*,orderPrint.*,category.*,group.*,user.*,utill.*,product.*,order.*,orderproduct.*,user.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");
HashMap<Integer,User> usermap = UserManager.getMap();
String id = request.getParameter("id");
Order or = OrderManager.getOrderID(user,Integer.valueOf(id)); 
String locate = or.getLocate()+or.getLocateDetail();
Company company = CompanyManager.getLocate();
String pisition = company.getLocate();
String location = user.getLocation();
System.out.println("adminmap"+location);

%>
 
<!DOCTYPE html>   
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  
<style type="text/css">  
body, html,#l-map {width: 100%;height: 100%;overflow: hidden;hidden;margin:0;}  
</style>  
<script type="text/javascript" src="http://api.map.baidu.com/api?type=quick&ak=3f86330da0dddbaa5bb13747c7957193&v=1.0"></script> 
<title>显示地图</title>  
</head>  
<body>  
<div id="l-map"></div>  
</body>
</html>  
<script type="text/javascript">
var locate = "<%=locate%>";
var nowlocate = "<%=location%>";   
var pisition = "<%=pisition%>" ;
//function getLocation(){
	   // if(navigator.geolocation){
	
	  //     navigator.geolocation.getCurrentPosition(showPosition);
	
	   //   }else{
	
	    //   alert("您的浏览器不支持地理定位");
	
	   //   }
	
	 //  }
	
	 
	
//	function showPosition(position){
	
	 //   lat=position.coords.latitude;
	
	 //   lon=position.coords.longitude;
	
	    //var map = new BMap.Map("container");            // 创建Map实例
	
	 //   var point = new BMap.Point(lon, lat);    // 创建点坐标
	
	    //map.centerAndZoom(point,15);                     //
	
	    //map.enableScrollWheelZoom();
	
	 //   var gc = new BMap.Geocoder();   
	
	 //   gc.getLocation(point, function(rs){
	
	 //      var addComp = rs.addressComponents;
	
	      // alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street);
	  //     nowlocate = addComp.district+addComp.street;
	  //    });
	
	 //  }

var map = new BMap.Map("l-map");  
map.centerAndZoom(pisition); 
var zoomControl=new BMap.ZoomControl();  
map.addControl(zoomControl);//添加缩放控件  
map.enableDragging();

var opts = {offset: new BMap.Size(70, 5)} ;     
map.addControl(new BMap.ScaleControl(opts)); 
var point = new BMap.Point(117.190691,39.138334);
var local = new BMap.LocalSearch(point,   
        {renderOptions: {map: map,autoViewport: true},pageCapacity: 8});    
   
//map.addControl(new BMap.ZoomControl());          //添加地图缩放控件
//var marker1 = new BMap.Marker(new BMap.Point(116.384, 39.925));  // 创建标注
//map.addOverlay(marker1);              // 将标注添加到地图中
//创建信息窗口
//var infoWindow1 = new BMap.InfoWindow("普通标注");
//marker1.addEventListener("click", function(){this.openInfoWindow(infoWindow1);});           // 创建Map实例          //添加地图缩放控件

//var infoWindow = new BMap.InfoWindow("点击获取路径", opts);  // 创建信息窗口对象
//map.openInfoWindow(infoWindow,point); //开启信息窗口


var marker1 = new BMap.Marker(new BMap.Point(117.190691,39.138334));  // 创建标注
map.addOverlay(marker1);              // 将标注添加到地图中
marker1.addEventListener("click", function(){
//getLocation();
	
/*start|end：（必选）
{name:string,latlng:Lnglat}
opts:
mode：导航模式，固定为
BMAP_MODE_TRANSIT、BMAP_MODE_DRIVING、
BMAP_MODE_WALKING、BMAP_MODE_NAVIGATION
分别表示公交、驾车、步行和导航，（必选）
region：城市名或县名  当给定region时，认为起点和终点都在同一城市，除非单独给定起点或终点的城市
origin_region/destination_region：同上
*/
	
	var start = {
	     name:nowlocate
	};
	var end = {
	    name:locate
	};
	var opts = {
	    mode:BMAP_MODE_DRIVING,
	    region:pisition
	};
	
	var ss = new BMap.RouteSearch();
	ss.routeCall(start,end,opts);
	
});
//local.search("<%=locate%>");    
</script>  