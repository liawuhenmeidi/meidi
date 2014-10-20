package user;

import group.Group;

import java.util.HashMap;
import java.util.List;

public class UserService {
   public static boolean flag = false ;
   public static HashMap<Integer,User>  usermap ;
   public static HashMap<String,User>  usermapStr ;
   public static HashMap<String,List<User>>  usermapBranch ;
   
 //获取二次配单元（工队）
   public static List<User>  list ;  
   
   public static HashMap<Integer,User> getMapId(){
	   
	   if(usermap == null || flag == true){
		   usermap = UserManager.getMap();
	   }
	    
	   return usermap;
   }
   
public static  List<User> getsencondDealsend(User user){
	   
	   if(list == null || flag == true){ 
		   list = UserManager.getUsers(user,Group.sencondDealsend);
	   }
	    
	   return list;
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
