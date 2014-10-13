package user;

import java.util.HashMap;

public class UserService {
   public static boolean flag = false ;
   public static HashMap<Integer,User>  usermap ;
   
   
   public static HashMap<Integer,User> getMapId(){
	   
	   if(usermap == null || flag == true){
		   usermap = UserManager.getMap();
	   }
	   
	   return usermap;
   }
}
