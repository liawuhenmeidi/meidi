  function aftersale(date,type){ 
	  if(32 == type || 33 == type){   
		  return aftersalesecond(date,type);
	  }else if( 29== type){
		  return aftersalerepair(date,type); 
	  }else if( 31 == type){ 
		  return aftersalesearch(date,type); 
	  }else if( 30 == type || 34 == type || 35 == type){
		  return aftersalerepare(date,type); 
	  }else if(36 == type || 37 == type){
		  return aftersalematianupload(date,type);
	  }   
}      
   
  function aftersalematianupload(date,type){
		var html = '';
		var statues = null;
		
		if(37 == type){
			statues =2 ; 
		} 
		var jsonlist =  $.parseJSON(date);
		for(var i=0;i<jsonlist.length;i++){
			var json = jsonlist[i];
			
			   if(null != json){
				   var cname = "";
					var tname = ""; 
					var uname = "";
					var uuname = "";
					var cause = "";
					if(null != json.asplist ){ 
						  
						for(var j=0;j<json.asplist.length;j++){
							cname += json.asplist[j].cname+"<p>";
							tname += json.asplist[j].tname; 
							sta = json.asplist[j].statues;   
							uname = json.asplist[j].dealName;
							uuname = json.asplist[j].dealsendName;
							cause = json.asplist[j].cause;  
						} 
					}  
			//alert(json.submituser.username);  
			//String tdcol = 'bgcolor="red"' ;   
				html += '<tr id='+json.as.id+'  class="asc"  onclick="updateClass(this)" ondblclick="detail('+json.as.id+','+statues+')">'; 
				
					html += '<td align="center" width="20"><input type="radio"  name="id"  value='+json.as.id+' ></input></td>'; 
				
				
				html += '<td align="center">'+json.as.printid+'</td>';  
				html += '<td align="center">'+json.as.uname+'</td>';  
				html += '<td align="center">'+json.as.phone+'</td>';
				//html += '<td align="center">'+json.as.submituser.username+'</td>';
				//html += '<td align="center">'+json.as.submituser.phone+'</td>'; 
				html += '<td align="center">'+json.as.cName+'</td>';
				html += '<td align="center">'+json.as.tName+'</td>';
				 
				html += '<td align="center">'+cause+'</td>';
				html += '<td align="center">'+cname+'</td>';
				html += '<td align="center">'+tname+'</td>';

				html += '<td align="center">'+uname+'</td>';
				html += '<td align="center">'+uuname+'</td>';
				
				html += '<td align="center">'+json.as.batchNumber+'</td>'; 
				html += '<td align="center">'+json.as.barcode+'</td>'; 
				 
				html += '<td align="center">'+json.as.location+'</td>';
				//html += '<td align="center">'+json.as.saledate+'</td>';
				//html += '<td align="center">'+json.as.andate+'</td>';  
				html += '<td align="center">'+json.as.detail+'</td>'; 
				html += '</tr>'; 
		   }
		} 
		return html;
	}

  function aftersalesecond(date,type){
		var html = '';
		var jsonlist =  $.parseJSON(date);
		for(var i=0;i<jsonlist.length;i++){
			var json = jsonlist[i];
			
			   if(null != json){
				   var cname = "";
					var tname = ""; 
					var sta = 0 ;
					var uname = "";
					var uuname = "";
					var cause  = "";
					var time = "";
					if(null != json.asplist ){
						  
						for(var j=0;j<json.asplist.length;j++){
							cname += json.asplist[j].cname+"<p>";
							tname += json.asplist[j].tname; 
							sta = json.asplist[j].statues;   
							uname = json.asplist[j].dealName;
							uuname = json.asplist[j].dealsendName;
							cause = json.asplist[j].cause;
							time = json.asplist[j].thistime;
						} 
					}  
			//alert(json.submituser.username); 
			//String tdcol = 'bgcolor="red"' ;  
				html += '<tr id='+json.as.id+'  class="asc"  onclick="updateClass(this)" >'; 
				if(sta == 0){
					html += '<td align="center" width="20"><input type="radio"  name="id" value='+json.as.id+' ></input></td>'; 
				}else {
					html += '<td align="center" width="20"></td>'; 
				}
				 
				html += '<td align="center">'+json.as.printid+'</td>';  
				html += '<td align="center">'+json.as.uname+'</td>'; 
				html += '<td align="center">'+json.as.phone+'</td>';
				//html += '<td align="center">'+json.as.submituser.username+'</td>';
		//html += '<td align="center">'+json.as.submituser.phone+'</td>'; 
				html += '<td align="center">'+json.as.cName+'</td>';
				html += '<td align="center">'+json.as.tName+'</td>';
				html += '<td align="center">'+cause+'</td>';
				html += '<td align="center">'+cname+'</td>';
				html += '<td align="center">'+tname+'</td>';
 
				if(sta == 0){
					html += '<td colspan="2" align="center">'+
			         '<select  name="uid'+json.as.id+'" id="uid'+json.as.id+'"  >'+
			          '<option></option>'; 
					  for(var m=0;m<listuser.length;m++){
						  html +=   '<option value="'+listuser[m].id+'" id="'+listuser[m].id+'">'+listuser[m].username+'</option>';
					 
					  }  

			     html += '</select> ';

			     html += '<input type="button" onclick="change(\'uid'+json.as.id+'\',\''+json.as.id+'\')\"  value=\"确定\"/></td>';
				}else if(sta == 1){
					html += '<td colspan="2" align="center">'+uname;
					// 售后文员配单
			         if(type == 33){
			        	 html += '<input type="button" value="同意" onclick="changeStatues('+json.as.id+',0)"/>';
			        	 html += '<input type="button" value="不同意" onclick="changeStatues('+json.as.id+',1)"/>';
			         }
 
					html += '</td>'; 
				}else if(sta == 2){
					html += '<td colspan="2" align="center">'+ uuname;
					// 售后文员配单 
			         if(type == 32){ 
			        	 html += '<input type="button" value="同意" onclick="changeStatues('+json.as.id+',0)"/>';
			        	 html += '<input type="button" value="不同意" onclick="changeStatues('+json.as.id+',1)"/>';
			         }

					html += '</td>'; 
				}
				
				html += '<td align="center">'+json.as.batchNumber+'</td>'; 
				html += '<td align="center">'+json.as.barcode+'</td>'; 
				 
				html += '<td align="center">'+json.as.location+'</td>';
				html += '<td align="center">'+time+'</td>';   
			//	html += '<td align="center">'+json.as.statuesName+"<p>"+json.as.statuestime+'</td>';
				html += '<td align="center">'+json.as.detail+'</td>'; 
				html += '</tr>'; 
		   } 
		} 
		return html;
	}
 
  function aftersalerepair(date,type){
		var html = '';
		var jsonlist =  $.parseJSON(date);
		for(var i=0;i<jsonlist.length;i++){
			var json = jsonlist[i];
			   if(null != json){
				    
			//alert(json.submituser.username); 
			//String tdcol = 'bgcolor="red"' ; 
				html += '<tr id='+json.printid+'  class="asc"  onclick="updateClass(this)" >';   
				html += '<td align="center" width="20"><input type="checkbox"  id="check_box" name ='+json.as.id+"_"+json.as.oriedid+'></input></td>';
				html += '<td align="center">'+json.as.printid+'</td>'; 
				html += '<td align="center">'+json.as.uname+'</td>';  
				html += '<td align="center">'+json.as.phone+'</td>';
				html += '<td align="center">'+json.as.submituser.username+'</td>';
				html += '<td align="center">'+json.as.submituser.phone+'</td>'; 
				html += '<td align="center">'+json.as.cName+'</td>';
				html += '<td align="center">'+json.as.tName+'</td>';
				html += '<td align="center">'+json.as.pcount+'</td>';
				
				html += '<td align="center">'+json.as.batchNumber+'</td>';
				html += '<td align="center">'+json.as.barcode+'</td>';
				 
				html += '<td align="center">'+json.as.location+'</td>';
				html += '<td align="center">'+json.as.typeName+'</td>';  
				html += '<td align="center">'+json.as.andate+'</td>';  
				html += '<td align="center">'+json.as.statuesName+"<p>"+json.as.statuestime+'</td>'; 
				html += '<td align="center">'+json.as.detail+'</td>';  
				html += '</tr>';
		   }
		}
		return html; 
	}
  
