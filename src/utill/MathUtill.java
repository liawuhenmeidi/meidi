package utill;

public class MathUtill {
    public static String getThree(int count){
    	String in = "000";
    	if(0<count && count<=9){
    		in = "00"+count;
    	}else if(9<count && count <= 99){
    		in = "0"+count;
    	}else {
    		in = count+"";
    	}
    	 
    	return in ;
    }
}
