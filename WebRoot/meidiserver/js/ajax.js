
    var keys = [37, 38, 39, 40];

    function preventDefault(e) {
      e = e || window.event;
      if (e.preventDefault)
          e.preventDefault();
      e.returnValue = false;  
    }
    
    function keydown(e) {
        for (var i = keys.length; i--;) {
            if (e.keyCode === keys[i]) {
                preventDefault(e);
                return;
            }
        }
    }

    function scrolling(e) {
        preventDefault(e);
    }

    function disableScrolling(){
        if(window.addEventListener){
            window.addEventListener('DOMMouseScroll', scrolling, false);
            window.addEventListener('touchmove',scrolling,false);
            window.onmousewheel = document.onmousewheel = scrolling;
            document.onkeydown = keydown;
        }
    }

    function enableScrolling(){
        if (window.removeEventListener) {
            window.removeEventListener('DOMMouseScroll', scrolling, false);
            window.removeEventListener('touchmove',scrolling,false);
        }
        window.onmousewheel = document.onmousewheel = document.onkeydown = null;
    }
/* ----------------------load begin------------------*/
	var me;
    var canceled;
	var Loader = function(){
        this.config = {autoshow : true, target: 'body', text: '载入中...',cancelable:true};
        canceled = false;
        if(this.config.autoshow) {
            this.show();
        }
    };
	 Loader.prototype.show = function() {
        me = this;
        disableScrolling();
        var targetOjb = $(this.config.target);
        var cube_loader =  this.find();
        if(cube_loader) return;

        cube_loader = $("<div/>").addClass("cube-loader");
        
        var cube_loader_block = $("<div/>").addClass("cube-loader-block");
        var cube_loader_icon = $("<img/>").addClass("cube-loader-icon").attr('src','data:img/jpg;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAABGBAMAAACDAP+3AAAAHlBMVEX///+3t7e6urq4uLi4uLi5ubm5ubm5ubm5ubm5ubm/fxe2AAAACXRSTlMAIEaMjaPH8PN2+20BAAAA80lEQVR4XsWWLQ4CMRCFgRCwGDzHAIfCcwMEB8BxABQaw9yWAE1HfOS9bjahI799See187OTjjE7XM8bLZluI+K2kpp1vOMoNbuP5iGzuX80T5XRPL6xF3YWRXOqhHaWRXOphHZSUwjt5FmV0E7NmQb5iQZ5RJISTDUJNbwEnsXLZM4k9E7COwThW5DwTUlYGyB/6ClypgcOm7Aveqpw0VPJRU8lRxn8KhiUEwqvTePPaszZe/d32PYW/k1RA11KkcT3ju9B38t+JvjZMmRG+VnnZ6afvX6GN+8Cv1P8bvI7btiupMFRuzsNjv2XgB1R/P3iBSZwz1CJGJLVAAAAAElFTkSuQmCC');
        var cube_cancel_icon = $("<img/>").addClass("cube-loader-cancel");
        cube_cancel_icon.click(function(){
            enableScrolling();
            if(me.config.cancelable!=false){
                cube_loader.remove();
                canceled = true;
            }
        });
        cube_cancel_icon[0].src = "../res/images/btn_login_cancel.png";

        // var p = $("<p/>").append(this.config.text);
        cube_loader_block.append(cube_loader_icon);
        // cube_loader_block.append(p);
        cube_loader.append(cube_loader_block);
        cube_loader.append(cube_cancel_icon);

        var children = $(this.config.target).children();
        if(children && children.length>0) {
            children.first().before(cube_loader);
        } else {
            $(targetOjb).append(cube_loader);
        }
    };

    //
    Loader.prototype.hide = function() {
        enableScrolling();
        var cube_loader = this.find();
        if(cube_loader) $(cube_loader).remove();
    };

    Loader.prototype.hideAll = function() {
        enableScrolling();
        var cube_loader = $(".cube-loader");
        if(cube_loader && cube_loader.length>0) {
            $(cube_loader).each(function(){
                $(this).remove();
            });
        }
    };

    Loader.prototype.find = function() {
        var targetOjb = $(this.config.target);
        var result;
        var children = targetOjb.children();
        $(children).each(function(){
            if($(this).hasClass("cube-loader")) {
                result = this;
            }
        });
        return result;
    };

    Loader.prototype.isCanceled = function(){
        return canceled;
    };
	//暂时最多只支持两个按钮
	var Dialog = function(config,btnConfigs){
	    var me = this;
	    this.config=config;
	    this.btnConfigs=btnConfigs;
	    window.onresize = function(){
	        me.calculatePosition();
	    };
	    if(me.config.autoshow && !me.isShow()) {
	        me.show();
	        disableScrolling();
	    }
	};