function aftersalesearch(date,type){
	var html = '';
	var jsonlist =  $.parseJSON(date);
	for(var i=0;i<jsonlist.length;i++){
		var json = jsonlist[i];
		   if(null != json){
			   var cname = "";
				var tname = ""; 
				var uname = "";
				var uuname = "";
				var resultStr = "";
				var cause = "";
				var typeStr = "";
				if(null != json.asplist ){
					  
					for(var j=0;j<json.asplist.length;j++){
						cname += json.asplist[j].cname+"<p>";
						tname += json.asplist[j].tname; 
						sta = json.asplist[j].statues;   
						uname = json.asplist[j].dealName; 
						uuname = json.asplist[j].dealsendName;
						resultStr = json.asplist[j].resultStr;
						cause = json.asplist[j].cause;
						typeStr = json.asplist[j].typeStr;
					}  
				}  
		//alert(json.submituser.username); 
		//String tdcol = 'bgcolor="red"' ; 
			html += '<tr id='+json.printid+'  class="asc"  onclick="updateClass(this)" ondblclick="detail('+json.as.id+')">'; 
			html += '<td align="center" width="20"><input type="radio"  value="'+json.as.id+'" name ="id" ></input></td>';
			html += '<td align="center">'+json.as.printid+'</td>';  
			html += '<td align="center">'+json.as.uname+'</td>'; 
			html += '<td align="center">'+json.as.phone+'</td>';
			html += '<td align="center">'+json.as.submituser.username+'</td>';
			html += '<td align="center">'+json.as.submituser.phone+'</td>'; 
			html += '<td align="center">'+json.as.cName+'</td>';
			html += '<td align="center">'+json.as.tName+'</td>';
			html += '<td align="center">'+json.as.pcount+'</td>';
			html += '<td align="center">'+cause+'</td>';
			html += '<td align="center">'+cname+'</td>';
			html += '<td align="center">'+tname+'</td>';
			//html += '<td align="center">'+uname+'</td>'; 
			//html += '<td align="center">'+uuname+'</td>';  
			//html += '<td align="center">'+typeStr+'</td>';
			html += '<td align="center">'+resultStr+'</td>';
			html += '<td align="center">'+json.as.batchNumber+'</td>';
			html += '<td align="center">'+json.as.barcode+'</td>';
			  
			html += '<td align="center">'+json.as.location+'</td>';
			//html += '<td align="center">'+json.as.typeName+'</td>';  
			//html += '<td align="center">'+json.as.saledate+'</td>';
			//html += '<td align="center">'+json.as.andate+'</td>';  
			//html += '<td align="center">'+json.as.statuesName+"<p>"+json.as.statuestime+'</td>'; 
			html += '<td align="center">'+json.as.detail+'</td>';  
			html += '</tr>';
	   } 
	}
	return html; 
}

