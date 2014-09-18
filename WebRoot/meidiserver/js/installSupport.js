function clickBackButton() {
	showMainPage();
}
function repairAsk(){
	window.location.href="./repairSupport.jsp?openId="+openID+"&fromId="+fromId;
}
function showDialog(content){
   new Dialog({autoshow : true, target: 'body', title:"提示",content: content},
		{configs:[{title:"知道了",eventName:'ok'}],
	ok:function() { }
    }
  );
}
function selectProductionCategory(){
	var otherPage = document.getElementById("otherPage");
	otherPage.innerHTML = "";
	Mbp.post({
		url		:	"wx/unit/commonTop.do",
		params 	:	{fromid:fromId},
		success	:	function(data, textStatus, jqXHR){
			if(data.length==0){
				showDialog("查询不到相关信息");
				return;
			}
			for (var i = 0; i < data.length; i++) {
				 var contentDiv=document.createElement("div")
				 var id = data[i].id;
				 contentDiv.setAttribute("id",id);
				 otherPage.appendChild(contentDiv);
				
				var div = document.createElement("div");
				if (id=="5") {

                }else{
                	div.innerHTML = data[i].name;
		            div.className="div-item";
		            contentDiv.appendChild(div);
					getProductType(fromId,id);
                }

			}			
			showOtherPage("请选择产品类型");
			
		},
		error 	:	function(e, xhr, type){
			
		}
	});
}

