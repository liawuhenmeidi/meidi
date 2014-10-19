package user;

import java.util.HashMap;
import java.util.List;

public class UserService {
   public static boolean flag = false ;
   public static HashMap<Integer,User>  usermap ;
   public static HashMap<String,User>  usermapStr ;
   public static HashMap<String,List<User>>  usermapBranch ;
   
   public static HashMap<Integer,User> getMapId(){
	   
	   if(usermap == null || flag == true){
		   usermap = UserManager.getMap();
	   }
	    
	   return usermap;
   }
   
 public static HashMap<String,User> getuserIdStr(){
	   
	   if(usermapStr == null || flag == true){
		   usermapStr = UserManager.getMap("");
	   }  
	   
	   return usermapStr; 
   }
 
public static HashMap<String,List<User>> getMapBranchid(){
	     
	   if(usermapBranch == null || flag == true){
		   usermapBranch = UserManager.getMapBranch();
	   }
	   
	   return usermapBranch;
   }
}