function aftersalerepare(date,type){
	var html = '';  
	var jsonlist =  $.parseJSON(date);
	for(var i=0;i<jsonlist.length;i++){ 
		var json = jsonlist[i];
		   if(null != json){ 
			   var cname = "";
				var tname = ""; 
				var uname = "";
				var uuname = "";
				var resultStr = "";
				var cause = "";
				var typeStr = "";
				if(null != json.asplist ){
					  
					for(var j=0;j<json.asplist.length;j++){
						cname += json.asplist[j].cname+"<p>";
						tname += json.asplist[j].tname; 
						sta = json.asplist[j].statues;   
						uname = json.asplist[j].dealName; 
						uuname = json.asplist[j].dealsendName;
						resultStr = json.asplist[j].resultStr;
						cause = json.asplist[j].cause; 
						typeStr= json.asplist[j].typeStr;
					}  
				}  
		//alert(json.submituser.username); 
		//String tdcol = 'bgcolor="red"' ;     
			html += '<tr id='+json.printid+'  class="asc"  onclick="updateClass(this)" ondblclick="detail('+json.as.id+',1)" >';  
			html += '<td align="center" width="20"><input type="radio"   name ="id"  value="'+json.as.id+'"></input></td>';
			html += '<td align="center">'+json.as.printid+'</td>';  
			html += '<td align="center">'+json.as.uname+'</td>'; 
			html += '<td align="center">'+json.as.phone+'</td>'; 
			//html += '<td align="center">'+json.as.submituser.username+'</td>';
			//html += '<td align="center">'+json.as.submituser.phone+'</td>'; 
			html += '<td align="center">'+json.as.cName+'</td>';
			html += '<td align="center">'+json.as.tName+'</td>'; 
			html += '<td align="center">'+json.as.pcount+'</td>';
			
			html += '<td align="center">'+json.as.batchNumber+'</td>';
			html += '<td align="center">'+json.as.barcode+'</td>';
			html += '<td align="center">'+json.as.location+'</td>'; 
			//html += '<td align="center">'+json.as.typeName+'</td>';   
			html += '<td align="center">'+json.as.andate+'</td>';  
			html += '<td align="center">'+json.as.nexttime+'</td>';     
			html += '<td align="center">'+json.as.detail+'</td>'; 
			html += '</tr>';
	   }
	}
	return html; 
}