function getProductType(fromid,id) {
	var divContent = document.getElementById(id);
	var pk={id:id,fromid:fromid};
	var page={data:JSON.stringify(pk)};
	Mbp.post({
		url		:	"wx/unit/commonType.do",
		params 	:	page,
		success	:	function(data, textStatus, jqXHR){
			if(data&&data.length==0){
				showDialog("查询不到相关信息");
				return;
			}
			for (var i = 0; i < data.length; i++) {
				var span = document.createElement("span");
	            span.innerHTML = data[i].name;
	            if(data[i].name=="电磁炉"||data[i].name=="微波炉"||data[i].name=="电压力锅"||data[i].name=="小烤箱"||data[i].name=="空气能热水器"||data[i].name=="面包机"||data[i].name=="煎烤机"){

	            }else{
	            	span.setAttribute("entId",data[i].orgId);
	                span.setAttribute("bigMachine",data[i].isDD);
	                span.setAttribute("typeId",data[i].typeId);
		            span.className="product-item";
		            divContent.appendChild(span);
	            }
                
	            span.onclick=function(e){
	            	var target=e.currentTarget;
	            	var bigMachine=target.getAttribute("bigMachine");
	            	var entId=target.getAttribute("entId");
	            	var typeId=target.getAttribute("typeId");
	            	var name=target.innerHTML;
	            	if(bigMachine=="Y"){
	            		if("MDV"==entId||"MDKT"==entId){
							$("#mallDiv").show();
						}else{
							$("#mallDiv").hide();
						}
	            		$("#districtDiv").css("width","50%");
						//$("#").hide();
	            	}else{
	            		//可以选择街道，可以选择服务类型
						$("#districtDiv").css("width","50%");
						//$("#rowDiv").show();
	            	}
					//产品类型
					$("#proCategory").html(name);
					$("#proCategory").attr("value",entId);
					$("#proCategory").attr("bigMachine",bigMachine);
					$("#proCategory").attr("typeId",typeId);
			
					//服务类型
					$("#serviceKinds").html("请选择");
					$("#serviceOption").hide();
					//反应类别
					$("#refCategory").html("请选择");
					$("#refCategory").attr("value","");
					$("#refCategoryDiv").show();
					//选择地址
					$("#selectProvince").html("请选择");
					$("#selectProvince").attr("value","");
					$("#selectProvince").attr("eid",null);
					$("#selectCity").html("请选择");
					$("#selectCity").attr("value","");
					$("#selectCity").attr("eid",null);
					$("#selectDistrict").html("请选择");
					$("#selectDistrict").attr("value","");
					$("#selectDistrict").attr("eid",null);
					$("#selectRow").html("请选择");
					$("#selectRow").attr("value","");
					$("#selectRow").attr("eid",null);
					Mbp.post({
						url		:	"wx/warranty/getAreajson.do",
						params 	:	{openid:openID,entid:entId},
						success	:	function(data, textStatus, jqXHR){
							if(data&&data.prov){
								var prov = data.prov.split('|');
								var city = data.city.split('|');
								var dist = data.dist.split('|');
								var row = data.row.split('|');
								$("#selectProvince").html(prov[1]);
								$("#selectCity").html(city[1]);
								$("#selectDistrict").html(dist[1]);
								$("#addressDes").val(data.addr);
								if(row[0].length>0){
									$("#selectRow").html(row[1]);
								}
								$("#selectProvince").attr("value",prov[0]);
								$("#selectCity").attr("value",city[0]);
								$("#selectDistrict").attr("value",dist[0]);
								//小电 eid->id value-code
								if(data.isdd){
									$("#selectProvince").attr("eid",entId);
									$("#selectCity").attr("eid",entId);
									$("#selectDistrict").attr("eid",entId);
								}else{
									$("#selectProvince").attr("eid",prov[2]);
									$("#selectCity").attr("eid",city[2]);
									$("#selectDistrict").attr("eid",dist[2]);
								}
								if(row[0].length>0){
									$("#selectRow").attr("value",row[0]);
									if(data.isdd){
										$("#selectRow").attr("eid",entId);
									}else{
										$("#selectRow").attr("eid",row[2]);
									}
								}
							}
							showMainPage(); 
						},
						error 	:	function(e, xhr, type){
							showMainPage(); 
						}
					});
	            };
			}
		},
		error 	:	function(e, xhr, type){
			
		}
	});
}
function showMainPage(){
	$("#otherDiv").hide();
	window.document.title="在线报装";
	$("#mainDiv").show();
	var y=Session.loadObject("scrollY");
	window.scrollTo(0,y);
}
function showOtherPage(title){
	$("#mainDiv").hide();
	window.document.title=title;
	$("#otherDiv").show();
	var y=window.scrollY;
	console.log(y);
	Session.saveObject("scrollY",y);
	window.scrollTo(0,0);
}
function serviceKinds(){
	   var display=$("#serviceOption").css("display");
	   if("none"!=display){
	        $("#serviceDiv").css("border-bottom-right-radius","5px");
			$("#serviceDiv").css("border-bottom-left-radius","5px");
			$("#serviceOption").hide();
			$("#selectDiv").hide();
			return;
	   }
	   var data=new Array();
	   for(var i=0;i<12;i++){
		   var a1=8+i;
		   var a2=9+i;
		   data.push(a1+":00"+"~"+a2+":00");
	   }
	   var serviceOption = document.getElementById("serviceOption");
	   serviceOption.innerHTML = "";
	   for (var i = 0; i < data.length; i++) {
			var div = document.createElement("li");
	        div.innerHTML = data[i];  
			div.className="left";
	        div.onclick=function(e){
	        	var target=e.currentTarget;
				var serviceName=target.innerHTML;
				$("#serviceKinds").html(serviceName);
				$("#serviceDiv").css("border-bottom-right-radius","5px");
				$("#serviceDiv").css("border-bottom-left-radius","5px");
				$("#serviceOption").hide();
				$("#selectDiv").hide();
	        };
			serviceOption.appendChild(div);
		}
	   
		$("#serviceDiv").css("border-bottom-right-radius","0px");
		$("#serviceDiv").css("border-bottom-left-radius","0px");
		$("#serviceOption").show(); 
		$("#selectDiv").show();
		
	}
function selectProvince(){
	var value=$("#proCategory").attr("value");
	var code="";
	if(null==value||""==value){
		showDialog("请选择产品类型");
	return;
	}
	changeProviceItem(value,code);
}