/* ----------------------load end------------------*/	
	
/* ----------------------dialog  begin------------------*/	
	var dialogElem =  '<div id="cube-dialog-wrapper">'
	                 +'<div class="cube-dialog ui-corner-all" style="z-index: 500;min-width:260px; position: fixed; height:auto;">'
	                 +   '<div style="margin-bottom: 4px;" class="ui-header ui-bar-b">'
	                 +       '<div class="ui-title cube-dialog-header" style="padding-top:10px">提示</div>'
	                 +   '</div>'
	                 +   '<div>'
	                 +       '<p class="cube-dialog-subtitle"></p>'
	                 +       '<div class="cube-dialog-controls">'
	                 +       '</div>'
	                 +   '</div>'
	                 +'</div>'
	                 +'<div class="cube-dialog-screen cube-dialog-screen-model" style="z-index: 1000; display: block; "></div>';
	                 +'</div>'
	
	
	var btnElem =    '<button class="btn cube-dialog-btn ui-shadow ui-btn-corner-all ui-btn-icon-left" eventname="abcd">'
	                 +    '<span class="ui-btn-inner ui-btn-corner-all">'
	                 +       '<span class="cube-dialog-btn-title ui-btn-text">确定</span>'
	                 +       '<span class="ui-icon ui-icon-check ui-icon-shadow">&nbsp;</span>'
	                 +    '</span>'
	                 +'</button>';
	
	Dialog.prototype.isShow = function(){
	    var dialogWrapper = document.getElementById('cube-dialog-wrapper');
	    if(dialogWrapper){
	        return true;
	    }
	    return false;
	}
	
	
	//change事件需要执行用户自定义事件，还要广播事件。
	Dialog.prototype.show = function() {
	    var targetObj = $(this.config.target);
	
	
	    $(targetObj).append(dialogElem);
	    $('.cube-dialog-header').html(this.config.title);       //弹出框标题
	    $('.cube-dialog-subtitle').html(this.config.content);   //弹出框内容
	    var dialog =  $('.cube-dialog');
	
	    
	    if(dialog){
	        //设置对话框宽度
	        this.calculatePosition();
	        this.initBtn(dialog);
	    }   
	};
	
	Dialog.prototype.calculatePosition = function(){
	        var pageHeight = document.getElementsByTagName('body')[0].scrollHeight;   //设置黑色背景全屏
	        $('.cube-dialog-screen').css('height',pageHeight);
	        
	        var targetObj = $(this.config.target);
	        var dialog =  $('.cube-dialog');
	        var targetWidth = parseInt(targetObj.css("width"));
	        var targetHeight = parseInt(targetObj.css("height"));
	        var dialogWidth =  parseInt(dialog.css('width'));
	        var top = document.documentElement.clientHeight;   //获取窗口高度
	        if($.browsers && $.browser.msie){
	            var wrapper = $('.cube-dialog-screen')[0];
	            $(wrapper).height(top);
	        }
	
	        var dialogHeight = dialog.height() + 42;
	        dialog.css("top", "50%");       //设置到当前窗口中间 
	        dialog.css("margin-top", "-"+(dialogHeight/2)+"px"); //反向拖对话框到中间
	        // dialog.css("top", scrollTop + (top/2) - parseInt(dialog.css("height"))/2+"px");       //设置到当前窗口中间
	        var marginLeft = (targetWidth - dialogWidth)/2 +10;
	        dialog.css('left',marginLeft+'px');
	        dialog.css('width', dialogWidth + 'px');
	        
	};
	
	Dialog.prototype.initBtn = function(dialog){
	    var me = this;
	    var controls = $('.cube-dialog-controls');
	    var dialogWidth = $(dialog).css('width');
	    if(!controls) return;
	    
	    for(var i = 0; i < this.btnConfigs.configs.length; i++){        //添加按钮
	        controls.append(btnElem);       
	    }
	
	   var btns =  $('.cube-dialog-btn');
	   var btnWidth= ((parseInt(dialogWidth) - 10 * (btns.length - 1) - 20 * btns.length) / btns.length);      //计算按钮宽度
	   var btnTitles = $('.cube-dialog-btn-title');
	   for(var i = 0; i < btns.length; i++){
	        var localBtn = $(btns[i]);
	        var btnConfigs = this.btnConfigs;
	        $(btnTitles[i]).html(this.btnConfigs.configs[i].title);             //设置按钮标题
	        localBtn.attr({'eventname':this.btnConfigs.configs[i].eventName});      //设置事件名称
	        localBtn.css('padding','4px 0px');                                  //设置按钮属性
	        localBtn.css('width', btnWidth + 'px');
	        localBtn.css('margin-left', 10 + 'px');
	        localBtn.css('margin-right', 10 + 'px');
	        localBtn.css('margin-bottom', 10 + 'px');
	        localBtn.bind('click',function(){                                   //从this.btnConfigs中调用方法
	            me.hide();
	            if($(this).attr('eventname')){
	                btnConfigs[$(this).attr('eventname')]();
	            }
	        })
	        $(dialog).append(localBtn);
	   }
	};
	
	
	Dialog.prototype.hide = function() {
	    var cube_dialog = this.find();
	    if(cube_dialog){ 
	        $(cube_dialog).remove();
	        enableScrolling();
	    }
	};
	
	Dialog.prototype.find = function() {
	    var targetOjb = $(this.config.target);
	    var result;
	    var children = targetOjb.children();
	    $(children).each(function(){
	        if($(this).attr('id') == 'cube-dialog-wrapper') {
	            result = this;
	        }
	    });
	    return result;
	};
