var $xialaSELECT; 
$(document).ready(function(){ 
initXialaSelect(); 
initSearch(); 
}); 
var temptimeout=null; 
var query=""; 
function searchDev(key){ 
//if(key == "")return; 
query=key; 
clearTimeout(temptimeout); 
temptimeout= setTimeout(findUnSaved, 500); 
} 
function findUnSaved() 
{ 
//alert("dd"); 
//if(1==1)return; 
$.ajax({ 
type: "post", 
data:{'query':query}, 
url: path + "/tList.action", 
success: function(data) { 
xiala(data); 
}, 
error: function(data) { 
alert("加载失败，请重试！"); 
} 
}); 
} 
function initSearch() 
{ 
//定义一个下拉按钮层，并配置样式(位置，定位点坐标，大小，背景图片，Z轴)，追加到文本框后面 
$xialaDIV = $('<div></div>').css('position', 'absolute').css('left', $('#province').position().left + $('#province').width() - 15 + 'px').css('top', 
$('#province').position().top + 4 + 'px').css('background', 'transparent url(../images/lala.gif) no-repeat top left').css('height', '16px').css('width', 
'15px').css('z-index', '100'); 
$('#province').after($xialaDIV); 
//鼠标进入修改背景图位置 
$xialaDIV.mouseover(function(){ 
$xialaDIV.css('background-position', ' 0% -16px'); 
}); 
//鼠标移出修改背景图位置 
$xialaDIV.mouseout(function(){ 
$xialaDIV.css('background-position', ' 0% -0px'); 
}); 
//鼠标按下修改背景图位置 
$xialaDIV.mousedown(function(){ 
$xialaDIV.css('background-position', ' 0% -32px'); 
}); 
//鼠标释放修改背景图位置 
$xialaDIV.mouseup(function(){ 
$xialaDIV.css('background-position', ' 0% -16px'); 
if($xialaSELECT) 
$xialaSELECT.show(); 
}); 
$('#province').mouseup(function(){ 
$xialaDIV.css('background-position', ' 0% -16px'); 
$xialaSELECT.show(); 
}); 
} 
var firstTimeYes=1; 
//文本框的下拉框div 
function xiala(data){ 
//first time 
if($xialaSELECT) 
{ 
$xialaSELECT.empty(); 
} 
//定义一个下拉框层，并配置样式(位置，定位点坐标，宽度，Z轴)，先将其隐藏 
//定义五个选项层，并配置样式(宽度，Z轴一定要比下拉框层高)，添加name、value属性，加入下拉框层 
$xialaSELECT.append(data); 
if(firstTimeYes == 1) 
{ 
firstTimeYes =firstTimeYes+1; 
}else{ 
$xialaSELECT.show(); 
} 
} 
function initXialaSelect() 
{ 
$xialaSELECT = $('<div></div>').css('position', 'absolute').css('overflow-y','scroll').css('overflow-x','hidden').css('border', '1px solid #809DB9').css('border-top','none').css('left', '125px').css 
('top', $('#province').position().top + $('#province').height() + 6 + 'px').css('width', $('#province').width() + 'px').css('z-index', '101').css('width','152px').css('background','#fff').css('height','200px').css('max-height','600px'); 
$('#province').after($xialaSELECT); 
//选项层的鼠标移入移出样式 
$xialaSELECT.mouseover(function(event){ 
if ($(event.target).attr('name') == 'option') { 
//移入时背景色变深，字色变白 
$(event.target).css('background-color', '#000077').css('color', 'white'); 
$(event.target).mouseout(function(){ 
//移出是背景色变白，字色变黑 
$(event.target).css('background-color', '#FFFFFF').css('color', '#000000'); 
}); 
} 
}); 
//通过点击位置，判断弹出的显示 
$xialaSELECT.mouseup(function(event){ 
//如果是下拉按钮层或下拉框层，则依然显示下拉框层 
if (event.target == $xialaSELECT.get(0) || event.target == $xialaDIV.get(0)) { 
$xialaSELECT.show(); 
} 
else { 
//如果是选项层，则改变文本框的值 
if ($(event.target).attr('name') == 'option') { 
//弹出value观察 
$('#nce').val($(event.target).html()); 
$('#d').val($(event.target).attr("d")); 
//if seleced host then hidden the dev type 
if($(event.target).attr("ass") == 3305) 
{ 
$("#ype").hide(); 
$("#ost").val(1); 
}else{ 
$("#ype").show(); 
$("#ost").val(-1); 
} 
} 
//如果是其他位置，则将下拉框层 
if ($xialaSELECT.css('display') == 'block') { 
$xialaSELECT.hide(); 
} 
} 
}); 
$xialaSELECT.hide(); 
} 
var k = 1; 
document.onclick = clicks; 
function clicks() 
{ 
if(k ==2){ 
k = 1; 
if($xialaSELECT) 
{ 
if ($xialaSELECT.css('display') == 'block') { 
$xialaSELECT.hide(); 
} 
} 
}else{ 
k = 2; 
} 
} 