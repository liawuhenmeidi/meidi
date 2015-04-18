package user;

import group.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utill.StringUtill;

public class UserService {
   public static boolean flag = true ;
   public static HashMap<Integer,User>  usermap ;
 //未被删除用户
   public static HashMap<Integer,User>  usermapstatues ;  
   
   public static HashMap<String,User>  usermapStr ;
   public static HashMap<String,List<User>>  usermapBranch ;
   
   public static List<Integer> listids;  // user 子用户id
 //获取二次配单元（工队）
   public static List<User>  list ;

   // 送货员
   public static List<User>  listsend ;  
   
   public static List<User> listall ; 
   
   public static List<Integer> GetListson(User user){
	   init(); 
	   List<Integer> list = new ArrayList<Integer>();
  	   if(null != listall){  
  		   for(int i=0;i<listall.size();i++){ 
  			   User u = listall.get(i);   
  			   //System.out.println(u.getc); 
  			   if(u.getCharge().equals(user.getId()+"")){
  				   list.add(u.getId()); 
  			   }   
  		   }   
  	   } 
  	   list.add(user.getId()); 
  	   return list ;
   }
    
   public static List<User> getjsuser(List<User> list){
	   List<User> listn = new ArrayList<User>(); 
	   if(null != list){
		   for(int i=0;i<list.size();i++){
			   User u = new User();
			   u.setId(list.get(i).getId());
			   u.setUsername(list.get(i).getUsername());
			   listn.add(u);
		   }
	   } 
	   return listn;
   }
     
    
   public static HashMap<String,User> getmapSencd(List<User> list){
	   HashMap<String,User> users = new HashMap<String,User>();
	   if(null !=list){
		   for(int i =0;i<list.size();i++){
			   User u = list.get(i); 
			   users.put(u.getId()+"", u);
		   } 
	   }
	    
	   return users ; 
   }

   
 
   public static HashMap<Integer,User> getMapId(){
	   init();
	   if(usermap == null){
		   usermap = UserManager.getMap();
	   }
	    
	   return usermap;
   }
   
   public static HashMap<Integer,User> getMapstatues(){
	   init();
	   if(usermapstatues == null){
		   usermapstatues = UserManager.getMapstatues();
	   }
	    
	   return usermapstatues;
   }
   
public static  List<User> getsencondDealsend(User user){
	 init();
	   if(null == list){ 
		   list = UserManager.getUsers(user,Group.sencondDealsend);
	   }
	    
	   return list;
   }

public static  List<User> getsend(User user){
	 init();
	   if(null == listsend){ 
		   listsend =UserManager.getUsers(user,Group.send);
	   }
	    
	   return listsend;
}

  public static HashMap<String,User> getuserIdStr(){
	   init();
	   if(usermapStr == null){
		   usermapStr = UserManager.getMap("");
	   }  
	   
	   return usermapStr; 
   }
 
public static HashMap<String,List<User>> getMapBranchid(){
	    init();
	   if(usermapBranch == null){
		   usermapBranch = UserManager.getMapBranch();
	   }
	   
	   return usermapBranch;
   }

 public static void init(){
	 if(flag){
		 usermap = UserManager.getMap();
		 usermapstatues = UserManager.getMapstatues();
		 usermapStr = UserManager.getMap(""); 
		 usermapBranch = UserManager.getMapBranch();
		 list = null ;
		 listsend = null; 
		 listall = UserManager.getUsers();
	 }
	 flag = false ;
 }
}