/* ----------------------dialog end------------------*/
/* ----------------------ajax请求begin------------------*/
var Mbp = function(){
		
};
  Mbp.post=function(obj){
	       
		   var loader = new Loader();
           var options = {
           block: true,
           timeout: 30 * 1000,
           async: true,
           traditional:true,
           type: "POST",
		   url: contextPath +"/" + obj.url,
           data: obj.params,
           dataType : "json",
           success: function(data, textStatus, jqXHR){ 
        	  
        	   if(loader.isCanceled())return;
        	   loader.hide();
	           if(obj.success) {
	               obj.success(data, textStatus, jqXHR);
	           }
	           return;
	      
           },
           error: function(e, xhr, type){
			 if(loader.isCanceled()) return;
			   loader.hide();
			   
			   new Dialog({autoshow : true, target: 'body', title:"提示",content: "网络或服务器异常，请稍候重试。"},
				    		{configs:[{title:"知道了",eventName:'ok'}],
				    	ok:function() { }
                }
              );
				
			   /*if(obj.error) {
				   obj.error(e, xhr, type);
			   }*/
           }
        };
           $.ajax(options); 
		
	};
/* ----------------------ajax请求end------------------*/
/* ----------------------session begin------------------*/
	var Session = function(){
		
	};

	Session.saveObject = function(key, object) {
		window.sessionStorage[key] = JSON.stringify(object);
	};

	Session.loadObject = function(key) {
		var objectString =  window.sessionStorage[key];
		return objectString == null ? null : JSON.parse(objectString);
	};

	Session.deleteObject = function(key) {
		window.sessionStorage[key] = null;
	};
/* ----------------------session end------------------*/
/**
 * HTML5本地存储模块，DB实例
 */
var Store = function() {
	
}

Store.saveObject = function(key, object) {
	window.localStorage[key] = JSON.stringify(object);
}

Store.loadObject = function(key) {
	var objectString =  window.localStorage[key];
	return objectString == null ? null : JSON.parse(objectString);
}

Store.deleteObject = function(key) {
	window.localStorage[key] = null;
}

Store.clear = function() {
	window.localStorage.clear();
}

function getDeviceType() {  
	var userAgent = navigator.userAgent.toLowerCase(); 

	var ipad     = userAgent.match(/ipad/i) == "ipad";  
	var iphoneOs = userAgent.match(/iphone os/i) == "iphone os";  
	var android  = userAgent.match(/android/i) == "android";  

	if (ipad || iphoneOs) {
		return "ios";
	} else if (android) {
		return "android";
	} else {
		return "other";
	}
}