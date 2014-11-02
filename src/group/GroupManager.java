package group;
import grouptype.Grouptype;
import grouptype.GrouptypeManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.BranchManager;

import category.CategoryManager;

import user.User;
import user.UserManager;
import utill.DBUtill;
import database.DB;

public class GroupManager {
	protected static Log logger = LogFactory.getLog(GroupManager.class);
	private static GroupManager instance ;
	
	private HashMap<Integer,List<Group>> map = new HashMap<Integer,List<Group>>();
	
	private GroupManager(){
	}
	
	public static GroupManager getInstance(){
		
		if(instance == null ){
			instance = new GroupManager();
		}
		
		return instance ;
	}
	
	private void init(){
		GroupManager.getInstance().map = new HashMap<Integer,List<Group>>() ;
	}
	  
	public List<Group> getListGroupFromPemission(int type){
		List<Group> list = map.get(type);
		if(list == null ){
			list = GroupManager.getInstance().getGroupPeimission(type);
			
			map.put(type, list);
		}
		return list ;
	} 
	

	public static boolean getName(String c){
    	boolean flag = false ;
		Connection conn = DB.getConn();
		String sql = "select * from mdgroup where groupname = '"+ c+ "'";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				flag = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
    	return flag;
    }
	
		public static boolean save(User user,Group group) throws Exception {
			
			if(CategoryManager.getName(group.getName())){
				return false;
			}  
			
			boolean flag = UserManager.checkPermissions(user, Group.Manger);
			
			if(flag){
				Connection conn = DB.getConn();
				
				//List<PreparedStatement> listsql = new ArrayList<PreparedStatement>();
				
				String sql1 = "insert into  mdgroup( id ,groupname, detail,statues, permissions, products,pid,ptype) VALUES (null,?,?,?,?,?,?,?)";

                logger.info(group.getName()+group.getPermissions()+group.getDetail()+group.getStatues()+group.getProducts());
				
                PreparedStatement pstmt = DB.prepare(conn, sql1);
                
				try { 
					pstmt.setString(1, group.getName());
					pstmt.setString(2, group.getDetail());
					pstmt.setInt(3, group.getStatues());
					pstmt.setString(4, group.getPermissions());
					pstmt.setString(5, group.getProducts());
					pstmt.setInt(6, group.getPid()); 
					pstmt.setInt(7, group.getPtype());  

                 // listsql.add(pstmt); 	
				Grouptype gtype = GrouptypeManager.getGrouptype(group.getPtype());	
                  if(gtype.getType() == 2){
                	    
                  	String sql2 = BranchManager.save();
                  	PreparedStatement pstmt1 = DB.prepare(conn, sql2); 
                  	String permissons = "2_";
                  	int type = 4; 
                  	pstmt1.setString(1, group.getName()+"员工");
					pstmt1.setString(2, group.getDetail());
					pstmt1.setInt(3, group.getStatues());
					pstmt1.setString(4, permissons);
					pstmt1.setString(5, group.getProducts());
					pstmt1.setInt(7,type);   

					String sql3 = "insert into mdbranch(id,bname,pid,bmessage,relatebranch) values (null, ?,?,?,'')";
					PreparedStatement pstmt3 = DB.prepare(conn, sql3); 
					pstmt3.setString(1,group.getName() );
					pstmt3.setInt(2,1); 
					pstmt3.setString(3, group.getName());
 
                    DBUtill.PreparedStatement(conn, pstmt, pstmt1, pstmt3);
                    GroupService.flag = true ;
                    
                  }	else {
                	  pstmt.executeUpdate();
                	  GroupService.flag = true ;
                  }
					
				GroupManager.getInstance().init();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(pstmt);
					DB.close(conn);
				}
			}
			return true;
		}
        // 获取所有用户组
		
