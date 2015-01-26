  function aftersale(date,type){
	  if(32 == type || 33 == type){  
		  return aftersalesecond(date,type);
	  }else if( 29== type || 31 == type){
		  return aftersalesearch(date,type); 
	  }else if( 30 == type){
		  return aftersalerepare(date,type); 
	  } 
}   
   
  function aftersalesecond(date,type){
		var html = '';
		var jsonlist =  $.parseJSON(date);
		for(var i=0;i<jsonlist.length;i++){
			var json = jsonlist[i];
			   if(null != json){ 
			//alert(json.submituser.username); 
			//String tdcol = 'bgcolor="red"' ;  
				html += '<tr id='+json.as.id+'  class="asc"  onclick="updateClass(this)" >'; 
				html += '<td align="center" width="20"><input type="checkbox"  id="check_box" name ='+json.as.id+'></input></td>';
				html += '<td align="center">'+json.as.printid+'</td>'; 
				html += '<td align="center">'+json.as.uname+'</td>'; 
				html += '<td align="center">'+json.as.phone+'</td>';
				html += '<td align="center">'+json.as.submituser.username+'</td>';
				html += '<td align="center">'+json.as.submituser.phone+'</td>'; 
				html += '<td align="center">'+json.as.cName+'</td>';
				html += '<td align="center">'+json.as.tName+'</td>';
				var cname = "";
				var tname = "";   
				if(null != json.asplist ){
					 
					for(var j=0;j<json.asplist.length;j++){
						cname += json.asplist[j].cname+"<p>";
						tname += json.asplist[j].tname; 
					} 
				} 
				html += '<td align="center">'+cname+'</td>';
				html += '<td align="center">'+tname+'</td>';
				html += '<td colspan="2" align="center">'+
				         '<select  name="uid'+json.as.id+'" id="uid'+json.as.id+'"  >'+
				          '<option></option>'; 
				  for(var m=0;m<listuser.length;m++){
					  html +=   '<option value="'+listuser[m].id+'" id="'+listuser[m].id+'">'+listuser[m].username+'</option>';
				 
				  } 
 
				  html += '</select> ';
   
				html += '<input type="button" onclick="change(\'uid'+json.as.id+'\',\''+json.as.id+'\')\"  value=\"确定\"/></td>';
				html += '<td align="center">'+json.as.batchNumber+'</td>'; 
				html += '<td align="center">'+json.as.barcode+'</td>'; 
				
				html += '<td align="center">'+json.as.location+'</td>';
				html += '<td align="center">'+json.as.typeName+'</td>'; 
				html += '<td align="center">'+json.as.saledate+'</td>';
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
		//alert(json.submituser.username); 
		//String tdcol = 'bgcolor="red"' ; 
			html += '<tr id='+json.printid+'  class="asc"  onclick="updateClass(this)" ondblclick="detail('+json.as.id+')">'; 
			html += '<td align="center" width="20"><input type="checkbox"  id="check_box" name ='+json.as.id+'></input></td>';
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
			html += '<td align="center">'+json.as.saledate+'</td>';
			html += '<td align="center">'+json.as.andate+'</td>';  
			html += '<td align="center">'+json.as.statuesName+"<p>"+json.as.statuestime+'</td>'; 
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
		//alert(json.submituser.username); 
		//String tdcol = 'bgcolor="red"' ; 
			html += '<tr id='+json.printid+'  class="asc"  onclick="updateClass(this)" ondblclick="detail('+json.as.id+')">'; 
			html += '<td align="center" width="20"><input type="radio"   name ="id"  value="'+json.as.id+'"></input></td>';
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
			html += '<td align="center">'+json.as.saledate+'</td>';
			html += '<td align="center">'+json.as.andate+'</td>';  
			html += '<td align="center">'+json.as.statuesName+"<p>"+json.as.statuestime+'</td>'; 
			html += '<td align="center">'+json.as.detail+'</td>'; 
			html += '</tr>';
	   }
	}
	return html; 
}

