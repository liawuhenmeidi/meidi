package utill;

public class RemarkUtill {
    public static int success = 1 ; 
    public static int error = 2 ;  
    public static int nopermission = 3 ;
    
    
    public static String getMessage(int mark){
    	String str = "";
    	if(success == mark){
    		str = "保存成功";
    	}else if(error == mark){
    		str = "保存失败";
    	}else if(nopermission == mark){
    		str = "没有权限";
    	} 
    	return str ;
    }
}
