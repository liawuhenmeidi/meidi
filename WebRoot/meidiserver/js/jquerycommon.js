Array.prototype.deleteEle=function(){
	    var newArr = this;
	    for (var i=newArr.length-1; i>=0; i--)
	    {
	        var targetNode = newArr[i];
	        for (var j=0; j<i; j++)
	        {
	            if(targetNode == newArr[j]){
	                newArr.splice(i,1);
	                //alert(arr);
	                break;
	            }
	        }
	    }
	    return newArr;
	};