		public static Map<String,List<Group>> getLocateMap() {
			Map<String,List<Group>> map = null;
			Connection conn = DB.getConn();
			String sql = "select * from mdgroup ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) { 
					if(map == null){ 
						map = new HashMap<String,List<Group>>();
					}
					Group g = GroupManager.getGroupFromRs(rs);
					int id = g.getPtype(); 
					List<Group> list = map.get(id+""); 
					if(list == null){   
						list = new ArrayList<Group>();
						map.put(id+"", list);
					}   
					list.add(g);
				}    
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return map;
		}
		
		
		public static Map<String,List<Group>> gettypeMap() {
			Map<String,List<Group>> map = new HashMap<String,List<Group>>();
			Connection conn = DB.getConn();
			String sql = "select * from mdgroup ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					Group g = GroupManager.getGroupFromRs(rs);
					int id = g.getPtype(); 
					
					List<Group> list = map.get(id+""); 
					if(list == null){   
						list = new ArrayList<Group>();
						map.put(id+"", list);
					}   
					//list.add(rs.getString("groupname"));
					list.add(g);
				}    
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return map;
		}
		
		
		public static List<Group> getGroup() {
			List<Group> users = new ArrayList<Group>();
			Connection conn = DB.getConn();
			String sql = "select * from mdgroup";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Group g = GroupManager.getGroupFromRs(rs);
					users.add(g);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}  
			return users;
		}
		
		
		
		// 获取所有用户组
		
				public static List<Group> getGroup(User user,int type) {
					List<Group> users = new ArrayList<Group>();
					Connection conn = DB.getConn();
					String sql = ""; 
					if(UserManager.checkPermissions(user, Group.Manger)){
						sql = "select * from mdgroup where ptype = "+ type ;
					}else {
						sql = "select * from mdgroup where pid = " + user.getUsertype() + " and ptype = "+ type;
					} 
					logger.info(sql);
					Statement stmt = DB.getStatement(conn);
					ResultSet rs = DB.getResultSet(stmt, sql);
					try {
						while (rs.next()) {
							Group g = GroupManager.getGroupFromRs(rs);
							users.add(g);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						DB.close(rs);
						DB.close(stmt);
						DB.close(conn);
					}
					return users;
				}
				
				public static List<Group> getGroupdown(User user , int type ,int count) {
					
					if(UserManager.checkPermissions(user, Group.Manger)){
						return null ;
					}  
					
					List<Group> users = new ArrayList<Group>();
					
					Connection conn = DB.getConn();
					String str1 = "(select id from mdgroup where pid = "+user.getUsertype()+" and ptype = "+ type +")"; 
					
					String str = " (select id from mdgroup where ptype = "+type+" and pid in " ;
					
					for(int i = 0 ;i < count ;i ++ ){
						str +=  str1 +")";
						
					}
					
					String sql = "select * from mdgroup where id in " + str ;
logger.info(sql);
					Statement stmt = DB.getStatement(conn);
					ResultSet rs = DB.getResultSet(stmt, sql);
					try {
						while (rs.next()) {
							Group g = GroupManager.getGroupFromRs(rs);
							users.add(g);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						DB.close(rs);
						DB.close(stmt);
						DB.close(conn);
					}
					return users;
				}	
				
		// 根据用户组id获取组
		public static Group getGroup(int type) {
			Connection conn = DB.getConn();
			String sql = "select * from mdgroup where id = "+ type+"";
			Group g =  null ;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) {
					g = GroupManager.getGroupFromRs(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			
			return g;
		}
		 
		// 根据用权限获取组
		public  static List<Group> getGroupPeimission(int type) {
			
			List<Group> list = new ArrayList<Group>();
			
			Connection conn = DB.getConn();
			//String sql = "select * from mdgroup where  permissions  LIKE '%"+ type + "%'";
			String sql = "select * from mdgroup ";
	
			Statement stmt = DB.getStatement(conn);
			
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) {
					Group g = GroupManager.getGroupFromRs(rs);
					String str = g.getPermissions();
					String[] strl = str.split("_");
					for(int i=0;i<strl.length;i++){
						if(Integer.valueOf(strl[i]) == type){
							//logger.info("匹配成功");
							list.add(g); 
						} 
					}
					
				}
		//logger.info(list.size());	  	
			} catch (SQLException e) {
				logger.info(e);
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			
			return list;
		}
		
		public static HashMap<Integer,Group> getGroupMap() {
			HashMap<Integer,Group> users = new HashMap<Integer,Group>();
			Connection conn = DB.getConn();
			String sql = "select * from mdgroup";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Group g = GroupManager.getGroupFromRs(rs);
					users.put(g.getId(), g);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return users;
		}
      
		public static HashMap<String,List<String>> getGroupPidMap() {
			HashMap<String,List<String>> map = new HashMap<String,List<String>>();
		    HashMap<String,List<String>> mapuser = UserManager.getMapPid();
			Connection conn = DB.getConn();   
			String sql = "select * from mdgroup";  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					Group g = GroupManager.getGroupFromRs(rs);
					List<String> list = map.get(g.getId()+"");
					if(list == null){    
					   // list = UserManager.getUsersregist(g.getPid());
				 		list = mapuser.get(g.getPid()+""); 
						map.put(g.getId()+"", list); 
					}
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return map;
		}
		
		public static int delete(String str ) {
			String ids = "(" + str + ")";
			 
			List<User> list = UserManager.getUsers(ids);
			
			if(list.size() > 0){   
				return -1 ;
			}
			
			int count = 0 ;
			Connection conn = DB.getConn();
			String sql = "delete from mdgroup where id in " + ids;
logger.info(sql);  
			Statement stmt = DB.getStatement(conn);

			try {
				count = DB.executeUpdate(stmt, sql);
				GroupService.flag = true ;
			} finally {
				DB.close(stmt);
				DB.close(conn);
			}
			return count;
		}

		public static Group check(String username, String password)
				 {
			Group u = null;
			Connection conn = DB.getConn();
			String sql = "select * from mduser where name = '" + username + "'";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				if(!rs.next()) {
					//throw new UserNotFoundException("用户不存在:" + username);
				} else {
					if(!password.equals(rs.getString("password"))) {
						//throw new PasswordNotCorrectException("密码不正确哦!");
					}
					u = new Group();
					u.setId(rs.getInt("id"));
					u.setName(rs.getString("name"));
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return u;
		}
		
		public static void updatePermisson(User user,Group group) {
			Connection conn = DB.getConn();
			//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
			String sql = "update mdgroup set statues = ?, products = ? , permissions = ? where id = ?";

			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setInt(1,group.getStatues());
				pstmt.setString(2, group.getProducts());
				pstmt.setString(3, group.getPermissions());
				pstmt.setInt(4, group.getId());
				if(group.getPtype() == 3){
					String sql1 = "update mdgroup set statues = ?, products = ? where pid = ?";
					PreparedStatement pstmt1 = DB.prepare(conn, sql1);
					pstmt1.setInt(1,group.getStatues());
					pstmt1.setString(2, group.getProducts());
					pstmt1.setInt(3, group.getId());
					pstmt.executeUpdate(); 
					pstmt1.executeUpdate();
					GroupService.flag = true ;
					GroupManager.getInstance().init();
				}else {
					pstmt.executeUpdate();
					GroupService.flag = true ;
					GroupManager.getInstance().init();
				}
logger.info(pstmt);				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
		}
		// 跟新组信息
		
		public static void updateName(User user,Group group) {
			Connection conn = DB.getConn();
			//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
			String sql = "update mdgroup set groupname = ?, detail = ? ,pid = ?  where id = ?";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setString(1,group.getName());
				pstmt.setString(2, group.getDetail());
				pstmt.setInt(3, group.getPid());
				pstmt.setInt(4, group.getId());
				if(group.getPtype() == 3){
					String sql1 = "update mdgroup set groupname = ?, detail = ?  where pid = ?";
					PreparedStatement pstmt1 = DB.prepare(conn, sql1);
					pstmt1.setString(1,group.getName());
					pstmt1.setString(2, group.getDetail());
					pstmt1.setInt(3, group.getId());
					pstmt.executeUpdate(); 
					pstmt1.executeUpdate();
				}else {
					 pstmt.executeUpdate();
               	     GroupService.flag = true ;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
		}
		 
		private static Group getGroupFromRs(ResultSet rs){
			Group g = new Group();
			try {  
				g.setId(rs.getInt("id"));
				g.setName(rs.getString("groupname"));
				g.setDetail(rs.getString("detail"));
				g.setPermissions(rs.getString("permissions"));
				g.setStatues(rs.getInt("statues"));
				g.setProducts(rs.getString("products"));
				g.setPid(rs.getInt("pid")); 
				g.setPtype(rs.getInt("ptype"));
			} catch (SQLException e) {
				e.printStackTrace();
			}	
			return g ;
		}
}