function changeProviceItem(entid,code){
  var otherPage = document.getElementById("otherPage");
  otherPage.innerHTML = "";
  var page = {entId : entid, code : code};
    Mbp.post({
		url		:	"wx/warranty/arealist.do",
		params 	:	page,
		success	:	function(data, textStatus, jqXHR){
			for (var i = 0; i < data.length; i++) {
				if(data&&data.length==0){
					showDialog("查询不到相关信息");
					return;
				}
				var div = document.createElement("div");
	            div.innerHTML = data[i].name;
	            var value = data[i].code;
				var id=data[i].id;
				if(null!=id&&""!=id){
					div.setAttribute("eid",id);
				}
                div.setAttribute("value",value);
	            div.className="list-item";
	            div.onclick=function(e){
	            	var target=e.currentTarget;
	               	var value=target.getAttribute("value");
					var id=target.getAttribute("eid");
					if(null!=id&&""!=id){
						$("#selectProvince").attr("eid",id);
					}
					var provinceName=target.innerHTML;
					$("#selectProvince").html(provinceName);
					$("#selectProvince").attr("value",value);
					$("#selectCity").html("请选择");
					$("#selectCity").attr("value","");
					$("#selectCity").attr("eid",null);
					$("#selectDistrict").html("请选择");
					$("#selectDistrict").attr("value","");
					$("#selectDistrict").attr("eid",null);
					$("#selectRow").html("请选择");
					$("#selectRow").attr("value","");
					$("#selectRow").attr("eid",null);
					showMainPage(); 
	            };
				otherPage.appendChild(div);
			}
			showOtherPage("请选择省份");
		},
		error 	:	function(e, xhr, type){
		
		}
	});
}
function selectCity(){
	var value=$("#proCategory").attr("value");
	var code=$("#selectProvince").attr("value");
	if(null==value||""==value){
	  showDialog("请选择产品类型");
	  return;
	}else if(null==code||""==code){
	  showDialog("请选择省份");
	  return;
	}
	changeCityItem(value,code);
}
function changeCityItem(entid,code){
  var otherPage = document.getElementById("otherPage");
  otherPage.innerHTML = "";
  var page = {entId : entid, code : code};
    Mbp.post({
		url		:	"wx/warranty/arealist.do",
		params 	:	page,
		success	:	function(data, textStatus, jqXHR){
			if(data&&data.length==0){
				showDialog("查询不到相关信息");
				return;
			}
			for (var i = 0; i < data.length; i++) {
				var div = document.createElement("div");
	            div.innerHTML = data[i].name;
	            var value = data[i].code;
				var id=data[i].id;
				if(null!=id&&""!=id){
					div.setAttribute("eid",id);
				}
                div.setAttribute("value",value);
	            div.className="list-item";
	            div.onclick=function(e){
	            	var target=e.currentTarget;
	               	var value=target.getAttribute("value");
					var id=target.getAttribute("eid");
					if(null!=id&&""!=id){
						$("#selectCity").attr("eid",id);
					}
					var cityName=target.innerHTML;
					$("#selectCity").html(cityName);
					$("#selectCity").attr("value",value);
					$("#selectDistrict").html("请选择");
					$("#selectDistrict").attr("value","");
					$("#selectDistrict").attr("eid",null);
					$("#selectRow").html("请选择");
					$("#selectRow").attr("value","");
					$("#selectRow").attr("eid",null);
					showMainPage();
	            };
				otherPage.appendChild(div);
			}
			showOtherPage("请选择城市");
		},
		error 	:	function(e, xhr, type){
		
		}
	});
}
function selectDistrict(){
	var value=$("#proCategory").attr("value");
	var bigMachine=$("#proCategory").attr("bigMachine");
	var code=$("#selectCity").attr("value");
	if(null==value||""==value){
		showDialog("请选择产品类型");
	  return;
	}else if(null==code||""==code){
		showDialog("请选择城市");
		return;
	}

	changeDistrictItem(value,code,bigMachine);
	
}
function changeDistrictItem(entid,code,isBigMechine){
  var otherPage = document.getElementById("otherPage");
  otherPage.innerHTML = "";
  var page = {entId : entid, code : code};
    Mbp.post({
		url		:	"wx/warranty/arealist.do",
		params 	:	page,
		success	:	function(data, textStatus, jqXHR){
			if(data&&data.length==0){
				showDialog("查询不到相关信息");
				return;
			}
			for (var i = 0; i < data.length; i++) {
				var div = document.createElement("div");
	            div.innerHTML = data[i].name;
	            var value = data[i].code;
			    var id=data[i].id;
				if(null!=id&&""!=id){
					div.setAttribute("eid",id);
				}
				if(isBigMechine){
					var zipCode=data[i].zipCode;
					var zoneNum=data[i].zoneNum;
				    div.setAttribute("zipCode",zipCode);
					div.setAttribute("zoneNum",zoneNum);
				}
                div.setAttribute("value",value);
	            div.className="list-item";
	            div.onclick=function(e){
	            	var target=e.currentTarget;
	               	var value=target.getAttribute("value");
					var id=target.getAttribute("eid");
					if(null!=id&&""!=id){
						$("#selectDistrict").attr("eid",id);
					}
					Session.deleteObject("zipCode");
					Session.deleteObject("zoneNum");
					if(isBigMechine){
						var zipCode=target.getAttribute("zipCode");
						var zoneNum=target.getAttribute("zoneNum");
						Session.saveObject("zipCode",zipCode);
						Session.saveObject("zoneNum",zoneNum);
					}
					var districtName=target.innerHTML;
					$("#selectDistrict").html(districtName);
					$("#selectDistrict").attr("value",value);
					$("#selectRow").html("请选择");
					$("#selectRow").attr("value","");
					$("#selectRow").attr("eid",null);
					showMainPage(); 
	            };
				otherPage.appendChild(div);
			}
			showOtherPage("请选择区/县");
		},
		error 	:	function(e, xhr, type){
		
		}
	});
}
function selectRow(){
	var value=$("#proCategory").attr("value");
	var code=$("#selectDistrict").attr("value");
	if(null==value||""==value){
	  showDialog("请选择产品类型");
	  return;
	}else if(null==code||""==code){
	  showDialog("请选择区县");
	  return;
	}
	changeRowItem(value,code);
}
function changeRowItem(entid,code){
  var otherPage = document.getElementById("otherPage");
  otherPage.innerHTML = "";
  var page = {entId : entid, code : code};
    Mbp.post({
		url		:	"wx/warranty/arealist.do",
		params 	:	page,
		success	:	function(data, textStatus, jqXHR){
		    if(data&&data.length==0){
			   showDialog("没有相关的街道信息");
			   return;
			}
			for (var i = 0; i < data.length; i++) {
				var div = document.createElement("div");
	            div.innerHTML = data[i].name;
	            var value = data[i].code;
				var id=data[i].id;
				if(null!=id&&""!=id){
					div.setAttribute("eid",id);
				}
                div.setAttribute("value",value);
	            div.className="list-item";
	            div.onclick=function(e){
	            	var target=e.currentTarget;
	               	var value=target.getAttribute("value");
					var id=target.getAttribute("eid");
					if(null!=id&&""!=id){
						$("#selectRow").attr("eid",id);
					}
					var zoneNum=target.getAttribute("zoneNum");
					$("#areaCode").val(zoneNum);
					var rowName=target.innerHTML;
					$("#selectRow").html(rowName);
					$("#selectRow").attr("value",value);
					showMainPage(); 
	            };
				otherPage.appendChild(div);
			}
			showOtherPage("请选择街道");
		},
		error 	:	function(e, xhr, type){
		
		}
	});
}
function resetInfo(){
	//产品类型
	$("#proCategory").html("请选择");
	$("#proCategory").attr("value","");
	$("#proCategory").attr("bigMachine","");
	$("#proCategory").attr("typeId","");
	//购买日期
	$("#buyDate").val("");
	//购机商场
	$("#buyMall").val("");
	//产品型号
	$("#proModel").val("");


	//服务诉求描述
	$("textarea").html("需要安装...");
	$("#calculate").html(0);
	$("#serviceDate").val("");
	$("#serviceKinds").html("请选择");
	//个人信息
	$("#userName").val("");
	$("#userEmail").val("");
	//选择地址
	$("#selectProvince").html("请选择");
	$("#selectProvince").attr("value","");
	$("#selectProvince").attr("eid",null);
	$("#selectCity").html("请选择");
	$("#selectCity").attr("value","");
	$("#selectCity").attr("eid",null);
	$("#selectDistrict").html("请选择");
	$("#selectDistrict").attr("value","");
	$("#selectDistrict").attr("eid",null);
	$("#selectRow").html("请选择");
	$("#selectRow").attr("value","");
	$("#selectRow").attr("eid",null);
	$("#addressDes").val("");
	$("#districtDiv").css("width","50%");
	//$("#rowDiv").hide();
	//联系电话
	$("#userPhone").val("");
	$("#areaCode").val("");
	$("#userTel").val("");

}

