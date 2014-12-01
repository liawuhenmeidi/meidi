<%@ page language="java" import="java.util.*,locate.*,utill.*,category.*,product.*,user.*,group.*,branchtype.*,branch.*,wilson.catergory.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>品类分组管理页</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../css/jquery-impromptu.min.css" />
<script type="text/javascript" src="../js/jquery-impromptu.min.js"></script> 
<script type="text/javascript" src="../js/calendar.js"></script> 
<script type="text/javascript" src="../js/common.js"></script>

<style type="text/css">
.fixedHead { 
position:fixed;
}  
</style>
<script type="text/javascript">
var a = 1;
var statesdemo = {
		state0: {
			title: '请输入名称',
			html:'<label>名称 <input type="text" name="fname" value=""></label><br />',
			buttons: { 取消: -1, 确认: 1 },
			focus: 1,
			submit:function(e,v,m,f){ 
				console.log(f);
				e.preventDefault();
				//if(v==-1) 
				if(v==1) a = f.fname;
				$.prompt.close();
			}
		},
};

$(function(){
	 $("#b").click(function(){
		 $.prompt("确认删除", {
				title: "确认删除",
				buttons: { "确认": true, "放弃": false },
				submit: function(e,v,m,f){
					if(v == true){
						alert('a');
					}
				}
			});
	  });
});


function cc(){
	if($('#ss').val()=="提交"){
		return false;
	}
}
</script>
 

</head>

<body style="scoll:no">
 
<!--   头部开始   --> 



<form action="" onsubmit="return cc()">
<br/><button id="b">a</button>
<input type="submit" value="提交" id="ss" onclick="$(this).val('提交中')"/>
</form>

</body>
</html>