function submitInfo(){
	 var value=$("#proCategory").attr("value");
	 var bigMachine=$("#proCategory").attr("bigMachine");
	 var typeId=$("#proCategory").attr("typeId");
	 if(null==value||""==value){
		showDialog("请选择产品类型");
	  return;
	}
	 var param;
	 if(bigMachine=="Y"){
		 param=getBigMachineParams(value,typeId);
	 }else{
		 param=getSmallMachineParams(typeId,value);
	 }
	if(null==param){
	 	return;
	 } 
	 var page={data:JSON.stringify(param)};
     Mbp.post({
		url		:	"wx/warranty/createrecord.do",
		params 	:	page,
		success	:	function(data, textStatus, jqXHR){
			if(data.success){
				new Dialog({autoshow : true, target: 'body', title:"提示",content: "提交成功，订单号为:"+data.data},
				    		{configs:[{title:"知道了",eventName:'ok'}],
				    	ok:function() { 
						   resetInfo();
						}});
			}else{
			   showDialog(data.msg);
			
			}
		},
		error 	:	function(e, xhr, type){
		
		}
	});
}
function getBigMachineParams(entid,id){
    var orgId=entid;
	var pubProdcode=id;
	var prodName = $("#proCategory").html();
	var date=new Date();
	var year=date.getFullYear();
	var month=date.getMonth()+1;
	if(month<10){
		month="0"+month;
	}
	var day=date.getDay();
	if(day<10){
		day="0"+day;
	}
	var purchaseDate ="";
	var purchaseMall="";
	var itemId = "";
	var serviceItemId;
	//洗衣机: A01
	//空调: A01
	//冰箱: A27
	if("MDKT"==id||"MDV"==id){
		serviceItemId="A01";
	}else if("MDBX"==id){
		serviceItemId="A27";
	}else if("MDXYJ"==id){
		serviceItemId="A01";
	}

	var custDesc=$("textarea").val();
//	if(null==custDesc||""==custDesc){
//		showDialog("请填写服务描述");
//	  return;
//	}
	var serviceDate=$("#serviceDate").val();
//	if(null==serviceDate||""==serviceDate){
//		showDialog("请选择预约日期");
//		  return;
//	}
	var serviceTime=$("#serviceKinds").html();
//	if("请选择"==serviceTime){
//		showDialog("请选择预约时间段");
//		  return;
//	}
	var custName=$("#userName").val();
	if(null==custName||""==custName){
		showDialog("请填写用户姓名");
	  return;
	}
	var custUnit=$("#userEmail").val();
	
	if(null!=custUnit&&custUnit.length>0){
          if(!verifyIsEmail(custUnit)){
          	  showDialog("邮箱格式错误，请重新输入");
          	  return;
          }
	}
	var cityAreaId = $("#selectCity").attr("value");
	if(null==cityAreaId||""==cityAreaId){
	  showDialog("请选择城市");
	  return;
	}
	var provinceAreaId =$("#selectProvince").attr("value");
	if(null==provinceAreaId||""==provinceAreaId){
	  showDialog("请选择省份");
	  return;
	}
	
	var regionId=$("#selectDistrict").attr("value");
	if(null==regionId||""==regionId){
		 showDialog("请选择区县");
	  return;
	}
	var rowId=$("#selectRow").attr("value");
	var address=$("#addressDes").val();
	if(null==address||""==address){
		 showDialog("请填写详细地址");
	  return;
	}

	var privince = $("#selectProvince").html(); // 省份
	var city     = $("#selectCity").html();     // 城市
	var district = $("#selectDistrict").html(); // 地区
	var row = $("#selectRow").html();			// 乡镇

	var area = privince;
	if (privince != city) {
		area += city;
	};
	if (city != district) {
		area += district;
	};
	address = area + address;

	var telephone1=$("#userPhone").val();
	if(null==telephone1||""==telephone1){
	  showDialog("请填写手机号码");
	  return;
	}else if(!verifyIsPhoneNO(telephone1)){
	   showDialog("请填写正确的手机号码");
	  return;
	}

	var areaCode   = $('#areaCode').val();
	var telephone3 = $("#userTel").val();
	if(!verifyIsTelNO(areaCode, telephone3)) {
	   showDialog("请填写正确的区号或座机号码");
	  return;
	} 
	if (!isNullOrEmpty(areaCode) && !isNullOrEmpty(telephone3)) {
		telephone3 = areaCode + "-" + telephone3;
	};
	
	var zipCode=Session.loadObject("zipCode");
	if(null==zipCode){
	zipCode="";
	}
	var zoneNum=Session.loadObject("zoneNum");
	if(null==zoneNum){
	zoneNum="";
	}
	var openid=openID;
	var fromid=fromId;
	var areajson = {prov:provinceAreaId+'|'+privince,city:cityAreaId+'|'+city,dist:regionId+'|'+district,row:rowId+"|"+row,addr:$("#addressDes").val()};
	return {recordType:1,areajson:areajson,problemdes:"需要安装",serviceTime:serviceTime,serviceDate:serviceDate,orgId:orgId,pubProdcode:pubProdcode,prodName:prodName,purchaseDate:purchaseDate,purchaseMall:purchaseMall,serviceItemId:serviceItemId,custDesc:custDesc,custName:custName,custUnit:custUnit,regionId:regionId,address:address,telephone1:telephone1,telephone2:"",telephone3:telephone3,zipCode:zipCode,areaNum:zoneNum,cardId:"",openid:openid,itemId:itemId,fromid:fromid};
}
function getSmallMachineParams(entid,id){
	var orgId=entid;
	var itemTypeId = id;
	var prodName = $("#proCategory").html();
	var date=new Date();
	var year=date.getFullYear();
	var month=date.getMonth()+1;
	if(month<10){
		month="0"+month;
	}
	var day=date.getDay()
	if(day<10){
		day="0"+day;
	}
	var purchaseDate ="";
	var purchaseMall="";
	var itemId = "";
	var problemId = "";

	var appealKindId="4";
	
	var appealContent = $("textarea").val();
//	if(null==appealContent||""==appealContent){
//		showDialog("请填写服务描述");
//	  return;
//	}
	var serviceDate=$("#serviceDate").val();
//	if(null==serviceDate||""==serviceDate){
//		showDialog("请选择预约日期");
//		  return;
//	}
	var serviceTime=$("#serviceKinds").html();
//	if("请选择"==serviceTime){
//		showDialog("请选择预约时间段");
//		return;
//	}
	var enduserName = $("#userName").val();
	if(null==enduserName||""==enduserName){
		showDialog("请填写用户姓名");
	  return;
	}
	var custUnit=$("#userEmail").val();
	
	if(null!=custUnit&&custUnit.length>0){
          if(!verifyIsEmail(custUnit)){
          	  showDialog("邮箱格式错误，请重新输入");
          	  return;
          }
	}
	var provinceAreaId =$("#selectProvince").attr("eid");
	if(null==provinceAreaId||""==provinceAreaId){
	  showDialog("请选择省份");
	  return;
	}

	var cityAreaId = $("#selectCity").attr("eid");
	if(null==cityAreaId||""==cityAreaId){
	  showDialog("请选择城市");
	  return;
	}
	var countryAreaId = $("#selectDistrict").attr("eid");
	if(null==countryAreaId||""==countryAreaId){
	  showDialog("请选择区县");
	  return;
	}
	var rowId=$("#selectRow").attr("eid");
	if(null==rowId||rowId==""){
	  var areaId = countryAreaId;
	}else{
	   var areaId=rowId;
	}
	var enduserAddress =$("#addressDes").val();
	if(null==enduserAddress||""==enduserAddress){
		 showDialog("请填写详细地址");
	  return;
	}

	var privince = $("#selectProvince").html(); // 省份
	var city     = $("#selectCity").html();     // 城市
	var district = $("#selectDistrict").html(); // 地区
	var row      = $("#selectRow").html();      // 街道
	var privinceCode = $("#selectProvince").attr("value"); // 省份
	var cityCode     = $("#selectCity").attr("value");     // 城市
	var districtCode = $("#selectDistrict").attr("value"); // 地区
	var rowCode      = $("#selectRow").attr("value");      // 街道

	var area = privince;
	if (privince != city) {
		area += city;
	};
	if (city != district) {
		area += district;
	};
	if (rowId != null && rowId != "" && district != row) { // 用户选择了街道，且地区与街道不同
		area += row;
	};

	enduserAddress = area + enduserAddress;	

	var mobile = $("#userPhone").val();
	if(null==mobile||""==mobile){
	  showDialog("请填写手机号码");
	  return;
	}else if(!verifyIsPhoneNO(mobile)){
	   showDialog("请填写正确的手机号码");
	  return;
	}
	
	var areaCode = $('#areaCode').val();
	var tel      = $("#userTel").val();
	if(!verifyIsTelNO(areaCode, tel)){
	   showDialog("请填写正确的区号或座机号码");
	   return;
	}
	if (!isNullOrEmpty(areaCode) && !isNullOrEmpty(tel)) {
		tel = areaCode + "-" + tel;
	};

    var openid=openID;
    var fromid=fromId;
	var areajson = {prov:privinceCode+'|'+privince+'|'+provinceAreaId,city:cityCode+'|'+city+'|'+cityAreaId,dist:districtCode+'|'+district+'|'+countryAreaId,row:rowCode+"|"+row+'|'+rowId,addr:$("#addressDes").val()};
	return {recordType:1,areajson:areajson,problemdes:"需要安装",serviceTime:serviceTime,serviceDate:serviceDate,orgId:orgId,appealKindId:appealKindId,prodName:prodName,purchaseMall:purchaseMall,enduserName:enduserName,custUnit:custUnit,mobile:mobile,tel:tel,itemTypeId:itemTypeId,itemId:itemId,provinceAreaId:provinceAreaId,cityAreaId:cityAreaId,countryAreaId:countryAreaId,areaId:areaId,enduserAddress:enduserAddress,problemId:problemId,purchaseDate:purchaseDate,appealContent:appealContent,openid:openid,fromid:fromid};
}
function verifyIsPureInt(string) {
	for (var i = 0; i < string.length; i++) {
		var character = string.charAt(i);
		if (character < '0' || character > '9') {
			return false;
		}
	}
	return true;
}
function verifyIsPhoneNO(string) {
    if (!this.verifyIsPureInt(string)) {
		return false;
	}
	if (string.length != 11) {
		return false;
	}
	var num = string.substr(0, 1);
	if (num != "1") {
		return false;
	}
	return true;
}
function verifyIsTelNO(areaCode, telephone) {
    if(isNullOrEmpty(areaCode) && isNullOrEmpty(telephone)) {
	   return true;
	}
	if(!isNullOrEmpty(areaCode) && !isNullOrEmpty(telephone) && verifyIsPureInt(areaCode) && verifyIsPureInt(telephone) && areaCode.length >= 3 && telephone.length >= 7) {
	   return true;
	}	
	return false;
}
function isNullOrEmpty(string) {
	if (null == string || "" == string) {
		return true;
	};
	return false;
}
function verifyIsEmail(string) {
	    var regex = /^\w+([.]\w+)*@\w+([-.]\w+)*\.\w+([.]\w+)*$/;
	    if(regex.test(string)) return true; 
		return false;
}